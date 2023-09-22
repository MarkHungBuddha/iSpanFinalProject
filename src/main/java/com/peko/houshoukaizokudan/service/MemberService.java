package com.peko.houshoukaizokudan.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.repository.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository mRepo;



}
