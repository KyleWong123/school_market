package com.school_market.web.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.school_market.entity.HeadLine;
import com.school_market.entity.ShopCategory;
import com.school_market.service.HeadLineService;
import com.school_market.service.ShopCategoryService;

@Controller
@RequestMapping(value="/frontend")
public class MainPageController {
	@Autowired
	private HeadLineService headLineService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	/***
	 * 初始化前端展示页面，包括头条展示以及一级店铺类别
	 */
	@RequestMapping(value="/listmainpageinfo",method={RequestMethod.GET})
	@ResponseBody
	private Map<String,Object> listMainPageInfo(){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		//获取一级店铺（即parentId为null的店铺类别）
		try {
			shopCategoryList = shopCategoryService.shopCategoryList(null);
			modelMap.put("shopCategoryList",shopCategoryList);
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		List<HeadLine> headLineList = new ArrayList<HeadLine>();
		try {
			//获取enableStatus为1的头条
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			headLineList = headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
		
	}
}
