package com.school_market.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.school_market.BaseTest;
import com.school_market.entity.ProductImg;

public class ProductImgDaoTest extends BaseTest{
	@Autowired 
	private ProductImgDao productImgDao;
	@Test
	@Ignore
	public void testInsertProductImg(){
		 ProductImg productImg = new ProductImg(); 
		 productImg.setImgAddr("测试1");
		 productImg.setImgDesc("批量增加");
		 productImg.setCreateTime(new Date());
		 productImg.setPriority(1);
		 productImg.setProductId(1);
		 ProductImg productImg1 = new ProductImg(); 
		 productImg1.setImgAddr("测试2");
		 productImg1.setImgDesc("批量增加2");
		 productImg1.setCreateTime(new Date());
		 productImg1.setPriority(5);
		 productImg1.setProductId(1);
		 List<ProductImg> list = new ArrayList<ProductImg>();
		 list.add(productImg);
		 list.add(productImg1);
		 int effectedNum =productImgDao.batchInsertProductImg(list);
		 System.out.println(effectedNum);
	}
	
	@Test
	@Ignore
	public void testDeleteProductImg(){
		int productId=1;
		productImgDao.deleteProductImgByProductId(productId);
	}
	
	@Test
	public void testQueryProductImg(){
	List<ProductImg> list= productImgDao.queryProductImgList(4);
	}
}
