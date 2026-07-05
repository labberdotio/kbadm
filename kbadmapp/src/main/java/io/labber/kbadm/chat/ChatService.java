
//
// Copyright (c) 2026, John Grundback
// All rights reserved.
//

package io.labber.kbadm.chat;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore;
import org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore.Neo4jDistanceType;
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

	protected boolean started = false;
	protected boolean thinking = false;
	protected boolean texting = false;

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
	 * @param url
	 * @param model
	 * @return
	 */
	public VectorStore vectorStore (
		String url, String model
	) {

		String databaseName = "neo4j";

		// String indexType = null;
		String indexName = "custom-index";
		String labelName = "Document";
		String embeddingProperty = "embedding";
		Neo4jDistanceType distanceType = Neo4jDistanceType.COSINE;

		int dimensions = 1536; // 1024;

		OllamaApi ollamaApi = OllamaApi.builder().baseUrl(
			url
		).build();

		// Driver neo4jDriver = GraphDatabase.driver("neo4j://10.88.88.194:7687",
		Driver neo4jDriver = GraphDatabase.driver("neo4j://10.88.88.195:7687",
			AuthTokens.basic("neo4j", "neo4j"));

		EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder().ollamaApi(
			ollamaApi
		// ).defaultOptions(
		).options(
			OllamaEmbeddingOptions.builder().model(
				model
			).build()
		).build();

		VectorStore vectorStore = Neo4jVectorStore.builder(
			neo4jDriver, 
			embeddingModel
		)
		// .databaseName(
		// 	this.databaseName // this.config.getDatabaseName()
		// )
		.distanceType(
			distanceType // this.config.getDistanceType()
		).embeddingDimension(
			dimensions // this.config.getDimensions()
		).label(
			labelName // this.config.getLabelName()
		).embeddingProperty(
			embeddingProperty // this.config.getEmbeddingProperty()
		).indexName(
			indexName // this.config.getIndexName()
		).initializeSchema(
			false // this.config.isInitializeSchema()
		).batchingStrategy(
			new TokenCountBatchingStrategy()
		).build();

		return vectorStore;
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
	public ChatTypeResponse map(ChatResponse response) {
		if( response.getResult().getMetadata().containsKey("thinking")) {
			return new ChatTypeResponse(
				"message", 
				"reasoning-1", 
				"reasoning", 
				response.getResult().getOutput().getText()
			);
		}
		return new ChatTypeResponse(
			"message", 
			"text-1", 
			"text", 
			response.getResult().getOutput().getText()
		);
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	public Flux<ServerSentEvent<ChatTypeResponse>> flatMap(ChatResponse response) {

		if( this.started == false ) {
			this.started = true;
			return Flux.just(
				ServerSentEvent.<ChatTypeResponse>builder()
				.id("message")
				.event("start")
				.data(
					new ChatTypeResponse(
						"message", 
						"text-1", 
						"start", 
						response.getResult().getOutput().getText()
					)
				)
				.build()
			);
		}

		if( response.getResult().getMetadata().containsKey("thinking")) {

			if( this.thinking == false ) {
				this.thinking = true;
				return Flux.just(
					ServerSentEvent.<ChatTypeResponse>builder()
						.id("message")
						.event("reasoning-start")
						.data(
							new ChatTypeResponse(
								"message", 
								"reasoning-1", 
								"reasoning-start", 
								// response.getResult().getOutput().getText()
								response.getResult().getMetadata().get("thinking").toString()
							)
						)
						.build(), 
					ServerSentEvent.<ChatTypeResponse>builder()
						.id("message")
						.event("reasoning-delta")
						.data(
							new ChatTypeResponse(
								"message", 
								"reasoning-1", 
								"reasoning-delta", 
								// response.getResult().getOutput().getText()
								response.getResult().getMetadata().get("thinking").toString()
							)
						)
						.build()
				);
			} else {
				return Flux.just(
					ServerSentEvent.<ChatTypeResponse>builder()
						.id("message")
						.event("reasoning-delta")
						.data(
							new ChatTypeResponse(
								"message", 
								"reasoning-1", 
								"reasoning-delta", 
								// response.getResult().getOutput().getText()
								response.getResult().getMetadata().get("thinking").toString()
							)
						)
						.build()
				);
			}

		}

		if( response.getMetadata().containsKey("done") ) {
			if( Boolean.parseBoolean(
				response.getMetadata().get("done").toString()
			)) {
				this.started = false;
				this.thinking = false;
				this.texting = false;
				return Flux.just(
					ServerSentEvent.<ChatTypeResponse>builder()
						.id("message")
						.event("text-end")
						.data(
							new ChatTypeResponse(
								"message", 
								"text-1", 
								"text-end", 
								response.getResult().getOutput().getText()
							)
						)
						.build(), 
					ServerSentEvent.<ChatTypeResponse>builder()
						.id("message")
						.event("finish")
						.data(
							new ChatTypeResponse(
								"message", 
								"text-1", 
								"finish", 
								response.getResult().getOutput().getText()
							)
						)
						.build()
				);
			}
		}

		if( this.texting == false ) {
			this.thinking = false;
			this.texting = true;
			return Flux.just(
				ServerSentEvent.<ChatTypeResponse>builder()
				.id("message")
				.event("reasoning-end")
				.data(
					new ChatTypeResponse(
						"message", 
						"reasoning-1", 
						"reasoning-end", 
						// response.getResult().getOutput().getText()
						// response.getResult().getMetadata().get("thinking").toString()
						null
					)
				)
				.build(), 
				ServerSentEvent.<ChatTypeResponse>builder()
					.id("message")
					.event("text-start")
					.data(
						new ChatTypeResponse(
							"message", 
							"text-1", 
							"text-start", 
							response.getResult().getOutput().getText()
						)
					)
					.build(), 
				ServerSentEvent.<ChatTypeResponse>builder()
					.id("message")
					.event("text-delta")
					.data(
						new ChatTypeResponse(
							"message", 
							"text-1", 
							"text-delta", 
							response.getResult().getOutput().getText()
						)
					)
					.build()
			);
		} else {
			return Flux.just(
				ServerSentEvent.<ChatTypeResponse>builder()
					.id("message")
					.event("text-delta")
					.data(
						new ChatTypeResponse(
							"message", 
							"text-1", 
							"text-delta", 
							response.getResult().getOutput().getText()
						)
					)
					.build()
			);
		}

	}

	/**
	 * 
	 * @param prompt
	 * @return
	 */
	public Flux<ServerSentEvent<ChatTypeResponse>> chat(
		String prompt
	) {
		// return chatClient.prompt()
		return this.chatClient(
			"http://10.88.88.180:11434", 
			"gpt-oss"
		).prompt()
			.advisors(QuestionAnswerAdvisor.builder(
				this.vectorStore(
					"http://10.88.88.180:11434", 
					"nomic-embed-text"
				)
			).build())
			.user(userMessage -> userMessage.text(prompt))
			.stream()
			.chatResponse()
			// .filter(response -> response.getResult() != null)
			// .map(response -> response.getResult().getOutput().getText())
			// .filter(text -> text != null && !text.isEmpty())
			// .filter(text -> !text.isEmpty())
			// .map(text -> ServerSentEvent.<ChatChunkResponse>builder()
			// .map(response -> ServerSentEvent.<ChatTypeResponse>builder()
			// 	.id("message")
			// 	.event("text-delta")
			// 	.data(this.map(response))
			// 	.build()
			// );
			.flatMap(response -> this.flatMap(response));
			// .distinctUntilChanged();
	}

}
