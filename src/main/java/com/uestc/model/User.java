package com.uestc.model;
/**
 * 
 * @author liukunsheng
 *
 */
public class User {
	private int id;
	private String name;//名字
	private String password;//密码
	private String salt;//盐
	private String headUrl;//头像的Url
	public User(){
		
	}
	public User(String name){
		this.name = name;
		this.password="";
		this.salt="";
		this.headUrl="";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	
	
	
}
