
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.pgvector;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
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
			this.config.getIndexType() // PgIndexType.HNSW
		).distanceType(
			this.config.getDistanceType() // PgDistanceType.COSINE_DISTANCE
		).dimensions(
			this.config.getDimensions() // 1024
		).schemaName(
			this.config.getSchemaName() // PgVectorStore.DEFAULT_SCHEMA_NAME
		).vectorTableName(
			this.config.getVectorTableName() // PgVectorStore.DEFAULT_TABLE_NAME // "vector_store"
		).initializeSchema(
			this.config.isInitializeSchema() // true
		).removeExistingVectorStoreTable(
			this.config.isRemoveExistingVectorStoreTable() // false
		).build(); 
	}

}
