package com.es.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.entity.TaskEstimates;
import com.es.repository.EstimatesRepository;
import com.es.service.EstimatesService;

@Service
public class EstimatesServiceImpl implements EstimatesService {

	
	@Autowired
	EstimatesRepository estimatesRepository;
	
	@Override
	public TaskEstimates getEstimatesById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskEstimates getEstimatesByTaskId(String taskId) {
		TaskEstimates estimates = new TaskEstimates();
		estimates = estimatesRepository.findByTaskId(taskId);
		return estimates;
	}

	@Override
	public TaskEstimates saveEstimates(TaskEstimates taskEstimates) {
		if(taskEstimates != null) {
			estimatesRepository.save(taskEstimates);
			return taskEstimates;
		}
		return null;
	}

	@Override
	public List<TaskEstimates> estimateslist(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
