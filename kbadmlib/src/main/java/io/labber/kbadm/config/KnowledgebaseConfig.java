package io.labber.kbadm.config;

import java.util.Map;

public class KnowledgebaseConfig {

	private String name;
	private String endpoint;
	private String vectorstore;
	private String textsplitter;

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
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * 
	 * @param endpoint
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
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
	public String getTextsplitter() {
		return textsplitter;
	}

	/**
	 * 
	 * @param textsplitter
	 */
	public void setTextsplitter(String textsplitter) {
		this.textsplitter = textsplitter;
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
