package com.school_market.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.school_market.BaseTest;
import com.school_market.dto.ImageHolder;
import com.school_market.dto.ProductExecution;
import com.school_market.entity.Product;
import com.school_market.entity.ProductCategory;
import com.school_market.entity.Shop;
import com.school_market.enums.ProductStateEnum;

public class ProductServiceTest extends BaseTest{
	@Autowired
	private ProductService productService;
	@Test
	//@Ignore
	public void testAddProduct() throws FileNotFoundException{
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(45L);
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(2L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("�����������");
		product.setProductDesc("��Ʒ����");
		product.setPriority(10);
		product.setCreateTime(new Date());
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		//��������ͼ
		File thumbnailFile = new File("D:/maven/image/coffe.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(),is);
		//������Ʒ�����б�
		File productImg1 = new File("D:/maven/image/��1.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("D:/maven/image/��.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(),is1));
		productImgList.add(new ImageHolder(productImg2.getName(),is2));
		//�����Ʒ
		ProductExecution pp = productService.addProduct(product, thumbnail, productImgList);
		
	}
	@Test
	@Ignore
	public void testModifyProduct() throws FileNotFoundException{
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(45L);
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(2L);
		product.setShop(shop);
		product.setProductCategory(productCategory);
		product.setProductId(4);
		product.setProductDesc("������ƽ");
		product.setProductName("������Ʒ");
		//��������ͼ
				File thumbnailFile = new File("D:/maven/image/coffe.jpg");
				InputStream is = new FileInputStream(thumbnailFile);
				ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(),is);
				//������Ʒ�����б�
				File productImg1 = new File("D:/maven/image/��.jpg");
				InputStream is1 = new FileInputStream(productImg1);
				File productImg2 = new File("D:/maven/image/c.png");
				InputStream is2 = new FileInputStream(productImg2);
				List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
				productImgList.add(new ImageHolder(productImg1.getName(),is1));
				productImgList.add(new ImageHolder(productImg2.getName(),is2));
				productService.modifyProduct(product, thumbnail, productImgList);
	}
}
