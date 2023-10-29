package com.peko.houshoukaizokudan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//@EnableWebSecurity
public class SecurityConfig  {
//	extends WebSecurityConfigurerAdapter

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//				.authorizeRequests()
//				.antMatchers("/admin/**").hasRole("超級管理員")
//				.antMatchers("/seller/**").hasAnyRole("賣家", "超級管理員")
//				.antMatchers("/customer/**").hasRole("顧客")
//				.antMatchers("/public/**").permitAll()
//				.anyRequest().authenticated()
//				.and()
//				.httpBasic();
//
//		// Disable CSRF and Frame options for simplicity in this example
//		http.csrf().disable();
//		http.headers().frameOptions().disable();
//	}
}
