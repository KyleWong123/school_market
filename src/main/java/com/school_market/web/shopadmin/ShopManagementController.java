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
	@ResponseBody // 自动转为JSON
	private Map<String, Object> registerShop(HttpServletRequest request) {
		/***
		 * 1.接受并转化相应的参数，包括店铺注册的信息以及图片信息 2.注册店铺 3.返回结果
		 */

		Map<String, Object> modelMap = new HashMap<String, Object>();
		//System.out.println(123);

		// 判断验证码
		if (!CodeUtil.checkVerifyCode(request)) {
			//System.out.println(456);
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入的验证码有误");
			return modelMap;
		}
		// 1.1接收前端输入的信息并转换为Shop实体
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
		// 1.2 图片处理
		CommonsMultipartFile shopImg = null;
		// 文件解析器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {// 如图图片不是必须要上传时，可去掉else
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图不能为空");
			return modelMap;
		}

		// 注册店铺
		if (shop != null && shopImg != null) {
			 PersonInfo owner =
			 (PersonInfo)request.getSession().getAttribute("user");
			// Session TODO
			/*PersonInfo owner = new PersonInfo();
			owner.setUserId(1L);*/
			shop.setOwner(owner);
			// 获得随机文件
			/*
			 * File shopImgFile= new
			 * File(PathUtil.getImgBasePath()+ImageUtil.getRandomFileName());
			 * try { shopImgFile.createNewFile(); } catch (Exception e)
			 * {//文件创建失败 // TODO: handle exception modelMap.put("success",
			 * false); modelMap.put("errMsg", e.getMessage()); return modelMap;
			 * }
			 */// 文件创建成功
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
					modelMap.put("success", true);// 上传成功
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					// 首次注册店铺,先创建列表再添加，若不是直接添加
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
			//System.out.println("天际成功");
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}

	/*
	 * private static void inputStreamToFile(InputStream inputStream,File file){
	 * FileOutputStream os = null; try { os = new FileOutputStream(file); int
	 * bytesRead = 0; byte[] buffer = new byte[1024]; while
	 * ((bytesRead=inputStream.read(buffer))!=-1){ os.write(buffer, 0,
	 * bytesRead); } } catch (Exception e) { // TODO: handle exception throw new
	 * RuntimeException("调用该方法产生异常："+e.getMessage()); }finally{ try {
	 * if(os!=null){ os.close(); } if(inputStream!=null){ inputStream.close(); }
	 * 
	 * } catch (IOException e2) { // TODO: handle exception throw new
	 * RuntimeException("inputStream关闭io产生异常"+e2.getMessage());
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
				//System.out.println("查询成功");
			} catch (Exception e) {
				// TODO: handle exception
			//	System.out.println("查询异常");
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
		//	System.out.println("查询失败");
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		//System.out.println("返回成功");
		return modelMap;
	}

	@RequestMapping(value = "/modifyshop", method = {RequestMethod.POST})
	@ResponseBody // 自动转为JSON
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// 判断验证码
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入的验证码有误");
			return modelMap;
		}
		// 1.1接收前端输入的信息并转换为Shop实体
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
		// 1.2 图片处理
		CommonsMultipartFile shopImg = null;
		// 文件解析器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		// 修改店铺信息
		//System.out.println("开始修改");
		if (shop!=null && shop.getShopId()!= null) {
			System.out.println("修改失败");
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
					modelMap.put("success", true);// 上传成功
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
			System.out.println("更新成功");
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺Id");
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
