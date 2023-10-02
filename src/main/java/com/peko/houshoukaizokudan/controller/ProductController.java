package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductCategory;
import com.peko.houshoukaizokudan.service.ProductBasicService;



@Controller
public class ProductController {

	@Autowired
	private ProductBasicService pbService;
	
	
	@GetMapping("/back/add")
	private String addPage(Model model) {
		return "background/uploadPage";
	}
	
	
	
	@PostMapping("/back/add")
	private String uploadPage(
			@RequestParam String productname,
			@RequestParam BigDecimal price,
			@RequestParam BigDecimal specialprice,
			@RequestParam Integer categoryid,
			@RequestParam Integer quantity,
			@RequestParam String description,
			Model model
			) {
		ProductCategory pc1=new ProductCategory();
		pc1.setId(categoryid);
		

		ProductBasic pb1=new ProductBasic();
		pb1.setSellermemberid();
		pb1.setProductname(productname);
		pb1.setPrice(price);
		pb1.setSpecialprice(specialprice);
		pb1.setCategoryid(pc1);
		pb1.setQuantity(quantity);
		pb1.setDescription(description);
		
		pbService.insert(pb1);
		return "background/uploadPage";
	}
	
	@GetMapping("/back/show")
	private String showProduct(Integer id,Model model) {
	    List<ProductBasic> pb2 = pbService.findBySellerMemberId(101);

		List<ProductBasic> list = new ArrayList<>();

		for(ProductBasic pro : pb2) {
			ProductBasic pb3 = new ProductBasic();
			pb3.setId(pro.getId());
			pb3.setSellermemberid(101);
			pb3.setProductname(pro.getProductname());
			pb3.setPrice(pro.getPrice());
			pb3.setSpecialprice(pro.getSpecialprice());
			pb3.setCategoryid(pro.getCategoryid());
			pb3.setQuantity(pro.getQuantity());
			pb3.setDescription(pro.getDescription());
			
			list.add(pb3);
		}
		model.addAttribute("list",list);
		System.out.println(list);
		
		return "background/showUpload";
	}

	@DeleteMapping("/back/delete")
	public String deleteProduct(@RequestParam("id") Integer id) {
		pbService.deleteById(id);
		return "redirect:/background/showUpload";
	}
	
	
	@GetMapping("/back/edit")
	public String editPage(@RequestParam("id") Integer id,Model model) {
		
		ProductBasic  pb5= pbService.findById(id);
		model.addAttribute("product",pb5);
		return "background/showUpload";
	}
	
}
