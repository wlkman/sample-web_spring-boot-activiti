package com.cuixb.activiti.service;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cuixb.activiti.bean.request.CompleteTaskRequestBean;
import com.cuixb.activiti.bean.request.ProcessRequestBean;

@Service
public class ActivitiService {
	
	@Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    
    @Autowired
    HistoryService historyService;

	@Transactional
    public void startProcess(ProcessRequestBean processReq) {
		//launch a process
		runtimeService.startProcessInstanceByKey(processReq.getProcessName(), processReq.getBusinessKey());
	}

	@Transactional
    public List<Task> getPendingTasks(String assignee) {
		//if param don't have assignee then return all unassigned tasks
		if(StringUtils.isBlank(assignee)) {
			return taskService.createTaskQuery().active().taskUnassigned().list();
		}else {//if param has assignee then return the corresponding tasks
			return taskService.createTaskQuery().taskAssignee(assignee).list();
		}
    }
	
	@Transactional
    public List<HistoricTaskInstance> getHistoricalTasks(String assignee) {
		//if param don't have assignee then return all unassigned tasks
		if(StringUtils.isBlank(assignee)) {
			return historyService.createHistoricTaskInstanceQuery().finished().list();
		}else {//if param has assignee then return the corresponding tasks
			return historyService.createHistoricTaskInstanceQuery().finished().taskAssignee(assignee).list();
		}
    }
	
	@Transactional
    public void completeTask(CompleteTaskRequestBean completeTaskRequestBean) {
		//set assignee before completing task
		taskService.claim(completeTaskRequestBean.getTaskId(), completeTaskRequestBean.getUserId());
		//complete task
		taskService.complete(completeTaskRequestBean.getTaskId());
	}
	
	public String getBusinessKeyByProcessInstanceId(String processInstanceId) {
		List<HistoricProcessInstance> historicProcessInstance= historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).list();
		return historicProcessInstance.get(0).getBusinessKey();
	}

}
