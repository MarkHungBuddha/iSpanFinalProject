package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.DTO.ProductBasicDto2;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductCategory;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import com.peko.houshoukaizokudan.service.ProductCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class ProductController {

	@Autowired
	private ProductBasicService prdService;

	@Autowired
	private ProductCategoryService pcService;

//	@GetMapping("/product/{productId}")
//	public String viewProduct(@PathVariable Integer productId, Model model) {
//		ProductBasicDto productDTO = prdService.getProductDTOById(productId).orElse(null);
//		model.addAttribute("product", productDTO);
//		return "product/productView";
//	}
//
//	@GetMapping("/back/add")
//	private String addPage(Model model) {
//		return "background/uploadPage";
//	}

	@PostMapping("/back/add")
	private ResponseEntity<Object> uploadPage(@RequestParam String productname, 
			@RequestParam BigDecimal price,
			@RequestParam BigDecimal specialprice, 
			@RequestParam Integer categoryid, 
			@RequestParam Integer quantity,
			@RequestParam String description,HttpServletRequest request

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
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/back/show")
	public List<ProductBasicDto> showPage(HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			List<ProductBasic> list = prdService.findAllProductBasic(loginUser);
			
	        List<ProductBasicDto> dtolist = prdService.findAllProductBasicDto(list);
	        return dtolist;
		}else {
			return null;
		}
	}
	

//	@DeleteMapping("/back/delete")
//	public String deleteProduct(@RequestParam("id") Integer id) {
//		prdService.deleteById(id);
//		return "redirect:/background/showUpload";
//	}

	@PutMapping("/back/edit/{id}")
	private ResponseEntity<Object> editPage(@PathVariable("id") Integer id,@RequestBody ProductBasic up,HttpServletRequest request) {

		
		
		ProductBasic ed = prdService.findById(id);
		if(ed==null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		ProductBasic upd = prdService.updateProduct(ed,up);
		
		ProductBasicDto2 nupd = prdService.findNewOne(upd);
		
		if (upd != null) {
		 return new ResponseEntity<>(nupd, HttpStatus.OK);
    } else {
        // 更新失敗，返回 500 Internal Server Error 或其他適當的錯誤狀態碼
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
		
    	
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
