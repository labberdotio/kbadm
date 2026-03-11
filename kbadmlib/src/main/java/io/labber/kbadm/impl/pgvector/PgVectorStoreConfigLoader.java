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

	String kb;
	EndpointConfig endpoint;
	DatasourceConfig datasource;
	TextsplitterConfig textsplitter;
	VectorstoreConfig vectorstore;
	KnowledgebaseConfig knowledgebase;

	DataSource dataSource = null;
	JdbcTemplate jdbcTemplate =  null;

	/**
	 * 
	 * @param kb
	 * @param config
	 */
	public PgVectorStoreConfigLoader(
		String kb, 
		Config config
	) {
		this.kb = kb;

		for( KnowledgebaseConfig knowledgebase : config.getKnowledgebases() ) {
			if( knowledgebase != null ) {
				if( knowledgebase.getName() != null ) {
					if( knowledgebase.getName().equalsIgnoreCase(this.kb) ) {

						this.knowledgebase = knowledgebase;

						if( (this.knowledgebase != null) && 
							(this.knowledgebase.getEndpoint() != null) ) {
							for( EndpointConfig endpoint : config.getEndpoints() ) {
								if( endpoint != null ) {
									if( endpoint.getName() != null ) {
										if( endpoint.getName().equalsIgnoreCase(
											this.knowledgebase.getEndpoint()
										) ) {
											this.endpoint = endpoint;
										}
									}
								}
							}
						}

						if( (this.knowledgebase != null) && 
							(this.knowledgebase.getVectorstore() != null) ) {
							for( VectorstoreConfig vectorstore : config.getVectorstores() ) {
								if( vectorstore != null ) {
									if( vectorstore.getName() != null ) {
										if( vectorstore.getName().equalsIgnoreCase(
											this.knowledgebase.getVectorstore()
										) ) {
											this.vectorstore = vectorstore;
										}
									}
								}
							}
						}

						if( (this.vectorstore != null) && 
							(this.vectorstore.getDatasource() != null) ) {
							for( DatasourceConfig datasource : config.getDatasources() ) {
								if( datasource != null ) {
									if( datasource.getName() != null ) {
										if( datasource.getName().equalsIgnoreCase(
											this.vectorstore.getDatasource()
										) ) {
											this.datasource = datasource;
										}
									}
								}
							}
						}

						if( (this.knowledgebase != null) && 
							(this.knowledgebase.getTextsplitter() != null) ) {
							for( TextsplitterConfig textsplitter : config.getTextsplitters() ) {
								if( textsplitter != null ) {
									if( textsplitter.getName() != null ) {
										if( textsplitter.getName().equalsIgnoreCase(
											this.knowledgebase.getTextsplitter()
										) ) {
											this.textsplitter = textsplitter;
										}
									}
								}
							}
						}

					}
				}
			}
		}

		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(this.datasource.getConfig().get("driver").toString());
		dataSourceBuilder.url(this.datasource.getConfig().get("url").toString());
		dataSourceBuilder.username(this.datasource.getConfig().get("username").toString());
		dataSourceBuilder.password(this.datasource.getConfig().get("password").toString());

		this.dataSource = dataSourceBuilder.build();
		this.jdbcTemplate =  new JdbcTemplate(this.dataSource);

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

		this.kb = kb;
		this.endpoint = endpoint;
		this.datasource = datasource;
		this.textsplitter = textsplitter;
		this.vectorstore = vectorstore;
		this.knowledgebase = knowledgebase;

		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(this.datasource.getConfig().get("driver").toString());
		dataSourceBuilder.url(this.datasource.getConfig().get("url").toString());
		dataSourceBuilder.username(this.datasource.getConfig().get("username").toString());
		dataSourceBuilder.password(this.datasource.getConfig().get("password").toString());

		this.dataSource = dataSourceBuilder.build();
		this.jdbcTemplate =  new JdbcTemplate(this.dataSource);

	}

	/**
	 * 
	 * @return
	 */
	public String getSchemaName() {
		return this.vectorstore.getConfig().get("schemaName").toString();
	}

	/**
	 * 
	 * @return
	 */
	public String getVectorTableName() {
		return this.vectorstore.getConfig().get("vectorTableName").toString();
	}

	/**
	 * 
	 * @return
	 */
	public String getFullyQualifiedTableName() {
		String schemaName = this.vectorstore.getConfig().get("schemaName").toString();
		String vectorTableName = this.vectorstore.getConfig().get("vectorTableName").toString();
		return schemaName + "." + vectorTableName;
	}

	/**
	 * 
	 * @return
	 */
	public String getColumnTypeName() {
		// TODO
		return "uuid";
	}

	/**
	 * 
	 * @return
	 */
	public int embeddingDimensions() {
		return Integer.parseInt(this.vectorstore.getConfig().get("dimensions").toString());
	}

	/**
	 * 
	 * @return
	 */
	public String getVectorIndexName() {
		String vectorTableName = this.vectorstore.getConfig().get("vectorTableName").toString();
		String vectorIndexName = vectorTableName.equals(PgVectorStore.DEFAULT_TABLE_NAME) ? PgVectorStore.DEFAULT_VECTOR_INDEX_NAME
			: vectorTableName + "_index";
		return vectorIndexName;
	}

	/**
	 * 
	 * @return
	 */
	public PgIndexType createIndexMethod() {
		// TODO
		return PgIndexType.HNSW;
	}

	/**
	 * 
	 * @return
	 */
	public PgDistanceType getDistanceType() {
		// TODO
		return PgDistanceType.COSINE_DISTANCE;
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

//		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//		dataSourceBuilder.driverClassName(this.datasource.getConfig().get("driver").toString());
//		dataSourceBuilder.url(this.datasource.getConfig().get("url").toString());
//		dataSourceBuilder.username(this.datasource.getConfig().get("username").toString());
//		dataSourceBuilder.password(this.datasource.getConfig().get("password").toString());
//
//		return dataSourceBuilder.build();
		return this.dataSource;
	}

	/**
	 * 
	 * @return
	 */
	public JdbcTemplate jdbcTemplate() {
//		return new JdbcTemplate(this.dataSource());
		return this.jdbcTemplate;
	}

	/**
	 * 
	 * @return
	 */
	public OllamaApi ollamaApi() {
		return OllamaApi.builder().baseUrl(
			this.endpoint.getConfig().get("url").toString()
		).build();
	}

	/**
	 * 
	 * @return
	 */
	public ChatClient chatClient() {
		OllamaChatModel chatModel = OllamaChatModel.builder().ollamaApi(
			ollamaApi()
		).defaultOptions(
			OllamaChatOptions.builder().model(
				// this.endpoint.getConfig().get("model").toString()
				this.knowledgebase.getConfig().get("model").toString()
			).build()
		).build();
		return ChatClient.builder(
			chatModel
		).build();
	}

	/**
	 * 
	 * @return
	 */
	public EmbeddingModel embeddingModel() {
		return OllamaEmbeddingModel.builder().ollamaApi(
			ollamaApi()
		).defaultOptions(
			OllamaEmbeddingOptions.builder().model(
				// this.endpoint.getConfig().get("embeddingModel").toString()
				this.knowledgebase.getConfig().get("embeddingModel").toString()
			).build()
		).build();
	}

	/**
	 * 
	 * @return
	 */
	public VectorStore vectorStore() {

		// this.createSchema(
		this.initSchema();

		return PgVectorStore.builder(
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

}
