package com.jobmanagement.service.scheduler;

import com.jobmanagement.service.jobs.JobService;

public interface ExecutorService {

	void addJob(JobService jobService);

	void removeJob();

	void start();
}
