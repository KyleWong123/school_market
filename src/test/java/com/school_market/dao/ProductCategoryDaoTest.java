package com.school_market.dao;
/***
 * Dao层测试类
 * @author wf
 *
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.school_market.BaseTest;
import com.school_market.entity.Area;
import com.school_market.entity.ProductCategory;
/***
 * 测试回旋
 * @FixMethodOrder(让方法按规定的顺序执行)
 * @author Administrator
 *
 */

public class ProductCategoryDaoTest extends BaseTest{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	@Ignore
	public void testQueryByShopId(){
		long shopId=45;
		List<ProductCategory> list = productCategoryDao.queryProductCategoryList(shopId);
	}
	
	@Test
	@Ignore
	public void testBatchProductCategory(){
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryName("测试1");
		productCategory.setPriority(5);
		productCategory.setCreateTime(new Date());
		productCategory.setShopId(50L);
		ProductCategory productCategory1 = new ProductCategory();
		productCategory1.setProductCategoryName("测试2");
		productCategory1.setPriority(2);
		productCategory1.setCreateTime(new Date());
		productCategory1.setShopId(50L);
		List<ProductCategory> list = new ArrayList<ProductCategory>();
		list.add(productCategory);
		list.add(productCategory1);
		int effectedNum = productCategoryDao.batchInsertProductCategory(list);
	}
	@Test
	@Ignore
	public void testDeleteProductCategory(){
		productCategoryDao.deleteProductCategory(2, 40L);
	}
	

}
