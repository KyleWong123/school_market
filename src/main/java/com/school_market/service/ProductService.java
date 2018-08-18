package com.school_market.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.school_market.dto.ImageHolder;
import com.school_market.dto.ProductExecution;
import com.school_market.dto.ShopExecution;
import com.school_market.entity.Product;
import com.school_market.entity.ProductImg;
import com.school_market.entity.Shop;
import com.school_market.exception.ProductOperationException;

public interface ProductService {
	/***
	 * �����Ʒ
	 * ��Ҫ�����Ʒ��Ϣ������ͼ���Լ�ͼƬ����
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product,ImageHolder thumbnail,
			List<ImageHolder> productImgList) throws ProductOperationException;
	
	/***
	 * ��ѯ��Ʒͨ��productId
	 */
	Product getProductById(long productId);
	/***
	 * �޸���Ʒ��Ϣ
	 */
	ProductExecution modifyProduct(Product product,ImageHolder thumbnail,
			List<ImageHolder> productImgList) throws ProductOperationException;
	
	/***
	 * ��ҳ�����б���Ϣ
	 * @param produtionCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);
}
