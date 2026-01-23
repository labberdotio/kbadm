
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import io.labber.kbadm.model.DocumentChunk;

/**
 * 
 * @author john
 *
 */
public class DocumentChunkRowMapper implements RowMapper<DocumentChunk> {

	@Override
	public DocumentChunk mapRow(ResultSet rs, int rowNum) throws SQLException {

		DocumentChunk chunk = new DocumentChunk();

		chunk.setDocumentId(rs.getString("document_id"));
		chunk.setVectorId(rs.getString("vector_id"));

		return chunk;
	}

}
