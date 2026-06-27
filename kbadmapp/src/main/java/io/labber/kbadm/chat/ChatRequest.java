
//
// Copyright (c) 2026, John Grundback
// All rights reserved.
//

package io.labber.kbadm.chat;

/**
 * 
 * @author john
 *
 */
public class ChatRequest {

	private String prompt;

	/**
	 * 
	 * @return
	 */
	public String getPrompt() {
		return prompt;
	}

	/**
	 * 
	 * @param prompt
	 */
	public void setPrompt(
		String prompt
	) {
		this.prompt = prompt;
	}

	/**
	 * 
	 * @return
	 */
	@Deprecated
	public String getMessage() {
		return prompt;
	}

}
