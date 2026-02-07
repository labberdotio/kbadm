
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.validator;

import io.labber.kbadm.model.Status;

/**
 * 
 * @author john
 *
 */
public class ValidationStatus {

	protected Status status;
	protected String reason;
	protected boolean repairable;

	/**
	 * 
	 * @param status
	 * @param reason
	 * @param repairable
	 */
	public ValidationStatus(
		Status status, 
		String reason, 
		boolean repairable
	) {
		this.status = status;
		this.reason = reason;
		this.repairable = repairable;
	}

	/**
	 * 
	 * @param status
	 * @param reason
	 */
	public ValidationStatus(
		Status status, 
		String reason
	) {
		this.status = status;
		this.reason = reason;
		this.repairable = status.isRepairable();
	}

	/**
	 * 
	 * @return
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * 
	 * @return
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isRepairable() {
		return repairable;
	}

}
