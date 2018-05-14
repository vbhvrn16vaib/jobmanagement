package com.jobmanagement.entities;

import java.time.LocalTime;
import java.util.List;

public class JobBatch {
	private String batchId;
	private List<Job> lists;
	private LocalTime startTime;
	private boolean isCompleted;
}
