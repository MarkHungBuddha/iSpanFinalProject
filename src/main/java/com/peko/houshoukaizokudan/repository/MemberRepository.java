package com.peko.houshoukaizokudan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.peko.houshoukaizokudan.model.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	Member findByUsername(String username);

	@Query(value = "SELECT * FROM Member  WHERE memberid = ?1", nativeQuery = true)
	Member findmemberBymemberid(Integer sellerId);

}
