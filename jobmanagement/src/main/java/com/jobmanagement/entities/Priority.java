package com.jobmanagement.entities;

public enum Priority {
	HIGH(0),
	LOW(1),
	MEDIUM(2);

	private int val;

	Priority(int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}
}
