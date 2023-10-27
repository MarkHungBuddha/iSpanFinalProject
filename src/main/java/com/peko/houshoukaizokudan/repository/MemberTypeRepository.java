package com.peko.houshoukaizokudan.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.MemberType;

public interface MemberTypeRepository extends JpaRepository<MemberType, Integer> {
	Optional<MemberType> findById(Integer id);
}
