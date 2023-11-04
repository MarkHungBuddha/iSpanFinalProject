package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.DTO.ProductBasicDto2;
import com.peko.houshoukaizokudan.DTO.ProductCategoryDto;
import com.peko.houshoukaizokudan.DTO.ProductDto;
import com.peko.houshoukaizokudan.Repository.*;
import com.peko.houshoukaizokudan.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

import java.util.List;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;



import com.peko.houshoukaizokudan.Repository.MemberRepository;
import com.peko.houshoukaizokudan.Repository.ParentCategoryRepository;
import com.peko.houshoukaizokudan.Repository.ProductCategoryRepository;
import com.peko.houshoukaizokudan.Repository.ProductImageRepository;
import com.peko.houshoukaizokudan.Repository.ProductReviewRepository;
import com.peko.houshoukaizokudan.Repository.QandARepository;
import com.peko.houshoukaizokudan.model.Member;

import io.jsonwebtoken.lang.Collections;

@Service
public class ProductBasicService {

    @Autowired
    private ProductBasicRepository productBasicRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private MemberRepository mRepo;

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

    //頁碼  //1頁2筆
    @Transactional
    public Page<ProductBasic> findProductByPage(Integer pageNumber) {
        Pageable pgb = PageRequest.of(pageNumber - 1, 3, Sort.Direction.ASC, "id");
        Page<ProductBasic> page = productBasicRepository.findAll(pgb);
        return page;
    }


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

    @Transactional
    public Optional<ProductBasicDto> getProductDTOById(Integer productId) {
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
        productDTO.setSellerLastName(productBasic.getSellermemberid().getLastname());
        productDTO.setSellerFirstName(productBasic.getSellermemberid().getFirstname());
        productDTO.setSellermemberid(productBasic.getSellermemberid().getId());
//		productDTO.setImages(productBasic.getProductImage());
//		productDTO.setReviews(productBasic.getProductReview());
//		productDTO.setQandAs(productBasic.getQandA());

        return Optional.of(productDTO);
    }

    // 頁碼 //1頁2筆
    @Transactional
    public Page<ProductDto> getProductByPage2(Pageable pageable, String productname, Integer memberIdd) {
        if (memberIdd != null) {
            // 单次筛选：根据会员ID筛选并根据产品名称模糊搜索
            Page<ProductBasic> pageByMemberId = productBasicRepository.findProductBasicBySellermemberidAndProductnameContaining(memberIdd, productname, pageable);

            List<ProductDto> productDtos = pageByMemberId.getContent().stream()
                    .map(this::convertToProductDto)
                    .collect(Collectors.toList());

            return new PageImpl<>(productDtos, pageable, pageByMemberId.getTotalElements());
        }

        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }
    // 模糊搜尋 + 價格範圍
    @Transactional
    public Page<ProductDto> getProductsByPage(String productname, Double minPrice, Double maxPrice, Pageable pageable) {
        // 檢查價格範圍的合理性 // 價格不能為負數，處理相應的錯誤邏輯，最小價格不能大於最大價格，處理相應的錯誤邏輯
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            // 如果價格範圍無效，返回包含錯誤訊息的結果
            throw new IllegalArgumentException("無效的價格範圍");
        }

