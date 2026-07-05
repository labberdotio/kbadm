
// 
// Copyright (c) 2024, 2025, 2026, John Grundback
// All rights reserved.
// 

package io.labber.neo4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.SessionConfig;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import io.labber.kbadm.KBException;

/**
 * 
 * @author john
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
// public class Neo4JTest extends TestCase {
public class Neo4JTest {

	@Configuration
	static class ContextConfiguration {

		// Bean definitions go here.

		@Bean
		public org.neo4j.driver.SessionConfig sessionConfig() {
			return SessionConfig.defaultConfig();
		}

		// @Bean
		// public Driver driver() {
		//     return GraphDatabase.driver("neo4j://<host>:<bolt-port>",
		//             AuthTokens.basic("<username>", "<password>"));
		// }

		@Bean
		public Driver driver() {
			// return GraphDatabase.driver("neo4j://10.88.88.194:7687",
			return GraphDatabase.driver("neo4j://10.88.88.195:7687",
				AuthTokens.basic("neo4j", "neo4j"));
		}

		@Bean
		public ChatClient chatClient() {

			OllamaApi ollamaApi = OllamaApi.builder().baseUrl(
				"http://10.88.88.180:11434"
			).build();

			OllamaChatModel chatModel = OllamaChatModel.builder().ollamaApi(
				ollamaApi
			// ).defaultOptions(
			).options(
				OllamaChatOptions.builder().model(
					"gpt-oss"
				).enableThinking().build()
			).build();

			ChatClient chatClient = ChatClient.builder(
				chatModel
			).build();

			return chatClient;
		}

		@Bean
		public VectorStore vectorStore(Driver driver, EmbeddingModel embeddingModel) {
		    return Neo4jVectorStore.builder(driver, embeddingModel)
		        .databaseName("neo4j")                // Optional: defaults to "neo4j"
		        .distanceType(Neo4jDistanceType.COSINE) // Optional: defaults to COSINE
		        .embeddingDimension(1536)                      // Optional: defaults to 1536
		        .label("Document")                     // Optional: defaults to "Document"
		        .embeddingProperty("embedding")        // Optional: defaults to "embedding"
		        .indexName("custom-index")             // Optional: defaults to "spring-ai-document-index"
		        .initializeSchema(true)                // Optional: defaults to false
		        .batchingStrategy(new TokenCountBatchingStrategy()) // Optional: defaults to TokenCountBatchingStrategy
		        .build();
		}

		// This can be any EmbeddingModel implementation
		// @Bean
		// public EmbeddingModel embeddingModel() {
		//     return new OpenAiEmbeddingModel(new OpenAiApi(System.getenv("OPENAI_API_KEY")));
		// }

		@Bean
		public EmbeddingModel embeddingModel() {

			OllamaApi ollamaApi = OllamaApi.builder().baseUrl(
				"http://10.88.88.180:11434"
			).build();

			EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder().ollamaApi(
				ollamaApi
			// ).defaultOptions(
			).options(
				OllamaEmbeddingOptions.builder().model(
					"nomic-embed-text"
				).build()
			).build();

			return embeddingModel;
		}

	}

	// Bean references go here.

	@Autowired
	protected org.neo4j.driver.SessionConfig sessionConfig;

	@Autowired
	// protected org.neo4j.driver.Driver neo4jDriver;
	protected org.neo4j.driver.Driver driver;

	// @Autowired
	// protected OllamaApi ollamaApi;

	@Autowired
	protected ChatClient chatClient;

	@Autowired
	protected VectorStore vectorStore;
	
	// 

	/**
	 * 
	 * @return
	 */
	public ChatClient chatClient() {
		return this.chatClient;
	}

	/**
	 * 
	 * @return
	 */
	public VectorStore vectorStore() {
		return this.vectorStore;
	}

	/**
	 * 
	 * @throws KBException
	 */
	public void initSchema() throws KBException {

		// this.sessionConfig = org.neo4j.driver.SessionConfig.defaultConfig();

		try (var session = this.driver.session(this.sessionConfig)) {

			session.executeWriteWithoutResult(tx -> {
				tx.run("CREATE CONSTRAINT %s IF NOT EXISTS FOR (n:%s) REQUIRE n.%s IS UNIQUE"
					.formatted(this.constraintName(), this.label(), this.idProperty())).consume();

				var statement = """
						CREATE VECTOR INDEX `%s` IF NOT EXISTS FOR (n:%s) ON (n.%s)
								OPTIONS {indexConfig: {
								`vector.dimensions`: %d,
								`vector.similarity_function`: '%s'
								}}
						""".formatted(this.indexName(), this.label(), this.embeddingProperty(), this.embeddingDimension(),
						this.distanceType().name);
				tx.run(statement).consume();
			});

			// Bad idea to retry this...
			session.run("CALL db.awaitIndexes()").consume();
		}

	}

	/**
	 * 
	 * @return
	 */
	public String label() {
		return "Document";
	}

	/**
	 * 
	 * @return
	 */
	public String indexName() {
		return "custom-index";
	}

	/**
	 * 
	 * @return
	 */
	public String constraintName() {
		return Neo4jVectorStore.DEFAULT_CONSTRAINT_NAME;
	}

	/**
	 * 
	 * @return
	 */
	public String idProperty() {
		return Neo4jVectorStore.DEFAULT_ID_PROPERTY;
	}

	/**
	 * 
	 * @return
	 */
	public String embeddingProperty() {
		return "embedding";
	}

	/**
	 * 
	 * @return
	 */
	public int embeddingDimension() {
		return 1536;
	}

	/**
	 * 
	 * @return
	 */
	public Neo4jDistanceType distanceType() {
		return Neo4jDistanceType.COSINE;
	}

	/*
	 * 
	 */

	@Test
	public void testCall1() throws Exception {

		ChatResponse response = this.chatClient.prompt()
			.advisors(QuestionAnswerAdvisor.builder(this.vectorStore).build())
			.user("Please describe ZFS").call().chatResponse();
		if( response != null ) {
			String answer = response.getResult().getOutput().getText();
			System.out.println(answer);
		}
	}

}
