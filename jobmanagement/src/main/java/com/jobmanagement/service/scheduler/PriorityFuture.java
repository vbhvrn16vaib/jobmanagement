package com.jobmanagement.service.scheduler;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

public class PriorityFuture<T> implements RunnableFuture<T> {

	private RunnableFuture<T> src;
	private int priority;

	public PriorityFuture(RunnableFuture<T> other, int priority) {
		this.src = other;
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return src.cancel(mayInterruptIfRunning);
	}

	public boolean isCancelled() {
		return src.isCancelled();
	}

	public boolean isDone() {
		return src.isDone();
	}

	public T get() throws InterruptedException, ExecutionException {
		return src.get();
	}

	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
		return src.get();
	}

	public void run() {
		src.run();
	}

	static Comparator<Runnable> COMP = (o1, o2) -> {
		if (o1 == null && o2 == null)
			return 0;
		else if (o1 == null)
			return -1;
		else if (o2 == null)
			return 1;
		else {
			int p1 = ((PriorityFuture<?>) o1).getPriority();
			int p2 = ((PriorityFuture<?>) o2).getPriority();

			return Integer.compare(p1, p2);
		}
	};
}
