package com.kkb.ssm.exception;

/***
 * 自定义编译时异常
 * @author think
 *
 */
public class CustomExcetion extends Exception {

	private String msg;

	public CustomExcetion(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
