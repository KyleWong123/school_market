 package com.school_market.service.impl;
/***
 * ���̲���service
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
		//�ж�����ֵ�Ƿ�Ϊ��
		if(shop==null){
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			//��������Ϣ����ʼֵ
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//��ӵ�����Ϣ
			int effectedNum = shopDao.inertShop(shop);
			//����sql��ִ�н���ж��Ƿ���ӳɹ�
			if(effectedNum<=0){
				throw new ShopOperationException("���̴���ʧ��");
			}else{
				
					if(thumanail.getImage()!=null){
					//�洢ͼƬ
					
						try{	addShopImg(shop,thumanail);
					//effectedNum=shopDao.updateShop(shop);
					
					}catch(Exception e){
						//System.out.println(e);
						throw new ShopOperationException("addShopImg Error"+e.getMessage()); 
					}
					//����ͼƬ�ĵ�ַ
					effectedNum = shopDao.updateShop(shop);
					if(effectedNum<=0){
						throw new ShopOperationException("����ͼƬ��ַʧ��ʧ��");
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
		//��ȡshopͼƬĿ¼�����·��
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(thumanail,dest);
		shop.setShopImg(shopImgAddr);
		
	}
	
	//ͨ��id��ѯ������Ϣ
	@Override
	public Shop getShopById(long shopId) {
		// TODO Auto-generated method stub
		return shopDao.queryByShopId(shopId);
	}
	
	
	
	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumanail) throws ShopOperationException{
		// TODO Auto-generated method stub
		/***
		 * 1.�ж��Ƿ���Ҫ����ͼƬ
		 * ���µ�����Ϣ
		 */
		if(shop==null||shop.getShopId()==null){
		//	System.out.println("idΪ��");
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else{
			//�ж��Ƿ���Ҫ����ͼƬ
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
			//���µ�����Ϣ
			shop.setLastEditTime(new Date());
		//	shop.setEnableStatus(1);
			int effectedNum = shopDao.updateShop(shop);
			if(effectedNum<=0){
				return new ShopExecution(ShopStateEnum.INNER_ERROR);
			}else{
				shop = shopDao.queryByShopId(shop.getShopId());
				//System.out.println("״̬����");
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
