package com.cuixb.activiti.bean.response;

public class StandardReponseCodeBean {
	
	public enum ResponseCode {
	    success("200"),
	    fail("500");

	    private String code;

	    private ResponseCode(String code){
	        this.code=code;
	    }

	    public String getCode(){
	        return code;
	    }

	}

}
