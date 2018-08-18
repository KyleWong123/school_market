package com.school_market.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.school_market.entity.Shop;

public interface ShopDao {
	/***
	 * ��ҳ��ѯ���̣������������У�������������״̬�������������ID��owner
	 * rowIndex�ӵڼ��п�ʼȡ
	 * pageSize���ص�����
	 */
	
	List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
			@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize); 
	
	
	/***
	 * ����queryShopList����
	 */
	int queryShopCount(@Param("shopCondition")Shop shopCondition);
	 /***
	 * 
	 * ��������
	 * @wf
	 */
	int inertShop(Shop shop);
	/***
	 * ���µ���
	 */
	int updateShop(Shop shop);
	
	/***
	 * ��ѯ����
	 */
	Shop queryByShopId(long shopId);

}
