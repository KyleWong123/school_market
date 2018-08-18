package com.school_market.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.school_market.entity.Product;
import com.school_market.entity.Shop;

public interface ProductDao {
	/***
	 * 插入商品
	 */
	int insertProduct(Product product);
	/***
	 * 通过productId查询唯一的商品信息
	 */
	Product queryProductById(long productId);
	/***
	 * 更新商品信息
	 */
	int updateProduct(Product product);
	/***
	 * 分页查询商品，可输入条件有：商品名，商品状态，店铺ID，商品类别
	 * rowIndex从第几行开始取
	 * pageSize返回的行数
	 */
	
	List<Product> queryProductList(@Param("productCondition")Product productCondition,
			@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize); 
	/***
	 * 删除商品之前，将商品类别id置位空
	 */
	int updateProductCategoryToNull(long productCategoryId);
	
	/***
	 * 返回queryProductList总数
	 */
	int queryProductCount(@Param("productCondition")Product productCondition);
}
