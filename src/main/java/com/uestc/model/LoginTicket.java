package com.uestc.model;

import java.util.Date;
/**
 * login ticket对应的model
 * 
 * @author liukunsheng
 *
 */
public class LoginTicket {
	private int id;//id
	private int userId;//用户id
	private String ticket;//
	private Date expired;//过期时间
	private int status;//状态，0有效，1无效
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public Date getExpired() {
		return expired;
	}
	public void setExpired(Date expired) {
		this.expired = expired;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
