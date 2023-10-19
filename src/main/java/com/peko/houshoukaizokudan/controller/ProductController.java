package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductCategory;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import com.peko.houshoukaizokudan.service.ProductCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

	@Autowired
	private ProductBasicService prdService;

	@Autowired
	private ProductCategoryService pcService;

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
	@GetMapping("/back/show")
	@ResponseBody
	public List<ProductBasicDto> showPage(HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			List<ProductBasic> list = prdService.findAllProductBasic(loginUser);
			
	        System.out.println("Size of list: " + list.size());	        
	        
//	        for (ProductBasic product : list) {
//	            System.out.println("Seller Member ID: " + product.getSellermemberid().getId());
//	            System.out.println("Product Name: " + product.getProductname());
//	            System.out.println("Price: " + product.getPrice());
//	            System.out.println("Special Price: " + product.getSpecialprice());
//	            System.out.println("Category ID: " + product.getCategoryid().getId());
//	            System.out.println("Quantity: " + product.getQuantity());
//	            System.out.println("Description: " + product.getDescription());
//	            System.out.println("-------------------");
//	        }
	        
	        List<ProductBasicDto> dtoList = list.stream()
	                .map(product -> {
	                	ProductBasicDto dto = new ProductBasicDto();
	                	dto.setProductId(product.getId());
	                    dto.setSellermemberid(product.getSellermemberid().getMemberid());
	                    // 其他字段設置...
//	                    if (product.getSellermemberid() != null) {
//	                        dto.setSellermemberid(product.getSellermemberid().getId());
//	                        if (product.getSellermemberid().getMembertypeid() != null) {
//	                            dto.setMembertypeid(product.getSellermemberid().getMembertypeid().getId());
//	                        }
//	                    }
	                    dto.setProductName(product.getProductname());
	                    dto.setPrice(product.getPrice());
	                    dto.setSpecialPrice(product.getSpecialprice());
	                    if (product.getCategoryid() != null) {
	                        dto.setCategoryName(product.getCategoryid().getCategoryname());
	                        dto.setParentCategoryName(product.getCategoryid().getParentid().getParentname());
	                    }
	                    dto.setQuantity(product.getQuantity());
	                    dto.setDescription(product.getDescription());
	                    
	                    
	                    return dto;
	                })
	                .collect(Collectors.toList());

	        return dtoList;
		} else {
			return null;
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
		return "product/productView";
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
