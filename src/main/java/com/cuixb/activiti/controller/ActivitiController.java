package com.cuixb.activiti.controller;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cuixb.activiti.bean.request.ProcessReq;
import com.cuixb.activiti.bean.response.StandardRep;
import com.cuixb.activiti.bean.response.StandardRepCode;
import com.cuixb.activiti.service.ActivitiService;

@RestController
public class ActivitiController {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivitiController.class);
	@Autowired
    private ActivitiService activitiService;
	
	@Autowired
	private StandardRep<?> standardRep;

    @RequestMapping(value="/process", method= RequestMethod.POST)
    public StandardRep<?> startProcessInstance(@RequestBody ProcessReq processReq) {    
        try {
        	activitiService.startProcess(processReq);
        	standardRep.setCode(StandardRepCode.ResponseCode.success.getCode());
        	standardRep.setMessage(StandardRepCode.ResponseCode.success.name());
		} catch (Exception e) {
			standardRep.setCode(StandardRepCode.ResponseCode.fail.getCode());
        	standardRep.setMessage(e.getMessage());
        	logger.error(e.getStackTrace().toString());
		}
        
		return standardRep;
    }

    @RequestMapping(value="/getPendingTasks", method= RequestMethod.GET)
    public List<TaskRepresentation> getTasks() {
        List<Task> tasks = activitiService.getTasks("");
        List<TaskRepresentation> dtos = new ArrayList<TaskRepresentation>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        return dtos;
    }

    static class TaskRepresentation {

        private String id;
        private String name;

        public TaskRepresentation(String id, String name) {
            this.id = id;
            this.name = name;
        }

         public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

    }
}
