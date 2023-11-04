package com.peko.houshoukaizokudan.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CustomerFilter> customerFilter() {
        FilterRegistrationBean<CustomerFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CustomerFilter());
        registrationBean.addUrlPatterns("/public/*", "/seller/*", "/customer/*");
        return registrationBean;
    }
}