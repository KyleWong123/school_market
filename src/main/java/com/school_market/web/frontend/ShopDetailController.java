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

import com.school_market.dto.ProductExecution;

import com.school_market.entity.Product;
import com.school_market.entity.ProductCategory;
import com.school_market.entity.Shop;

import com.school_market.service.ProductCategoryService;
import com.school_market.service.ProductService;
import com.school_market.service.ShopService;
import com.school_market.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ShopService shopService;

	/**
	 * 获取店铺信息以及店铺下面的商品类别列表
	 */
	@RequestMapping(value = "/listshopdetailpageinfo", method = { RequestMethod.GET })
	@ResponseBody
	private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从前端获取店铺id
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Shop shop = null;
		List<ProductCategory> productCategoryList = null;
		// 空值判断
		if (shopId != -1) {
			// 获取店铺id为shopId的店铺信息
			shop = shopService.getShopById(shopId);
			// 获取该店铺下的类别商品列表
			productCategoryList = productCategoryService.getProductCategoryList(shopId);
			modelMap.put("shop", shop);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);

		} else {

			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");

		}
		return modelMap;
	}

	/**天健下的店铺列表条件分页列出店铺下的所有商品
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/listproductsbyshop", method = { RequestMethod.GET })
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取一夜需要显示出的数据条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 从前端获取店铺id
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		// 非空判断
		if ((pageIndex > -1) && (pageSize > -1)&&(shopId>-1)) {
			// 获取商品类别id
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			// 获取模糊查询的名字
			String productName = new String(request.getParameter("productName").getBytes("ISO-8859-1"), "utf-8");
		//	System.out.println(productName);
			// 获取组合查询条件
			Product productCondition = compactProductCondition4Search(shopId,productCategoryId, productName);
			// 根据查询条件获取店铺列表
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}
	/**
	 * 组合查询条件
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */

	private Product compactProductCondition4Search(long shopId,long productCategoryId, String productName) {
		// TODO Auto-generated method stub
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if(productCategoryId!=-1L){
			//查询某个商品类别下的商品列表
			ProductCategory  productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if (productName != null) {
			productCondition.setProductName(productName);
		}
		productCondition.setEnableStatus(1);
		return productCondition;
	}
}
