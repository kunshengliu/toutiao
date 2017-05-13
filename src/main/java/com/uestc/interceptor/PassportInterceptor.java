package com.uestc.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.uestc.dao.LoginTicketDAO;
import com.uestc.dao.UserDAO;
import com.uestc.model.HostHolder;
import com.uestc.model.LoginTicket;
import com.uestc.model.User;
/**
 * 用户登录的时候要进行的拦截
 * @author liukunsheng
 *
 */
@Component
public class PassportInterceptor implements  HandlerInterceptor {
	
	@Autowired
	private LoginTicketDAO loginTicketDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private HostHolder hostHolder;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String ticket=null;
		if(request.getCookies()!=null){
			for(Cookie cookie:request.getCookies()){
				if(cookie.getName().equals("ticket")){
					ticket=cookie.getValue();
					break;
				}
			}
		}
		if(ticket!=null){
			LoginTicket loginTicket = loginTicketDAO.getLonginTicketByTicket(ticket);
			if(loginTicket==null||loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0){
				return true;
			}
			User user = userDAO.selectById(loginTicket.getUserId());
			hostHolder.setUser(user);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView!=null&&hostHolder.getUser()!=null){
			modelAndView.addObject("user",hostHolder.getUser());
		}
		
		
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		hostHolder.clear();
	}

}
