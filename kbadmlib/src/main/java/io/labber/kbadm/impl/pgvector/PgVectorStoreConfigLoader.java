package io.labber.kbadm.impl.pgvector;

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

import io.labber.kbadm.config.Config;
import io.labber.kbadm.config.DatasourceConfig;
import io.labber.kbadm.config.EndpointConfig;
import io.labber.kbadm.config.KnowledgebaseConfig;
import io.labber.kbadm.config.TextsplitterConfig;
import io.labber.kbadm.config.VectorstoreConfig;

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
	JdbcTemplate jdbcTemplate =  null;

	OllamaApi ollamaApi = null;
	ChatClient chatClient = null;
	VectorStore vectorStore = null;

	EmbeddingModel embeddingModel = null;

	/**
	 * 
	 * @param kb
	 * @param config
	 */
	public PgVectorStoreConfigLoader(
		String kb, 
		Config config
	) {

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
	) {
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
	) {

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
			ollamaApi()
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
			ollamaApi()
		).defaultOptions(
			OllamaEmbeddingOptions.builder().model(
				// endpoint.getConfig().get("embeddingModel").toString()
				knowledgebase.getConfig().get("embeddingModel").toString()
			).build()
		).build();

		// this.createSchema(
		this.initSchema();

		this.vectorStore = PgVectorStore.builder(
			this.jdbcTemplate(), 
			this.embeddingModel()
		).indexType(
			this.createIndexMethod()
		).distanceType(
			this.getDistanceType()
		).dimensions(
			this.embeddingDimensions()
		).schemaName(
			this.getSchemaName()
		).vectorTableName(
			this.getVectorTableName()
		).initializeSchema(
			false
		).removeExistingVectorStoreTable(
			false
		).build();

	}

	/**
	 * 
	 */
	public void close() {

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
	public String getSchemaName() {
		return this.schemaName;
	}

	/**
	 * 
	 * @return
	 */
	public String getVectorTableName() {
		return this.vectorTableName;
	}

	/**
	 * 
	 * @return
	 */
	public String getFullyQualifiedTableName() {
		return this.getSchemaName() + "." + this.getVectorTableName();
	}

	/**
	 * 
	 * @return
	 */
	public String getColumnTypeName() {
		return this.columnTypeName;
	}

	/**
	 * 
	 * @return
	 */
	public int embeddingDimensions() {
		return this.dimensions;
	}

	/**
	 * 
	 * @return
	 */
	public String getVectorIndexName() {
		String vectorTableName = this.getVectorTableName();
		String vectorIndexName = vectorTableName.equals(PgVectorStore.DEFAULT_TABLE_NAME) ? PgVectorStore.DEFAULT_VECTOR_INDEX_NAME
			: vectorTableName + "_index";
		return vectorIndexName;
	}

	/**
	 * 
	 * @return
	 */
	public PgIndexType createIndexMethod() {
		return this.indexType;
	}

	/**
	 * 
	 * @return
	 */
	public PgDistanceType getDistanceType() {
		return this.distanceType;
	}

	/**
	 * 
	 */
	public void initSchema(
		
	) {

		this.jdbcTemplate().execute(String.format("CREATE SCHEMA IF NOT EXISTS %s", this.getSchemaName()));

		// Remove existing VectorStoreTable
		// if (this.removeExistingVectorStoreTable) {
		// 	this.jdbcTemplate().execute(String.format("DROP TABLE IF EXISTS %s", this.getFullyQualifiedTableName(schema, table)));
		// }

		this.jdbcTemplate().execute(String.format("""
				CREATE TABLE IF NOT EXISTS %s (
					id %s PRIMARY KEY,
					content text,
					metadata json,
					embedding vector(%d)
				)
				""", this.getFullyQualifiedTableName(), this.getColumnTypeName(), this.embeddingDimensions()));

		// if (this.createIndexMethod != PgIndexType.NONE) {
			this.jdbcTemplate().execute(String.format("""
					CREATE INDEX IF NOT EXISTS %s ON %s USING %s (embedding %s)
					""", this.getVectorIndexName(), this.getFullyQualifiedTableName(), this.createIndexMethod(),
					this.getDistanceType().index));
		// }

	}

	/**
	 * 
	 * @return
	 */
	public DataSource dataSource() {
		return this.dataSource;
	}

	/**
	 * 
	 * @return
	 */
	public JdbcTemplate jdbcTemplate() {
		return this.jdbcTemplate;
	}

	/**
	 * 
	 * @return
	 */
	public OllamaApi ollamaApi() {
		return this.ollamaApi;
	}

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
	public EmbeddingModel embeddingModel() {
		return this.embeddingModel;
	}

	/**
	 * 
	 * @return
	 */
	public VectorStore vectorStore() {
		return this.vectorStore;
	}

}
