package com.peko.houshoukaizokudan.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.repository.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository mRepo;
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	
	public Member addMember(Member mb) {
		String encodePwd = pwdEncoder.encode(mb.getPasswdbcrypt());
		mb.setPasswdbcrypt(encodePwd);
		return mRepo.save(mb);
	}

	public boolean checkIfUsernameExist(String username) {
		Member dbUser = mRepo.findByUsername(username);

		if (dbUser != null) {
			return true;
		} else {
			return false;
		}
	}

	public Member checkLogin(String username, String inputpwd) {
		Member dbUser = mRepo.findByUsername(username);

		if (dbUser != null) {
			if (pwdEncoder.matches(inputpwd, dbUser.getPasswdbcrypt())) {
				return dbUser;
			}
		}
		return null;

	}

	public Member findById(Integer id) {
		return mRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("MemberType not found for id: " + id));
		
	}
}
