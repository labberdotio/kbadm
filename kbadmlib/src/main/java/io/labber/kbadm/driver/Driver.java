
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.driver;

import io.labber.kbadm.store.IStore;
import io.labber.kbadm.store.IStoreConfig;
import io.labber.kbadm.store.IStoreMetadata;
import io.labber.kbadm.store.Store;

/**
 * 
 * @author john
 *
 */
public abstract class Driver implements IDriver {

	private IStore store;
	private IStoreConfig config;
	private IStoreMetadata metadata;

	/**
	 * 
	 * @param store
	 */
	public Driver(
		IStore store
	) {
		this.store = store;
		this.config = store.getConfig();
		this.metadata = store.getMetadata();
	}

	/**
	 * 
	 * @param store
	 */
	public Driver(
		Store store
	) {
		this.store = store;
		this.config = store.getConfig();
		this.metadata = store.getMetadata();
	}

	@Override
	public IStore getStore() {
		return this.store;
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
