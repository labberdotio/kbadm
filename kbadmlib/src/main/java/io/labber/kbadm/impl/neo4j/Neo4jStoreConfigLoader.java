
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.neo4j;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.ai.chat.client.ChatClient;
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

import io.labber.kbadm.KBException;
import io.labber.kbadm.config.Config;
import io.labber.kbadm.config.DatasourceConfig;
import io.labber.kbadm.config.EndpointConfig;
import io.labber.kbadm.config.KnowledgebaseConfig;
import io.labber.kbadm.config.TextsplitterConfig;
import io.labber.kbadm.config.VectorstoreConfig;

/**
 * 
 * @author john
 *
 */
public class Neo4jStoreConfigLoader {

	String databaseName = null;

	// String indexType = null;
	String indexName = null;
	String labelName = null;
	String embeddingProperty = null;
	Neo4jDistanceType distanceType = Neo4jDistanceType.COSINE;

	int dimensions;

	// String model;
	// String embeddingModel;

	Driver neo4jDriver = null;

	OllamaApi ollamaApi = null;
	ChatClient chatClient = null;
	VectorStore vectorStore = null;

	EmbeddingModel embeddingModel = null;

	Neo4jStoreDriverImpl driver = null;

	/**
	 * 
	 * @param kb
	 * @param config
	 */
	public Neo4jStoreConfigLoader(
		String kb, 
		Config config
	) throws KBException {

		KnowledgebaseConfig knowledgebase = config.getKnowledgebaseConfig(kb);
		EndpointConfig endpoint = config.getEndpointConfig(kb, knowledgebase);
		VectorstoreConfig vectorstore = config.getVectorstoreConfig(kb, knowledgebase);
		DatasourceConfig datasource = config.getDatasourceConfig(kb, vectorstore);
		TextsplitterConfig textsplitter = config.getTextsplitterConfig(kb, knowledgebase);

		this.init(
			kb, 
			endpoint, 
			datasource, 
			textsplitter, 
			vectorstore, 
			knowledgebase
		);

	}

	/**
	 * 
	 * @param kb
	 * @param endpoint
	 * @param datasource
	 * @param textsplitter
	 * @param vectorstore
	 * @param knowledgebase
	 */
	public Neo4jStoreConfigLoader(
		String kb, 
		EndpointConfig endpoint, 
		DatasourceConfig datasource, 
		TextsplitterConfig textsplitter, 
		VectorstoreConfig vectorstore, 
		KnowledgebaseConfig knowledgebase
	) throws KBException {
		this.init(
			kb, 
			endpoint, 
			datasource, 
			textsplitter, 
			vectorstore, 
			knowledgebase
		);
	}

	/**
	 * 
	 * @param kb
	 * @param endpoint
	 * @param datasource
	 * @param textsplitter
	 * @param vectorstore
	 * @param knowledgebase
	 */
	public void init(
		String kb, 
		EndpointConfig endpoint, 
		DatasourceConfig datasource, 
		TextsplitterConfig textsplitter, 
		VectorstoreConfig vectorstore, 
		KnowledgebaseConfig knowledgebase
	) throws KBException {

		this.databaseName = "neo4j";

		// this.indexType = null;
		this.indexName = "custom-index";
		this.labelName = "Document";
		this.embeddingProperty = "embedding";
		this.distanceType = Neo4jDistanceType.COSINE;

		this.dimensions = Integer.parseInt(vectorstore.getConfig().get("dimensions").toString());

		// this.neo4jDriver = GraphDatabase.driver("neo4j://10.88.88.194:7687",
		this.neo4jDriver = GraphDatabase.driver("neo4j://10.88.88.195:7687",
            AuthTokens.basic("neo4j", "neo4j"));

		this.ollamaApi = OllamaApi.builder().baseUrl(
			endpoint.getConfig().get("url").toString()
		).build();

		OllamaChatModel chatModel = OllamaChatModel.builder().ollamaApi(
			this.ollamaApi
		// ).defaultOptions(
		).options(
			OllamaChatOptions.builder().model(
				// endpoint.getConfig().get("model").toString()
				knowledgebase.getConfig().get("model").toString()
			).build()
		).build();

		this.chatClient = ChatClient.builder(
			chatModel
		).build();

		this.embeddingModel = OllamaEmbeddingModel.builder().ollamaApi(
			this.ollamaApi
		// ).defaultOptions(
		).options(
			OllamaEmbeddingOptions.builder().model(
				// endpoint.getConfig().get("embeddingModel").toString()
				knowledgebase.getConfig().get("embeddingModel").toString()
			).build()
		).build();

		// // this.createSchema(
		// this.initSchema();

		this.vectorStore = Neo4jVectorStore.builder(
			this.neo4jDriver, 
			this.embeddingModel
		)
		// .databaseName(
		// 	this.databaseName // this.config.getDatabaseName()
		// )
		.distanceType(
			this.distanceType // this.config.getDistanceType()
		).embeddingDimension(
			this.dimensions // this.config.getDimensions()
		).label(
			this.labelName // this.config.getLabelName()
		).embeddingProperty(
			this.embeddingProperty // this.config.getEmbeddingProperty()
		).indexName(
			this.indexName // this.config.getIndexName()
		).initializeSchema(
			false // this.config.isInitializeSchema()
		).batchingStrategy(
			new TokenCountBatchingStrategy()
		).build();

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("name", "");

		properties.put("dimensions", 768);
		properties.put("schemaName", "kbtwo");
		properties.put("vectorTableName", "vector_store");
		properties.put("initializeSchema", false);
		properties.put("recreateSchema", false);

		this.driver = new Neo4jStoreDriverImpl(
			neo4jDriver, 
			chatClient, 
			vectorStore, 
			new Neo4jStoreImpl(
				new Neo4jStoreConfigImpl(properties), 
				new Neo4jStoreMetadataImpl()
			)
		);

	}

	/**
	 * 
	 */
	public void close() throws KBException {

		// this.neo4jDriver.close();
		this.neo4jDriver = null;

		this.ollamaApi = null;
		this.chatClient = null;
		this.embeddingModel = null;
		this.vectorStore = null;

	}

	/**
	 * 
	 * @return
	 */
	public Neo4jStoreDriverImpl getDriver() {
		return this.driver;
	}

	/**
	 * 
	 * @return
	 */
	public Neo4jStoreDriverImpl driver() {
		return this.driver;
	}

}
