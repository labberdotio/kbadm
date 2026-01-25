
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.store;

/**
 * 
 * @author john
 *
 */
public class Store implements IStore {

	private IStoreConfig config;
	private IStoreMetadata metadata;

	/**
	 * 
	 * @param config
	 * @param metadata
	 */
	public Store(
		IStoreConfig config, 
		IStoreMetadata metadata
	) {
		this.config = config;
		this.metadata = metadata;
	}

	@Override
	public IStoreConfig getConfig() {
		return this.config;
	}

	@Override
	public IStoreMetadata getMetadata() {
		return this.metadata;
	}

}
