
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
public class ChatResponseChunk {

	private String content;

	/**
	 * 
	 * @param content
	 */
	public ChatResponseChunk(
		String content
	) {
		this.content = content;
	}

	/**
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

}
