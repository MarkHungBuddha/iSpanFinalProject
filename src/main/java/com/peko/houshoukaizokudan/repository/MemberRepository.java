package com.peko.houshoukaizokudan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.DTO.MemberDTO;
import com.peko.houshoukaizokudan.model.Member;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Integer>{

	Member findByUsername(String username);
	


	@Query(value = "SELECT * FROM Member  WHERE memberid = ?1", nativeQuery = true)
	Member findmemberBymemberid(Integer sellerId);

	Member findByEmail(String email);

	

}
