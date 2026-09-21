
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.pgvector;

import io.labber.kbadm.store.Store;

/**
 * 
 * @author john
 *
 */
public class PgVectorStoreImpl extends Store {

	private PgVectorStoreConfigImpl config;
	private PgVectorStoreMetadataImpl metadata;

	/**
	 * 
	 * @param config
	 * @param metadata
	 */
	public PgVectorStoreImpl(
		PgVectorStoreConfigImpl config, 
		PgVectorStoreMetadataImpl metadata
	) {
		super(config, metadata);
		this.config = config;
		this.metadata = metadata;
	}

	@Override
	public PgVectorStoreConfigImpl getConfig() {
		return this.config;
	}

	@Override
	public PgVectorStoreMetadataImpl getMetadata() {
		return this.metadata;
	}

}
