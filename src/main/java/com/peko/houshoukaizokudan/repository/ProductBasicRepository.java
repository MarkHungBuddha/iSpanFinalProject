package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductBasic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBasicRepository  extends JpaRepository<ProductBasic,Integer> {
    List<ProductBasic> findByProductnameLike(String keyword);
}