package com.cuixb.activiti.service;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cuixb.activiti.bean.request.ProcessReq;

@Service
public class ActivitiService {
	
	@Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    
    @Autowired
    HistoryService historyService;

	@Transactional
    public void startProcess(ProcessReq processReq) {
		String processInstanceId =runtimeService.startProcessInstanceByKey(processReq.getProcessName()).getProcessInstanceId();
		runtimeService.updateBusinessKey(processInstanceId, processReq.getBusinessKey());
	}

	@Transactional
    public List<Task> getTasks(String assignee) {
		if(StringUtils.isBlank(assignee)) {
			return taskService.createTaskQuery().active().taskUnassigned().list();
		}else {
			return taskService.createTaskQuery().taskAssignee(assignee).list();
		}
		
       
    }

}
