 package com.school_market.web.shopadmin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school_market.dto.ProductCategoryExecution;
import com.school_market.dto.Result;
import com.school_market.dto.ShopExecution;
import com.school_market.entity.Area;
import com.school_market.entity.PersonInfo;
import com.school_market.entity.ProductCategory;
import com.school_market.entity.Shop;
import com.school_market.entity.ShopCategory;
import com.school_market.enums.ProductCategoryStateEnum;
import com.school_market.enums.ShopStateEnum;
import com.school_market.exception.ProductCategoryOperationException;
import com.school_market.exception.ShopOperationException;
import com.school_market.service.AreaService;
import com.school_market.service.ProductCategoryService;
import com.school_market.service.ShopCategoryService;
import com.school_market.service.ShopService;
import com.school_market.util.CodeUtil;
import com.school_market.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/shopadmin")
public class productCategoryManagementController {
	@Autowired
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = "/getproductcategorylist", method = { RequestMethod.GET })
	@ResponseBody
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
		/*
		 * Shop shop = new Shop(); shop.setShopId(45L);
		 * request.getSession().setAttribute("currentShop", shop);
		 */
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> list = null;
		if (currentShop != null && currentShop.getShopId() > 0) {
			list = productCategoryService.getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true, list);
		} else {
			// ProductCategoryStateEnum ps =
			return null;

		}

	}

	@RequestMapping(value = "/addproductcategorys", method = { RequestMethod.POST })
	@ResponseBody
	private Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		for (ProductCategory pc : productCategoryList) {
			pc.setCreateTime(new Date());
			pc.setShopId(currentShop.getShopId());
		}
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				ProductCategoryExecution pe = productCategoryService.batchInsertProductCategory(productCategoryList);
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);

				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				// TODO: handle exception
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一种商品类别");
		}
		return modelMap;

	}
	
	@RequestMapping(value = "/removeproductcategorys", method = { RequestMethod.POST })
	@ResponseBody
	private Map<String, Object> removeProductCategorys(Long productCategoryId,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		if (productCategoryId != null && productCategoryId> 0) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);

				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				// TODO: handle exception
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一种商品类别");
		}
		return modelMap;

	}

}
