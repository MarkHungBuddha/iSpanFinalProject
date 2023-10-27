package com.peko.houshoukaizokudan.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;

public interface OrderBasicRepository extends JpaRepository<OrderBasic, Integer> {


	List<OrderBasic> findOrderBasicBybuyer(Member buyer);

	@Query("SELECT SUM(ob.totalamount) FROM OrderBasic ob WHERE FUNCTION('YEAR', ob.merchanttradedate) = :year AND ob.seller.id = :memberIdd")
	Integer findTotalAmountByYearAndSeller(@Param("year") Integer year, @Param("memberIdd") Integer memberIdd);
	@Query("SELECT SUM(ob.totalamount) FROM OrderBasic ob WHERE FUNCTION('YEAR', ob.merchanttradedate) = :year AND FUNCTION('MONTH', ob.merchanttradedate) = :month AND ob.seller.id = :memberIdd")
	Integer findTotalAmountByYearAndMonthAndSeller(@Param("year") Integer year, @Param("month") Integer month, @Param("memberIdd") Integer memberIdd);


}
