package com.school_market.dto;

import java.util.List;

import com.school_market.entity.Product;
import com.school_market.enums.ProductCategoryStateEnum;
import com.school_market.enums.ProductStateEnum;

public class ProductExecution {
	//结果状态
			private int state;
			//状态标识
			private String stateInfo;
			//商品数量
			private int count;
			private Product product;
			private List<Product> productList;
			public ProductExecution() {
				super();
				// TODO Auto-generated constructor stub
			}
			//操作失败时
			public ProductExecution(ProductStateEnum stateEnum) {
				super();
				this.state = stateEnum.getState();
				this.stateInfo = stateEnum.getStateInfo();
			}
			
			//单个商品操作成功
			public ProductExecution(ProductStateEnum stateEnum,Product product){
				this.state = stateEnum.getState();
				this.stateInfo=stateEnum.getStateInfo();
				this.product=product;
			}
			//商品列表操作成功
			public ProductExecution(ProductStateEnum stateEnum,List<Product> productList){
				this.state = stateEnum.getState();
				this.stateInfo=stateEnum.getStateInfo();
				this.productList=productList;
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
			public Product getProduct() {
				return product;
			}
			public void setProduct(Product product) {
				this.product = product;
			}
			public List<Product> getProductList() {
				return productList;
			}
			public void setProductList(List<Product> productList) {
				this.productList = productList;
			}
}
