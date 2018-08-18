package com.school_market.service;

import java.util.List;


import com.school_market.entity.Area;
import com.school_market.entity.ShopCategory;
public interface ShopCategoryService {
	List<ShopCategory> shopCategoryList(ShopCategory shopCategoryCondition);

}
