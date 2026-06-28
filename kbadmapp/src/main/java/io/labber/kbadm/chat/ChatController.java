
//
// Copyright (c) 2026, John Grundback
// All rights reserved.
//

package io.labber.kbadm.chat;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

/**
 * 
 * @author john
 *
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

	private final ChatService chatService;

	/**
	 * 
	 * @param chatService
	 */
	public ChatController(
		ChatService chatService
	) {
		this.chatService = chatService;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<ChatTypeResponse>> chat(
		@RequestBody ChatRequest request
	) {
		return chatService.chat(request.getPrompt());
	}

}