        Page<ProductBasic> productBasics = productBasicRepository
                .findProductBasicByProductNameAndPriceRange(productname, minPrice, maxPrice, pageable);
        List<ProductDto> result = productBasics.getContent().stream().map(pro -> {
            ProductDto dto = new ProductDto();
            dto.setProductId(pro.getId());
            dto.setProductName(pro.getProductname());
            dto.setPrice(pro.getPrice());
            dto.setSpecialPrice(pro.getSpecialprice());
            dto.setCategoryName(pro.getCategoryid().getCategoryname());
            dto.setQuantity(pro.getQuantity());
            dto.setDescription(pro.getDescription());
            // 使用 ProductImageRepository 查詢圖像路徑
            String imagepath = productImageRepository.findImagepathByProductid(pro.getId());
            dto.setImagepath(imagepath);
            return dto;
        }).collect(Collectors.toList());
        return new PageImpl<>(result, pageable, productBasics.getTotalElements());
    }

    //價格範圍搜尋
    @Transactional
    public Page<ProductCategoryDto> getCategoryNameByPriceRange(String categoryname, Double minPrice, Double maxPrice, Pageable pageable){
        // 檢查價格範圍的合理性 // 價格不能為負數，處理相應的錯誤邏輯，最小價格不能大於最大價格，處理相應的錯誤邏輯
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            // 如果價格範圍無效，返回包含錯誤訊息的結果
            throw new IllegalArgumentException("無效的價格範圍");
        }

        Integer categoryid = productCategoryRepository.findCategoryIdByCategoryName(categoryname);
        Page<ProductBasic> productBasics = productCategoryRepository.findProductBasicsByCategoryIdAndPriceRange(categoryid, minPrice, maxPrice, pageable);
        // 將 ProductBasic 資料映射到 ProductCategoryDto 中
        List<ProductCategoryDto> result = productBasics.stream().map(pro -> {
                    ProductCategoryDto dto = new ProductCategoryDto();
                    dto.setCategoryid(pro.getCategoryid().getId());
                    dto.setProductname(pro.getProductname());
                    dto.setPrice(pro.getPrice());
                    dto.setSpecialprice(pro.getSpecialprice());
                    dto.setCategoryname(pro.getCategoryid().getCategoryname());
                    dto.setParentid(pro.getCategoryid().getParentid().getId());
                    dto.setParentname(pro.getCategoryid().getParentid().getParentname());
                    // 使用 ProductImageRepository 查詢圖像路徑
                    String imagepath = productImageRepository.findImagepathByProductid(pro.getId());
                    dto.setImagepath(imagepath);
                    // 你可能需要添加更多的映射適應你的資料結構
                    return dto;
                })
                .collect(Collectors.toList()); //收集dto的數據s
        return new PageImpl<>(result, pageable, productBasics.getTotalElements());

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




    public ProductDto convertToProductDto(ProductBasic productBasic) {
        ProductDto productDto = new ProductDto();
        // 执行转换逻辑，将 productBasic 的属性赋值给 productDto
        productDto.setProductId(productBasic.getId());
        productDto.setProductName(productBasic.getProductname());
        productDto.setPrice(productBasic.getPrice());
        productDto.setSpecialPrice(productBasic.getSpecialprice());
        String ip = productImageRepository.findImagepathByProductid(productBasic.getId());
        productDto.setImagepath(ip);
        // 其他属性的转换
        return productDto;
    }

    public ProductBasicDto convertToProductBasicDto(ProductBasic productBasic) {
        ProductBasicDto productBasicDto = new ProductBasicDto();
        // 执行属性赋值
        productBasicDto.setProductId(productBasic.getId());
        productBasicDto.setProductName(productBasic.getProductname());
        productBasicDto.setPrice(productBasic.getPrice());
        productBasicDto.setSpecialPrice(productBasic.getSpecialprice());
        productBasicDto.setDescription(productBasic.getDescription());
        productBasicDto.setQuantity(productBasic.getQuantity());
        productBasicDto.setCategoryName(productBasic.getCategoryid().getCategoryname());
        productBasicDto.setParentCategoryName(productBasic.getParentid().getParentname());

        return productBasicDto;
    }
    public ProductBasicDto2 convertToProductBasicDto2(ProductBasic productBasic) {
        ProductBasicDto2 productBasicDto = new ProductBasicDto2();
        // 执行属性赋值
        productBasicDto.setProductId(productBasic.getId());
        productBasicDto.setProductName(productBasic.getProductname());
        productBasicDto.setPrice(productBasic.getPrice());
        productBasicDto.setSpecialPrice(productBasic.getSpecialprice());
        productBasicDto.setDescription(productBasic.getDescription());
        productBasicDto.setQuantity(productBasic.getQuantity());
        productBasicDto.setCategoryName(productBasic.getCategoryid().getCategoryname());
        productBasicDto.setParentCategoryName(productBasic.getParentid().getParentname());

        return productBasicDto;
    }



    public List<ProductBasic> findAllProductBasic(Member sellermemberid) {
        List<ProductBasic> list = productBasicRepository.findBySellermemberid(sellermemberid);

        if (list.isEmpty()) {
            return null;
        }

        return list;
    }

    public List<ProductBasicDto> findAllProductBasicDto(List<ProductBasic> list) {
        List<ProductBasicDto> dtoList = list.stream().map(product -> {
            ProductBasicDto dto = new ProductBasicDto();
            dto.setProductId(product.getId());
            dto.setSellermemberid(product.getSellermemberid().getId());
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
        }).collect(Collectors.toList());

        return dtoList;
    }

    @Transactional
    public ProductBasic updateProduct(ProductBasic ed, ProductBasic up) {
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
        if (up.getCategoryid() != null) {
            // 直接将新的categoryid设置给ProductBasic
            ed.setCategoryid(up.getCategoryid());
            ed.setParentid(up.getParentid());
        }

        if (up.getSellermemberid() != null) {
            Integer memberId = up.getSellermemberid().getId(); // 假设 Member 对象有名为 "id" 的属性
            Member mb = mRepo.findById(memberId).orElse(null);
            if (mb != null) {
                ed.setSellermemberid(mb);
            }

        }

        return productBasicRepository.save(ed);
    }

    public ProductBasicDto findNewOne(ProductBasic upd) {

        ProductBasicDto dto = new ProductBasicDto();
        dto.setProductId(upd.getId());
        dto.setProductName(upd.getProductname());
        dto.setPrice(upd.getPrice());
        if (upd.getCategoryid() != null) {
            dto.setCategoryName(upd.getCategoryid().getCategoryname());
            if (upd.getCategoryid().getParentid() != null) {
                dto.setParentCategoryName(upd.getCategoryid().getParentid().getParentname());
            }
        }
        dto.setSellermemberid(upd.getSellermemberid().getId());
        dto.setSpecialPrice(upd.getSpecialprice());
        dto.setQuantity(upd.getQuantity());
        dto.setDescription(upd.getDescription());

        return dto;
    }

    public Page<ProductBasicDto> getAllProductByPage(Pageable pageable, Integer memberIdd) {
        if (memberIdd != null) {
            Page<ProductBasic> pageByMemberId = productBasicRepository.findProductBasicBySellermemberid(memberIdd, pageable);

            List<ProductBasicDto> productDtos = pageByMemberId.getContent().stream()
                    .map(this::convertToProductBasicDto)
                    .collect(Collectors.toList());

            return new PageImpl<>(productDtos, pageable, pageByMemberId.getTotalElements());
        }

        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Transactional
    public void removePd(ProductBasic qu) {
        qu.setQuantity(0);
        productBasicRepository.save(qu);
    }

    public Optional<ProductBasicDto> findByIdAndSellerId(Integer id, Integer memberIdd) {
        Optional<ProductBasic> productBasic = productBasicRepository.findByIdAndSellerId(id, memberIdd);

        if (productBasic.isPresent()) {
            ProductBasicDto productBasicDto = convertToProductBasicDto(productBasic.get());
            return Optional.of(productBasicDto);
        } else {
            return Optional.empty();
        }
    }

    public void saveProducts(List<ProductBasic> products) {
        productBasicRepository.saveAll(products);

    }


//
//
//
//    public Optional<ProductBasicDto> getProductDTOById(Integer productId) {
//        // Logic to fetch and assemble data into ProductDTO
//        return Optional.empty(); // Placeholder, you'll need to implement the actual logic
//    }




}
