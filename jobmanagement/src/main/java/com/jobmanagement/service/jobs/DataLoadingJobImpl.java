package com.jobmanagement.service.jobs;

import com.jobmanagement.entities.Job;
import com.jobmanagement.entities.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataLoadingJobImpl implements JobService {

	private int priority;
	private Job job;
	private Logger log = LoggerFactory.getLogger(DataLoadingJobImpl.class);

	public DataLoadingJobImpl(Job job) {
		priority = job.getPriority();
		this.job = job;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	public Job getJob() {
		return job;
	}

	@Override
	public void failOver() {
		log.info("Rollback is started for dataMovement!!");
		log.info("Rollback successful");

	}

	public Job call() throws InterruptedException {
			job.setStatus(JobStatus.RUNNING);
			log.info("Data Loading job started : {}!!!",job);
			Thread.sleep(3000);
			log.info("Data loading task executed successfully");
			job.setStatus(JobStatus.SUCCESS);
			return job;
	}

}
