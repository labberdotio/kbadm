
//
// Copyright (c) 2026, John Grundback
// All rights reserved.
//

package io.labber.kbadm.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;

/**
 * 
 * @author john
 *
 */
@Component
public class ChatService {

	@Autowired
	protected ChatClient chatClient;

	/**
	 * 
	 * @return
	 */
	public ChatClient chatClient() {
		return chatClient;
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	public ChatChunkResponse map(
		ChatResponse response
	) {
		return new ChatChunkResponse(
			response.getResult().getOutput().getText()
		);
	}

	/**
	 * 
	 * @param prompt
	 * @return
	 */
	public Flux<ServerSentEvent<ChatChunkResponse>> chat(
		String prompt
	) {
		return chatClient.prompt()
			.user(userMessage -> userMessage.text(prompt))
			.stream()
			.chatResponse()
			// .filter(response -> response.getResult() != null)
			// .map(response -> response.getResult().getOutput().getText())
			// .filter(text -> text != null && !text.isEmpty())
			// .filter(text -> !text.isEmpty())
			// .map(text -> ServerSentEvent.<ChatChunkResponse>builder()
			.map(response -> ServerSentEvent.<ChatChunkResponse>builder()
				.id("message")
				.event("content")
				.data(this.map(response))
				.build()
			);
			// .distinctUntilChanged();
	}

}
