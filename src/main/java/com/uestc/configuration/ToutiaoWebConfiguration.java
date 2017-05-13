package com.uestc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.uestc.interceptor.PassportInterceptor;
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
	
	@Autowired
	private PassportInterceptor passportInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(passportInterceptor);
		super.addInterceptors(registry);
	}
	
	
	
	
}
