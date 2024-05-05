package com.es.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.dto.TaskAiEstimatesDto;
import com.es.entity.TaskEstimates;
import com.es.response.AiEstimatesResponse;
import com.es.response.TaskEstimatesResponse;
import com.es.service.AiEstimatesService;

@RestController
@RequestMapping("/estimation-tool")
public class AiEstimatesController {
	
	@Autowired
	AiEstimatesService aiEstimatesService;
	
	@PostMapping("/aiestimates")
	public AiEstimatesResponse aiEstimates(@RequestBody Map<String, Object> requestBody) {
	    AiEstimatesResponse response = new AiEstimatesResponse();

	    try {
	        // Extract "updateTask" object from the request body
	        Map<String, Object> updateTaskObj = (Map<String, Object>) requestBody.get("value");

	        if (updateTaskObj != null) {
	            // Create a TaskAiEstimatesDto object and set values
	            TaskAiEstimatesDto taskEstimates = new TaskAiEstimatesDto();

	            // Extract values from the map and handle null values appropriately
	            taskEstimates.setTaskId(updateTaskObj.get("id") != null ? updateTaskObj.get("id").toString() : "");
	            taskEstimates.setLowEstimate(updateTaskObj.get("lowEstimate") != null ? Integer.parseInt(updateTaskObj.get("lowEstimate").toString()) : 0);
	            taskEstimates.setDescription(updateTaskObj.get("description") != null ? updateTaskObj.get("description").toString() : "");
	            taskEstimates.setTitle(updateTaskObj.get("title") != null ? updateTaskObj.get("title").toString() : "");
	            taskEstimates.setHighEstimate(updateTaskObj.get("highEstimate") != null ? Integer.parseInt(updateTaskObj.get("highEstimate").toString()) : 0);
	            taskEstimates.setRealisticEstimate(updateTaskObj.get("realisticEstimate") != null ? Integer.parseInt(updateTaskObj.get("realisticEstimate").toString()) : 0);
	            taskEstimates.setThreePointEstimate(updateTaskObj.get("threePointEstimate") != null ? Integer.parseInt(updateTaskObj.get("threePointEstimate").toString()) : 0);

	            // Check if taskEstimates is not null
	            if (taskEstimates != null) {
	                double weightedAverage = aiEstimatesService.weightedAverage(taskEstimates.getLowEstimate(), taskEstimates.getHighEstimate(), taskEstimates.getRealisticEstimate());
	                double standardDeviation = aiEstimatesService.standardDeviation(taskEstimates.getLowEstimate(), taskEstimates.getHighEstimate());

	                // Validate the values and perform further checks
	                if (weightedAverage != 0 && standardDeviation != 0) {
	                    double randomValue;
	                    
	                    // Generate random values until a non-zero value is obtained
	                    do {
	                        randomValue = Math.random() * 10;
	                    } while (randomValue == 0);

	                    // Convert to int if needed
	                    int intValue = (int) randomValue;
	                    // Create a response data object if needed
	                   
                          taskEstimates.setAiEstimate(intValue);
	                    response.setCode(200);
	                    response.setData(taskEstimates);
	                    return response;
	                } else {
	                    response.setCode(404);
	                    response.setMessage("weightedAverage or standardDeviation is null");
	                    return response;
	                }
	            } else {
	                response.setCode(404);
	                response.setMessage("all fields required");
	                return response;
	            }
	        } else {
	            response.setCode(404);
	            response.setMessage("updateTask is null");
	            return response;
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // Log the exception details for debugging
	        response.setCode(500);
	        response.setMessage("something went wrong: " + e.getMessage());
	        return response;
	    }
	}



}

