package com.school_market.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school_market.dao.ProductCategoryDao;
import com.school_market.dao.ProductDao;
import com.school_market.dto.ProductCategoryExecution;
import com.school_market.entity.ProductCategory;
import com.school_market.enums.ProductCategoryStateEnum;
import com.school_market.exception.ProductCategoryOperationException;
import com.school_market.service.ProductCategoryService;
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private ProductDao productDao;
	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		// TODO Auto-generated method stub
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	public ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		// TODO Auto-generated method stub
		if(productCategoryList!=null&&productCategoryList.size()>0){
			try {
				int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if(effectedNum<=0){
					throw new ProductCategoryOperationException("店铺类别创建失败");
				}else{
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw new ProductCategoryOperationException("创建失败"+e.getMessage());
			}
		}else{
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		// TODO 将此商品类别下的商品的类别id置位空
		try {
			int effectNum = productDao.updateProductCategoryToNull(productCategoryId);
			if(effectNum<0){
				throw new ProductCategoryOperationException("类别更改失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new ProductCategoryOperationException("更改失败"+e.getMessage());
		}
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum<=0){
				throw new ProductCategoryOperationException("店铺类别删除失败");

			}else{
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);

			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new ProductCategoryOperationException("删除失败"+e.getMessage());
		}
	}

}
