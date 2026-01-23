package io.labber.kbadm.model;

public enum Status {

	INITIAL("Initial"), 
	COMPLETE("Complete"), 
	INCOMPLETE("Incomplete"),
	FAILED("Failed");

	private final String status;

	/**
	 * 
	 * @param status
	 */
	private Status(
		String status
	) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

}
