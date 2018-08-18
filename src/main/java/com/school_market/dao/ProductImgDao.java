package com.school_market.dao;

import java.util.List;

import com.school_market.entity.ProductImg;

public interface ProductImgDao {
	/***
	 * 批量添加商品图片
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	/***
	 * 删除指定商品下的详情图
	 */
	int deleteProductImgByProductId(long productId);
	
	/***
	 * 查询缩略图
	 */
	List<ProductImg> queryProductImgList(long prouctId);

}
