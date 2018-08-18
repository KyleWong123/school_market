 package com.school_market.service.impl;
/***
 * 店铺操作service
 * wf
 */

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school_market.dao.ShopDao;
import com.school_market.dto.ImageHolder;
import com.school_market.dto.ShopExecution;
import com.school_market.entity.Shop;
import com.school_market.enums.ShopStateEnum;
import com.school_market.exception.ShopOperationException;
import com.school_market.service.ShopService;
import com.school_market.util.ImageUtil;
import com.school_market.util.PageCalculator;
import com.school_market.util.PathUtil;
@Service
public class ShopServiceImpl implements ShopService{
	@Autowired
	private ShopDao shopDao;
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder thumanail)throws ShopOperationException {
		// TODO Auto-generated method stub
		//判断所传值是否为空
		if(shop==null){
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			//给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//添加店铺信息
			int effectedNum = shopDao.inertShop(shop);
			//根据sql的执行结果判断是否添加成功
			if(effectedNum<=0){
				throw new ShopOperationException("店铺创建失败");
			}else{
				
					if(thumanail.getImage()!=null){
					//存储图片
					
						try{	addShopImg(shop,thumanail);
					//effectedNum=shopDao.updateShop(shop);
					
					}catch(Exception e){
						//System.out.println(e);
						throw new ShopOperationException("addShopImg Error"+e.getMessage()); 
					}
					//更新图片的地址
					effectedNum = shopDao.updateShop(shop);
					if(effectedNum<=0){
						throw new ShopOperationException("更新图片地址失败失败");
					}
					}
					}
				}catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
					throw new ShopOperationException("addShopImg Error"+e.getMessage());

				}
			
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}
	private void addShopImg(Shop shop,ImageHolder thumanail) {
		// TODO Auto-generated method stub
		//获取shop图片目录的相对路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(thumanail,dest);
		shop.setShopImg(shopImgAddr);
		
	}
	
	//通过id查询商铺信息
	@Override
	public Shop getShopById(long shopId) {
		// TODO Auto-generated method stub
		return shopDao.queryByShopId(shopId);
	}
	
	
	
	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumanail) throws ShopOperationException{
		// TODO Auto-generated method stub
		/***
		 * 1.判断是否需要处理图片
		 * 更新店铺信息
		 */
		if(shop==null||shop.getShopId()==null){
		//	System.out.println("id为控");
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else{
			//判断是否需要处理图片
			try {
				
			if(thumanail.getImage()!=null&&thumanail.getImageName()!=null&&!"".equals(thumanail.getImageName())){
				Shop tempShop = shopDao.queryByShopId(shop.getShopId());
				/*System.out.println(tempShop.getShopImg());
				System.out.println("error0");*/
				if(tempShop.getShopImg()!=null){
					//System.out.println("error");
					ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					//System.out.println("error2");
					}
				//System.out.println("error3");
				addShopImg(shop, thumanail);
			}
			//更新店铺信息
			shop.setLastEditTime(new Date());
		//	shop.setEnableStatus(1);
			int effectedNum = shopDao.updateShop(shop);
			if(effectedNum<=0){
				return new ShopExecution(ShopStateEnum.INNER_ERROR);
			}else{
				shop = shopDao.queryByShopId(shop.getShopId());
				//System.out.println("状态更改");
				//shop.setEnableStatus(1);
				ShopExecution se = new ShopExecution(ShopStateEnum.SUCCESS,shop);
			//	se.setState(1);
			//	System.out.println(se.getState());
			//	System.out.println(se.getStateInfo());
				return se;
			}
			} catch (Exception e) {
				// TODO: handle exception
				throw new ShopOperationException("modify err"+e.getMessage());
			}
		}
	}
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		int rowIndex=PageCalculator.calculatorRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se= new ShopExecution();
		if(shopList!=null){
			se.setShopList(shopList);
			se.setCount(count);
		}else{
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

}
