package com.jobmanagement.entities;

import java.time.LocalDateTime;

public class Job {
	private String name;
	private Integer jobId;
	private String action;
	private JobStatus status;
	private LocalDateTime startTime;
	private Boolean isActive;
	private Integer priority;

	public Job(String name, Integer jobId, String action, JobStatus status, LocalDateTime startTime, Boolean isActive, Integer priority) {
		this.name = name;
		this.jobId = jobId;
		this.action = action;
		this.status = status;
		this.startTime = startTime;
		this.isActive = isActive;
		this.priority = priority;
	}


	public Job(String name, Integer jobId, String action, Priority priority) {
		this(name, jobId, action, JobStatus.QUEUED, LocalDateTime.now(), Boolean.TRUE, priority.getVal());
	}

	public Job(String name, Integer jobId, String action) {
		this.name = name;
		this.jobId = jobId;
		this.action = action;
		this.status = JobStatus.QUEUED;
		this.priority = Priority.MEDIUM.getVal();
		this.startTime = LocalDateTime.now();
	}

	public Job(String name, String action) {
		this.name = name;
		this.action = action;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	@Override
	public String toString() {
		return "Job{" +
			   "name='" + name + '\'' +
			   ", jobId=" + jobId +
			   ", action='" + action + '\'' +
			   ", status=" + status +
			   ", startTime=" + startTime +
			   ", isActive=" + isActive +
			   ", priority=" + priority +
			   '}';
	}
}
