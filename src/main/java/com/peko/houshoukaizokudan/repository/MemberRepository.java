package com.peko.houshoukaizokudan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.Member;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Integer>{

	Member findByUsername(String username);

	@Query(value = "SELECT * FROM Member  WHERE memberid = ?1", nativeQuery = true)
	Member findmemberBymemberid(Integer sellerId);

	Member findByEmail(String email);

	Member findByPhone(String phone);
	
	@Query(value = "SELECT membertypeid FROM Member  WHERE memberid = ?1",nativeQuery = true)
	Integer findmembertypebymemberid(Integer memberid);




}
