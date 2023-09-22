package com.peko.houshoukaizokudan.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

	Member findByUsername(String username);
	
}
