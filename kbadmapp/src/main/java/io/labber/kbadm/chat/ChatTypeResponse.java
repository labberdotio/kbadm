
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
public class ChatTypeResponse {

	private String type;
	private String content;

	/**
	 * 
	 * @param content
	 */
	public ChatTypeResponse(
		String type, 
		String content
	) {
		this.type = type;
		this.content = content;
	}

	/**
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

}
