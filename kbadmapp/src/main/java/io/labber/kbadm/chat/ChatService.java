
//
// Copyright (c) 2026, John Grundback
// All rights reserved.
//

package io.labber.kbadm.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
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

	/**
	 * 
	 * @param url
	 * @param model
	 * @return
	 */
	public ChatClient chatClient(
		String url, String model
	) {

		OllamaApi ollamaApi = OllamaApi.builder().baseUrl(
			url
		).build();

		OllamaChatModel chatModel = OllamaChatModel.builder().ollamaApi(
			ollamaApi
		// ).defaultOptions(
		).options(
			OllamaChatOptions.builder().model(
				model
			).enableThinking().build()
		).build();

		ChatClient chatClient = ChatClient.builder(
			chatModel
		).build();

		return chatClient;
	}

	/**
	 * 
	 * @param delta
	 * @return
	 */
	public ChatChunkResponse map(String delta) {
		return new ChatChunkResponse(delta);
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	public ChatChunkResponse map(ChatResponse response) {
		return new ChatChunkResponse(response.getResult().getOutput().getText());
	}

	/**
	 * 
	 * @param prompt
	 * @return
	 */
	public Flux<ServerSentEvent<ChatChunkResponse>> chat(
		String prompt
	) {
		// return chatClient.prompt()
		return this.chatClient(
			"http://10.88.88.180:11434", 
			"gpt-oss"
		).prompt()
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
