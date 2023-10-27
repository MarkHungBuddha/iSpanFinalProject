package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    boolean existsByIdAndProductid_Id(Integer orderDetailId, Integer productId);
}