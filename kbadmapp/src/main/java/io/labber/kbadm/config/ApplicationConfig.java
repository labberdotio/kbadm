
//
// Copyright (c) 2019, 2023, 2024, 2025, John Grundback
// All rights reserved.
//

package io.labber.kbadm.config;

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
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EntityScan(basePackages = { "io.labber", "io.labber.app", "io.labber.kbadm" })
@ComponentScan(basePackages = { "io.labber", "io.labber.app", "io.labber.kbadm" })
public class ApplicationConfig {

	@Bean
	public DataSource dataSource() {

		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.postgresql.Driver");
		dataSourceBuilder.url("jdbc:postgresql://10.88.88.197:5432/pgvector_db");
		dataSourceBuilder.username("postgres");
		dataSourceBuilder.password("postgres");

		return dataSourceBuilder.build();
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(this.dataSource());
	}

	@Bean
	public OllamaApi customOllamaApi() {
		return OllamaApi.builder().baseUrl("http://10.88.88.180:11434").build();
	}

	@Bean
	public ChatClient customOllamaChatClient() {
		OllamaChatModel customChatModel = OllamaChatModel.builder().ollamaApi(
			customOllamaApi()
		).defaultOptions(
			OllamaChatOptions.builder().model(
				"gpt-oss"
			).build()
		).build();
		return ChatClient.builder(
			customChatModel
		).build();
	}

	@Bean
	public EmbeddingModel embeddingModel() {
		return OllamaEmbeddingModel.builder().ollamaApi(
			customOllamaApi()
		).defaultOptions(
			OllamaEmbeddingOptions.builder().model(
				"nomic-embed-text"
			).build()
		).build();
	}

	@Bean
	public VectorStore vectorStore() {
		// The PgVectorStore uses a default table name 'vector_store'.
		// You can use a builder to customize schema, table name, distance type, etc.
		return PgVectorStore.builder(
			this.jdbcTemplate(), 
			this.embeddingModel()
		).indexType(
			PgIndexType.HNSW
		).distanceType(
			PgDistanceType.COSINE_DISTANCE
		).dimensions(
			1024
		).schemaName(
			PgVectorStore.DEFAULT_SCHEMA_NAME
		).vectorTableName(
			"vector_store"
		).initializeSchema(
			false // true
		).removeExistingVectorStoreTable(
			false // false
		).build(); 
	}

}
