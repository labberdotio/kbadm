
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.driver;

import io.labber.kbadm.store.IStore;
import io.labber.kbadm.store.IStoreConfig;
import io.labber.kbadm.store.IStoreMetadata;

/**
 * 
 * @author john
 *
 */
public interface IDriver {

	/**
	 * 
	 * @return
	 */
	public IStore getStore();

	/**
	 * 
	 * @return
	 */
	public IStoreConfig getConfig();

	/**
	 * 
	 * @return
	 */
	public IStoreMetadata getMetadata();

}
