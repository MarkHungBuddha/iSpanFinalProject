package com.peko.houshoukaizokudan.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.MemberType;
import com.peko.houshoukaizokudan.repository.MemberTypeRepository;

@Service
public class MemberTypeService {

	@Autowired
	private MemberTypeRepository mtRepo;

	public MemberType findById(Integer id) {
	    return mtRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("MemberType not found for id: " + id));
	}
}
