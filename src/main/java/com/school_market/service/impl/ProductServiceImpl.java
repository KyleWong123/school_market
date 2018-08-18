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
	 * 1.处理缩略图，获取缩略图相对路径并赋值给product
	 * 2.往tab_product中写入商品信息，获取productId
	 * 3.结合productId 批量处理商品详情图
	 * 4.将商品详情图列表批量插入tab_product_img中
	 */
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		// TODO Auto-generated method stub
		//空值判断
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
			//为商品设置默认属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			//设置为上架状态
			product.setEnableStatus(1);
			//若商品缩略图不为空则添加
			if(thumbnail!=null){
				addThumbnail(product,thumbnail);
				
			}
			try {
				//创建商品信息
				int effectedNum = productDao.insertProduct(product);
				if(effectedNum<=0){
					throw new ProductOperationException("商品创建失败");
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw new ProductOperationException("商品创建失败"+e.getMessage());
			}
			//若商品详情图不为空则添加
			if(productImgList!=null&&productImgList.size()>0){
				addProductImgList(product,productImgList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
		}else{
			//传参为空则返回空值信息
			return new ProductExecution(ProductStateEnum.EMPTY_LIST);
		}
	}
private void addProductImgList(Product product, List<ImageHolder> productImgList) {
		// TODO Auto-generated method stub
		//获取图片存储路径，直接存放到相应店铺的文件夹下
	String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
	List<ProductImg> productIList = new ArrayList<ProductImg>();
	//遍历productImgList并将其添加到ProductImg实体
	for(ImageHolder productImgHolder:productImgList){
		String imgAddr = ImageUtil.generateNormalImg(productImgHolder,dest);
		ProductImg productImg = new ProductImg();
		productImg.setImgAddr(imgAddr);
		productImg.setProductId(product.getProductId());
		productImg.setCreateTime(new Date());
		productIList.add(productImg);	
	}
	//如果有图片详情需要添加，就执行批量添加
	if(productIList.size()>0){
		try {
			int efectedNum = productImgDao.batchInsertProductImg(productIList);
			if(efectedNum<=0){
				
				throw new ProductOperationException("商品详情创建失败");

			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new ProductOperationException("商品详情创建失败"+e.getMessage());

		}
	}
	}
/***
 * 添加缩略图
 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		// TODO Auto-generated method stub
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}
	
	/***
	 * 获取商品列表
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
	//空值判断
	if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
		//给商品设置默认属性
		product.setLastEditTime(new Date());
		if(thumbnail!=null){
			//查询出原来的缩略图并删除
			Product tempProduct = productDao.queryProductById(product.getProductId());
			if(tempProduct.getImgAddr()!=null){
				ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
			}
			//添加新的缩略图
			addThumbnail(product, thumbnail);
		}
		if(productImgList!=null&&productImgList.size()>0){
			deleteProductImgList(product.getProductId());
			addProductImgList(product,productImgList);
		}
		//更新商品
		try {
			int effectedNum = productDao.updateProduct(product);
			if(effectedNum<=0){
				throw new ProductOperationException("更新失败");
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
		} catch (Exception e) {
			// TODO: handle exception
			throw new ProductOperationException("更新失败"+e.getMessage());

		}
		
	}else{
		return new ProductExecution(ProductStateEnum.EMPTY_LIST);

	}
}
/***
 * 添加缩略图
 */
/***
 * 删除详情图
 * @param productId
 */
private void deleteProductImgList(long productId) {
	// TODO Auto-generated method stub
	List<ProductImg> productImg = productImgDao.queryProductImgList(productId);
	//删除原来的图片
	for(ProductImg productimg:productImg){
		ImageUtil.deleteFileOrPath(productimg.getImgAddr());
		
	}
	//删除数据库里原有的数据信息
	productImgDao.deleteProductImgByProductId(productId);
}
@Override
public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
	// TODO Auto-generated method stub
	//页码转为数据库的行吗
	int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
	List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
	//返回商品总数
	int count  = productDao.queryProductCount(productCondition);
	ProductExecution pe = new ProductExecution();
	pe.setCount(count);
	pe.setProductList(productList);
	return pe;
}
		
}
