package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.Repository.*;
import com.peko.houshoukaizokudan.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductBasicService {

    @Autowired
    private static ProductBasicRepository productBasicRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private QandARepository qandARepository;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private  ParentCategoryRepository parentCategoryRepository;
    //建立商品
    @Transactional
    public ProductBasic addProductWithImages(ProductBasic productBasic, ProductCategory category, List<ProductImage> images) {
        // Save the product basic information
        ProductBasic savedProduct = productBasicRepository.save(productBasic);

        // Link product to its category
        category.setProductBasic(List.of(savedProduct));
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
    public ProductBasic editProduct(ProductBasic productBasic,ProductCategory category, List<ProductImage> images) {
        if (productBasicRepository.existsById(productBasic.getId())) {
             productBasicRepository.save(productBasic);

            // Link product to its category
            category.setProductBasic(List.of(productBasic));
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

    //列出全部商品
    public List<ProductBasic> listAllProducts() {
        return productBasicRepository.findAll();
    }

    //商品like搜尋
    public List<ProductBasic> searchProductsByName(String keyword) {
        return productBasicRepository.findProductBasicDataByproductnameLike(keyword);
    }



	
	@Autowired
	private ProductBasicRepository prdRepo;
	
	
	public List<ProductBasic> findProductBasicDataByproductname(String productname) {	
	
		
		List<ProductBasic> products =prdRepo.findProductBasicDataByproductnameLike("%"+productname+"%");
		
		if(products.isEmpty()) {
			return null; 
		}
		return 	products;		
	};
	
	

	
	
	//頁碼  //1頁2筆

	public Page<ProductBasic> findProductByPage(Integer pageNumber){
		Pageable pgb= PageRequest.of(pageNumber-1, 3,Sort.Direction.ASC, "id");
		Page<ProductBasic> page = prdRepo.findAll(pgb);
		return page;
	}
	
//	public ProductBasic findLastest() {
//		return prdRepo.findFirstByOrderIdDesc();
//	}
	// 	return 	products;
		
	// };
	
	@Autowired
	private ProductBasicRepository pbRepo;

	public void insert(ProductBasic pb) {
		pbRepo.save(pb);
	}

//	public List<ProductBasic> findBySellerMemberId(Integer sellermemberid) {
//		List<ProductBasic> pbList = pbRepo.findBySellermemberid(sellermemberid);
//		return pbList;
//	}

	public void deleteById(Integer id) {
		pbRepo.deleteById(id);
	}

	public ProductBasic findById(Integer id) {
		Optional<ProductBasic> optioanl = pbRepo.findById(id);

		if (optioanl.isPresent()) {
			return optioanl.get();
		}

		return null;
	}



    public ProductBasicDto findProductInformation(Integer productID) {
        Optional<ProductBasic> productBasicOptional = productBasicRepository.findById(productID);

        if (!productBasicOptional.isPresent()) {
            return null; // 或者拋出一個適當的異常，例如 `EntityNotFoundException`
        }

        ProductBasic productBasic = productBasicOptional.get();
        Member seller = productBasic.getSellermemberid();
        ParentCategory parentCategory=parentCategoryRepository.findById(productBasic.getCategoryid().getParentid());

        ProductBasicDto productBasicDto = ProductBasicDto.builder()
                .id(productID)
                .sellermemberid(seller)
                .productname(productBasic.getProductname())
                .price(productBasic.getPrice())
                .specialprice(productBasic.getSpecialprice())
                .categoryid(productBasic.getCategoryid())
                .quantity(productBasic.getQuantity())
                .description(productBasic.getDescription())
                .productImage(productBasic.getProductImage())
                .productReview(productBasic.getProductReview())
                .qandA(productBasic.getQandA())
                .parentCategory(parentCategory)
                .build()
                ;

        return productBasicDto;
    }


}
