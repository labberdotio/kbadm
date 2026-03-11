package io.labber.kbadm.config;

import java.util.Map;

public class KnowledgebaseConfig {

	private String name;
	private String vectorstore;

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
	public String getVectorstore() {
		return vectorstore;
	}

	/**
	 * 
	 * @param vectorstore
	 */
	public void setVectorstore(String vectorstore) {
		this.vectorstore = vectorstore;
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
