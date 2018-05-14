package com.jobmanagement.controller;

import com.jobmanagement.entities.Job;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobRegistrationController {

	/**
	 * get job details
	 */
	@GetMapping("/job/{id}")
	public void getJobDetails(@PathVariable String id){
		throw new UnsupportedOperationException("Not supported as of now!!");
	}

	/**
	 * register job
	 */
	@PostMapping("/job/")
	public void createJob(@RequestBody Job job){

	}


}
