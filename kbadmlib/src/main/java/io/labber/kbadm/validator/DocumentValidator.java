package io.labber.kbadm.validator;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import io.labber.kbadm.KBException;
import io.labber.kbadm.model.Chunk;
import io.labber.kbadm.model.Document;
import io.labber.kbadm.model.Metadata;
import io.labber.kbadm.model.Status;

public class DocumentValidator implements IDocumentValidator {

	protected Document document;
	protected Collection<Chunk> chunks;

	/**
	 * 
	 * @param document
	 * @param chunks
	 */
	public DocumentValidator(
		Document document, 
		Collection<Chunk> chunks
	) {
		this.document = document;
		this.chunks = chunks;
	}

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public Pair<Status, String> status() throws KBException {

		if( this.document == null ) {
			return Pair.of(Status.INITIAL, "New document");
		}

		if( this.document.getId() == null ) {
			return Pair.of(Status.INITIAL, "New document");
		}

		if( this.document.getName() == null ) {
			return Pair.of(Status.INITIAL, "New document");
		}

		int chunk_count = 0;
		// int total_chunks = 0;
		// int chunk_index = 0;

		List<Integer> chunk_indexes = new ArrayList<Integer>();

		if( this.chunks == null ) {
			return Pair.of(Status.INITIAL, "Empty document");
		}

		chunk_count = this.chunks.size();
		if( chunk_count == 0 ) {
			return Pair.of(Status.INITIAL, "Empty document");
		}

		Iterator<Chunk> listiter = this.chunks.iterator();
		while( listiter.hasNext() ) {

			Chunk chunk = listiter.next();

			Metadata metadata = new Metadata(chunk);

			// total_chunks = metadata.getTotalChunks();
			// chunk_index = metadata.getChunkIndex();

			if( !this.document.getId().equalsIgnoreCase(metadata.getParentDocumentId()) ) {
				return Pair.of(Status.FAILED, "Inconsistent parent document id for chunk index " + metadata.getChunkIndex());
			}

			if( !this.document.getName().equalsIgnoreCase(metadata.getSource()) ) {
				return Pair.of(Status.FAILED, "Inconsistent parent document source for chunk index " + metadata.getChunkIndex());
			}

			if( metadata.getTotalChunks() != chunk_count ) {
				return Pair.of(Status.FAILED, "Inconsistent chunk total count for chunk index " + metadata.getChunkIndex());
			}

			if( chunk_indexes.contains(metadata.getChunkIndex()) ) {
				return Pair.of(Status.FAILED, "Inconsistent chunk index for chunk index " + metadata.getChunkIndex());
			}

			chunk_indexes.add(metadata.getChunkIndex());
		}

		if( chunk_indexes.size() != chunk_count ) {
			return Pair.of(Status.INCOMPLETE, "Missing or inconsistent chunk count");
		}

		// if( chunk_indexes.size() != total_chunks ) {
		// 	return Pair.of(Status.INCOMPLETE, "Missing or inconsistent chunk count");
		// }

		return Pair.of(Status.COMPLETE, "Complete document");
	}

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public Document validate() throws KBException {
		Pair<Status, String> status = this.status();
		Date date = new Date(System.currentTimeMillis());
		return new Document()
			.withId(this.document.getId())
			.withName(this.document.getName())
			.withDescription(this.document.getDescription())
			.withChunks(this.chunks.size())
			.withTotal(this.chunks.size())
			.withStatus(status.getLeft().getStatus()) // this.status().getStatus())
			.withReason(status.getRight()) // this.status().getStatus())
			// .withCreated(this.document.getCreated())
			// .withModified(this.document.getModified())
			// .withTimestamp(Date.valueOf(LocalDate.now()));
			.withCreated(date)
			.withModified(date)
			.withTimestamp(date);
	}

}
