package io.labber.kbadm.model;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.labber.kbadm.KBException;

public class Metadata {

	protected int chunk_index;
	protected int total_chunks;

	protected String source;
	protected String parent_document_id;

	/**
	 * 
	 * @param chunk
	 * @throws KBException
	 */
	@SuppressWarnings("unchecked")
	public Metadata(
		Chunk chunk
	) throws KBException {

		ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> metadata;
		try {
			metadata = mapper.readValue(chunk.getMetadata(), Map.class);
		} catch (JsonProcessingException e) {
			throw new KBException(e);
		}

		this.chunk_index = Integer.parseInt(metadata.get("chunk_index").toString());
		this.total_chunks = Integer.parseInt(metadata.get("total_chunks").toString());

		this.source = metadata.get("chunk_index").toString();
		this.parent_document_id = metadata.get("chunk_index").toString();

	}

	/**
	 * 
	 * @param metadatajson
	 * @throws KBException
	 */
	@SuppressWarnings("unchecked")
	public Metadata(
		String metadatajson
	) throws KBException {

		ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> metadata;
		try {
			metadata = mapper.readValue(metadatajson, Map.class);
		} catch (JsonProcessingException e) {
			throw new KBException(e);
		}

		this.chunk_index = Integer.parseInt(metadata.get("chunk_index").toString());
		this.total_chunks = Integer.parseInt(metadata.get("total_chunks").toString());

		this.source = metadata.get("chunk_index").toString();
		this.parent_document_id = metadata.get("chunk_index").toString();

	}

	/**
	 * 
	 * @return
	 */
	public int getChunkIndex() {
		return chunk_index;
	}

	/**
	 * 
	 * @return
	 */
	public int getTotalChunks() {
		return total_chunks;
	}

	/**
	 * 
	 * @return
	 */
	public String getSource() {
		return source;
	}

	/**
	 * 
	 * @return
	 */
	public String getParentDocumentId() {
		return parent_document_id;
	}

}
