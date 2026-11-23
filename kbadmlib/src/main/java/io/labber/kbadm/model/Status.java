package io.labber.kbadm.model;

public enum Status {

	INITIAL("Initial", false), 
	COMPLETE("Complete", false), 
	INCOMPLETE("Incomplete", false),
	INCONSISTENT("Inconsistent", true),
	FAILED("Failed", false);

	private final String status;
	private final boolean repairable;

	/**
	 * 
	 * @param status
	 * @param repairable
	 */
	private Status(
		String status, 
		boolean repairable
	) {
		this.status = status;
		this.repairable = repairable;
	}

	public String getStatus() {
		return this.status;
	}

	public boolean isRepairable() {
		return this.repairable;
	}

}
