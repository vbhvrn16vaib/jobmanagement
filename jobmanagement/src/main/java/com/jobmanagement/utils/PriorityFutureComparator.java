package com.jobmanagement.utils;

import com.jobmanagement.service.jobs.JobService;
import java.util.Comparator;

public class PriorityFutureComparator implements Comparator<JobService> {
	public int compare(JobService o1, JobService o2) {
		if (o1 == null && o2 == null)
			return 0;
		else if (o1 == null)
			return -1;
		else if (o2 == null)
			return 1;
		else {
			int p1 = o1.getPriority();
			int p2 = o2.getPriority();

			return Integer.compare(p1, p2);
		}
	}
}
