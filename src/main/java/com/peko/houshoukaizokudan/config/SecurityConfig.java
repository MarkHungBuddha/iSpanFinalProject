package com.peko.houshoukaizokudan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//	}
}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//				.csrf((csrf) -> csrf.disable())
//				.authorizeHttpRequests((authorize) -> {
//					authorize.antMatchers("/admin/**").hasRole("ADMIN");
//					authorize.antMatchers("/user/**").hasRole("USER");
//					authorize.anyRequest().authenticated();
//				})
//				.formLogin()
//				.and()
//				.httpBasic();
//	}



//	@Bean
//	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//		http
//				.authorizeExchange(authorizeExchangeSpec ->
//						authorizeExchangeSpec
//								.pathMatchers("/public/**").permitAll()
//								.pathMatchers("/customer/**").hasAnyRole("ADMIN", "SELLER", "BUYER")
//								.pathMatchers("/seller/**").hasAnyRole("ADMIN", "SELLER")
//								.anyExchange().authenticated()
//				)
//				.httpBasic(withDefaults())
//				.csrf(csrfSpec -> csrfSpec.disable());
//
//		return http.build();
//	}


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
