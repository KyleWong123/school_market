package com.school_market.util;

public class PageCalculator {
	public static int calculatorRowIndex(int pageIndex,int pageSize){
		return (pageIndex>0)?(pageIndex-1)*pageSize:0;
		
	}

}
