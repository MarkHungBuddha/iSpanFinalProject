package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ParentCategoryData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentCategoryRepository extends JpaRepository<ParentCategoryData, Integer> {
}
