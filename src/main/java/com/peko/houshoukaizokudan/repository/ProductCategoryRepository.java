package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    Optional<ProductCategory> findById(Integer id);

    //價格範圍搜尋 + 分類名稱搜尋
    //以ProductCategory 表找 categoryname 取的 categoryid
    @Query("SELECT pc.id FROM ProductCategory pc WHERE pc.categoryname = :categoryname")
    Integer findCategoryIdByCategoryName(String categoryname);

    //以ProductBasic 表外來鍵 categoryid 找到 price 進行價格範圍的過濾功能
    @Query("SELECT pb FROM ProductBasic pb " +
            "WHERE pb.categoryid.id = :categoryid " +
            "AND pb.price >= :minPrice AND pb.price <= :maxPrice")
    Page<ProductBasic> findProductBasicsByCategoryIdAndPriceRange(Integer categoryid, Double minPrice, Double maxPrice, Pageable pageable);
}
