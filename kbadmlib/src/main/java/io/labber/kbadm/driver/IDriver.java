
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.driver;

import java.util.Collection;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;

import io.labber.kbadm.KBException;
import io.labber.kbadm.model.Chunk;
import io.labber.kbadm.model.Document;
import io.labber.kbadm.model.DocumentChunk;
import io.labber.kbadm.store.IStore;
import io.labber.kbadm.store.IStoreConfig;
import io.labber.kbadm.store.IStoreMetadata;

/**
 * 
 * @author john
 *
 */
public interface IDriver {

	/**
	 * 
	 * @return
	 */
	public IStore getStore();

	/**
	 * 
	 * @return
	 */
	public IStoreConfig getConfig();

	/**
	 * 
	 * @return
	 */
	public IStoreMetadata getMetadata();

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<Document> getDocuments(
		String document
	) throws KBException;

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<Chunk> getChunks(
		String document
	) throws KBException;

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<Chunk> getChunks(
		Document document
	) throws KBException;

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<DocumentChunk> getDocumentChunks(
		Document document
	) throws KBException;

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public VectorStore vectorStore(
	) throws KBException;

	/**
	 * 
	 * @param embeddingModel
	 * @return
	 * @throws KBException
	 */
	// public VectorStore vectorStore(
	// 	EmbeddingModel embeddingModel
	// ) throws KBException;

}
