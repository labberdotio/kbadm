
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.store;

import java.util.UUID;

/**
 * 
 * @author john
 *
 */
public interface IStoreConfig {

	/**
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * 
	 * @return
	 */
	public UUID getUuid();

	/**
	 * 
	 * @return
	 */
	public String getName();

}
