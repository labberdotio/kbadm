
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.neo4j;

import java.util.Collection;

// import org.neo4j.driver.SessionConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore;
import org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore.Neo4jDistanceType;

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

	private org.neo4j.driver.SessionConfig sessionConfig;
	private org.neo4j.driver.Driver neo4jDriver;

	// private OllamaApi ollamaApi = null;
	private ChatClient chatClient = null;
	private VectorStore vectorStore = null;

	// private EmbeddingModel embeddingModel = null;

	/**
	 * 
	 * @param neo4jDriver
	 * @param chatClient
	 * @param vectorStore
	 * @param store
	 * @throws KBException
	 */
	public Neo4jStoreDriverImpl(
		org.neo4j.driver.Driver neo4jDriver, 
		ChatClient chatClient, 
		VectorStore vectorStore, 
		Neo4jStoreImpl store
	) throws KBException {

		super(store);

		this.store = store;
		this.config = store.getConfig();
		this.metadata = store.getMetadata();
		this.sessionConfig = org.neo4j.driver.SessionConfig.defaultConfig();
		this.neo4jDriver = neo4jDriver;
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;

		this.init();

	}

	/**
	 * 
	 * @throws KBException
	 */
	public void init() throws KBException {

		// this.createSchema(
		this.initSchema();

	}

	/**
	 * 
	 * @throws KBException
	 */
	public void close() throws KBException {

		// 

	}

	/**
	 * 
	 * @return
	 */
	public ChatClient chatClient() {
		return this.chatClient;
	}

	/**
	 * 
	 * @return
	 */
	public VectorStore vectorStore() {
		return this.vectorStore;
	}

	/**
	 * 
	 * @throws KBException
	 */
	public void initSchema() throws KBException {

		// this.sessionConfig = org.neo4j.driver.SessionConfig.defaultConfig();

		try (var session = this.neo4jDriver.session(this.sessionConfig)) {

			session.executeWriteWithoutResult(tx -> {
				tx.run("CREATE CONSTRAINT %s IF NOT EXISTS FOR (n:%s) REQUIRE n.%s IS UNIQUE"
					.formatted(this.constraintName(), this.label(), this.idProperty())).consume();

				var statement = """
						CREATE VECTOR INDEX `%s` IF NOT EXISTS FOR (n:%s) ON (n.%s)
								OPTIONS {indexConfig: {
								`vector.dimensions`: %d,
								`vector.similarity_function`: '%s'
								}}
						""".formatted(this.indexName(), this.label(), this.embeddingProperty(), this.embeddingDimension(),
						this.distanceType().name);
				tx.run(statement).consume();
			});

			// Bad idea to retry this...
			session.run("CALL db.awaitIndexes()").consume();
		}

	}

	/**
	 * 
	 * @return
	 */
	public String label() {
		return this.config.getLabelName();
	}

	/**
	 * 
	 * @return
	 */
	public String indexName() {
		return this.config.getIndexName();
	}

	/**
	 * 
	 * @return
	 */
	public String constraintName() {
		return Neo4jVectorStore.DEFAULT_CONSTRAINT_NAME;
	}

	/**
	 * 
	 * @return
	 */
	public String idProperty() {
		return Neo4jVectorStore.DEFAULT_ID_PROPERTY;
	}

	/**
	 * 
	 * @return
	 */
	public String embeddingProperty() {
		return this.config.getEmbeddingProperty();
	}

	/**
	 * 
	 * @return
	 */
	public int embeddingDimension() {
		return this.config.getDimensions();
	}

	/**
	 * 
	 * @return
	 */
	public Neo4jDistanceType distanceType() {
		return this.config.getDistanceType();
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
	// protected VectorStore vectorStore(
	// 	org.neo4j.driver.Driver driver, 
	// 	EmbeddingModel embeddingModel
	// ) {
	// 	return Neo4jVectorStore.builder(
	// 		driver, 
	// 		embeddingModel
	// 	).databaseName(
	// 		this.config.getDatabaseName() // "neo4j"
	// 	).distanceType(
	// 		this.config.getDistanceType() // Neo4jDistanceType.COSINE
	// 	).embeddingDimension(
	// 		this.config.getDimensions() // 1536
	// 	).label(
	// 		this.config.getLabelName() // "Document"
	// 	).embeddingProperty(
	// 		this.config.getEmbeddingProperty() // "embedding"
	// 	).indexName(
	// 		this.config.getIndexName() // "custom-index"
	// 	).initializeSchema(
	// 		this.config.isInitializeSchema() // true
	// 	).batchingStrategy(
	// 		new TokenCountBatchingStrategy()
	// 	).build();
	// }

}
