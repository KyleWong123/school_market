package com.school_market.web.shopadmin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school_market.dto.ImageHolder;
import com.school_market.dto.ShopExecution;
import com.school_market.entity.Area;
import com.school_market.entity.PersonInfo;
import com.school_market.entity.Shop;
import com.school_market.entity.ShopCategory;
import com.school_market.enums.ShopStateEnum;
import com.school_market.exception.ShopOperationException;
import com.school_market.service.AreaService;
import com.school_market.service.ShopCategoryService;
import com.school_market.service.ShopService;
import com.school_market.util.CodeUtil;
import com.school_market.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopService shopService;

	@RequestMapping(value = "/getshopinitinfo", method = { RequestMethod.GET })
	@ResponseBody
	private Map<String, Object> getShopInitInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService.shopCategoryList(new ShopCategory());
			areaList = areaService.getQueryList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMsp", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/registershop", method = { RequestMethod.POST })
	@ResponseBody // �Զ�תΪJSON
	private Map<String, Object> registerShop(HttpServletRequest request) {
		/***
		 * 1.���ܲ�ת����Ӧ�Ĳ�������������ע�����Ϣ�Լ�ͼƬ��Ϣ 2.ע����� 3.���ؽ��
		 */

		Map<String, Object> modelMap = new HashMap<String, Object>();
		//System.out.println(123);

		// �ж���֤��
		if (!CodeUtil.checkVerifyCode(request)) {
			//System.out.println(456);
			modelMap.put("success", false);
			modelMap.put("errMsg", "�������֤������");
			return modelMap;
		}
		// 1.1����ǰ���������Ϣ��ת��ΪShopʵ��
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 1.2 ͼƬ����
		CommonsMultipartFile shopImg = null;
		// �ļ�������
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {// ��ͼͼƬ���Ǳ���Ҫ�ϴ�ʱ����ȥ��else
			modelMap.put("success", false);
			modelMap.put("errMsg", "�ϴ�ͼ����Ϊ��");
			return modelMap;
		}

		// ע�����
		if (shop != null && shopImg != null) {
			 PersonInfo owner =
			 (PersonInfo)request.getSession().getAttribute("user");
			// Session TODO
			/*PersonInfo owner = new PersonInfo();
			owner.setUserId(1L);*/
			shop.setOwner(owner);
			// �������ļ�
			/*
			 * File shopImgFile= new
			 * File(PathUtil.getImgBasePath()+ImageUtil.getRandomFileName());
			 * try { shopImgFile.createNewFile(); } catch (Exception e)
			 * {//�ļ�����ʧ�� // TODO: handle exception modelMap.put("success",
			 * false); modelMap.put("errMsg", e.getMessage()); return modelMap;
			 * }
			 */// �ļ������ɹ�
			/*
			 * try { inputStreamToFile(shopImg.getInputStream(),shopImgFile); }
			 * catch (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
			ShopExecution se;
			try {
				ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
				se = shopService.addShop(shop,imageHolder);
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);// �ϴ��ɹ�
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					// �״�ע�����,�ȴ����б�����ӣ�������ֱ�����
					if (shopList == null || shopList.size() == 0) {
						shopList = new ArrayList<Shop>();

					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getState());
				}
			} catch (ShopOperationException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			//System.out.println("��ʳɹ�");
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "�����������Ϣ");
			return modelMap;
		}
	}

	/*
	 * private static void inputStreamToFile(InputStream inputStream,File file){
	 * FileOutputStream os = null; try { os = new FileOutputStream(file); int
	 * bytesRead = 0; byte[] buffer = new byte[1024]; while
	 * ((bytesRead=inputStream.read(buffer))!=-1){ os.write(buffer, 0,
	 * bytesRead); } } catch (Exception e) { // TODO: handle exception throw new
	 * RuntimeException("���ø÷��������쳣��"+e.getMessage()); }finally{ try {
	 * if(os!=null){ os.close(); } if(inputStream!=null){ inputStream.close(); }
	 * 
	 * } catch (IOException e2) { // TODO: handle exception throw new
	 * RuntimeException("inputStream�ر�io�����쳣"+e2.getMessage());
	 * 
	 * }
	 * 
	 * } }
	 */
	@RequestMapping(value = "/getshopbyid", method = { RequestMethod.GET })
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > -1) {
			try {
				Shop shop = shopService.getShopById(shopId);
				List<Area> areaList = areaService.getQueryList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success",true);
				//System.out.println("��ѯ�ɹ�");
			} catch (Exception e) {
				// TODO: handle exception
			//	System.out.println("��ѯ�쳣");
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
		//	System.out.println("��ѯʧ��");
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		//System.out.println("���سɹ�");
		return modelMap;
	}

	@RequestMapping(value = "/modifyshop", method = {RequestMethod.POST})
	@ResponseBody // �Զ�תΪJSON
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// �ж���֤��
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "�������֤������");
			return modelMap;
		}
		// 1.1����ǰ���������Ϣ��ת��ΪShopʵ��
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
			shop.setEnableStatus(1);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 1.2 ͼƬ����
		CommonsMultipartFile shopImg = null;
		// �ļ�������
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		// �޸ĵ�����Ϣ
		//System.out.println("��ʼ�޸�");
		if (shop!=null && shop.getShopId()!= null) {
			System.out.println("�޸�ʧ��");
			ShopExecution se;
			try {
				if (shopImg == null) {
					
					se = shopService.modifyShop(shop, null);
					System.out.println("controller:"+se.getState());
				} else {
					ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());

					se = shopService.modifyShop(shop,imageHolder);
					System.out.println("controller:"+se.getState());
				}
				if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
					
					System.out.println("XCGBDFHDFH:"+se.getState());
					modelMap.put("success", true);// �ϴ��ɹ�
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getState());
				}
			} catch (ShopOperationException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			System.out.println("���³ɹ�");
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "���������Id");
			return modelMap;
		}

	}
	
	@RequestMapping(value="/getshoplist",method={RequestMethod.GET})
	@ResponseBody
	private Map<String,Object> getShopList(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		user.setName("wf");
		request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 20);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMsg",e.getMessage());
		}
		return modelMap;
		
	}
	@RequestMapping(value="/getshopmanagementinfo",method={RequestMethod.GET})
	@ResponseBody
	private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId<=0){
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if(currentShopObj==null){
				modelMap.put("redirect", true);
				modelMap.put("url", "/school-market/shopadmin/shoplist");
			}else{
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId",currentShop.getShopId());
			}
		}else{
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
}
