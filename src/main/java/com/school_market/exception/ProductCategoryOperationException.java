package com.school_market.exception;

public class ProductCategoryOperationException extends RuntimeException{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1891104669658648397L;

	/***
	 *  
	 * @param msg
	 */
	public ProductCategoryOperationException(String msg){
		super(msg);
	}

}
