package com.school_market.enums;

public enum ProductStateEnum {
	SUCCESS(1,"�����ɹ�"),INNER_ERROR(-1001,"����ʧ��"),EMPTY_LIST(-1002,"���ʧ��");
	private int state;
	private String stateInfo;
	private ProductStateEnum(int state, String stateInfo) {
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
	public static ProductStateEnum stateOf(int index){
		for(ProductStateEnum state:values()){
			if(state.getState()==index){
				return state;
			}
			
		}
		return null;
	}
}
