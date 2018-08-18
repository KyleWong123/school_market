package com.school_market.enums;

public enum ShopStateEnum {
	CHECK(0,"�����"),OFFLINE(-1,"�Ƿ�����"),SUCCESS(1,"�����ɹ�"),
	PASS(2,"ͨ����֤"),INNER_ERROR(-1001,"ϵͳ�ڲ�����"),NULL_SHOPID(-1002,"ShopIdΪ��"),NULL_SHOP(-1003,"shop��ϢΪ��");
	private int state;
	private String stateInfo;
	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	/***
	 * ���ݴ����state������Ӧ��enumֵ
	 */
	public static ShopStateEnum stateOf(int state){
		
		for(ShopStateEnum stateEnum:values()){
			if(stateEnum.getState()==state){
				return stateEnum;
		}
	}
	return null;
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

}
