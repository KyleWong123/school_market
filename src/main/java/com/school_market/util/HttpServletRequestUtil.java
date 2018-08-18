package com.school_market.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {
	public static int getInt(HttpServletRequest request ,String key){
		try {
			return Integer.decode(request.getParameter(key));
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		}
	}
	public static long getLong(HttpServletRequest request ,String key){
		try {
			return Long.valueOf(request.getParameter(key));
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		}
	}
	
	public static double getDouble(HttpServletRequest request ,String key){
		try {
			return Double.valueOf(request.getParameter(key));
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		}
	}
	
	public static boolean getBoolean(HttpServletRequest request ,String key){
		try {
			return Boolean.valueOf(request.getParameter(key));
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	public static String getString(HttpServletRequest request ,String key){
		try {
			String result = request.getParameter(key);
			//如果字符串不为空，去电左右空格
			if(result!=null){
				result=result.trim();
			}
			//如果为空，则返回空值
			if("".equals(result)){
				result=null;
			}
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

}
