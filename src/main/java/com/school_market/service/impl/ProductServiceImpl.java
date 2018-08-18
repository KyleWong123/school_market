package com.school_market.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school_market.dao.ProductDao;
import com.school_market.dao.ProductImgDao;
import com.school_market.dto.ImageHolder;
import com.school_market.dto.ProductExecution;
import com.school_market.entity.Product;
import com.school_market.entity.ProductImg;
import com.school_market.enums.ProductStateEnum;
import com.school_market.exception.ProductOperationException;
import com.school_market.service.ProductService;
import com.school_market.util.ImageUtil;
import com.school_market.util.PageCalculator;
import com.school_market.util.PathUtil;
@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	
	@Override
	@Transactional
	/***
	 * 1.��������ͼ����ȡ����ͼ���·������ֵ��product
	 * 2.��tab_product��д����Ʒ��Ϣ����ȡproductId
	 * 3.���productId ����������Ʒ����ͼ
	 * 4.����Ʒ����ͼ�б���������tab_product_img��
	 */
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		// TODO Auto-generated method stub
		//��ֵ�ж�
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
			//Ϊ��Ʒ����Ĭ������
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			//����Ϊ�ϼ�״̬
			product.setEnableStatus(1);
			//����Ʒ����ͼ��Ϊ�������
			if(thumbnail!=null){
				addThumbnail(product,thumbnail);
				
			}
			try {
				//������Ʒ��Ϣ
				int effectedNum = productDao.insertProduct(product);
				if(effectedNum<=0){
					throw new ProductOperationException("��Ʒ����ʧ��");
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw new ProductOperationException("��Ʒ����ʧ��"+e.getMessage());
			}
			//����Ʒ����ͼ��Ϊ�������
			if(productImgList!=null&&productImgList.size()>0){
				addProductImgList(product,productImgList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
		}else{
			//����Ϊ���򷵻ؿ�ֵ��Ϣ
			return new ProductExecution(ProductStateEnum.EMPTY_LIST);
		}
	}
private void addProductImgList(Product product, List<ImageHolder> productImgList) {
		// TODO Auto-generated method stub
		//��ȡͼƬ�洢·����ֱ�Ӵ�ŵ���Ӧ���̵��ļ�����
	String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
	List<ProductImg> productIList = new ArrayList<ProductImg>();
	//����productImgList��������ӵ�ProductImgʵ��
	for(ImageHolder productImgHolder:productImgList){
		String imgAddr = ImageUtil.generateNormalImg(productImgHolder,dest);
		ProductImg productImg = new ProductImg();
		productImg.setImgAddr(imgAddr);
		productImg.setProductId(product.getProductId());
		productImg.setCreateTime(new Date());
		productIList.add(productImg);	
	}
	//�����ͼƬ������Ҫ��ӣ���ִ���������
	if(productIList.size()>0){
		try {
			int efectedNum = productImgDao.batchInsertProductImg(productIList);
			if(efectedNum<=0){
				
				throw new ProductOperationException("��Ʒ���鴴��ʧ��");

			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new ProductOperationException("��Ʒ���鴴��ʧ��"+e.getMessage());

		}
	}
	}
/***
 * �������ͼ
 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		// TODO Auto-generated method stub
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}
	
	/***
	 * ��ȡ��Ʒ�б�
	 */
@Override
public Product getProductById(long productId) {
	// TODO Auto-generated method stub
	return productDao.queryProductById(productId);
}



@Override
public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
		throws ProductOperationException {
	// TODO Auto-generated method stub
	//��ֵ�ж�
	if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
		//����Ʒ����Ĭ������
		product.setLastEditTime(new Date());
		if(thumbnail!=null){
			//��ѯ��ԭ��������ͼ��ɾ��
			Product tempProduct = productDao.queryProductById(product.getProductId());
			if(tempProduct.getImgAddr()!=null){
				ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
			}
			//����µ�����ͼ
			addThumbnail(product, thumbnail);
		}
		if(productImgList!=null&&productImgList.size()>0){
			deleteProductImgList(product.getProductId());
			addProductImgList(product,productImgList);
		}
		//������Ʒ
		try {
			int effectedNum = productDao.updateProduct(product);
			if(effectedNum<=0){
				throw new ProductOperationException("����ʧ��");
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
		} catch (Exception e) {
			// TODO: handle exception
			throw new ProductOperationException("����ʧ��"+e.getMessage());

		}
		
	}else{
		return new ProductExecution(ProductStateEnum.EMPTY_LIST);

	}
}
/***
 * �������ͼ
 */
/***
 * ɾ������ͼ
 * @param productId
 */
private void deleteProductImgList(long productId) {
	// TODO Auto-generated method stub
	List<ProductImg> productImg = productImgDao.queryProductImgList(productId);
	//ɾ��ԭ����ͼƬ
	for(ProductImg productimg:productImg){
		ImageUtil.deleteFileOrPath(productimg.getImgAddr());
		
	}
	//ɾ�����ݿ���ԭ�е�������Ϣ
	productImgDao.deleteProductImgByProductId(productId);
}
@Override
public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
	// TODO Auto-generated method stub
	//ҳ��תΪ���ݿ������
	int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
	List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
	//������Ʒ����
	int count  = productDao.queryProductCount(productCondition);
	ProductExecution pe = new ProductExecution();
	pe.setCount(count);
	pe.setProductList(productList);
	return pe;
}
		
}
