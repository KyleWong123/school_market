package com.school_market.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school_market.dto.ImageHolder;
import com.school_market.dto.ProductExecution;
import com.school_market.entity.Product;
import com.school_market.entity.ProductCategory;
import com.school_market.entity.Shop;
import com.school_market.enums.ProductStateEnum;
import com.school_market.exception.ProductOperationException;
import com.school_market.service.ProductCategoryService;
import com.school_market.service.ProductService;
import com.school_market.util.CodeUtil;
import com.school_market.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/shopadmin")
public class ProductManagementController {
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService; 
	//支持上传图片详情的最大值
	private static final int IMAGEMAXCOUNT=6;
	@RequestMapping(value="/addproducts",method = {RequestMethod.POST})
	@ResponseBody // 自动转为JSON
	private Map<String, Object> addProduct(HttpServletRequest request) {
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
		//
		ObjectMapper mapper = new ObjectMapper();
		Product product=null;
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		ImageHolder thumbnail =null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			if (commonsMultipartResolver.isMultipart(request)) {
				 thumbnail = handleImage(request, thumbnail, productImgList);
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg","上传图片不能为空");
			return modelMap;
		}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			modelMap.put("success", false);
			modelMap.put("errMsg",e.toString());
			return modelMap;
		}
		try {
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMsg",e.toString());
			return modelMap;
		}
		if(product!=null&&thumbnail!=null&&productImgList.size()>0){
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				Shop shop = new Shop();
				shop.setShopId(currentShop.getShopId());
				product.setShop(shop);
				//执行添加操作
				ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState()){
					modelMap.put("success", true);
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg",pe.getStateInfo());
					
				}
			} catch (ProductOperationException e) {
				// TODO: handle exception
				System.out.println(e);
				modelMap.put("success", false);
				modelMap.put("errMsg",e.toString());
				return modelMap;
			}
			
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg","请输入商品信息");
		}
		System.out.println(1);
	return modelMap;
	}
	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws IOException {
		MultipartHttpServletRequest multipartHttpServletRequest;
		multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		 CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
		if(thumbnailFile!=null){
		 thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
		 }
//取出详情图片并构建List<ImageHolder>列表对象，最多支持6张图片
		 for(int i=0;i<IMAGEMAXCOUNT;i++){
			 CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg"+i);
		if(productImgFile!=null){
			//若取出的第i各图片详情流不为空，则加入详情列表
			ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),productImgFile.getInputStream());
			productImgList.add(productImg);
		}else{
			//若文件流为空则终止循环
			break;
		}
}
		return thumbnail;
	}
	/***
	 * 通过productId查询商品信息
	 * @param productId
	 * @return
	 */
	@RequestMapping(value="/getproductbyid",method = {RequestMethod.GET})
	@ResponseBody // 自动转为JSON
	private Map<String, Object> getProductById(@RequestParam int productId) {
	
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//非空判断
		if(productId>-1){
			//获取商品信息
			Product product = productService.getProductById(productId);
			//获取店铺下的商品列表
			List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	
	}
	
	/***
	 * 修改商品
	 */
	@RequestMapping(value="/modifyproduct",method = {RequestMethod.POST})
	@ResponseBody // 自动转为JSON
	private Map<String, Object> modifyProduct(HttpServletRequest request) {
	
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//若为商品编辑则需要验证码，否则不需要
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		//判断验证码
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			System.out.println(456);
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入的验证码有误");
			return modelMap;
		}
		ObjectMapper mapper = new ObjectMapper();
		Product product=null;
		ImageHolder thumbnail =null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			if (commonsMultipartResolver.isMultipart(request)) {
				 thumbnail = handleImage(request, thumbnail, productImgList);
		}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("1"+e);
			modelMap.put("success", false);
			modelMap.put("errMsg",e.toString());
			return modelMap;
		}try {
			String productStr = HttpServletRequestUtil.getString(request, "productStr");
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("2"+e);

			modelMap.put("success", false);
			modelMap.put("errMsg",e.toString());
			return modelMap;
		}
		//非空判断
		if(product!=null){
			try {
				//从session中获得shopId
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				//跟新商品
				ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState()){
					modelMap.put("success", true);
					
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg",pe.getStateInfo());
				}
						
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
				modelMap.put("success", false);
				modelMap.put("errMsg",e.toString());
				return modelMap;
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg","请输入商品信息");
		}
		return modelMap;
	}	
	/***
	 * 
	 * 获取店铺下的所有商品信息
	 */
	
	@RequestMapping(value="/getproductlistbyshop",method = {RequestMethod.GET})
	@ResponseBody // 自动转为JSON
	private Map<String, Object> getProductListByShop(HttpServletRequest request) {
	
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取前台传来的页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取前台传来的每页展示商品的最大数量
		int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
		//从当前的Session中获取店铺信息，主要是shopId
		Shop currentShop= (Shop) request.getSession().getAttribute("currentShop");
		//判空
		if((pageIndex>-1)&&(pageSize>-1)&&(currentShop!=null)&&(currentShop.getShopId()!=null)){
			
			//获取搜索商品的条件
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName = HttpServletRequestUtil.getString(request, "productName");
			Product productCondition = compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
		//传入插入条件及分页信息进行查询
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
			
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
	private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
		// TODO Auto-generated method stub
		//获取shopId;
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		//若有指定商品类别的要求则添加进去
		if(productCategoryId!=-1L){
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		//若有指定商品名模糊查询的要求则添加进去
		if(productName!=null){
			productCondition.setProductName(productName);
		}
		return productCondition;
	}
	
	
}