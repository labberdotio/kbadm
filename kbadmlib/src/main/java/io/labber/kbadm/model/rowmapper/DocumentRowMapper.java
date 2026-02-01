
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import io.labber.kbadm.model.Document;

/**
 * 
 * @author john
 *
 */
public class DocumentRowMapper implements RowMapper<Document> {

	@Override
	public Document mapRow(ResultSet rs, int rowNum) throws SQLException {

		Document document = new Document();

		document.setId(rs.getString("id"));
		document.setName(rs.getString("name"));
		document.setDescription(rs.getString("description"));

		return document;
	}

}
