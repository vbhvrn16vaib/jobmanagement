package com.jobmanagement.service.jobs;

import com.jobmanagement.entities.Job;
import java.util.concurrent.Callable;

public interface JobService extends Callable {

	void failOver();

	Job getJob();

	int getPriority();
}
