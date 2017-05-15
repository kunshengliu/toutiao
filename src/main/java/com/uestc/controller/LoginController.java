package com.uestc.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uestc.service.UserService;
import com.uestc.util.ToutiaoUtils;
@Controller
public class LoginController {
	private static final Logger logger=LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;
	@RequestMapping("/reg/")
	@ResponseBody
	public String register(Model model,@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam(value="rember",defaultValue="0") int remberme,
			HttpServletResponse response){		
		try {
			Map<String,Object> map = userService.register(username, password,remberme);
			if(map.containsKey("ticket")){
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				cookie.setPath("/");
				if(remberme>0){
					cookie.setMaxAge(3600*24);
				}
				response.addCookie(cookie);
				return ToutiaoUtils.getJSONString(0,"注册成功");
			}else{
				return ToutiaoUtils.getJSONString(1, "注册失败");
			}	
		} catch (Exception e) {
			logger.error("注册异常"+e.getMessage());
			return ToutiaoUtils.getJSONString(1,"注册异常");
		}
	}
	
	
	
	@RequestMapping("/login/")
	@ResponseBody
	public String login(Model model,@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam(value="rember",defaultValue="0") int remberme,
			HttpServletResponse response){		
		try {
			
			Map<String,Object> map = userService.login(username, password);
			if(map.containsKey("ticket")){
				//把ticket加入cookie
				Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
				cookie.setPath("/");
				if(remberme>0){
					cookie.setMaxAge(3600*24); //设置cookie的有效时间。
				}
				response.addCookie(cookie);				
				return ToutiaoUtils.getJSONString(0,"登录成功");
			}else{
				return ToutiaoUtils.getJSONString(1, "登录失败");
			}	
		} catch (Exception e) {
			logger.error("登录异常"+e.getMessage());
			return ToutiaoUtils.getJSONString(1,"登录异常");

		}
	}
	@RequestMapping(path="/logout/",method={RequestMethod.GET,RequestMethod.POST})
	public String logout(@CookieValue("ticket") String ticket){
		userService.logout(ticket);	
		return "redirect:/";
		
	}
	
	
}
