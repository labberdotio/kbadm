
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

	private String id;
	private String messageId;

	private String type;
	private String content;

	/**
	 * 
	 * @param id
	 * @param messageId
	 * @param type
	 * @param content
	 */
	public ChatTypeResponse(
		String id, 
		String messageId, 
		String type, 
		String content
	) {
		this.id = id;
		this.messageId = messageId;
		this.type = type;
		this.content = content;
	}

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public String getMessageId() {
		return messageId;
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

	/**
	 * 
	 * @return
	 */
	public String getDelta() {
		return content;
	}

}
