package com.peko.houshoukaizokudan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peko.houshoukaizokudan.model.ProductImage;
import com.peko.houshoukaizokudan.service.ProductImageService;

@Controller
public class ImageController {

	@Autowired
	private ProductImageService imageService;
	
	@GetMapping("/image/imageFind")
	public String imageFindPage() {
		return "image/imageFindPage";
		
	}
	
	@PostMapping("/image/imageFind")
	public String imageFind(@RequestParam("imagepath") String imagepath,Model model) {
		
		List<ProductImage> images = imageService.findProductImageDataByimagepath(imagepath);
		
		model.addAttribute("images", images);
		
		return "image/imageFindPage";
	}
	
	
	
	
	
	
}
 