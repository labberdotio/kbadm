package io.labber.kbadm.config;

import java.util.Map;

public class DatasourceConfig {

	private String name;
	private String type;

	private Map<String, Object> config;

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Object> getConfig() {
		return config;
	}

	/**
	 * 
	 * @param config
	 */
	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}

}
