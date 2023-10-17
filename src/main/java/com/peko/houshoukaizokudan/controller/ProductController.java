package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;
import java.util.List;

import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductCategory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import com.peko.houshoukaizokudan.service.ProductCategoryService;

@Controller
public class ProductController {

    @Autowired
    private ProductBasicService prdService;
    
    @Autowired
    private ProductCategoryService pcService;
    
    
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



//	@GetMapping("product/{id}")
//	public String findProduct(@PathVariable("id") Integer productid, Model model) {
//
//		ProductBasicDto productBasicDto = prdService.findProductInformation(productid);
//
//		// Adding productBasicDto to the model
//		model.addAttribute("product", productBasicDto);
////		System.out.println(productBasicDto.toString());
//
//		return "product/productView";
//	}

	@GetMapping("/product/{productId}")
	public String viewProduct(@PathVariable Integer productId, Model model) {
		ProductBasicDto productDTO = prdService.getProductDTOById(productId).orElse(null);
		model.addAttribute("product", productDTO);
		return "product/productView";
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
			ProductCategory pc1 = pcService.findById(categoryid);
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

//        findProductByPageLikeProductName
//        productFindPage
//        findByCategoryOrderByRating
//        findProductBasicByID
//        findProductReviewByID
//        findProductQandAByIDByPage
//        增QandA
//        編輯QandA
//        刪除QandA

}
