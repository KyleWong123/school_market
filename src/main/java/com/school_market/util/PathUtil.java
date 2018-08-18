package com.school_market.util;
/***
 * ·������ 1.������ĿͼƬ�ĸ�·������·��  
 * @author wf
 *
 */
public class PathUtil {
	//��ȡ�ļ��ķָ���
	private static String seperator = System.getProperty("file.seperator");
	public static String getImgBasePath(){
		String os = System.getProperty("os.name");
		String basePath = "";
		//windowsϵͳ
		if(os.toLowerCase().startsWith("win")){
			//���·��
			basePath="D:/maven/image/";
		}else{
			basePath="*/home/image";//����ϵͳ
		}
		//basePath = basePath.replace("/", seperator);
		return basePath;
	}
	//��ȡ���̵Ĵ洢·��
	public static String getShopImagePath(long shopId){
		String imagePath = "/upload/image/"+shopId+"/";
		return imagePath;
	}

}
