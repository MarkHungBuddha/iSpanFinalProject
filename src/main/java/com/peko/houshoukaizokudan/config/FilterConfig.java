//package com.peko.houshoukaizokudan.config;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
//
//@Configuration
//public class FilterConfig {
//    @Bean
//    public FilterRegistrationBean<OpenEntityManagerInViewFilter> openEntityManagerInViewFilter() {
//        FilterRegistrationBean<OpenEntityManagerInViewFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new OpenEntityManagerInViewFilter());
//        registrationBean.addUrlPatterns("/*"); // 您可以根据需求配置URL模式
//        return registrationBean;
//    }
//}
