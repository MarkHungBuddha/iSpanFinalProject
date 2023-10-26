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



}
