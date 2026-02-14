
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
public class RepairStatus {

	protected Status status;
	protected String reason;
	protected boolean repaired;

	/**
	 * 
	 * @param status
	 * @param reason
	 * @param repairable
	 */
	public RepairStatus(
		Status status, 
		String reason, 
		boolean repaired
	) {
		this.status = status;
		this.reason = reason;
		this.repaired = repaired;
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
	public boolean isRepaired() {
		return repaired;
	}

}
