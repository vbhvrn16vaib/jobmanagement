package com.jobmanagement.service.scheduler;

import com.jobmanagement.entities.Job;
import com.jobmanagement.entities.JobStatus;
import com.jobmanagement.service.jobs.JobService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledExecutorFixedRateServiceImpl implements ExecutorService {

	private Logger log = LoggerFactory.getLogger(ScheduledExecutorFixedRateServiceImpl.class);
	private PriorityBlockingQueue<JobService> jobServiceList = new PriorityBlockingQueue(10, Comparator.comparing(JobService::getPriority));
	private ScheduledExecutorService executor;
	private boolean termainate;
	private Long delay;

	public ScheduledExecutorFixedRateServiceImpl(List<JobService> jobServiceList, Integer threads, Long delay) {
		this.jobServiceList.addAll(jobServiceList);
		this.executor = Executors.newScheduledThreadPool(threads);
		this.delay = delay;
		this.termainate = false;
	}

	@Override
	public void addJob(JobService jobService) {
		this.jobServiceList.add(jobService);
	}

	@Override
	public void removeJob() {
		throw new UnsupportedOperationException("This operation is not supported at the moment !!");
	}

	/**
	 * This will run with frequency according to delay set.
	 * Condition : Any one job should be in the queue for execution. Or Scheduler will stop
	 */
	@Override
	public void start() {
		int c = 0;
		while (!isTermainate() && jobServiceList.stream().anyMatch(x -> x.getJob().getStatus().equals(JobStatus.QUEUED))){
			delay = computeNextDelay(0,0,0);
			jobServiceList.forEach(this::execute);

			//Temporary to terminate demon while loop (We can use another ThreadPoolExecutor instead of while)
			if(c > 2){
				setTermainate(true);
			}
			c++;
		}
	}

	public void stop() {
		termainate = true;
		executor.shutdown();
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
			log.info("Forcefully Stopping the executor!!!");
		} catch (InterruptedException ex) {
			log.error("Interrupted !!");
		}
	}

	private boolean isTermainate() {
		return termainate;
	}

	public void setTermainate(boolean termainate) {
		this.termainate = termainate;
	}

	@SuppressWarnings("unchecked")
	private void execute(JobService jobService) {

		Job job = jobService.getJob();

//		if(job.getStatus().equals(JobStatus.FAILED)){
//			log.info("This job is already failed {} - {}",jobService.getJob());
//			return;
//		}

		try {
			log.info("delay : {}", delay);
			Future<?> x = executor.schedule(jobService, delay, TimeUnit.SECONDS);
			x.get();
			if (x.isDone()) {

				job.setStatus(JobStatus.SUCCESS);
				log.info("Scheduled Executor executed job Successfully : {} - {}", job.getJobId(),job.getStatus());
				Thread.sleep(100);

				job.setStatus(JobStatus.QUEUED);
				log.info("Scheduled Executor queuing job for next Run : {} - {}", job.getJobId(),job.getStatus());

			} else {
				job.setStatus(JobStatus.FAILED);
				log.info("Scheduled Executor failed to execute Job : {} - {}", job.getJobId(),job.getStatus());
				log.info("Executor called failover for {}!!!",job.getJobId());
				jobService.failOver();
			}
		} catch (Exception e) {
			job.setStatus(JobStatus.FAILED);
			log.info("Scheduled Executor failed to execute Job : {} - {}", job.getJobId(),job.getStatus());
			log.info("Executor called failover for {}!!!",job.getJobId());
			jobService.failOver();
		}
	}

	private Long computeNextDelay(int targetHour, int targetMin, int targetSec) {
		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.systemDefault();
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
		if (zonedNow.compareTo(zonedNextTarget) > 0) {
			zonedNextTarget = zonedNextTarget.plusSeconds(10);
		}

		Duration duration = Duration.between(zonedNow, zonedNextTarget);
		return duration.getSeconds();
	}
}
