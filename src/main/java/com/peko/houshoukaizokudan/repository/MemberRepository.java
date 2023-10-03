package com.peko.houshoukaizokudan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	Member findByUsername(String username);		
	
}
