package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;

import java.util.List;

import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.DTO.ProductDto;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductCategory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;



import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.service.ProductBasicService;

@RestController
@Controller
public class ProductController {

    @Autowired
    private ProductBasicService prdService;
    
    
	//跳頁
	@GetMapping("/product/productFind")
	public String productFindPage() {
		return "product/productFindPage";
	}
    
	//模糊搜尋 存頁碼跟關鍵字
	//未給頁碼 預設第一頁
	
//	@GetMapping("/product/page")
//	 public String findProductByPage(
//	 @RequestParam(name = "pageNumber", defaultValue = "1") Integer pageNumber,
//	 @RequestParam(name = "productname", required = false) String productname, 
//	 Model model) {
//	 Page<ProductBasic> page = prdService.findProductByPage(pageNumber, productname);
//	 // 傳出 page P改小寫
//	 model.addAttribute("page", page);
//
//	 return "product/productFindPages";
//	 }
//  
	
	
	
	@ResponseBody
	@GetMapping("/api/products")
	public Page<ProductDto> getProductsByPage(
	    @RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
	    @RequestParam(name = "productname", required = false) String productname) {
	    Pageable pageable = PageRequest.of(pageNumber - 1, 3); // 3 items per page
	    Page<ProductDto> page = prdService.getProductsByPage(pageable, productname);
	    return page;
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
