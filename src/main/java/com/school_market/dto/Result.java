package com.school_market.dto;
/***
 * 分装JSON对象，返回所有结果都是用它
 * @author Administrator
 *
 */
public class Result<T> {
	private boolean success;//是否成功标志
	private T data;//成功是返回数据
	private String erroMsg;
	private int errorCode;//错误信息
	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}
	//成功是的构造器
	public Result(boolean success, T data) {
		super();
		this.success = success;
		this.data = data;
	}
	//错误是的构造器
	public Result(boolean success, String erroMsg, int errorCode) {
		super();
		this.success = success;
		this.erroMsg = erroMsg;
		this.errorCode = errorCode;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getErroMsg() {
		return erroMsg;
	}
	public void setErroMsg(String erroMsg) {
		this.erroMsg = erroMsg;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
