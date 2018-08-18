package com.school_market.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.school_market.dto.ProductCategoryExecution;
import com.school_market.entity.Area;
import com.school_market.entity.ProductCategory;
import com.school_market.exception.ProductCategoryOperationException;

public interface ProductCategoryService {
	/***
	 * 查询指定店铺下的所有商品类别
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
	/**
	 * 批量增加
	 */
	ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList)
	throws ProductCategoryOperationException;
	/***
	 * 删除商品类别
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) 
			throws ProductCategoryOperationException;
}
