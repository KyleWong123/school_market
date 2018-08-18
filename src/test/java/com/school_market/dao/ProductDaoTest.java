package com.school_market.dao;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.school_market.BaseTest;
import com.school_market.entity.Product;
import com.school_market.entity.ProductCategory;
import com.school_market.entity.Shop;

public class ProductDaoTest extends BaseTest{
	@Autowired
	private ProductDao productDao;
	@Test
	@Ignore
	public void testInsertProduct(){
		Shop shop = new Shop();
		shop.setShopId(45L);
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(2L);
		Product product = new Product();
		product.setCreateTime(new Date());
		product.setEnableStatus(0);
		product.setImgAddr("≤‚ ‘");
		product.setLastEditTime(new Date());
		product.setNormalPrice("25");
		product.setPriority(1);
		product.setPromotionPrice("20");
		product.setProductCategory(pc);
		product.setShop(shop);
		product.setProductName("≤‚ ‘ppp");
		product.setProductDesc("sdfsdf");
		productDao.insertProduct(product);
		
	}
	@Test
	@Ignore
	public void testQuetyByProductId(){
		int productId=4;
		productDao.queryProductById(productId);
	}
	
	@Test
	@Ignore
	public void testUpdateProduct(){
		Shop shop = new Shop();
		shop.setShopId(45L);
		Product product = new Product();
		product.setProductName("≤‚ ‘");
		product.setProductId(4);
		product.setShop(shop);
		productDao.updateProduct(product);
	}
	
	@Test
	@Ignore
	public void testQueryProductList(){
		Product productCondition = new Product();
		productCondition.setProductName("≤‚ ‘");
		//productCondition.setEnableStatus(1);
		List<Product> list = productDao.queryProductList(productCondition, 0, 4);
		System.out.println(list.size());
		int count = productDao.queryProductCount(productCondition);
		System.out.println(count);
	}
	@Test
	public void testUpdateProductById(){
		productDao.updateProductCategoryToNull(16);
	}
	
	
}
