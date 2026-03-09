
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.neo4j;

import io.labber.kbadm.store.Store;

/**
 * 
 * @author john
 *
 */
public class Neo4jStoreImpl extends Store {

	private Neo4jStoreConfigImpl config;
	private Neo4jStoreMetadataImpl metadata;

	/**
	 * 
	 * @param config
	 * @param metadata
	 */
	public Neo4jStoreImpl(
		Neo4jStoreConfigImpl config, 
		Neo4jStoreMetadataImpl metadata
	) {
		super(config, metadata);
		this.config = config;
		this.metadata = metadata;
	}

	@Override
	public Neo4jStoreConfigImpl getConfig() {
		return this.config;
	}

	@Override
	public Neo4jStoreMetadataImpl getMetadata() {
		return this.metadata;
	}

}
