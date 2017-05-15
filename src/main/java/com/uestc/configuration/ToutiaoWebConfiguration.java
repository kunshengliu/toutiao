package com.uestc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.uestc.interceptor.LoginRequiredInterceptor;
import com.uestc.interceptor.PassportInterceptor;
/**
 * 
 * 对拦截器进行注册
 * 
 * @author liukunsheng
 *
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
	
	@Autowired
	private PassportInterceptor passportInterceptor;
	
	@Autowired
	private LoginRequiredInterceptor loginRequiredInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(passportInterceptor);
		registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
		super.addInterceptors(registry);
	}
	
	
	
	
}
