package io.labber.kbadm.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.labber.kbadm.KBException;
import io.labber.kbadm.model.Chunk;
import io.labber.kbadm.model.Document;
import io.labber.kbadm.model.Metadata;
import io.labber.kbadm.model.Status;

public class DocumentValidator {

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
	public Status validate() throws KBException {

		if( this.document == null ) {
			return Status.INITIAL;
		}

		int chunk_count = 0;
		int total_chunks = 0;
		int chunk_index = 0;

		List<Integer> chunk_indexes = new ArrayList<Integer>();

		if( this.chunks == null ) {
			return Status.INITIAL;
		}

		chunk_count = this.chunks.size();
		if( chunk_count == 0 ) {
			return Status.INITIAL;
		}

		Iterator<Chunk> listiter = this.chunks.iterator();
		while( listiter.hasNext() ) {

			Chunk chunk = listiter.next();

			Metadata metadata = new Metadata(chunk);

			total_chunks = metadata.getTotalChunks();
			chunk_index = metadata.getChunkIndex();

			if( total_chunks != chunk_count ) {
				return Status.INCOMPLETE;
			}

			if( chunk_indexes.contains(chunk_index) ) {
				return Status.FAILED;
			}

			chunk_indexes.add(chunk_index);
		}

		if( chunk_indexes.size() != chunk_count ) {
			return Status.INCOMPLETE;
		}

		if( chunk_indexes.size() != total_chunks ) {
			return Status.INCOMPLETE;
		}

		return Status.COMPLETE;
	}

}
