
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
public class DocumentChunk {

	// [document_id, vector_id]

	protected String documentId;
	protected String vectorId;

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

}
