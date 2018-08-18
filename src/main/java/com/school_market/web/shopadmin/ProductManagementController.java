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
	//֧���ϴ�ͼƬ��������ֵ
	private static final int IMAGEMAXCOUNT=6;
	@RequestMapping(value="/addproducts",method = {RequestMethod.POST})
	@ResponseBody // �Զ�תΪJSON
	private Map<String, Object> addProduct(HttpServletRequest request) {
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
			modelMap.put("errMsg","�ϴ�ͼƬ����Ϊ��");
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
				//ִ����Ӳ���
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
			modelMap.put("errMsg","��������Ʒ��Ϣ");
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
//ȡ������ͼƬ������List<ImageHolder>�б�������֧��6��ͼƬ
		 for(int i=0;i<IMAGEMAXCOUNT;i++){
			 CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg"+i);
		if(productImgFile!=null){
			//��ȡ���ĵ�i��ͼƬ��������Ϊ�գ�����������б�
			ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),productImgFile.getInputStream());
			productImgList.add(productImg);
		}else{
			//���ļ���Ϊ������ֹѭ��
			break;
		}
}
		return thumbnail;
	}
	/***
	 * ͨ��productId��ѯ��Ʒ��Ϣ
	 * @param productId
	 * @return
	 */
	@RequestMapping(value="/getproductbyid",method = {RequestMethod.GET})
	@ResponseBody // �Զ�תΪJSON
	private Map<String, Object> getProductById(@RequestParam int productId) {
	
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//�ǿ��ж�
		if(productId>-1){
			//��ȡ��Ʒ��Ϣ
			Product product = productService.getProductById(productId);
			//��ȡ�����µ���Ʒ�б�
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
	 * �޸���Ʒ
	 */
	@RequestMapping(value="/modifyproduct",method = {RequestMethod.POST})
	@ResponseBody // �Զ�תΪJSON
	private Map<String, Object> modifyProduct(HttpServletRequest request) {
	
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//��Ϊ��Ʒ�༭����Ҫ��֤�룬������Ҫ
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		//�ж���֤��
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			System.out.println(456);
			modelMap.put("success", false);
			modelMap.put("errMsg", "�������֤������");
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
		//�ǿ��ж�
		if(product!=null){
			try {
				//��session�л��shopId
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				//������Ʒ
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
			modelMap.put("errMsg","��������Ʒ��Ϣ");
		}
		return modelMap;
	}	
	/***
	 * 
	 * ��ȡ�����µ�������Ʒ��Ϣ
	 */
	
	@RequestMapping(value="/getproductlistbyshop",method = {RequestMethod.GET})
	@ResponseBody // �Զ�תΪJSON
	private Map<String, Object> getProductListByShop(HttpServletRequest request) {
	
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//��ȡǰ̨������ҳ��
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//��ȡǰ̨������ÿҳչʾ��Ʒ���������
		int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
		//�ӵ�ǰ��Session�л�ȡ������Ϣ����Ҫ��shopId
		Shop currentShop= (Shop) request.getSession().getAttribute("currentShop");
		//�п�
		if((pageIndex>-1)&&(pageSize>-1)&&(currentShop!=null)&&(currentShop.getShopId()!=null)){
			
			//��ȡ������Ʒ������
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName = HttpServletRequestUtil.getString(request, "productName");
			Product productCondition = compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
		//���������������ҳ��Ϣ���в�ѯ
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
		//��ȡshopId;
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		//����ָ����Ʒ����Ҫ������ӽ�ȥ
		if(productCategoryId!=-1L){
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		//����ָ����Ʒ��ģ����ѯ��Ҫ������ӽ�ȥ
		if(productName!=null){
			productCondition.setProductName(productName);
		}
		return productCondition;
	}
	
	
}