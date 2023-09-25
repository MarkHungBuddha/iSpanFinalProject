package com.peko.houshoukaizokudan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.MemberRepository;

@Service
public class MemberService {
	
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	@Autowired
	private MemberRepository usersRepo;
	
	public Member addUser(Member users) {
		String encodedPwd = pwdEncoder.encode(users.getPasswdbcrypt());   // 加密
		users.setPasswdbcrypt(encodedPwd);
		return usersRepo.save(users);
	}
	
	public boolean checkIfUsernameExist(String username) {
		
		Member dbUser = usersRepo.findByUsername(username);
		
		if(dbUser != null) {
			return true;
		}else {
			return false;
		}
	}
	
	public Member checkLogin(String username, String inputPwd) {
		Member dbUser = usersRepo.findByUsername(username);
		
		if(dbUser != null) {
			                     // (要去比對的, 加密過後的)
			if(pwdEncoder.matches(inputPwd, dbUser.getPasswdbcrypt())) {
				return dbUser;
			}
		}
		
		return null;
	}

}