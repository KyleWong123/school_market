package com.school_market.dto;
/***
 * 1.存储店铺信息
 * @author Administrator
 *
 */

import java.util.List;

import com.school_market.entity.Shop;
import com.school_market.enums.ShopStateEnum;

public class ShopExecution {
	//结果状态,以文字形式返回店铺状态,采用枚举类型
	private int state;
	//状态标识
	private String stateInfo;
	//店铺数量
	private int count;
	//操作的shop
	private Shop shop;
	//shop列表（查询店铺列表时使用）
	private List<Shop> shopList;
	public ShopExecution() {
		//super();
		// TODO Auto-generated constructor stub
	}
	//店铺操作失败时使用失败的构造器
	public ShopExecution(ShopStateEnum stateEnum) {
		//super();
		// TODO Auto-generated constructor stub
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}
	//店铺操作成功的构造器
	public ShopExecution(ShopStateEnum stateEnum,Shop shop){
		//super();
		// TODO Auto-generated constructor stub
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shop = shop;
	}
	//店铺操作成功的构造器
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
