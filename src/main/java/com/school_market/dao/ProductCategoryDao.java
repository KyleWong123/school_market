package com.school_market.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.school_market.entity.ProductCategory;

public interface ProductCategoryDao {
	/***
	 * 通过商品id查询店铺商品类别
	 */

	List<ProductCategory> queryProductCategoryList(long shopId);
	/***
	 * 批量添加商品类别
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	
	/***
	 * 删除商品类别
	 */
	int deleteProductCategory(@Param("productCategoryId")long productCategoryId,@Param("shopId")long shopId);
}
