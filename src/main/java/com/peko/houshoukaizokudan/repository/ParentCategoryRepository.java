package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductBasic;
import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.ParentCategory;

import java.util.Optional;

public interface ParentCategoryRepository extends JpaRepository<ParentCategory, Integer>{

    ParentCategory findById(ParentCategory parentid);
}
