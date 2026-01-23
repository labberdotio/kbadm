
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import io.labber.kbadm.model.Chunk;

/**
 * 
 * @author john
 *
 */
public class ChunkRowMapper implements RowMapper<Chunk> {

	@Override
	public Chunk mapRow(ResultSet rs, int rowNum) throws SQLException {

		Chunk chunk = new Chunk();

		// [metadata, id, embedding, content]
		chunk.setId(rs.getString("id"));
		chunk.setMetadata(rs.getString("metadata"));
		chunk.setEmbedding(rs.getString("embedding"));
		chunk.setContent(rs.getString("content"));

		return chunk;
	}

}
