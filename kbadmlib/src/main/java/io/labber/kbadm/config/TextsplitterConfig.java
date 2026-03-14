package io.labber.kbadm.config;

import java.util.Map;

public class TextsplitterConfig {

	private String name;

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
