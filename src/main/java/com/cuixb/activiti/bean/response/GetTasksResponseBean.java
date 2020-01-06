package com.cuixb.activiti.bean.response;

import java.util.Date;

import lombok.Data;

@Data
public class GetTasksResponseBean {
	 private String id;
     private String name;
     private String assignee;
     private Date createTime;
     private Date endTime;
     private Date claimTime;
     private Long workTimeInMillis;
}
