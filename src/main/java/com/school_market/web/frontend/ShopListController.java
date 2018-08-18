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
	 * ������Ʒ�б������shopCategory�б��Լ�������Ϣ�б�
	 */
	@RequestMapping(value="/listshoppageinfo",method={RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> listShopPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// ��ǰ�˻�ȡparentId
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		if (parentId != -1) {
			// ���parentId���ڣ���ȡ������shopCategory�б�
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
				// ���parentId�����ڣ���ȡ������һ��shopCategory��ȫ����Ʒ�б�
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
			// ��ȡ����������Ϣ
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
	 * ��ȡָ����ѯ�콡�µĵ����б�
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/listshops",method={RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// ��ȡҳ��
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// ��ȡһҹ��Ҫ��ʾ������������
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// �ǿ��ж�
		if ((pageIndex > -1) && (pageSize > -1)) {
			// ��ȡһ�����id
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			// ��ȡ�������Id
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			// ��ȡ����Id
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			// ��ȡģ����ѯ������
			String shopName = new String(request.getParameter("shopName").getBytes("ISO-8859-1"),"utf-8");
			System.out.println(shopName);
			// ��ȡ��ϲ�ѯ����
			Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
			// ���ݲ�ѯ������ȡ�����б�
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
	 * ��ϲ�ѯ����
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
		//��ѯĳ��һ��shopCategory�������������shopCategory��ĵ����б�
		ShopCategory childCategory = new ShopCategory();
		ShopCategory parentCategory = new ShopCategory();
		parentCategory.setShopCategoryId(parentId);
		childCategory.setParent(parentCategory);
		shopCondition.setShopCategory(childCategory);
	}
	if(shopCategoryId!=-1L){
		//��ѯ����shopCategory����ĵ����б�
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(shopCategoryId);
		shopCondition.setShopCategory(shopCategory);
	}
	if(areaId!=-1L){
		//��ѯĳ������id�µĵ����б�
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
