package com.cuixb.activiti.bean.request;

import lombok.Data;

@Data
public class CompleteTaskRequestBean {
	private String taskId;
	private String userId;
}
