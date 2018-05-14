package com.jobmanagement.service;

import com.jobmanagement.entities.Job;
import com.jobmanagement.entities.Priority;
import com.jobmanagement.service.jobs.DataLoadingJobImpl;
import com.jobmanagement.service.jobs.FileCleaningJobServiceImpl;
import com.jobmanagement.service.jobs.JobService;
import com.jobmanagement.service.jobs.SendingMailJobImpl;
import com.jobmanagement.service.scheduler.CustomExecutorServiceImpl;
import com.jobmanagement.service.scheduler.ExecutorService;
import com.jobmanagement.service.scheduler.ScheduledExecutorFixedRateServiceImpl;
import java.util.Collections;
import org.junit.Test;

public class JobSchedulerTest {

	/**
	 * Without Scheduling
	 */
	@Test
	public void testCustomScheduler() {

		//Create Jobs
		Job dataMovement = new Job("dataMovement", 1, "DB",Priority.LOW);
		Job fileManagement = new Job("fileManage", 2, "fileSystem", Priority.HIGH);


		JobService jobService = new DataLoadingJobImpl(dataMovement);
		JobService jobService2 = new FileCleaningJobServiceImpl(fileManagement);

		ExecutorService executorService = new CustomExecutorServiceImpl(1);
		executorService.addJob(jobService);
		executorService.addJob(jobService2);

		//Executor
		executorService.start();
	}


	/**
	 * Scheduler can be scheduled to run with delay frequency
	 */
	@Test
	public void testSchedulerWithSchedule() {

		//Create Jobs
		Job dataMovement = new Job("dataMovement", 1, "DB",Priority.LOW);
		Job fileManagement = new Job("fileManage", 2, "fileSystem", Priority.HIGH);


		JobService jobService = new DataLoadingJobImpl(dataMovement);
		JobService jobService2 = new FileCleaningJobServiceImpl(fileManagement);

		ExecutorService executorService = new ScheduledExecutorFixedRateServiceImpl(Collections.singletonList(jobService),1,1l);

		executorService.addJob(jobService2);

		//Executor
		executorService.start();

	}

	/**
	 * Failing scenario
	 */
	@Test
	public void testSchedulerWithRollBack() {
		//Create Jobs
		Job dataMovement = new Job("dataMovement", 1, "DB",Priority.HIGH);
		Job sendMail = new Job("sendMail", 2, "Send Mail", Priority.LOW);
		Job fileManagement = new Job("fileManagement", 3, "filemanagement", Priority.LOW);


		JobService jobService = new DataLoadingJobImpl(dataMovement);
		JobService jobService2 = new FileCleaningJobServiceImpl(fileManagement);
		JobService jobService3 = new SendingMailJobImpl(sendMail);

		ExecutorService executorService = new CustomExecutorServiceImpl(1);
		executorService.addJob(jobService);
		executorService.addJob(jobService2);
		executorService.addJob(jobService3);

		//Executor
		executorService.start();
	}

}
