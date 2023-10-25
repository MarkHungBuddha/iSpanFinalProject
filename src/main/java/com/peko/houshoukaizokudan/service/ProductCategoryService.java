package com.peko.houshoukaizokudan.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peko.houshoukaizokudan.DTO.ProductCategoryDto;
import com.peko.houshoukaizokudan.Repository.ProductCategoryRepository;
import com.peko.houshoukaizokudan.Repository.ProductImageRepository;
import com.peko.houshoukaizokudan.model.ProductCategory;
import com.peko.houshoukaizokudan.model.ProductBasic;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    //新增商品分類
    
	public ProductCategory findById(Integer id) {
		Optional<ProductCategory> optioanl = productCategoryRepository.findById(id);

		if (optioanl.isPresent()) {
			return optioanl.get();
		}

		return null;

	}
	
	
	//搜尋產品分類的類別id
	@Transactional
	public Page<ProductCategoryDto> getCategoryId(Pageable pageable, Integer categoryid) {
	    Page<ProductBasic> page = productCategoryRepository.findProductBasicByCategoryId(categoryid, pageable);
	    
	    List<ProductCategoryDto> dtos = page.getContent().stream().map(pro -> {
	        ProductCategoryDto dto = new ProductCategoryDto();
	        // 將 ProductBasic 的信息映射到 ProductCategoryDto	        
	        dto.setCategoryid(pro.getCategoryid().getId());
	        dto.setProductname(pro.getProductname());
	        dto.setPrice(pro.getPrice());
	        dto.setSpecialprice(pro.getSpecialprice());     
	        dto.setCategoryname(pro.getCategoryid().getCategoryname());
	        dto.setParentid(pro.getCategoryid().getParentid().getId());
	        dto.setParentname(pro.getCategoryid().getParentid().getParentname());	        
	        String imagepath = productImageRepository.findImagepathByProductid(pro.getId());
	        dto.setImagepath(imagepath);        
	        return dto;
	    }).collect(Collectors.toList());    
	    // 傳回一個新的 Page 對象，包含 ProductCategoryDto
	    return new PageImpl<>(dtos, pageable, page.getTotalElements());
	}	
}

