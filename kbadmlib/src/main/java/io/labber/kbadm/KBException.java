
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm;

/**
 * 
 * @author john
 *
 */
public class KBException extends Exception {

	private static final long serialVersionUID = -3468674727337642257L;

	/**
	 * 
	 * @param s
	 */
	public KBException(String s) {
		super(s);
	}

	/**
	 * 
	 * @param e
	 */
	public KBException(Exception e) {
		super(e);
	}

	/**
	 * 
	 * @param s
	 * @param e
	 */
	public KBException(String s, Exception e) {
		super(s, e);
	}

}
