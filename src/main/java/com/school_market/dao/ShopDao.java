package com.school_market.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.school_market.entity.Shop;

public interface ShopDao {
	/***
	 * 分页查询店铺，可输入条件有：店铺名，店铺状态，店铺类别，区域ID，owner
	 * rowIndex从第几行开始取
	 * pageSize返回的行数
	 */
	
	List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
			@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize); 
	
	
	/***
	 * 返回queryShopList总数
	 */
	int queryShopCount(@Param("shopCondition")Shop shopCondition);
	 /***
	 * 
	 * 新增店铺
	 * @wf
	 */
	int inertShop(Shop shop);
	/***
	 * 更新店铺
	 */
	int updateShop(Shop shop);
	
	/***
	 * 查询商铺
	 */
	Shop queryByShopId(long shopId);

}
