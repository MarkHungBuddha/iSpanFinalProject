package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.DTO.ProductBasicDto2;
import com.peko.houshoukaizokudan.DTO.ProductDto;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductCategory;
import com.peko.houshoukaizokudan.model.ProductImage;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import com.peko.houshoukaizokudan.service.ProductCategoryService;
import com.peko.houshoukaizokudan.service.ProductImageService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class ProductController {

	@Autowired
	private ProductBasicService prdService;

	@Autowired
	private ProductCategoryService pcService;
	@Autowired
	private ProductImageService piService;

	@GetMapping("/back/products")
	public ResponseEntity<Page<ProductDto>> getProductsByPage(
			@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
			@RequestParam(name = "productname", required = false) String productname, HttpServletRequest request) {
		HttpSession session = request.getSession();

		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			Integer memberIdd = loginUser.getId();
			System.out.println("Member ID: " + memberIdd);
			Pageable pageable = PageRequest.of(pageNumber - 1, 3); // 3 items per page
			Page<ProductDto> page = prdService.getProductByPage(pageable, productname, memberIdd);
			return new ResponseEntity<>(page, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

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

	@PostMapping("/back/add/{od}")
	private ResponseEntity<Object> uploadPage(@RequestParam String productname, @RequestParam BigDecimal price,
			@RequestParam BigDecimal specialprice, @RequestParam Integer categoryid, @RequestParam Integer quantity,
			@RequestParam String description, HttpServletRequest request, @RequestPart("file") MultipartFile file
			,@PathVariable("od") Integer od
	) throws java.io.IOException {
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
			pb1.setParentid(pb1.getCategoryid().getParentid());
			prdService.insert(pb1);

			int id = pb1.getId();
			System.out.println("ID:" + id);

			// 圖片s
			String imageUrl = null;
			try {
				imageUrl = piService.uploadImage(file, id,od);
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// 加PAGE
	@GetMapping("/back/show")
	public ResponseEntity<Page<ProductBasicDto>> showPage(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber, HttpSession session) {
	    Member loginUser = (Member) session.getAttribute("loginUser");
	    if (loginUser != null) {
	        Integer memberIdd = loginUser.getId();
	        System.out.println("Member ID: " + memberIdd);
	        Pageable pageable = PageRequest.of(pageNumber - 1, 3); // 3 items per page
	        Page<ProductBasicDto> page = prdService.getAllProductByPage(pageable, memberIdd);
	        return new ResponseEntity<>(page, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }
	}

	@PutMapping("/back/editImg/{id}/{od}")
	public ResponseEntity<Object> editImage(@RequestPart("file") MultipartFile file,@PathVariable("id") Integer id,@PathVariable("od") Integer od,HttpServletRequest request) throws IOException, java.io.IOException{
		
		String pi= piService.updateImage(file, id,od);
		
		return new ResponseEntity<>(pi, HttpStatus.OK);
	}


//加鎖ID
	@PutMapping("/back/edit/{id}")
	public ResponseEntity<Object> editPage(@PathVariable("id") Integer id, @RequestPart("product") ProductBasic up, HttpServletRequest request) throws java.io.IOException {
		HttpSession session = request.getSession();
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		ProductBasic ed = prdService.findById(id);
	    if (ed == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	    // 保存更新后的ProductBasic
	    ProductBasic updatedProduct = prdService.updateProduct(ed, up);
	    ProductBasicDto2 nupd = prdService.findNewOne(updatedProduct);
	    if (nupd != null) {
	        // 传递productId和imageUrl给saveProductImage方法
//	        piService.saveProductImage(updatedProduct.getId(), imageUrl);
	        return new ResponseEntity<>(nupd, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
////        findProductByPageLikeProductName
//        productFindPage
//        findByCategoryOrderByRating
//        findProductBasicByID
//        findProductReviewByID
//        findProductQandAByIDByPage
//        增QandA
//        編輯QandA
//        刪除QandA

	

}
