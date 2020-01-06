package com.cuixb.activiti.bean.request;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class StandardRequestBean<T> {
	private String token;
	private T body;
}
