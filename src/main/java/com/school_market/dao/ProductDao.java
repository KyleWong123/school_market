package com.school_market.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.school_market.entity.Product;
import com.school_market.entity.Shop;

public interface ProductDao {
	/***
	 * ������Ʒ
	 */
	int insertProduct(Product product);
	/***
	 * ͨ��productId��ѯΨһ����Ʒ��Ϣ
	 */
	Product queryProductById(long productId);
	/***
	 * ������Ʒ��Ϣ
	 */
	int updateProduct(Product product);
	/***
	 * ��ҳ��ѯ��Ʒ�������������У���Ʒ������Ʒ״̬������ID����Ʒ���
	 * rowIndex�ӵڼ��п�ʼȡ
	 * pageSize���ص�����
	 */
	
	List<Product> queryProductList(@Param("productCondition")Product productCondition,
			@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize); 
	/***
	 * ɾ����Ʒ֮ǰ������Ʒ���id��λ��
	 */
	int updateProductCategoryToNull(long productCategoryId);
	
	/***
	 * ����queryProductList����
	 */
	int queryProductCount(@Param("productCondition")Product productCondition);
}
