package com.meijialife.dingdang.bean;

public enum ErrorEnum {
	// 请求超时
	REQUEST_TIMEOUT(-1000L , "数据解析错误") ,
	// 客户端自定义，参数验证错误
	APP_CHECK_PARAMS_ERROR(-10000L , "参数错误")  ,
	// 发送请求出现异常
	SEND_REQUEST_ERROR(-20000L , "发送请求出现异常");
	;

	// 错误号
	private Long errorNumber;
	// 错误信息
	private String errorMsg;

	private ErrorEnum(long errorNumber , String errorMsg) {
		this.errorNumber = errorNumber;
		this.errorMsg = errorMsg;
	}

	public static ErrorEnum getErrorEnumByErrorNumber(Long errorNumber){
		for(ErrorEnum item : ErrorEnum.values()){
			if(item.getErrorNumber().equals(errorNumber)){
				return item;
			}
		}

		return null;
	}

	public Long getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(Long errorNumber) {
		this.errorNumber = errorNumber;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
