package com.jobmanagement.service.jobs;

import com.jobmanagement.entities.Job;
import com.jobmanagement.entities.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendingMailJobImpl implements JobService {

	private int priority;
	private Job job;
	private Logger log = LoggerFactory.getLogger(SendingMailJobImpl.class);


	public SendingMailJobImpl(Job job) {
		priority = job.getPriority();
		this.job = job;
	}

	public Job getJob() {
		return job;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public void failOver() {
		log.info("No rollback required MailServices!!");
	}

	public Object call() throws Exception {
		job.setStatus(JobStatus.RUNNING);
		log.info("Sending Mail job started : {}!!!",job);
		Thread.sleep(3000);
		throw new InterruptedException("Failed");
	}
}
