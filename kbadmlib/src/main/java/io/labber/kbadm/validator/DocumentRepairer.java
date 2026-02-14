
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.validator;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.labber.kbadm.KBException;
import io.labber.kbadm.model.Chunk;
import io.labber.kbadm.model.Document;
import io.labber.kbadm.model.DocumentChunk;
import io.labber.kbadm.model.Metadata;
import io.labber.kbadm.model.Status;

/**
 * 
 * @author john
 *
 */
public class DocumentRepairer implements IDocumentRepairer {

	protected Document document;
	protected Collection<Chunk> chunks;
	protected Collection<DocumentChunk> documentChunks;
	protected ValidationStatus status;

	/**
	 * 
	 * @param document
	 * @param chunks
	 * @param documentChunks
	 * @param status
	 */
	public DocumentRepairer(
		Document document, 
		Collection<Chunk> chunks, 
		Collection<DocumentChunk> documentChunks, 
		ValidationStatus status
	) {
		this.document = document;
		this.chunks = chunks;
		this.documentChunks = documentChunks;
		this.status = status;
	}

	/**
	 * 
	 * @param document
	 * @param chunks
	 * @param documentChunks
	 * @throws KBException
	 */
	public DocumentRepairer(
		Document document, 
		Collection<Chunk> chunks, 
		Collection<DocumentChunk> documentChunks
	) throws KBException {
		this.document = document;
		this.chunks = chunks;
		this.documentChunks = documentChunks;
		this.status = new DocumentValidator(
			document, 
			chunks, 
			documentChunks
		).status();
	}

	/**
	 * 
	 * @param document
	 * @param chunks
	 * @throws KBException 
	 */
	public DocumentRepairer(
		Document document, 
		Collection<Chunk> chunks
	) throws KBException {
		this.document = document;
		this.chunks = chunks;
		this.documentChunks = null;
		this.status = new DocumentValidator(
			document, 
			chunks
		).status();
	}

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public RepairStatus status() throws KBException {

		if( this.document == null ) {
			return new RepairStatus(Status.INITIAL, "New document", false);
		}

		if( this.document.getId() == null ) {
			return new RepairStatus(Status.INITIAL, "New document", false);
		}

		if( this.document.getName() == null ) {
			return new RepairStatus(Status.INITIAL, "New document", false);
		}

		int chunk_count = 0;
		// int total_chunks = 0;
		// int chunk_index = 0;

		List<Integer> chunk_indexes = new ArrayList<Integer>();

		if( this.chunks == null ) {
			return new RepairStatus(Status.INITIAL, "Empty document", false);
		}

		chunk_count = this.chunks.size();
		if( chunk_count == 0 ) {
			return new RepairStatus(Status.INITIAL, "Empty document", false);
		}

		Iterator<Chunk> listiter = this.chunks.iterator();
		while( listiter.hasNext() ) {

			Chunk chunk = listiter.next();

			Metadata metadata = new Metadata(chunk);

			// total_chunks = metadata.getTotalChunks();
			// chunk_index = metadata.getChunkIndex();

			if( !this.document.getId().equalsIgnoreCase(metadata.getParentDocumentId()) ) {
				return new RepairStatus(Status.FAILED, "Inconsistent parent document id for chunk index " + metadata.getChunkIndex(), false);
			}

			if( !this.document.getName().equalsIgnoreCase(metadata.getSource()) ) {
				return new RepairStatus(Status.FAILED, "Inconsistent parent document source for chunk index " + metadata.getChunkIndex(), false);
			}

			if( metadata.getTotalChunks() != chunk_count ) {
				return new RepairStatus(Status.FAILED, "Inconsistent chunk total count for chunk index " + metadata.getChunkIndex(), false);
			}

			if( chunk_indexes.contains(metadata.getChunkIndex()) ) {
				return new RepairStatus(Status.FAILED, "Inconsistent chunk index for chunk index " + metadata.getChunkIndex(), false);
			}

			chunk_indexes.add(metadata.getChunkIndex());
		}

		if( chunk_indexes.size() != chunk_count ) {
			return new RepairStatus(Status.INCOMPLETE, "Missing or inconsistent chunk count", false);
		}

		// if( chunk_indexes.size() != total_chunks ) {
		// 	return new ValidationStatus(Status.INCOMPLETE, "Missing or inconsistent chunk count", false);
		// }

		if( this.documentChunks != null ) {

			if( this.documentChunks.size() != chunk_count ) {

				Collection<DocumentChunk> newDocumentChunks = new ArrayList<DocumentChunk>();

				Iterator<Chunk> listiter2 = this.chunks.iterator();
				while( listiter.hasNext() ) {
					
				}

				return new RepairStatus(Status.COMPLETE, "Repaired", true);

			}

		}

		return new RepairStatus(Status.COMPLETE, "Complete document", false);
	}

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public Document repair() throws KBException {
		RepairStatus status = this.status();
		Date date = new Date(System.currentTimeMillis());
		return new Document()
			.withId(this.document.getId())
			.withName(this.document.getName())
			.withDescription(this.document.getDescription())
			.withChunks(this.chunks.size())
			.withTotal(this.chunks.size())
			.withStatus(status.getStatus().getStatus()) // this.status().getStatus())
			.withReason(status.getReason()) // this.status().getStatus())
			// .withCreated(this.document.getCreated())
			// .withModified(this.document.getModified())
			// .withTimestamp(Date.valueOf(LocalDate.now()));
			.withCreated(date)
			.withModified(date)
			.withTimestamp(date);
	}

}
