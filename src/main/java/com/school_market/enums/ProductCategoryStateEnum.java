package com.school_market.enums;

public enum ProductCategoryStateEnum {
	SUCCESS(1,"�����ɹ�"),INNER_ERROR(-1001,"����ʧ��"),EMPTY_LIST(-1002,"���ʧ��");
	private int state;
	private String stateInfo;
	private ProductCategoryStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
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
	public static ProductCategoryStateEnum stateOf(int index){
		for(ProductCategoryStateEnum state:values()){
			if(state.getState()==index){
				return state;
			}
			
		}
		return null;
	}
}
