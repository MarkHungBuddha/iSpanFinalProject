package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.DTO.ProductDto;
import com.peko.houshoukaizokudan.Repository.*;
import com.peko.houshoukaizokudan.model.*;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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


    //建立商品
    @Transactional
    public ProductBasic addProductWithImages(ProductBasic productBasic, ProductCategory category, List<ProductImage> images) {
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

    //編輯商品

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

    //刪除商品
    @Transactional
    public void deleteProduct(Integer productId) {
        productBasicRepository.deleteById(productId);
    }

    @Transactional
    //列出全部商品
    public List<ProductBasic> listAllProducts() {
        return productBasicRepository.findAll();
    }

    @Transactional
    //商品like搜尋
    public List<ProductBasic> searchProductsByName(String keyword) {
        return productBasicRepository.findProductBasicDataByproductnameLike(keyword);
    }


    @Transactional
    public List<ProductBasic> findProductBasicDataByproductname(String productname) {


        List<ProductBasic> products = productBasicRepository.findProductBasicDataByproductnameLike("%" + productname + "%");

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
        productDTO.setSellerUsername(productBasic.getSellermemberid().getUsername());
        productDTO.setImages(productBasic.getProductImage());
        productDTO.setReviews(productBasic.getProductReview());
        productDTO.setQandAs(productBasic.getQandA());

        return Optional.of(productDTO);
    }




  //頁碼  //1頁3筆
//    @Transactional
//    public Page<ProductBasic> findProductByPage(Integer pageNumber, String productname) {
//	     Pageable pageable = PageRequest.of(pageNumber - 1, 3, Sort.Direction.ASC, "id");
//	     return productBasicRepository.findProductBasicByproductname(productname, pageable);
//	    }
// 
    //模糊搜尋 + ProductBasic欄位 +Image 的圖片路徑
    @Transactional
    public Page<ProductDto> getProductsByPage(Pageable pageable, String productname) {
        Page<ProductBasic> page = productBasicRepository.findProductBasicByproductname(productname, pageable);
        List<ProductDto> dtos = page.getContent().stream().map(pro -> {
            ProductDto dto = new ProductDto();
            dto.setProductid(pro.getId());
            dto.setProductname(pro.getProductname());
            dto.setPrice(pro.getPrice());
            dto.setSpecialprice(pro.getSpecialprice());
            dto.setCategoryname(pro.getCategoryid().getCategoryname());
            dto.setQuantity(pro.getQuantity());
            dto.setDescription(pro.getDescription());
            
            
            // 使用 ProductImageRepository 查詢圖像路徑
            String imagepath = productImageRepository.findImagepathByProductid(pro.getId());
            dto.setImagepath(imagepath);
            return dto;
        }).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    



//	public ProductBasic findLastest() {
//		return prdRepo.findFirstByOrderIdDesc();
//	}
  // 	return 	products;

    // };

    @Transactional
    public void insert(ProductBasic pb) {
        productBasicRepository.save(pb);
    }

    //	public List<ProductBasic> findBySellerMemberId(Integer sellermemberid) {
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

//
//
//
//    public Optional<ProductBasicDto> getProductDTOById(Integer productId) {
//        // Logic to fetch and assemble data into ProductDTO
//        return Optional.empty(); // Placeholder, you'll need to implement the actual logic
//    }




}
