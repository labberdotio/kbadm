
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.model;

import java.sql.Date;

/**
 * 
 * @author john
 *
 */
public class DocumentChunk {

	// [document_id, vector_id]

	protected String documentId;
	protected String vectorId;

	protected Date timestamp;

	/**
	 * 
	 * @return
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * 
	 * @param documentId
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * 
	 * @return
	 */
	public String getVectorId() {
		return vectorId;
	}

	/**
	 * 
	 * @param vectorId
	 */
	public void setVectorId(String vectorId) {
		this.vectorId = vectorId;
	}

	/**
	 * 
	 * @return
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
