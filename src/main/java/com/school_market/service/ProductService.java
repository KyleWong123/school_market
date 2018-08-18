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
	 * 添加商品
	 * 需要添加商品信息，缩略图，以及图片集合
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product,ImageHolder thumbnail,
			List<ImageHolder> productImgList) throws ProductOperationException;
	
	/***
	 * 查询商品通过productId
	 */
	Product getProductById(long productId);
	/***
	 * 修改商品信息
	 */
	ProductExecution modifyProduct(Product product,ImageHolder thumbnail,
			List<ImageHolder> productImgList) throws ProductOperationException;
	
	/***
	 * 分页返回列表信息
	 * @param produtionCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);
}
