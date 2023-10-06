package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;
import java.util.List;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductCategory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.service.ProductBasicService;

@Controller
public class ProductController {

    @Autowired
    private ProductBasicService prdService;
    
    
	//跳頁
	@GetMapping("/product/productFind")
	public String productFindPage() {
		return "product/productFindPage";
	}
    
    @PostMapping("/product/productFind")
    public String productFind(@RequestParam("productName") String productname, Model model) {
        List<ProductBasic> products = prdService.findProductBasicDataByproductname(productname);
        
        model.addAttribute("products", products);
        return "product/productFindPage"; 
    }
    
    @GetMapping("/product/page")
	public String findProductByPage(@RequestParam(name="p", defaultValue = "1") Integer pageNumber, Model model){
    	Page<ProductBasic> Page = prdService.findProductByPage(pageNumber);
    	
    	model.addAttribute("Page" ,Page);

    	return "product/productFindPages";
	}
 
    
   
 
 


	@GetMapping("/back/add")
	private String addPage(Model model) {
		return "background/uploadPage";
	}

	@PostMapping("/back/add")
	private String uploadPage(@RequestParam String productname, @RequestParam BigDecimal price,
			@RequestParam BigDecimal specialprice, @RequestParam Integer categoryid, @RequestParam Integer quantity,
			@RequestParam String description, Model model, HttpServletRequest request

	) {
		// 获取 HttpSession 对象
		HttpSession session = request.getSession();

		// 从 HttpSession 中获取存储的用户信息
		Member loginUser = (Member) session.getAttribute("loginUser");

		if (loginUser != null) {
			ProductCategory pc1 = new ProductCategory();
			pc1.setId(categoryid);
			ProductBasic pb1 = new ProductBasic();
			pb1.setSellermemberid(loginUser);
			pb1.setProductname(productname);
			pb1.setPrice(price);
			pb1.setSpecialprice(specialprice);
			pb1.setCategoryid(pc1);
			pb1.setQuantity(quantity);
			pb1.setDescription(description);

			prdService.insert(pb1);
			return "background/uploadPage";
		} else {
			return "background/uploadPage";
		}
	}



	@DeleteMapping("/back/delete")
	public String deleteProduct(@RequestParam("id") Integer id) {
		prdService.deleteById(id);
		return "redirect:/background/showUpload";
	}

	@GetMapping("/back/edit")
	public String editPage(@RequestParam("id") Integer id, Model model) {

		ProductBasic pb5 = prdService.findById(id);
		model.addAttribute("product", pb5);
		return "background/showUpload";
	}

}
