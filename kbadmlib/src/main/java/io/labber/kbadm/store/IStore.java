
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
public interface IStore {

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
