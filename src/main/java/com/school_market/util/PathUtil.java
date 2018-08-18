package com.school_market.util;
/***
 * 路径处理 1.返回项目图片的根路径和子路径  
 * @author wf
 *
 */
public class PathUtil {
	//获取文件的分隔符
	private static String seperator = System.getProperty("file.seperator");
	public static String getImgBasePath(){
		String os = System.getProperty("os.name");
		String basePath = "";
		//windows系统
		if(os.toLowerCase().startsWith("win")){
			//存放路径
			basePath="D:/maven/image/";
		}else{
			basePath="*/home/image";//其他系统
		}
		//basePath = basePath.replace("/", seperator);
		return basePath;
	}
	//获取店铺的存储路径
	public static String getShopImagePath(long shopId){
		String imagePath = "/upload/image/"+shopId+"/";
		return imagePath;
	}

}
