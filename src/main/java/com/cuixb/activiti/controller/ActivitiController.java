package com.cuixb.activiti.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cuixb.activiti.bean.request.CompleteTaskRequestBean;
import com.cuixb.activiti.bean.request.GetTasksRequestBean;
import com.cuixb.activiti.bean.request.ProcessRequestBean;
import com.cuixb.activiti.bean.response.GetHistoricalTasksResponseBean;
import com.cuixb.activiti.bean.response.GetPendingTasksResponseBean;
import com.cuixb.activiti.bean.response.StandardReponseCodeBean;
import com.cuixb.activiti.bean.response.StandardResponseBean;
import com.cuixb.activiti.service.ActivitiService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
public class ActivitiController {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivitiController.class);
	
	@Autowired
    private ActivitiService activitiService;
	
	@Autowired
	private StandardResponseBean<?> standardRep;
	
    @Autowired
	ProcessEngineConfiguration cfg;
    
    @Autowired
    ProcessEngine processEngine;
    
    @Autowired
    HistoryService historyService;
    

    @ApiOperation(value = "startProcess", notes = "start a process")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "processName", value = "workflow name", dataType = "string", paramType = "query", example = "EsmtcPlaceOrderProcess", required = true),
        @ApiImplicitParam(name = "businessKey", value = "your order id", dataType = "string", paramType = "query", example = "367966", required = true)
    })
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
    
    @ApiOperation(value = "getPendingTasks", notes = "get pending tasks")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "assignee", value = "staff's account", dataType = "string", paramType = "query", example = "cxinbin", required = false)
    })
    @RequestMapping(value="/getPendingTasks", method= RequestMethod.POST)
    public StandardResponseBean<List<GetPendingTasksResponseBean>> getPendingTasks(@RequestBody GetTasksRequestBean getTasksRequestBean) {
    	StandardResponseBean<List<GetPendingTasksResponseBean>> standardRep = new StandardResponseBean<List<GetPendingTasksResponseBean>>();
    	try {
    		List<Task> tasks = activitiService.getPendingTasks(getTasksRequestBean.getAssignee());
            List<GetPendingTasksResponseBean> dtos = new ArrayList<GetPendingTasksResponseBean>();
            for (Task task : tasks) {
            	GetPendingTasksResponseBean getTasksResponseBean = new GetPendingTasksResponseBean();
            	getTasksResponseBean.setAssignee(task.getAssignee());
            	getTasksResponseBean.setCreateTime(task.getCreateTime());
            	getTasksResponseBean.setId(task.getId());
            	getTasksResponseBean.setName(task.getName());
            	getTasksResponseBean.setBusinessKey(activitiService.getBusinessKeyByProcessInstanceId(task.getProcessInstanceId()));
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
    
    @ApiOperation(value = "getHistoricalTasks", notes = "get historical tasks")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "assignee", value = "staff's account", dataType = "string", paramType = "query", example = "cxinbin", required = false)
    })
    @RequestMapping(value="/getHistoricalTasks", method= RequestMethod.POST)
    public StandardResponseBean<List<GetHistoricalTasksResponseBean>> getHistoricalTasks(@RequestBody GetTasksRequestBean getTasksRequestBean) {
    	StandardResponseBean<List<GetHistoricalTasksResponseBean>> standardRep = new StandardResponseBean<List<GetHistoricalTasksResponseBean>>();
    	try {
    		List<HistoricTaskInstance> tasks = activitiService.getHistoricalTasks(getTasksRequestBean.getAssignee());
            List<GetHistoricalTasksResponseBean> dtos = new ArrayList<GetHistoricalTasksResponseBean>();
            for (HistoricTaskInstance task : tasks) {
            	GetHistoricalTasksResponseBean getTasksResponseBean = new GetHistoricalTasksResponseBean();
            	getTasksResponseBean.setAssignee(task.getAssignee());
            	getTasksResponseBean.setClaimTime(task.getClaimTime());
            	getTasksResponseBean.setCreateTime(task.getCreateTime());
            	getTasksResponseBean.setEndTime(task.getEndTime());
            	getTasksResponseBean.setId(task.getId());
            	getTasksResponseBean.setName(task.getName());
            	getTasksResponseBean.setWorkTimeInMillis(task.getWorkTimeInMillis());
            	getTasksResponseBean.setBusinessKey(activitiService.getBusinessKeyByProcessInstanceId(task.getProcessInstanceId()));
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
    
    @ApiOperation(value = "completeTask", notes = "complete this task")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "taskId", value = "task id", dataType = "string", paramType = "query", example = "15005", required = true),
        @ApiImplicitParam(name = "userId", value = "staff's account", dataType = "string", paramType = "query", example = "cxinbin", required = true)
    })
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
    
    @ApiOperation(value = "showWorkFlowImage", notes = "get image of current workflow")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "taskId", value = "task id", dataType = "string", paramType = "path", example = "10004", required = true)
    })
    @RequestMapping(value = "/showWorkFlowImage/{taskId}", method = RequestMethod.GET)  
	@ResponseBody
	public void showImage(@PathVariable String taskId,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String processId = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult().getProcessInstanceId();
		HistoricActivityInstance historicActivityInstance=historyService.createHistoricActivityInstanceQuery().processInstanceId(processId).unfinished().singleResult();
		List<String> arg2=null;
		String processDefinId="";
		if (historicActivityInstance!=null) {
			String activityId = historicActivityInstance.getActivityId();
			 // write the file
			arg2 = new ArrayList<>();
			//ACT_HI_ACTINST.ACT_ID_
			arg2.add(activityId);
			processDefinId=historicActivityInstance.getProcessDefinitionId();
		}else{
			HistoricProcessInstance HistoricProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();
			processDefinId=HistoricProcessInstance.getProcessDefinitionId();
		}
		
		//PROC_DEF_ID_	    
		BpmnModel model = processEngine.getRepositoryService().getBpmnModel(processDefinId);  
		InputStream inputStream=null;
		if (arg2!=null) {
			inputStream=cfg.getProcessDiagramGenerator().generateDiagram(model, "png", arg2, Collections.emptyList(), "宋体", "宋体", "宋体", null, 1.0);
		}else{
			inputStream=cfg.getProcessDiagramGenerator().generatePngDiagram(model);
		}
		response.setContentType("multipart/form-data");
		response.setCharacterEncoding("UTF-8");
		ServletOutputStream out = response.getOutputStream();  
		//read file input stream
        int len = 0;  
        byte[] buffer = new byte[1024 * 10];  
        while ((len = inputStream.read(buffer)) != -1){  
            out.write(buffer,0,len);  
        }  
        out.flush();  
       
	}

}
