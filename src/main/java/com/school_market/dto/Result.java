package com.school_market.dto;
/***
 * ��װJSON���󣬷������н����������
 * @author Administrator
 *
 */
public class Result<T> {
	private boolean success;//�Ƿ�ɹ���־
	private T data;//�ɹ��Ƿ�������
	private String erroMsg;
	private int errorCode;//������Ϣ
	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}
	//�ɹ��ǵĹ�����
	public Result(boolean success, T data) {
		super();
		this.success = success;
		this.data = data;
	}
	//�����ǵĹ�����
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
