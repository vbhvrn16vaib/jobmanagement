package com.jobmanagement.service.jobs;

import com.jobmanagement.entities.Job;
import com.jobmanagement.entities.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileCleaningJobServiceImpl implements JobService {

	private int priority;
	private Job job;
	private Logger log = LoggerFactory.getLogger(FileCleaningJobServiceImpl.class);

	public FileCleaningJobServiceImpl(Job job) {
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
		log.info("file cleaning  rollback is initiated !!!");
	}

	public Job call() throws InterruptedException {
		log.info("File cleaning service started !!");
		job.setStatus(JobStatus.RUNNING);
		Thread.sleep(5000);
		job.setStatus(JobStatus.SUCCESS);
		log.info("File cleaning successful!!");
		return job;
	}
}
