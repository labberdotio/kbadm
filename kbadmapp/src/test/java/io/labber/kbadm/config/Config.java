package io.labber.kbadm.config;

import java.util.Collection;

public class Config {

	private Collection<EndpointConfig> endpoints;
	private Collection<DatasourceConfig> datasources;
	private Collection<VectorstoreConfig> vectorstores;
	private Collection<KnowledgebaseConfig> knowledgebases;

	/**
	 * 
	 * @return
	 */
	public Collection<EndpointConfig> getEndpoints() {
		return endpoints;
	}

	/**
	 * 
	 * @param endpoints
	 */
	public void setEndpoints(Collection<EndpointConfig> endpoints) {
		this.endpoints = endpoints;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<DatasourceConfig> getDatasources() {
		return datasources;
	}

	/**
	 * 
	 * @param datasources
	 */
	public void setDatasources(Collection<DatasourceConfig> datasources) {
		this.datasources = datasources;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<VectorstoreConfig> getVectorstores() {
		return vectorstores;
	}

	/**
	 * 
	 * @param vectorstores
	 */
	public void setVectorstores(Collection<VectorstoreConfig> vectorstores) {
		this.vectorstores = vectorstores;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<KnowledgebaseConfig> getKnowledgebases() {
		return knowledgebases;
	}

	/**
	 * 
	 * @param knowledgebases
	 */
	public void setKnowledgebases(Collection<KnowledgebaseConfig> knowledgebases) {
		this.knowledgebases = knowledgebases;
	}

}
