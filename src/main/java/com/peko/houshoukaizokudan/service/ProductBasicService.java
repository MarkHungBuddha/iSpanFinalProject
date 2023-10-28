package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.Repository.*;
import com.peko.houshoukaizokudan.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;

import java.util.Optional;
import java.util.Set;

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
//
//    @Transactional
//    //商品like搜尋
//    public List<ProductBasic> searchProductsByName(String keyword) {
//        return productBasicRepository.findProductBasicDataByproductnameLike(keyword);
//    }
//
//
//    @Transactional
//    public List<ProductBasic> findProductBasicDataByproductname(String productname) {
//
//
//        List<ProductBasic> products = productBasicRepository.findProductBasicDataByproductnameLike("%" + productname + "%");
//
//        if (products.isEmpty()) {
//            return null;
//        }
//        return products;
//    }

//    @Transactional
//    public Optional<ProductBasicDto> getProductDTOById(Integer productId) {
////        Optional<ProductBasic> productOptional = productBasicRepository.findById(productId);
//        Optional<ProductBasic> productOptional = productBasicRepository.findByIdWithRelationships(productId);
//
//        if (!productOptional.isPresent()) {
//            return Optional.empty();
//        }
//
//        ProductBasic productBasic = productOptional.get();
//        ProductBasicDto productDTO = new ProductBasicDto();
//
//        // Map entity to DTO
//        productDTO.setProductId(productBasic.getId());
//        productDTO.setProductName(productBasic.getProductname());
//        productDTO.setPrice(productBasic.getPrice());
//        productDTO.setSpecialPrice(productBasic.getSpecialprice());
//        productDTO.setDescription(productBasic.getDescription());
//        productDTO.setQuantity(productBasic.getQuantity());
//        productDTO.setCategoryName(productBasic.getCategoryid().getCategoryname());
//        productDTO.setParentCategoryName(productBasic.getCategoryid().getParentid().getParentname());
//        productDTO.setSellerUsername(productBasic.getSellermemberid().getUsername());
//        productDTO.setImages(productBasic.getProductImage());
//        productDTO.setReviews(productBasic.getProductReview());
//        productDTO.setQandAs(productBasic.getQandA());
//
//        return Optional.of(productDTO);
//    }




    //頁碼  //1頁2筆
    @Transactional
    public Page<ProductBasic> findProductByPage(Integer pageNumber) {
        Pageable pgb = PageRequest.of(pageNumber - 1, 3, Sort.Direction.ASC, "id");
        Page<ProductBasic> page = productBasicRepository.findAll(pgb);
        return page;
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