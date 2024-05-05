package com.es.controller;	
	import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.entity.TaskEstimates;
import com.es.response.EstimatesResponse;
import com.es.response.ExceptionEnum;
import com.es.response.SuccessEnum;
import com.es.response.TaskEstimatesResponse;
import com.es.service.EstimatesService;
import com.es.service.JIRARestService;
	 
	@RestController
	@RequestMapping("/estimation-tool")
	public class EstimatesController {
		@Autowired
		EstimatesService estimatesService;
		@Autowired
		JIRARestService jiraRestService;
		@PostMapping("/updatestimates")
		public EstimatesResponse saveEstimates(@RequestBody Map<String, Object> requestBody, Model model) {
		    EstimatesResponse response = new EstimatesResponse();

		    Object updateTaskObj = requestBody.get("updateTask");

		    if (updateTaskObj instanceof List) {
		    	//Estimates estimates = new Estimates();
		        List<Map<String, Object>> updateTaskList = (List<Map<String, Object>>) updateTaskObj;
		        for (Map<String, Object> task : updateTaskList) {
		            String taskId = (String) task.get("id");
		            int aiEstimates = (int) task.get("aiEstimate");
//		            estimates.setTaskId(taskId);
//		            estimates.setTaskId(aiEstimates);

		            //estimatesService.saveEstimates(estimates);
		            this.jiraRestService.updateToJIRA(taskId, aiEstimates);

		            if (taskId == null) {
		                response.setCode(ExceptionEnum.INVALID_AUTH_USER.getErrorCode());
		                response.setMessage(ExceptionEnum.INVALID_AUTH_USER.getMessage());
		                return response;
		            }
		        }
		        response.setData(updateTaskObj); 
		        response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
		        response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
		        return response;
		    } else if(updateTaskObj != null){
		    	
		         String taskId = (String) ((Map<String, Object>) updateTaskObj).get("id");
		         int aiEstimates = (int) ((Map<String, Object>) updateTaskObj).get("aiEstimate");
		         this.jiraRestService.updateToJIRA(taskId, aiEstimates);
		         response.setData(updateTaskObj); 
			        response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
			        response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
			        return response;
		       
		    }else {
		        // Handle if it's an unexpected type
		        response.setCode(ExceptionEnum.FAILED_TYPE.getErrorCode());
		        response.setMessage("Invalid updateTask format in the request");
		        return response;
		    }
		}
		
		@PostMapping("/savetaskestimates")
		public TaskEstimatesResponse saveTaskEstimates(@RequestBody Map<String, Object> requestBody) {
		    TaskEstimatesResponse response = new TaskEstimatesResponse();

		    try {
		        // Extract "updateTask" object from the request body
		        Map<String, Object> updateTaskObj = (Map<String, Object>) requestBody.get("updateTask");

		        // Create a TaskEstimates object and set values
		        TaskEstimates taskEstimates = new TaskEstimates();

		        // Extract values from the map and handle null values appropriately
		        Object lowValue = updateTaskObj.get("lowEstimate");
		        Object realisticValue = updateTaskObj.get("realisticEstimate");
		        Object highValue = updateTaskObj.get("highEstimate");
		        Object taskIdValue = updateTaskObj.get("id");

		        // Set values in TaskEstimates object after checking for null
		        taskEstimates.setLow(lowValue != null ? Integer.parseInt(lowValue.toString()) : 0);
		        taskEstimates.setRealistic(realisticValue != null ? Integer.parseInt(realisticValue.toString()) : 0);
		        taskEstimates.setHigh(highValue != null ? Integer.parseInt(highValue.toString()) : 0);
		        taskEstimates.setTaskId(taskIdValue != null ? taskIdValue.toString() : "");

		        // Save TaskEstimates to the database using Spring Data JPA repository (assumes you have a repository)
		        estimatesService.saveEstimates(taskEstimates);

		        response.setCode(200);
		        response.setData(updateTaskObj);
		    } catch (Exception e) {
		        // Handle exceptions appropriately
		        response.setCode(500); // Internal Server Error
		        response.setMessage("Error saving task estimates: " + e.getMessage());
		    }

		    return response;
		}


}
