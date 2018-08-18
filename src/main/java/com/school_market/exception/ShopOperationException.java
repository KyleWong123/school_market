package com.school_market.exception;

public class ShopOperationException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/***
	 *  
	 * @param msg
	 */
	public ShopOperationException(String msg){
		super(msg);
	}

}
