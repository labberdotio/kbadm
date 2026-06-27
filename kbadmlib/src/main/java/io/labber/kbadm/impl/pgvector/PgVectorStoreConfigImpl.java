
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

	protected int dimensions = 768; // 1024;
	protected String schemaName = PgVectorStore.DEFAULT_SCHEMA_NAME;
	protected String vectorTableName = PgVectorStore.DEFAULT_TABLE_NAME; // "vector_store";

	protected boolean initializeSchema = true;
	protected boolean recreateSchema = false;

	/**
	 * 
	 * @param properties
	 */
	public PgVectorStoreConfigImpl(Map<String, Object> properties) {
		super(properties);
		this.indexType = this.readIndexType(properties, PgIndexType.HNSW);
		this.distanceType = this.readDistanceType(properties, PgDistanceType.COSINE_DISTANCE);
		this.dimensions = this.readInteger("dimensions", properties, 768); // 1024);
		this.schemaName = this.readString("schemaName", properties, PgVectorStore.DEFAULT_SCHEMA_NAME);
		this.vectorTableName = this.readString("vectorTableName", properties, PgVectorStore.DEFAULT_TABLE_NAME);
		this.initializeSchema = this.readBoolean("initializeSchema", properties, true);
		this.recreateSchema = this.readBoolean("recreateSchema", properties, false);
	}

	/**
	 * 
	 * @param properties
	 */
	public PgVectorStoreConfigImpl(Properties properties) {
		super(properties);
		this.indexType = this.readIndexType(this.readProperties(properties), PgIndexType.HNSW);
		this.distanceType = this.readDistanceType(this.readProperties(properties), PgDistanceType.COSINE_DISTANCE);
		this.dimensions = this.readInteger("dimensions", this.readProperties(properties), 768); // 1024);
		this.schemaName = this.readString("schemaName", this.readProperties(properties), PgVectorStore.DEFAULT_SCHEMA_NAME);
		this.vectorTableName = this.readString("vectorTableName", this.readProperties(properties), PgVectorStore.DEFAULT_TABLE_NAME);
		this.initializeSchema = this.readBoolean("initializeSchema", this.readProperties(properties), true);
		this.recreateSchema = this.readBoolean("recreateSchema", this.readProperties(properties), false);
	}

	/**
	 * 
	 */
	public PgVectorStoreConfigImpl() {
		super();
		this.indexType = PgIndexType.HNSW;
		this.distanceType = PgDistanceType.COSINE_DISTANCE;
		this.dimensions = 768; // 1024;
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

	/**
	 * 
	 * @param properties
	 * @param def
	 * @return
	 */
	protected PgIndexType readIndexType(
		Map<String, Object> properties, 
		PgIndexType def
	) {
		// TODO
		return PgIndexType.HNSW;
	}

	/**
	 * 
	 * @param properties
	 * @param def
	 * @return
	 */
	protected PgDistanceType readDistanceType(
		Map<String, Object> properties, 
		PgDistanceType def
	) {
		// TODO
		return PgDistanceType.COSINE_DISTANCE;
	}

}
