
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.model;

/**
 * 
 * @author john
 *
 */
public class Chunk {

	// [metadata, id, embedding, content]

	protected String id;
	protected String metadata;
	protected String embedding;
	protected String content;

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public String getMetadata() {
		return metadata;
	}

	/**
	 * 
	 * @param metadata
	 */
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	/**
	 * 
	 * @return
	 */
	public String getEmbedding() {
		return embedding;
	}

	/**
	 * 
	 * @param embedding
	 */
	public void setEmbedding(String embedding) {
		this.embedding = embedding;
	}

	/**
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Chunk withId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * 
	 * @param metadata
	 * @return
	 */
	public Chunk withMetadata(String metadata) {
		this.metadata = metadata;
		return this;
	}

	/**
	 * 
	 * @param embedding
	 * @return
	 */
	public Chunk withEmbedding(String embedding) {
		this.embedding = embedding;
		return this;
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	public Chunk withContent(String content) {
		this.content = content;
		return this;
	}

}
