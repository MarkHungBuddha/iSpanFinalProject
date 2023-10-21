package com.peko.houshoukaizokudan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.DTO.ProductBasicDto2;
import com.peko.houshoukaizokudan.Repository.ParentCategoryRepository;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.ProductCategoryRepository;
import com.peko.houshoukaizokudan.Repository.ProductImageRepository;
import com.peko.houshoukaizokudan.Repository.ProductReviewRepository;
import com.peko.houshoukaizokudan.Repository.QandARepository;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductCategory;
import com.peko.houshoukaizokudan.model.ProductImage;

@Service
public class ProductBasicService {

	@Autowired
	private ProductBasicRepository productBasicRepository;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;


	@Autowired
	private QandARepository qandARepository;

	@Autowired
	private ProductReviewRepository productReviewRepository;

	@Autowired
	private ProductImageRepository productImageRepository;

	@Autowired
	private ParentCategoryRepository parentCategoryRepository;

	
	// 建立商品
	@Transactional
	public ProductBasic addProductWithImages(ProductBasic productBasic, ProductCategory category,
			List<ProductImage> images) {
		// Save the product basic information
		ProductBasic savedProduct = productBasicRepository.save(productBasic);

		// Link product to its category
		category.setProductBasic(Set.of(savedProduct));
		productCategoryRepository.save(category);

		// Save the product images
		images.forEach(image -> {
			image.setProductid(savedProduct);
			productImageRepository.save(image);
		});

		return savedProduct;
	}

	// 編輯商品

	@Transactional
	public ProductBasic editProduct(ProductBasic productBasic, ProductCategory category, List<ProductImage> images) {
		if (productBasicRepository.existsById(productBasic.getId())) {
			productBasicRepository.save(productBasic);

			// Link product to its category
			category.setProductBasic(Set.of(productBasic));
			productCategoryRepository.save(category);

			// Save the product images
			images.forEach(image -> {
				image.setProductid(productBasic);
				productImageRepository.save(image);
			});

			return productBasic;
		} else {
			throw new RuntimeException("Product not found with ID: " + productBasic.getId());
		}
	}

	// 刪除商品
	@Transactional
	public void deleteProduct(Integer productId) {
		productBasicRepository.deleteById(productId);
	}

	@Transactional
	// 列出全部商品
	public List<ProductBasic> listAllProducts() {
		return productBasicRepository.findAll();
	}

	@Transactional
	// 商品like搜尋
	public List<ProductBasic> searchProductsByName(String keyword) {
		return productBasicRepository.findProductBasicDataByproductnameLike(keyword);
	}

	@Transactional
	public List<ProductBasic> findProductBasicDataByproductname(String productname) {

		List<ProductBasic> products = productBasicRepository
				.findProductBasicDataByproductnameLike("%" + productname + "%");

		if (products.isEmpty()) {
			return null;
		}
		return products;
	}

	@Transactional
	public Optional<ProductBasicDto> getProductDTOById(Integer productId) {
//        Optional<ProductBasic> productOptional = productBasicRepository.findById(productId);
		Optional<ProductBasic> productOptional = productBasicRepository.findByIdWithRelationships(productId);

		if (!productOptional.isPresent()) {
			return Optional.empty();
		}

		ProductBasic productBasic = productOptional.get();
		ProductBasicDto productDTO = new ProductBasicDto();

		// Map entity to DTO
		productDTO.setProductId(productBasic.getId());
		productDTO.setProductName(productBasic.getProductname());
		productDTO.setPrice(productBasic.getPrice());
		productDTO.setSpecialPrice(productBasic.getSpecialprice());
		productDTO.setDescription(productBasic.getDescription());
		productDTO.setQuantity(productBasic.getQuantity());
		productDTO.setCategoryName(productBasic.getCategoryid().getCategoryname());
		productDTO.setParentCategoryName(productBasic.getCategoryid().getParentid().getParentname());
//		productDTO.setSellerUsername(productBasic.getSellermemberid().getUsername());
//		productDTO.setImages(productBasic.getProductImage());
//		productDTO.setReviews(productBasic.getProductReview());
//		productDTO.setQandAs(productBasic.getQandA());

		return Optional.of(productDTO);
	}

