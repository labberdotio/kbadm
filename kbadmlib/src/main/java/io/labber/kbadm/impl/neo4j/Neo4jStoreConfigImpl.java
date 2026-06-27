
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.neo4j;

import java.util.Map;
import java.util.Properties;

import org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore.Neo4jDistanceType;

import io.labber.kbadm.store.StoreConfig;

/**
 * 
 * @author john
 *
 */
public class Neo4jStoreConfigImpl extends StoreConfig {

	protected String databaseName = "neo4j";

	// protected String indexType = null;
	protected String indexName = "custom-index";
	protected String labelName = "Document";
	protected String embeddingProperty = "embedding";
	protected Neo4jDistanceType distanceType = Neo4jDistanceType.COSINE;

	protected int dimensions = 1536; // 1024;

	protected boolean initializeSchema = true;
	protected boolean recreateSchema = false;

	/**
	 * 
	 * @param properties
	 */
	public Neo4jStoreConfigImpl(Map<String, Object> properties) {
		super(properties);
		this.databaseName = this.readString("databaseName", properties, null);
		// this.indexType = this.readString("indexType", properties, null);
		this.indexName = this.readString("indexName", properties, "custom-index");
		this.labelName = this.readString("labelName", properties, "Document");
		this.embeddingProperty = this.readString("embeddingProperty", properties, "embedding");
		this.distanceType = this.readDistanceType(properties, Neo4jDistanceType.COSINE);
		this.dimensions = this.readInteger("dimensions", properties, 1536);
		this.initializeSchema = this.readBoolean("initializeSchema", properties, true);
		this.recreateSchema = this.readBoolean("recreateSchema", properties, false);
	}

	/**
	 * 
	 * @param properties
	 */
	public Neo4jStoreConfigImpl(Properties properties) {
		super(properties);
		this.databaseName = this.readString("databaseName", this.readProperties(properties), null);
		// this.indexType = this.readString("indexType", this.readProperties(properties), null);
		this.indexName = this.readString("indexName", this.readProperties(properties), "custom-index");
		this.labelName = this.readString("labelName", this.readProperties(properties), "Document");
		this.embeddingProperty = this.readString("embeddingProperty", this.readProperties(properties), "embedding");
		this.distanceType = this.readDistanceType(this.readProperties(properties), Neo4jDistanceType.COSINE);
		this.dimensions = this.readInteger("dimensions", this.readProperties(properties), 1536);
		this.initializeSchema = this.readBoolean("initializeSchema", this.readProperties(properties), true);
		this.recreateSchema = this.readBoolean("recreateSchema", this.readProperties(properties), false);
	}

	/**
	 * 
	 */
	public Neo4jStoreConfigImpl() {
		super();
		this.databaseName = null;
		// this.indexType = null;
		this.indexName = "custom-index";
		this.labelName = "Document";
		this.embeddingProperty = "embedding";
		this.distanceType = Neo4jDistanceType.COSINE;
		this.dimensions = 1536;
		this.initializeSchema = true;
		this.recreateSchema = false;
	}

	/**
	 * 
	 * @return
	 */
	public String getDatabaseName() {
		return this.databaseName;
	}

	/**
	 * 
	 * @return
	 */
	// public String getIndexType() {
	// 	return this.indexType;
	// }

	/**
	 * 
	 * @return
	 */
	public String getIndexName() {
		return this.indexName;
	}

	/**
	 * 
	 * @return
	 */
	public String getLabelName() {
		return this.labelName;
	}

	/**
	 * 
	 * @return
	 */
	public String getEmbeddingProperty() {
		return this.embeddingProperty;
	}

	/**
	 * 
	 * @return
	 */
	public Neo4jDistanceType getDistanceType() {
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
	protected Neo4jDistanceType readDistanceType(
		Map<String, Object> properties, 
		Neo4jDistanceType def
	) {
		// TODO
		return Neo4jDistanceType.COSINE;
	}

}
