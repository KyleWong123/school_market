package com.school_market.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.school_market.dto.ProductCategoryExecution;
import com.school_market.entity.Area;
import com.school_market.entity.ProductCategory;
import com.school_market.exception.ProductCategoryOperationException;

public interface ProductCategoryService {
	/***
	 * ��ѯָ�������µ�������Ʒ���
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
	/**
	 * ��������
	 */
	ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList)
	throws ProductCategoryOperationException;
	/***
	 * ɾ����Ʒ���
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) 
			throws ProductCategoryOperationException;
}
