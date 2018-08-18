package com.school_market.service;

import java.io.File;
import java.io.InputStream;

import com.school_market.dto.ImageHolder;
import com.school_market.dto.ShopExecution;
import com.school_market.entity.Shop;
import com.school_market.exception.ShopOperationException;

public interface ShopService {
	//��ӵ���
	ShopExecution addShop(Shop shop,ImageHolder thumanail) throws ShopOperationException;
	
	//ͨ��id��ѯ������Ϣ
	Shop getShopById(long shopId);
	
	//�޸�������Ϣ
	ShopExecution modifyShop(Shop shop,ImageHolder thumanail) throws ShopOperationException;
	/***
	 * ��ҳ�����б���Ϣ
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	

}
