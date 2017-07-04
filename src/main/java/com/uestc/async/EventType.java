package com.uestc.async;
/**
 * 表示发生什么事件
 * @author liukunsheng
 *
 */
public enum EventType {
	LIKE(0),
	COMMENT(1),
	LOGIN(2),
	MAIL(3);
	private int value;
	EventType(int value){
		this.value=value;
	}
	public int getValue(){
		return value;
	}
	
}
