package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {


    @Query("SELECT pi.imagepath FROM ProductImage pi WHERE pi.productid.id = :productId ORDER BY pi.orderID")
    List<String> findImagePathsByProductIdOrderByOrderID(Integer productId);

    //1個商品會有多張圖片, 取的imagepath 資料
    //外來鍵 productid 取得多筆重複的值,orderID 升序排序,取第一行會是最小orderID值
    @Query(value = "SELECT TOP 1 pic.imagepath FROM ProductImage pic "
            + "WHERE pic.productid = :productid "
            + "ORDER BY pic.orderID ASC", nativeQuery = true)
    String findImagepathByProductid(@Param("productid") Integer productid);



    @Query("SELECT pi.imagepath FROM ProductImage pi WHERE pi.productid.id = :id AND pi.orderID = :od")
    String findByProductIdAndOrderId(@Param("id") Integer id, @Param("od") Integer od);

    @Modifying
    @Query("DELETE FROM ProductImage pi WHERE pi.productid.id = :id AND pi.orderID = :od")
    void deleteProductImage(@Param("id") Integer id, @Param("od") Integer od);

    @Modifying
    @Query("DELETE FROM ProductImage pi WHERE pi.productid.id = :id AND pi.orderID = :od")
    void deleteById(@Param("id") Integer id, @Param("od") Integer od);
}
