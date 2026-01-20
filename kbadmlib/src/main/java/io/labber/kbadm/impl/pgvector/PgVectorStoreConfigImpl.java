
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.pgvector;

import java.util.Map;
import java.util.Properties;

import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType;

import io.labber.kbadm.store.StoreConfig;

/**
 * 
 * @author john
 *
 */
public class PgVectorStoreConfigImpl extends StoreConfig {

	protected PgIndexType indexType = PgIndexType.HNSW;
	protected PgDistanceType distanceType = PgDistanceType.COSINE_DISTANCE;

	protected int dimensions = 1024;
	protected String schemaName = PgVectorStore.DEFAULT_SCHEMA_NAME;
	protected String vectorTableName = PgVectorStore.DEFAULT_TABLE_NAME; // "vector_store";

	protected boolean initializeSchema = true;
	protected boolean recreateSchema = false;

	/**
	 * 
	 * @param properties
	 */
	public PgVectorStoreConfigImpl(Map<String, Object> properties) {
		this.indexType = PgIndexType.HNSW;
		this.distanceType = PgDistanceType.COSINE_DISTANCE;
		this.dimensions = 1024;
		this.schemaName = PgVectorStore.DEFAULT_SCHEMA_NAME;
		this.vectorTableName = PgVectorStore.DEFAULT_TABLE_NAME;
		this.initializeSchema = true;
		this.recreateSchema = false;
	}

	/**
	 * 
	 * @param properties
	 */
	public PgVectorStoreConfigImpl(Properties properties) {
		this.indexType = PgIndexType.HNSW;
		this.distanceType = PgDistanceType.COSINE_DISTANCE;
		this.dimensions = 1024;
		this.schemaName = PgVectorStore.DEFAULT_SCHEMA_NAME;
		this.vectorTableName = PgVectorStore.DEFAULT_TABLE_NAME;
		this.initializeSchema = true;
		this.recreateSchema = false;
	}

	/**
	 * 
	 */
	public PgVectorStoreConfigImpl() {
		this.indexType = PgIndexType.HNSW;
		this.distanceType = PgDistanceType.COSINE_DISTANCE;
		this.dimensions = 1024;
		this.schemaName = PgVectorStore.DEFAULT_SCHEMA_NAME;
		this.vectorTableName = PgVectorStore.DEFAULT_TABLE_NAME;
		this.initializeSchema = true;
		this.recreateSchema = false;
	}

	/**
	 * 
	 * @return
	 */
	public PgIndexType getIndexType() {
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
	 * @return
	 */
	public int getDimensions() {
		return this.dimensions;
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
	public boolean isInitializeSchema() {
		return this.initializeSchema;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isRemoveExistingVectorStoreTable() {
		return this.recreateSchema;
	}

}
