package com.school_market.dao;

import java.util.List;

import com.school_market.entity.ProductImg;

public interface ProductImgDao {
	/***
	 * ���������ƷͼƬ
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	/***
	 * ɾ��ָ����Ʒ�µ�����ͼ
	 */
	int deleteProductImgByProductId(long productId);
	
	/***
	 * ��ѯ����ͼ
	 */
	List<ProductImg> queryProductImgList(long prouctId);

}
