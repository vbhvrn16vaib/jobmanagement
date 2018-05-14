package com.jobmanagement.service.scheduler;

import com.jobmanagement.entities.Job;
import com.jobmanagement.entities.JobStatus;
import com.jobmanagement.service.jobs.JobService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomExecutorServiceImpl implements ExecutorService {

	private Logger log = LoggerFactory.getLogger(CustomExecutorServiceImpl.class);
	private Set<JobService> jobServiceList;
	private ThreadPoolExecutor executor;

	public CustomExecutorServiceImpl(Integer threads) {
		this.jobServiceList = new HashSet<>();
		this.executor = getPriorityExecutor(threads);
	}

	@Override
	public void addJob(JobService task) {
		log.info("Registration of new Job {} - {}", task.getJob().getJobId(),task.getJob().getStatus());
		jobServiceList.add(task);
		log.info("{}", task);
	}

	@Override
	public void removeJob() {
		throw new UnsupportedOperationException("Currently not implemented !!");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void start() {
		jobServiceList.forEach(this::executeJob);
	}

	@SuppressWarnings("unchecked")
	private void executeJob(JobService jobService) {
		Job job = jobService.getJob();
		try {
			Future<?> f = executor.submit(jobService);

			f.get(1000000, TimeUnit.MILLISECONDS);

			if (f.isDone()) {

				job.setStatus(JobStatus.SUCCESS);
				log.info("Execution of job completed successfully {} - {}!!!", job.getJobId(),job.getStatus());

			} else {
				callFailover(jobService, job);
			}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			callFailover(jobService, job);
		}
	}

	private void callFailover(JobService jobService, Job job) {
		job.setStatus(JobStatus.FAILED);
		log.error("Execution of Failed {} - {}!!!", job.getJobId(),job.getStatus());
		log.info("Executor calling failover !!!");
		jobService.failOver();
	}

	@SuppressWarnings("unchecked")
	private ThreadPoolExecutor getPriorityExecutor(int nThreads) {
		return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
				new PriorityBlockingQueue<>(10, PriorityFuture.COMP)) {

			protected <T> RunnableFuture<T> newTaskFor(JobService jobService) {
				RunnableFuture<T> newTaskFor = super.newTaskFor(jobService);
				return new PriorityFuture<T>(newTaskFor, jobService.getPriority());
			}
		};
	}
}
