package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.QandA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QandARepository extends JpaRepository<QandA, Integer> {

    List<QandA> findByProductid_Id(Integer productId);
    List<QandA> findBySellerMember_IdAndAnswerIsNull(Integer memberId);


    @Query(value ="SELECT * FROM dbo.qanda WHERE buyermemberid = ?1 ",nativeQuery = true)
    List<QandA> findAllByBuyerMember_Id(Integer memberId);

    @Query(value ="SELECT * FROM QandA where sellermemberid = ?1",nativeQuery = true)
    List<QandA> findAllBySellerMember_Id(Integer sellermemberid);


}
