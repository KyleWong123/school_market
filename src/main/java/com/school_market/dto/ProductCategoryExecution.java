package com.school_market.dto;

import java.util.List;

import com.school_market.entity.ProductCategory;
import com.school_market.enums.ProductCategoryStateEnum;

public class ProductCategoryExecution {
	//结果状态
	private int state;
	//状态标识
	private String stateInfo;
	private List<ProductCategory> productCategoryList;
	public ProductCategoryExecution() {
		super();
		// TODO Auto-generated constructor stub
	}
	//批量添加失败时
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	//批量添加成功
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum,List<ProductCategory> productCategoryList){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.productCategoryList=productCategoryList;
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
	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}
	public void setProductCategoryList(List<ProductCategory> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}
}
