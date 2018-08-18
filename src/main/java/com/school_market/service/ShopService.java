package com.school_market.service;

import java.io.File;
import java.io.InputStream;

import com.school_market.dto.ImageHolder;
import com.school_market.dto.ShopExecution;
import com.school_market.entity.Shop;
import com.school_market.exception.ShopOperationException;

public interface ShopService {
	//添加店铺
	ShopExecution addShop(Shop shop,ImageHolder thumanail) throws ShopOperationException;
	
	//通过id查询商铺信息
	Shop getShopById(long shopId);
	
	//修改商铺信息
	ShopExecution modifyShop(Shop shop,ImageHolder thumanail) throws ShopOperationException;
	/***
	 * 分页返回列表信息
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	

}
