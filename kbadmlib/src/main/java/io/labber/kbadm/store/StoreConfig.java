
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 
 * @author john
 *
 */
public class StoreConfig implements IStoreConfig {

	/**
	 * 
	 * @param properties
	 */
	public StoreConfig(Map<String, Object> properties) {
		// 
	}

	/**
	 * 
	 * @param properties
	 */
	public StoreConfig(Properties properties) {
		// 
	}

	/**
	 * 
	 */
	public StoreConfig() {
		// 
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	protected Map<String, Object> readProperties(
		Properties properties
	) {

		Map<String, Object> ret = new HashMap<String, Object>();

		if( properties != null ) {
			Set<String> names = properties.stringPropertyNames();
			for( String name : names ) {
				ret.put(
					name, 
					properties.getProperty(name)
				);
			}
		}

		return ret;
	}

	/**
	 * 
	 * @param property
	 * @param properties
	 * @param def
	 * @return
	 */
	protected String readString(
		String property, 
		Map<String, Object> properties, 
		String def
	) {

		if( property == null ) {
			return def;
		}

		if( properties == null ) {
			return def;
		}

		if( !properties.containsKey(property) ) {
			return def;
		}

		return properties.get(property).toString();
	}

	/**
	 * 
	 * @param property
	 * @param properties
	 * @param def
	 * @return
	 */
	protected int readInteger(
		String property, 
		Map<String, Object> properties, 
		int def
	) {

		if( property == null ) {
			return def;
		}

		if( properties == null ) {
			return def;
		}

		if( !properties.containsKey(property) ) {
			return def;
		}

		return Integer.valueOf(properties.get(property).toString());
	}

	/**
	 * 
	 * @param property
	 * @param properties
	 * @param def
	 * @return
	 */
	protected boolean readBoolean(
		String property, 
		Map<String, Object> properties, 
		boolean def
	) {

		if( property == null ) {
			return def;
		}

		if( properties == null ) {
			return def;
		}

		if( !properties.containsKey(property) ) {
			return def;
		}

		return Boolean.valueOf(properties.get(property).toString());
	}

}
