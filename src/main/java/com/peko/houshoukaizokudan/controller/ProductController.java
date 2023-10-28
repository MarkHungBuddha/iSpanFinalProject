package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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


	//新增商品跟圖片(編碼ORDERID)
	@PostMapping("/back/add/{od}")
	private ResponseEntity<Object> uploadPage(@RequestParam String productname, @RequestParam BigDecimal price,
											  @RequestParam BigDecimal specialprice, @RequestParam Integer categoryid, @RequestParam Integer quantity,
											  @RequestParam String description, HttpServletRequest request, @RequestPart("file") MultipartFile file,
											  @PathVariable("od") Integer od) throws java.io.IOException {
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
				imageUrl = piService.uploadImage(file, id, od);
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// 顯示所有上傳
	@GetMapping("/back/show")
	public ResponseEntity<Page<ProductBasicDto>> showPage(
			@RequestParam(name = "p", defaultValue = "1") Integer pageNumber, HttpSession session) {
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

	//更新商品圖片
	@PutMapping("/back/editImg/{id}/{od}")
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

	//更新商品資料
	@PutMapping("/back/edit/{id}")
	public ResponseEntity<Object> editPage(@PathVariable("id") Integer id, @RequestPart("product") ProductBasic up,
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
	//商品下架
	@PutMapping("/back/editQ/{id}")
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
	@DeleteMapping("/back/deleteEachImg/{id}/{od}")
	public ResponseEntity<Object> deleteImg(@PathVariable("id")Integer id,@PathVariable("od")Integer od,HttpServletRequest request){
		HttpSession session = request.getSession();
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			piService.deleteImgByod(id,od);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}


	@GetMapping("/back/showOne")
	public ResponseEntity<ProductBasicDto2> showOne(
			@RequestParam("id") Integer id ,HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			Integer memberIdd = loginUser.getId();
			System.out.println("Member ID: " + memberIdd);
			Optional<ProductBasicDto2> pb=prdService.findByIdAndSellerId(id,memberIdd);

			if(pb.isPresent()) {
				return new ResponseEntity<>(pb.get(),HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}




	@PostMapping("/csv")
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

	@GetMapping("/product/{productId}")
	public ResponseEntity<ProductBasicDto> viewProduct(@PathVariable Integer productId) {
		ProductBasicDto productDTO = prdService.getProductDTOById(productId).orElse(null);

		if(productDTO == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(productDTO);
	}

}
