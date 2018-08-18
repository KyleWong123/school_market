package com.school_market.dto;
/***
 * 1.�洢������Ϣ
 * @author Administrator
 *
 */

import java.util.List;

import com.school_market.entity.Shop;
import com.school_market.enums.ShopStateEnum;

public class ShopExecution {
	//���״̬,��������ʽ���ص���״̬,����ö������
	private int state;
	//״̬��ʶ
	private String stateInfo;
	//��������
	private int count;
	//������shop
	private Shop shop;
	//shop�б���ѯ�����б�ʱʹ�ã�
	private List<Shop> shopList;
	public ShopExecution() {
		//super();
		// TODO Auto-generated constructor stub
	}
	//���̲���ʧ��ʱʹ��ʧ�ܵĹ�����
	public ShopExecution(ShopStateEnum stateEnum) {
		//super();
		// TODO Auto-generated constructor stub
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}
	//���̲����ɹ��Ĺ�����
	public ShopExecution(ShopStateEnum stateEnum,Shop shop){
		//super();
		// TODO Auto-generated constructor stub
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shop = shop;
	}
	//���̲����ɹ��Ĺ�����
		public ShopExecution(ShopStateEnum stateEnum,List<Shop> shopList){
			super();
			// TODO Auto-generated constructor stub
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
			this.shopList = shopList;
		}
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
		public String getStateInfo() {
			return stateInfo;
		}
		public void setStateInfo(String stateInfo) {
			this.stateInfo = stateInfo;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public Shop getShop() {
			return shop;
		}
		public void setShop(Shop shop) {
			this.shop = shop;
		}
		public List<Shop> getShopList() {
			return shopList;
		}
		public void setShopList(List<Shop> shopList) {
			this.shopList = shopList;
		}

}
