package com.school_market.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.school_market.entity.ProductCategory;

public interface ProductCategoryDao {
	/***
	 * ͨ����Ʒid��ѯ������Ʒ���
	 */

	List<ProductCategory> queryProductCategoryList(long shopId);
	/***
	 * ���������Ʒ���
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	
	/***
	 * ɾ����Ʒ���
	 */
	int deleteProductCategory(@Param("productCategoryId")long productCategoryId,@Param("shopId")long shopId);
}
