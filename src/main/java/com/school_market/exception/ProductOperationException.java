package com.school_market.exception;

public class ProductOperationException extends RuntimeException{
	

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -748096767269121981L;

	/***
	 *  
	 * @param msg
	 */
	public ProductOperationException(String msg){
		super(msg);
	}

}
