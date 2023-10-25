package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductBasicRepository  extends JpaRepository<ProductBasic,Integer> {
    List<ProductBasic> findByProductnameLike(String keyword);

    @Query("SELECT p FROM ProductBasic p " +
            "LEFT JOIN FETCH p.categoryid c " +
            "LEFT JOIN FETCH c.parentid " +
            "WHERE p.id = :id")
    Optional<ProductBasic> findByIdWithRelationships(@Param("id") Integer id);

}
