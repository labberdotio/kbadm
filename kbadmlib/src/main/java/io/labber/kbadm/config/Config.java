package io.labber.kbadm.config;

import java.util.ArrayList;
import java.util.Collection;

public class Config {

	private Collection<EndpointConfig> endpoints = new ArrayList<EndpointConfig>();
	private Collection<DatasourceConfig> datasources = new ArrayList<DatasourceConfig>();
	private Collection<TextsplitterConfig> textsplitters = new ArrayList<TextsplitterConfig>();
	private Collection<VectorstoreConfig> vectorstores = new ArrayList<VectorstoreConfig>();
	private Collection<KnowledgebaseConfig> knowledgebases = new ArrayList<KnowledgebaseConfig>();

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
	public Collection<TextsplitterConfig> getTextsplitters() {
		return textsplitters;
	}

	/**
	 * 
	 * @param textsplitters
	 */
	public void setTextsplitters(Collection<TextsplitterConfig> textsplitters) {
		this.textsplitters = textsplitters;
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

	/**
	 * 
	 * @param kb
	 * @return
	 */
	public KnowledgebaseConfig getKnowledgebaseConfig(
		String kb
	) {
		Config config = this;
		for( KnowledgebaseConfig knowledgebase : config.getKnowledgebases() ) {
			if( knowledgebase != null ) {
				if( knowledgebase.getName() != null ) {
					if( knowledgebase.getName().equalsIgnoreCase(kb) ) {
						return knowledgebase;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param kb
	 * @param knowledgebase
	 * @return
	 */
	public EndpointConfig getEndpointConfig(
		String kb, 
		KnowledgebaseConfig knowledgebase
	) {
		Config config = this;
		if( (knowledgebase != null) && 
			(knowledgebase.getEndpoint() != null) ) {
			for( EndpointConfig endpoint : config.getEndpoints() ) {
				if( endpoint != null ) {
					if( endpoint.getName() != null ) {
						if( endpoint.getName().equalsIgnoreCase(
							knowledgebase.getEndpoint()
						) ) {
							return endpoint;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param kb
	 * @param knowledgebase
	 * @return
	 */
	public VectorstoreConfig getVectorstoreConfig(
		String kb, 
		KnowledgebaseConfig knowledgebase
	) {
		Config config = this;
		if( (knowledgebase != null) && 
			(knowledgebase.getVectorstore() != null) ) {
			for( VectorstoreConfig vectorstore : config.getVectorstores() ) {
				if( vectorstore != null ) {
					if( vectorstore.getName() != null ) {
						if( vectorstore.getName().equalsIgnoreCase(
							knowledgebase.getVectorstore()
						) ) {
							return vectorstore;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param kb
	 * @param vectorstore
	 * @return
	 */
	public DatasourceConfig getDatasourceConfig(
		String kb, 
		VectorstoreConfig vectorstore
	) {
		Config config = this;
		if( (vectorstore != null) && 
			(vectorstore.getDatasource() != null) ) {
			for( DatasourceConfig datasource : config.getDatasources() ) {
				if( datasource != null ) {
					if( datasource.getName() != null ) {
						if( datasource.getName().equalsIgnoreCase(
							vectorstore.getDatasource()
						) ) {
							return datasource;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param kb
	 * @param knowledgebase
	 * @return
	 */
	public TextsplitterConfig getTextsplitterConfig(
		String kb, 
		KnowledgebaseConfig knowledgebase
	) {
		Config config = this;
		if( (knowledgebase != null) && 
			(knowledgebase.getTextsplitter() != null) ) {
			for( TextsplitterConfig textsplitter : config.getTextsplitters() ) {
				if( textsplitter != null ) {
					if( textsplitter.getName() != null ) {
						if( textsplitter.getName().equalsIgnoreCase(
							knowledgebase.getTextsplitter()
						) ) {
							return textsplitter;
						}
					}
				}
			}
		}
		return null;
	}

}
