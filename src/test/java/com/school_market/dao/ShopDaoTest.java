package com.school_market.dao;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.school_market.BaseTest;
import com.school_market.entity.Area;
import com.school_market.entity.PersonInfo;
import com.school_market.entity.Shop;
import com.school_market.entity.ShopCategory;

public class ShopDaoTest extends BaseTest{
	@Autowired
	private ShopDao shopDao;
	@Test
	@Ignore
	public void testInsertShop(){
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
	  shop.setAdvice("�����");
	  shop.setCreateTime(new Date());
	  shop.setEnableStatus(1);
	  shop.setPhone("15129173235");
	  shop.setPriority(1);
	  shop.setShopName("����");
	  shop.setShopAddr("����");
	  shop.setShopDesc("����");
	  int effectNum = shopDao.inertShop(shop);
	  
	}
	@Test
	@Ignore
	public void testupdateShop(){
	  Shop shop = new Shop();
	  shop.setShopId(1L);
	  shop.setShopName("������̩");
	  shop.setShopAddr("������̨���");
	  shop.setLastEditTime(new Date());
	  shopDao.updateShop(shop);
	  
	}
	@Test
	@Ignore
	public void testQueryByShopId(){
	long shopId=35L;
	Shop shop = shopDao.queryByShopId(shopId);
	System.out.println(shop.getShopImg());
	System.out.println(shop.getArea().getAreaName());
	}
	@Test
	public void testQueryShopList(){
		Shop shopCondition = new Shop();
		/*PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);*/
		ShopCategory childCategory = new ShopCategory();
		ShopCategory parentCategory = new ShopCategory();
		parentCategory.setShopCategoryId(1L);
		childCategory.setParent(parentCategory);
		shopCondition.setShopCategory(childCategory);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, 0,10);
		System.out.println("�����б�Ĵ�С"+shopList.size());
		int count = shopDao.queryShopCount(shopCondition);
		System.out.println("��������"+count);
		/*ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(2L);
		shopCondition.setShopCategory(sc);
		shopList = shopDao.queryShopList(shopCondition, 0,10);
		System.out.println("�µ�"+shopList.size());
		count = shopDao.queryShopCount(shopCondition);
		System.out.println(count);*/
		
	}

}
