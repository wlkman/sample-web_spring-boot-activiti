package com.cuixb.activiti.controller;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cuixb.activiti.bean.request.CompleteTaskRequestBean;
import com.cuixb.activiti.bean.request.GetTasksRequestBean;
import com.cuixb.activiti.bean.request.ProcessRequestBean;
import com.cuixb.activiti.bean.response.GetTasksResponseBean;
import com.cuixb.activiti.bean.response.StandardReponseCodeBean;
import com.cuixb.activiti.bean.response.StandardResponseBean;
import com.cuixb.activiti.service.ActivitiService;

@RestController
public class ActivitiController {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivitiController.class);
	
	@Autowired
    private ActivitiService activitiService;
	
	@Autowired
	private StandardResponseBean<?> standardRep;

    @RequestMapping(value="/startProcess", method= RequestMethod.POST)
    public StandardResponseBean<?> startProcessInstance(@RequestBody ProcessRequestBean processReq) {    
        try {
        	activitiService.startProcess(processReq);
        	standardRep.setCode(StandardReponseCodeBean.ResponseCode.success.getCode());
        	standardRep.setMessage(StandardReponseCodeBean.ResponseCode.success.name());
		} catch (Exception e) {
			standardRep.setCode(StandardReponseCodeBean.ResponseCode.fail.getCode());
        	standardRep.setMessage(e.getMessage());
        	logger.error(e.getStackTrace().toString());
		}
        
		return standardRep;
    }
    
    @RequestMapping(value="/getPendingTasks", method= RequestMethod.POST)
    public StandardResponseBean<List<GetTasksResponseBean>> getPendingTasks(@RequestBody GetTasksRequestBean getTasksRequestBean) {
    	StandardResponseBean<List<GetTasksResponseBean>> standardRep = new StandardResponseBean<List<GetTasksResponseBean>>();
    	try {
    		List<Task> tasks = activitiService.getPendingTasks(getTasksRequestBean.getAssignee());
            List<GetTasksResponseBean> dtos = new ArrayList<GetTasksResponseBean>();
            for (Task task : tasks) {
            	GetTasksResponseBean getTasksResponseBean = new GetTasksResponseBean();
            	getTasksResponseBean.setAssignee(task.getAssignee());
            	getTasksResponseBean.setCreateTime(task.getCreateTime());
            	getTasksResponseBean.setId(task.getId());
            	getTasksResponseBean.setName(task.getName());
                dtos.add(getTasksResponseBean);
            }
    		standardRep.setCode(StandardReponseCodeBean.ResponseCode.success.getCode());
        	standardRep.setMessage(StandardReponseCodeBean.ResponseCode.success.name());
        	standardRep.setBody(dtos);
    	}catch(Exception e) {
    		standardRep.setCode(StandardReponseCodeBean.ResponseCode.fail.getCode());
        	standardRep.setMessage(e.getMessage());
        	logger.error(e.getStackTrace().toString());
    	}
        return standardRep;
    }
    
    @RequestMapping(value="/getHistoricalTasks", method= RequestMethod.POST)
    public StandardResponseBean<List<GetTasksResponseBean>> getHistoricalTasks(@RequestBody GetTasksRequestBean getTasksRequestBean) {
    	StandardResponseBean<List<GetTasksResponseBean>> standardRep = new StandardResponseBean<List<GetTasksResponseBean>>();
    	try {
    		List<HistoricTaskInstance> tasks = activitiService.getHistoricalTasks(getTasksRequestBean.getAssignee());
            List<GetTasksResponseBean> dtos = new ArrayList<GetTasksResponseBean>();
            for (HistoricTaskInstance task : tasks) {
            	GetTasksResponseBean getTasksResponseBean = new GetTasksResponseBean();
            	getTasksResponseBean.setAssignee(task.getAssignee());
            	getTasksResponseBean.setClaimTime(task.getClaimTime());
            	getTasksResponseBean.setCreateTime(task.getCreateTime());
            	getTasksResponseBean.setEndTime(task.getEndTime());
            	getTasksResponseBean.setId(task.getId());
            	getTasksResponseBean.setName(task.getName());
            	getTasksResponseBean.setWorkTimeInMillis(task.getWorkTimeInMillis());
                dtos.add(getTasksResponseBean);
            }
    		standardRep.setCode(StandardReponseCodeBean.ResponseCode.success.getCode());
        	standardRep.setMessage(StandardReponseCodeBean.ResponseCode.success.name());
        	standardRep.setBody(dtos);
    	}catch(Exception e) {
    		standardRep.setCode(StandardReponseCodeBean.ResponseCode.fail.getCode());
        	standardRep.setMessage(e.getMessage());
        	logger.error(e.getStackTrace().toString());
    	}
        return standardRep;
    }
    
    @RequestMapping(value="/completeTask", method= RequestMethod.POST)
    public StandardResponseBean<?> completeTask(@RequestBody CompleteTaskRequestBean completeTaskRequestBean) {    
        try {
        	activitiService.completeTask(completeTaskRequestBean);
        	standardRep.setCode(StandardReponseCodeBean.ResponseCode.success.getCode());
        	standardRep.setMessage(StandardReponseCodeBean.ResponseCode.success.name());
		} catch (Exception e) {
			standardRep.setCode(StandardReponseCodeBean.ResponseCode.fail.getCode());
        	standardRep.setMessage(e.getMessage());
        	logger.error(e.getStackTrace().toString());
		}
        
		return standardRep;
    }

}
