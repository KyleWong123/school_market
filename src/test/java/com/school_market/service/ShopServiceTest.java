package com.school_market.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.school_market.BaseTest;
import com.school_market.dto.ImageHolder;
import com.school_market.dto.ShopExecution;
import com.school_market.entity.Area;
import com.school_market.entity.PersonInfo;
import com.school_market.entity.Shop;
import com.school_market.entity.ShopCategory;
import com.school_market.enums.ShopStateEnum;

public class ShopServiceTest extends BaseTest{
	@Autowired
	private ShopService shopService;
	@Test
	@Ignore
	public void testModifyShop() throws FileNotFoundException{
		Shop shop = new Shop();
		shop.setShopId(41L);
		shop.setShopName("¥Û÷Ì");
	//shop.setEnableStatus(1);
		File shopImg = new File("D:/maven/image/coffe.jpg");
		  InputStream is = new FileInputStream(shopImg);
		  ImageHolder imageHolder = new ImageHolder("coffe.jpg",is);
		  ShopExecution se =shopService.modifyShop(shop,imageHolder );
		  assertEquals(ShopStateEnum.SUCCESS.getState(),se.getState());
	}
	@Test
	//@Ignore
	public void testAddShop() throws FileNotFoundException{
		Shop shop = new Shop();
		  PersonInfo pi = new PersonInfo();
		  ShopCategory sc = new ShopCategory();
		  Area area = new Area();
		  pi.setUserId(1L);
		  sc.setShopCategoryId(1L);
		  area.setAreaId(1);
		  shop.setOwner(pi);
		  shop.setShopCategory(sc);
		   shop.setArea(area);
		  shop.setAdvice("…Û∫À÷–");
		  shop.setCreateTime(new Date());
		  shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		  shop.setPhone("15129173235");
		  shop.setPriority(1);
		  shop.setShopName("≤‚ ‘888");
		  shop.setShopAddr("±¶º¶");
		  shop.setShopDesc("≤‚ ‘2");
		  File shopImg = new File("D:/Spring/image/food1.jpg");
		  InputStream is = new FileInputStream(shopImg);
		  ImageHolder imageHolder = new ImageHolder(shopImg.getName(),is);

		  ShopExecution se = shopService.addShop(shop,imageHolder);
		  assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
		
	}
	@Test
	@Ignore
	public void testGetShopList(){
		Shop shopCondition = new Shop();
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(2L);
		shopCondition.setShopCategory(sc);
		ShopExecution se = shopService.getShopList(shopCondition, 3, 7);
		System.out.println(se.getShopList().size());
		System.out.println(se.getCount());
	}

}
