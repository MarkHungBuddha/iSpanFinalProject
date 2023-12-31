package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.DTO.ProductBasicDto2;
import com.peko.houshoukaizokudan.DTO.ProductDto;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ParentCategory;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductCategory;
import com.peko.houshoukaizokudan.service.ParentCategoryService;
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
	@Autowired
	private ParentCategoryService paService;


	//分頁顯示上傳商品(搜尋)
	@GetMapping("/seller/api/products/search")
	public ResponseEntity<Page<ProductBasicDto>> getProductsByPage(
			@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
			@RequestParam(name = "productname", required = false) String productname, HttpServletRequest request) {
		HttpSession session = request.getSession();

		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			Integer memberIdd = loginUser.getId();
			System.out.println("Member ID: " + memberIdd);
			Pageable pageable = PageRequest.of(pageNumber - 1, 10); // 3 items per page
			Page<ProductBasicDto> page = prdService.getProductByPage2(pageable, productname, memberIdd);
			return new ResponseEntity<>(page, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}


	// 新增商品
	@PostMapping("/seller/api/product")
	@Transactional
	public ResponseEntity<Object> uploadPage(@RequestParam String productname,
											 @RequestParam BigDecimal price, @RequestParam BigDecimal specialprice,
											 @RequestParam Integer categoryid, @RequestParam Integer quantity,
											 @RequestParam String description, HttpServletRequest request) throws java.io.IOException {

		HttpSession session = request.getSession();
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

			int productId = pb1.getId(); // 获取创建的商品ID
			System.out.println(productId);
			// 创建一个map来作为响应主体
			Map<String, Object> responseBody = new HashMap<>();
			responseBody.put("productId", productId); // 放入商品ID
			responseBody.put("message", "Product created successfully!"); // 也可以放入其他响应信息

			return new ResponseEntity<>(responseBody, HttpStatus.OK);
		} else {
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("error", "User not found or not logged in.");
			return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
		}
	}

	// 顯示所有上傳
	@GetMapping("/seller/api/products")
	public ResponseEntity<?> showPage(
			@RequestParam(name = "p", defaultValue = "1") Integer pageNumber, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			Integer memberIdd = loginUser.getId();
			System.out.println("Member ID: " + memberIdd);
			Pageable pageable = PageRequest.of(pageNumber - 1, 10); // 3 items per page
			try {
				Page<ProductBasicDto> page = prdService.getAllProductByPage(pageable, memberIdd);
				return new ResponseEntity<>(page, HttpStatus.OK);
			} catch (Exception e) {
				// Log the exception and return an appropriate error response
				System.err.println("Error retrieving products: " + e.getMessage());
				return new ResponseEntity<>("無法檢索產品信息", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("未授權訪問", HttpStatus.UNAUTHORIZED);
		}
	}


	// 更新商品圖片
	@PutMapping("/seller/api/product/{id}/{od}/editImg")
	public ResponseEntity<Object> editImage(@RequestPart("file") MultipartFile file, @PathVariable("id") Integer id,
											@PathVariable("od") Integer od, HttpServletRequest request) throws IOException, java.io.IOException {
		HttpSession session = request.getSession();
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			String pi = piService.updateImage(file, id, od);

			return new ResponseEntity<>(pi, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	// 更新商品資料
	@PutMapping("/seller/api/product/{id}")
	public ResponseEntity<Object> editPage(@PathVariable("id") Integer id, @RequestPart("productData") ProductBasic up,
										   HttpServletRequest request) throws java.io.IOException {
		HttpSession session = request.getSession();
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			ProductBasic ed = prdService.findById(id);
			if (ed == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			// 保存更新后的ProductBasic
			ProductBasic updatedProduct = prdService.updateProduct(ed, up);
			ProductBasicDto nupd = prdService.findNewOne(updatedProduct);
			if (nupd != null) {
				return new ResponseEntity<>(nupd, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	// 商品下架
	@PutMapping("/seller/api/{id}/remove")
	public ResponseEntity<Object> editQuntity(@PathVariable("id") Integer id, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			ProductBasic qu = prdService.findById(id);
			if (qu == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			prdService.removePd(qu);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//刪除圖片
	@DeleteMapping("/seller/api/{id}/{od}/deleteEachImg")
	public ResponseEntity<Object> deleteImg(@PathVariable("id")Integer id,@PathVariable("od")Integer od,HttpServletRequest request){
		HttpSession session = request.getSession();
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			piService.deleteImgByod(id,od);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	//顯示單筆商品
	@GetMapping("/seller/api/product")
	public ResponseEntity<ProductBasicDto> showOne(@RequestParam("id") Integer id, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			Integer memberIdd = loginUser.getId();
			System.out.println("Member ID: " + memberIdd);
			Optional<ProductBasicDto> pb = prdService.findByIdAndSellerId(id, memberIdd);

			if (pb.isPresent()) {
				return new ResponseEntity<>(pb.get(), HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}



	//賣家csv上傳商品(尚未測試)
	@PostMapping("/seller/api/csv")
	public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
		try {
			CSVParser csvParser = CSVParser.parse(file.getInputStream(), Charset.defaultCharset(), CSVFormat.DEFAULT);
			List<ProductBasic> products = new ArrayList<>();

			for (CSVRecord record : csvParser) {
				// 从CSV记录中提取字段并创建ProductBasic对象
				ProductBasic product = new ProductBasic();
				String a = record.get(3);
				int b = Integer.parseInt(a);
				ProductCategory c =pcService.findById(b);
				String d =c.getParentid().toString();
				int e =Integer.parseInt(d);
				ParentCategory f = paService.findbyid(e);
				String g = record.get(5);
				int h = Integer.parseInt(g);
				product.setProductname(record.get(0));  // 例如，第一个字段是产品名称
				product.setPrice(new BigDecimal(record.get(1)));
				product.setSpecialprice(new BigDecimal(record.get(2)));
				product.setCategoryid(c);
				product.setParentid(f);
				product.setQuantity(h);
				product.setDescription(new String(record.get(6)));

				// 将ProductBasic对象添加到列表
				products.add(product);
			}

//	            List<ProductBasic> products = CSVParser.parse(file.getInputStream(),Charset.defaultCharset(), CSVFormat.DEFAULT); // 自行实现CSVParser

			// 将提取的数据映射到ProductBasic实体类对象并保存到数据库
			prdService.saveProducts(products);

			return ResponseEntity.ok("CSV data uploaded and saved.");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error uploading and saving CSV data.");
		}
	}

	//顯示商品資料
	@GetMapping("/public/product/{productId}")
	public ResponseEntity<ProductBasicDto> viewProduct(@PathVariable Integer productId) {
		ProductBasicDto productDTO = prdService.getProductDTOById(productId).orElse(null);

		if(productDTO == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(productDTO);
	}

}
