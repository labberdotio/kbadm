
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

/**
 * 
 * @author john
 *
 */
public class StoreConfig implements IStoreConfig {

	protected String id;
	protected UUID uuid;
	protected String name;

	/**
	 * 
	 * @param properties
	 */
	public StoreConfig(Map<String, Object> properties) {
		this.id = this.readString("id", properties, null);
		this.uuid = UUID.fromString(
			this.readString("uuid", properties, UUID.randomUUID().toString())
		);
		this.name = this.readString("name", properties, null);
	}

	/**
	 * 
	 * @param properties
	 */
	public StoreConfig(Properties properties) {
		this.id = this.readString("id", this.readProperties(properties), null);
		this.uuid = UUID.fromString(
			this.readString("uuid", this.readProperties(properties), UUID.randomUUID().toString())
		);
		this.name = this.readString("name", this.readProperties(properties), null);
	}

	/**
	 * 
	 */
	public StoreConfig() {
		this.id = null;
		this.uuid = UUID.randomUUID();
		this.name = null;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public UUID getUuid() {
		return this.uuid;
	}

	@Override
	public String getName() {
		return this.name;
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
