
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.pgvector;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

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
public class PgVectorStoreConfigLoader {

	String schemaName;
	String vectorTableName;
	String columnTypeName;

	int dimensions;
	PgIndexType indexType = PgIndexType.HNSW;
	PgDistanceType distanceType = PgDistanceType.COSINE_DISTANCE;

	// String model;
	// String embeddingModel;

	DataSource dataSource = null;
	JdbcTemplate jdbcTemplate = null;

	OllamaApi ollamaApi = null;
	ChatClient chatClient = null;
	VectorStore vectorStore = null;

	EmbeddingModel embeddingModel = null;

	PgVectorStoreDriverImpl driver = null;

	/**
	 * 
	 * @param kb
	 * @param config
	 */
	public PgVectorStoreConfigLoader(
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
	public PgVectorStoreConfigLoader(
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

		this.schemaName = vectorstore.getConfig().get("schemaName").toString();
		this.vectorTableName = vectorstore.getConfig().get("vectorTableName").toString();
		this.columnTypeName = "uuid";

		this.dimensions = Integer.parseInt(vectorstore.getConfig().get("dimensions").toString());

		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(datasource.getConfig().get("driver").toString());
		dataSourceBuilder.url(datasource.getConfig().get("url").toString());
		dataSourceBuilder.username(datasource.getConfig().get("username").toString());
		dataSourceBuilder.password(datasource.getConfig().get("password").toString());

		// this.model = endpoint.getConfig().get("model").toString();
		// this.model = knowledgebase.getConfig().get("model").toString();

		// this.embeddingModel = endpoint.getConfig().get("embeddingModel").toString();
		// this.embeddingModel = knowledgebase.getConfig().get("embeddingModel").toString();

		this.dataSource = dataSourceBuilder.build();
		this.jdbcTemplate =  new JdbcTemplate(this.dataSource);

		this.ollamaApi = OllamaApi.builder().baseUrl(
			endpoint.getConfig().get("url").toString()
		).build();

		OllamaChatModel chatModel = OllamaChatModel.builder().ollamaApi(
			this.ollamaApi
		).defaultOptions(
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
		).defaultOptions(
			OllamaEmbeddingOptions.builder().model(
				// endpoint.getConfig().get("embeddingModel").toString()
				knowledgebase.getConfig().get("embeddingModel").toString()
			).build()
		).build();

		// // this.createSchema(
		// this.initSchema();

		this.vectorStore = PgVectorStore.builder(
			this.jdbcTemplate, 
			this.embeddingModel
		).indexType(
			this.indexType
		).distanceType(
			this.distanceType
		).dimensions(
			this.dimensions
		).schemaName(
			this.schemaName
		).vectorTableName(
			this.vectorTableName
		).initializeSchema(
			false
		).removeExistingVectorStoreTable(
			false
		).build();

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("name", "");

		properties.put("dimensions", 768);
		properties.put("schemaName", "kbtwo");
		properties.put("vectorTableName", "vector_store");
		properties.put("initializeSchema", false);
		properties.put("recreateSchema", false);

		this.driver = new PgVectorStoreDriverImpl(
			jdbcTemplate, 
			chatClient, 
			vectorStore, 
			new PgVectorStoreImpl(
				new PgVectorStoreConfigImpl(properties), 
				new PgVectorStoreMetadataImpl()
			)
		);

	}

	/**
	 * 
	 */
	public void close() throws KBException {

		// this.jdbcTemplate.close();
		this.jdbcTemplate = null;

		// this.dataSource.close();
		this.dataSource = null;

		this.ollamaApi = null;
		this.chatClient = null;
		this.embeddingModel = null;
		this.vectorStore = null;

	}

	/**
	 * 
	 * @return
	 */
	public PgVectorStoreDriverImpl getDriver() {
		return this.driver;
	}

	/**
	 * 
	 * @return
	 */
	public PgVectorStoreDriverImpl driver() {
		return this.driver;
	}

}
