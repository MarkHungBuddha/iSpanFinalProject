package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.ParentCategoryRepository;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.ProductCategoryRepository;
import com.peko.houshoukaizokudan.Repository.ProductImageRepository;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductCategory;
import com.peko.houshoukaizokudan.model.ProductImage;
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
import com.peko.houshoukaizokudan.model.ProductBasicRepository;

import java.util.List;

@Service
public class ProductBasicService {

    @Autowired
    private ProductBasicRepository productBasicRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ParentCategoryRepository parentCategoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

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
        return productBasicRepository.findByProductnameLike(keyword);
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

}
