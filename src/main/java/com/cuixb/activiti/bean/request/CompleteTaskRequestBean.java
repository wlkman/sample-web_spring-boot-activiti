package com.cuixb.activiti.bean.request;

import java.util.Map;

import lombok.Data;

@Data
public class CompleteTaskRequestBean {
	private String taskId;
	private String userId;
	private Map variables;
}
