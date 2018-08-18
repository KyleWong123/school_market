package com.school_market.util;
/***
 * ��ͼƬ���ϱ�������ѧԺˮӡ
 * wf
 */


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.school_market.dto.ImageHolder;

import ch.qos.logback.classic.Logger;

public class ImageUtil {
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

	private static final Random r = new Random();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); // ʱ���ʽ���ĸ�ʽ		
	public static String generateThumbnail(ImageHolder thumanail,String targetAddr) {
		String realFileName = getRandomFileName();
		String extension = getFileExtension(thumanail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		//System.out.println(relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumanail.getImage()).size(200,200)
			.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f).outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("��������ͼʧ�ܣ�" + e.toString());
		}
		return relativeAddr;
	}

	/*public static String generateNormalImg(CommonsMultipartFile thumbnail, String targetAddr) {
		String realFileName =getRandomFileName();
		String extension = getFileExtension(thumbnail);
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getInputStream()).size(337, 640)
			.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
			.outputQuality(0.5f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("��������ͼʧ�ܣ�" + e.toString());
		}
		return relativeAddr;
	}*/

	/*public static List<String> generateNormalImgs(List<CommonsMultipartFile> imgs, String targetAddr) {
		int count = 0;
		List<String> relativeAddrList = new ArrayList<String>();
		if (imgs != null && imgs.size() > 0) {
			makeDirPath(targetAddr);
			for (CommonsMultipartFile img : imgs) {
				String realFileName = FileUtil.getRandomFileName();
				String extension = getFileExtension(img);
				String relativeAddr = targetAddr + realFileName + count + extension;
				File dest = new File(FileUtil.getImgBasePath() + relativeAddr);
				count++;
				try {
					Thumbnails.of(img.getInputStream()).size(600, 300)
					.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
					.outputQuality(0.5f).toFile(dest);
				} catch (IOException e) {
					throw new RuntimeException("����ͼƬʧ�ܣ�" + e.toString());
				}
				relativeAddrList.add(relativeAddr);
			}
		}
		return relativeAddrList;
	}*/
/***
 * ��������ļ���
 * @return
 * @throws IOException 
 */
	public static String getRandomFileName() {
		// TODO Auto-generated method stub
		// ��������ļ�������ǰ������ʱ����+��λ�������Ϊ����ʵ����Ŀ�з�ֹ�ļ�ͬ�������еĴ���
		int rannum =r.nextInt(89999) + 10000; // ��ȡ�����
		String nowTimeStr = sDateFormat.format(new Date()); // ��ǰʱ��
		return nowTimeStr + rannum;
	}
//����Ŀ��·�����漰����·��
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}
//��ȡ�����ļ�����չ��
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
	
	/***
	 * storePath���ļ�·������Ŀ¼��·��
	 * ���storePath���ļ�·����ɾ�����ļ�
	 * ���storePath��Ŀ¼·����ɾ��Ŀ¼�µ������ļ�
	 */
	public static void deleteFileOrPath(String storePath){
		File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
		System.out.println(storePath);
		System.out.println(fileOrPath);
		if(fileOrPath.exists()){
			if(fileOrPath.isDirectory()){
				File files[] = fileOrPath.listFiles();
				for(int i=0;i<files.length;i++){
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
	
	public static void main(String[] args) throws IOException{
		Thumbnails.of(new File("D:/Spring/image/teacher3.jpg")).size(200,200)
		.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
		.outputQuality(0.25f).toFile("D:/Spring/image/teacher33.jpg");	
	}

	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		// TODO Auto-generated method stub
		String realFileName = getRandomFileName();
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		//System.out.println(relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(337,640)
			.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f).outputQuality(0.9f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("��������ͼʧ�ܣ�" + e.toString());
		}
		return relativeAddr;
		
	}
	
}

