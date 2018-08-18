package com.school_market.web.frontend;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.school_market.dto.ShopExecution;
import com.school_market.entity.Area;
import com.school_market.entity.Shop;
import com.school_market.entity.ShopCategory;
import com.school_market.service.AreaService;
import com.school_market.service.ShopCategoryService;
import com.school_market.service.ShopService;
import com.school_market.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopListController {
	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private ShopService shopService;

	/**
	 * 返回商品列表里面的shopCategory列表，以及区域信息列表
	 */
	@RequestMapping(value="/listshoppageinfo",method={RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> listShopPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从前端获取parentId
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		if (parentId != -1) {
			// 如果parentId存在，则取出二级shopCategory列表
			try {
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parent = new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.shopCategoryList(shopCategoryCondition);
			} catch (Exception e) {
				// TODO: handle exception
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
			try {
				// 如果parentId不存在，则取出所有一级shopCategory（全部商品列表）
				shopCategoryList = shopCategoryService.shopCategoryList(null);
			} catch (Exception e) {
				// TODO: handle exception
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		List<Area> areaList = null;
		try {
			// 获取所有区域信息
			areaList = areaService.getQueryList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
			return modelMap;
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	/**
	 * 获取指定查询天健下的店铺列表
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/listshops",method={RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取一夜需要显示出的数据条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 非空判断
		if ((pageIndex > -1) && (pageSize > -1)) {
			// 获取一级类别id
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			// 获取二级类别Id
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			// 获取区域Id
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			// 获取模糊查询的名字
			String shopName = new String(request.getParameter("shopName").getBytes("ISO-8859-1"),"utf-8");
			System.out.println(shopName);
			// 获取组合查询条件
			Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
			// 根据查询条件获取店铺列表
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}

	/***
	 * 组合查询条件
	 * @param parentId
	 * @param shopCategoryId
	 * @param areaId
	 * @param shopName
	 * @return
	 */
	private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
		// TODO Auto-generated method stub
	Shop shopCondition = new Shop();
	if(parentId!=-1L){
		//查询某个一级shopCategory下面的所欲二级shopCategory里的店铺列表
		ShopCategory childCategory = new ShopCategory();
		ShopCategory parentCategory = new ShopCategory();
		parentCategory.setShopCategoryId(parentId);
		childCategory.setParent(parentCategory);
		shopCondition.setShopCategory(childCategory);
	}
	if(shopCategoryId!=-1L){
		//查询二级shopCategory下面的店铺列表
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(shopCategoryId);
		shopCondition.setShopCategory(shopCategory);
	}
	if(areaId!=-1L){
		//查询某个区域id下的店铺列表
		Area area = new Area();
		area.setAreaId(areaId);
		shopCondition.setArea(area);
	}
	if(shopName!=null){
		shopCondition.setShopName(shopName);
	}
	shopCondition.setEnableStatus(1);
		return shopCondition;
	}
}
