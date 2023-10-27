package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.QandA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QandARepository extends JpaRepository<QandA, Integer> {

    List<QandA> findByProductid_Id(Integer productId);
}
