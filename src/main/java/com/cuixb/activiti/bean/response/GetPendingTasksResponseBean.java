package com.cuixb.activiti.bean.response;

import java.util.Date;

import lombok.Data;

@Data
public class GetPendingTasksResponseBean {
	 private String id;
     private String name;
     private String assignee;
     private Date createTime;
     private Date claimTime;
     private String businessKey;
}
