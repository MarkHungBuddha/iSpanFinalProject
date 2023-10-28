package com.peko.houshoukaizokudan.Repository;

import java.util.List;

import com.peko.houshoukaizokudan.model.OrderDetail;
import com.peko.houshoukaizokudan.model.ProductBasic;
import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderBasicRepository extends JpaRepository<OrderBasic, Integer> {

    List<OrderBasic> findOrderBasicBybuyer(Member buyer);

    boolean existsByIdAndBuyer_Id(Integer orderId, Integer memberId);

    @Query("SELECT SUM(ob.totalamount) FROM OrderBasic ob WHERE FUNCTION('YEAR', ob.merchanttradedate) = :year AND ob.seller.id = :memberIdd")
    Integer findTotalAmountByYearAndSeller(@Param("year") Integer year, @Param("memberIdd") Integer memberIdd);
    @Query("SELECT SUM(ob.totalamount) FROM OrderBasic ob WHERE FUNCTION('YEAR', ob.merchanttradedate) = :year AND FUNCTION('MONTH', ob.merchanttradedate) = :month AND ob.seller.id = :memberIdd")
    Integer findTotalAmountByYearAndMonthAndSeller(@Param("year") Integer year, @Param("month") Integer month, @Param("memberIdd") Integer memberIdd);


}