	// 頁碼 //1頁2筆
	@Transactional
	public Page<ProductBasic> findProductByPage(Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber - 1, 3, Sort.Direction.ASC, "id");
		Page<ProductBasic> page = productBasicRepository.findAll(pgb);
		return page;
	}

//	public ProductBasic findLastest() {
//		return prdRepo.findFirstByOrderIdDesc();
//	}
	// return products;

	// };

	@Transactional
	public void insert(ProductBasic pb) {
		productBasicRepository.save(pb);
	}

	// public List<ProductBasic> findBySellerMemberId(Integer sellermemberid) {
//		List<ProductBasic> pbList = pbRepo.findBySellermemberid(sellermemberid);
//		return pbList;
//	}
	@Transactional
	public void deleteById(Integer id) {
		productBasicRepository.deleteById(id);
	}

	public ProductBasic findById(Integer id) {
		Optional<ProductBasic> optioanl = productBasicRepository.findById(id);

		if (optioanl.isPresent()) {
			return optioanl.get();
		}

		return null;
	}

	public List<ProductBasic> findAllProductBasic(Member sellermemberid) {
		List<ProductBasic> list = productBasicRepository.findBySellermemberid(sellermemberid);

		if (list.isEmpty()) {
			return null;
		}

		return list;
	}

	public List<ProductBasicDto> findAllProductBasicDto(List<ProductBasic> list){
		List<ProductBasicDto> dtoList = list.stream()
                .map(product -> {
                	ProductBasicDto dto = new ProductBasicDto();
                	dto.setProductId(product.getId());
                    dto.setSellermemberid(product.getSellermemberid().getMemberid());
                    // 其他字段設置...
//                    if (product.getSellermemberid() != null) {
//                        dto.setSellermemberid(product.getSellermemberid().getId());
//                        if (product.getSellermemberid().getMembertypeid() != null) {
//                            dto.setMembertypeid(product.getSellermemberid().getMembertypeid().getId());
//                        }
//                    }
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
	}
	@Transactional
	public ProductBasic updateProduct(ProductBasic ed,ProductBasic up) {
		 if (up.getProductname() != null) {
			 ed.setProductname(up.getProductname());
		    }
		 if (up.getPrice() != null) {
			 ed.setPrice(up.getPrice());
		 }
		 if (up.getSpecialprice() != null) {
			 ed.setSpecialprice(up.getSpecialprice());
		 }
		 if (up.getQuantity() != null) {
			 ed.setQuantity(up.getQuantity());
		 }
		 if (up.getDescription() != null) {
			 ed.setDescription(up.getDescription());
		 }
//		 if (up.getCategoryid() != null) {
//		        // 直接将新的categoryid设置给ProductBasic
//		        ed.setCategoryid(up.getCategoryid());
//		    }

		    

		 
		return productBasicRepository.save(ed);
	}

	

	

	

	public ProductBasicDto2 findNewOne(ProductBasic upd) {
	
	
	ProductBasicDto2 dto = new ProductBasicDto2();

	dto.setProductId(upd.getId());
//	dto.setSellermemberid(upd.getSellermemberid());
    dto.setProductName(upd.getProductname());
    dto.setPrice(upd.getPrice());
//    if (upd.getCategoryid() != null) {
//        dto.setCategoryName(upd.getCategoryid().getCategoryname());
//        if (upd.getCategoryid().getParentid() != null) {
//            dto.setParentCategoryName(upd.getCategoryid().getParentid().getParentname());
//        }
//    }
    dto.setSpecialPrice(upd.getSpecialprice());
    dto.setQuantity(upd.getQuantity());
    dto.setDescription(upd.getDescription());
	
    return dto;
	}

}
	
	
	
//
//
//
//    public Optional<ProductBasicDto> getProductDTOById(Integer productId) {
//        // Logic to fetch and assemble data into ProductDTO
//        return Optional.empty(); // Placeholder, you'll need to implement the actual logic
//    }


