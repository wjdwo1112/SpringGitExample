package com.example.test.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.test.commons.HttpMethodOverrideFilter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	
	@Bean
	FilterRegistrationBean<HttpMethodOverrideFilter> httpMethodOverrideFilter(){
		FilterRegistrationBean<HttpMethodOverrideFilter> registrationBean = new FilterRegistrationBean<>(new HttpMethodOverrideFilter());
		registrationBean.setFilter(new HttpMethodOverrideFilter());
		registrationBean.addUrlPatterns("/api/*"); //필터가 적용될 URL패턴
		return registrationBean;
	}
	
	
	
}
