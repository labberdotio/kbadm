
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.pgvector;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType;
import org.springframework.jdbc.core.JdbcTemplate;

import io.labber.kbadm.driver.Driver;

/**
 * 
 * @author john
 *
 */
public class PgVectorStoreDriverImpl extends Driver {

	private PgVectorStoreImpl store;
	private PgVectorStoreConfigImpl config;
	private PgVectorStoreMetadataImpl metadata;

	/**
	 * 
	 * @param store
	 */
	public PgVectorStoreDriverImpl(
		PgVectorStoreImpl store
	) {
		super(store);
		this.store = store;
		this.config = store.getConfig();
		this.metadata = store.getMetadata();
	}

	/**
	 * 
	 * @param jdbcTemplate
	 * @param embeddingModel
	 * @return
	 */
	protected VectorStore vectorStore(
		JdbcTemplate jdbcTemplate, 
		EmbeddingModel embeddingModel
	) {
		return PgVectorStore.builder(
			jdbcTemplate, 
			embeddingModel
		).indexType(
			PgIndexType.HNSW
		).distanceType(
			PgDistanceType.COSINE_DISTANCE
		).dimensions(
			1024
		).schemaName(
			PgVectorStore.DEFAULT_SCHEMA_NAME
		).vectorTableName(
			PgVectorStore.DEFAULT_TABLE_NAME // "vector_store"
		).initializeSchema(
			true
		).removeExistingVectorStoreTable(
			false
		).build(); 
	}

}
