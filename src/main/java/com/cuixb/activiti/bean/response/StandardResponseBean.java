package com.cuixb.activiti.bean.response;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class StandardResponseBean<T> {
	private String code;
	private String message;
	private T body;
}
