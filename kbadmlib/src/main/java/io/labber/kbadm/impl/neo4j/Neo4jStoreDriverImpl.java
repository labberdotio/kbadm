
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.neo4j;

import java.util.Collection;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore;

import io.labber.kbadm.KBException;
import io.labber.kbadm.driver.Driver;
import io.labber.kbadm.model.Chunk;
import io.labber.kbadm.model.Document;
import io.labber.kbadm.model.DocumentChunk;

/**
 * 
 * @author john
 *
 */
public class Neo4jStoreDriverImpl extends Driver {

	private Neo4jStoreImpl store;
	private Neo4jStoreConfigImpl config;
	private Neo4jStoreMetadataImpl metadata;

	org.neo4j.driver.Driver neo4jDriver;

	/**
	 * 
	 * @param neo4jDriver
	 * @param store
	 */
	public Neo4jStoreDriverImpl(
		org.neo4j.driver.Driver neo4jDriver, 
		Neo4jStoreImpl store
	) {
		super(store);
		this.store = store;
		this.config = store.getConfig();
		this.metadata = store.getMetadata();
		this.neo4jDriver = neo4jDriver;
	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<Document> getDocuments(
		String document
	) throws KBException {
		// TODO
		Collection<Document> list = null;
		return list;
	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<Chunk> getChunks(
		String document
	) throws KBException {
		// TODO
		Collection<Chunk> list = null;
		return list;
	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<Chunk> getChunks(
		Document document
	) throws KBException {
		// TODO
		Collection<Chunk> list = null;
		return list;
	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<DocumentChunk> getDocumentChunks(
		Document document
	) throws KBException {
		// TODO
		Collection<DocumentChunk> list = null;
		return list;
	}

	/**
	 * 
	 * @param neo4jDriver
	 * @param embeddingModel
	 * @return
	 */
	protected VectorStore vectorStore(
		org.neo4j.driver.Driver driver, 
		EmbeddingModel embeddingModel
	) {
		return Neo4jVectorStore.builder(
			driver, 
			embeddingModel
		).databaseName(
			this.config.getDatabaseName() // "neo4j"
		).distanceType(
			this.config.getDistanceType() // Neo4jDistanceType.COSINE
		).embeddingDimension(
			this.config.getDimensions() // 1536
		).label(
			this.config.getLabelName() // "Document"
		).embeddingProperty(
			this.config.getEmbeddingProperty() // "embedding"
		).indexName(
			this.config.getIndexName() // "custom-index"
		).initializeSchema(
			this.config.isInitializeSchema() // true
		).batchingStrategy(
			new TokenCountBatchingStrategy()
		).build();
	}

	/**
	 * 
	 * @return
	 */
	public VectorStore vectorStore() {
		return this.vectorStore(
			neo4jDriver, 
			null
		);
	}

	/**
	 * 
	 * @param embeddingModel
	 * @return
	 */
	public VectorStore vectorStore(
		EmbeddingModel embeddingModel
	) {
		return this.vectorStore(
			neo4jDriver, 
			embeddingModel
		);
	}

}
