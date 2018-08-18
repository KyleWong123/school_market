package com.school_market.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school_market.dao.AreaDao;
import com.school_market.dao.ShopCategoryDao;
import com.school_market.entity.Area;
import com.school_market.entity.ShopCategory;
import com.school_market.service.AreaService;
import com.school_market.service.ShopCategoryService;
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService{
	@Autowired
	private ShopCategoryDao ShopCategoryDao;

	@Override
	public List<ShopCategory> shopCategoryList(ShopCategory shopCategoryCondition) {
		// TODO Auto-generated method stub
		return ShopCategoryDao.queryShopCategory(shopCategoryCondition);
	}

}
