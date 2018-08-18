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
	 * ��ȡ������Ϣ�Լ������������Ʒ����б�
	 */
	@RequestMapping(value = "/listshopdetailpageinfo", method = { RequestMethod.GET })
	@ResponseBody
	private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// ��ǰ�˻�ȡ����id
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Shop shop = null;
		List<ProductCategory> productCategoryList = null;
		// ��ֵ�ж�
		if (shopId != -1) {
			// ��ȡ����idΪshopId�ĵ�����Ϣ
			shop = shopService.getShopById(shopId);
			// ��ȡ�õ����µ������Ʒ�б�
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

	/**�콡�µĵ����б�������ҳ�г������µ�������Ʒ
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/listproductsbyshop", method = { RequestMethod.GET })
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// ��ȡҳ��
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// ��ȡһҹ��Ҫ��ʾ������������
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// ��ǰ�˻�ȡ����id
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		// �ǿ��ж�
		if ((pageIndex > -1) && (pageSize > -1)&&(shopId>-1)) {
			// ��ȡ��Ʒ���id
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			// ��ȡģ����ѯ������
			String productName = new String(request.getParameter("productName").getBytes("ISO-8859-1"), "utf-8");
		//	System.out.println(productName);
			// ��ȡ��ϲ�ѯ����
			Product productCondition = compactProductCondition4Search(shopId,productCategoryId, productName);
			// ���ݲ�ѯ������ȡ�����б�
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
	 * ��ϲ�ѯ����
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
			//��ѯĳ����Ʒ����µ���Ʒ�б�
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
