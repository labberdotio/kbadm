
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.pgvector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.jdbc.core.JdbcTemplate;

import io.labber.kbadm.KBException;
import io.labber.kbadm.driver.Driver;

/**
 * 
 * @author john
 *
 */
public class PgVectorStoreDriverImpl extends Driver {

	private PgVectorStoreImpl store;
	private PgVectorStoreConfigImpl config;
	private PgVectorStoreMetadataImpl metadata;

	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @param jdbcTemplate
	 * @param store
	 */
	public PgVectorStoreDriverImpl(
		JdbcTemplate jdbcTemplate, 
		PgVectorStoreImpl store
	) {
		super(store);
		this.store = store;
		this.config = store.getConfig();
		this.metadata = store.getMetadata();
		this.jdbcTemplate = jdbcTemplate;
	}

//	public void init() throws KBException {
//
//		/*
//		 * Main table
//		 */
//		try(
//			Connection conn = this.jdbcTemplate.getDataSource().getConnection();
//			Statement stmt = conn.createStatement();
//		) {
//			stmt.executeUpdate(
//					"CREATE TABLE IF NOT EXISTS " + this.getStoreTableName() + " ("
//							+ " id SERIAL PRIMARY KEY,"
//							+ " name VARCHAR(255) NOT NULL"
//							+ ");"
//			);
//		} catch( SQLException e ) {
//			e.printStackTrace();
//		}
//
//	}

	/**
	 * 
	 * @throws KBException
	 */
	public void createSchema() throws KBException {
		this.createTable(
			"CREATE TABLE IF NOT EXISTS " + this.getStoreTableName() + " ("
					+ " id SERIAL PRIMARY KEY,"
					+ " name VARCHAR(255) NOT NULL"
					+ ");"
		);
	}

	/**
	 * 
	 * @throws KBException
	 */
	public void destroySchema() throws KBException {
		this.dropTable(
			"DROP TABLE IF EXISTS " + this.getStoreTableName() + ";"
		);
	}

	/**
	 * 
	 * @param table
	 * @return
	 */
	protected String getTableName(String table) {
		// TODO: Clean up to only generate valid PostgreSQL table names.
		return this.config.getName().toLowerCase() + "_" + table;
	}

	/**
	 * 
	 * @return
	 */
	protected String getStoreTableName() {
		return this.config.getName().toLowerCase();
	}

	/**
	 * 
	 * @return
	 */
	protected String getStoreConfigTableName() {
		return this.getTableName("config");
	}

	/**
	 * 
	 * @return
	 */
	protected String getStoreMetadataTableName() {
		return this.getTableName("metadata");
	}

	/**
	 * 
	 * @return
	 */
	protected String getStoreDocumentsTableName() {
		return this.getTableName("documents");
	}

	/**
	 * 
	 * @return
	 */
	protected String getStoreChunksTableName() {
		return this.getTableName("chunks");
	}

	/**
	 * 
	 * @return
	 */
	protected String getVectorStoreTableName() {
		return this.getTableName(
			this.config.getVectorTableName() // PgVectorStore.DEFAULT_TABLE_NAME // "vector_store"
		);
	}

	/**
	 * 
	 * @param createTableSQL
	 * @return
	 * @throws KBException
	 */
	protected boolean createTable(String createTableSQL) throws KBException {

		/*
		 * 
		 */
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(createTableSQL);
			return true;
		} catch( SQLException e ) {
			throw new KBException(e);
			// return false;
		} finally {
			if( rs != null ) {
				try { rs.close(); } catch (SQLException e) { /* log error */ }
			}
			if( stmt != null ) {
				try { stmt.close(); } catch (SQLException e) { /* log error */ }
			}
			if( conn != null ) {
				try { conn.close(); } catch (SQLException e) { /* log error */ }
			}
		}

	}

	/**
	 * 
	 * @param dropTableSQL
	 * @return
	 * @throws KBException
	 */
	protected boolean dropTable(String dropTableSQL) throws KBException {

		/*
		 * 
		 */
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(dropTableSQL);
			return true;
		} catch( SQLException e ) {
			throw new KBException(e);
			// return false;
		} finally {
			if( rs != null ) {
				try { rs.close(); } catch (SQLException e) { /* log error */ }
			}
			if( stmt != null ) {
				try { stmt.close(); } catch (SQLException e) { /* log error */ }
			}
			if( conn != null ) {
				try { conn.close(); } catch (SQLException e) { /* log error */ }
			}
		}

	}

	/**
	 * 
	 * @param jdbcTemplate
	 * @param embeddingModel
	 * @return
	 */
	protected VectorStore vectorStore(
		JdbcTemplate jdbcTemplate, 
		EmbeddingModel embeddingModel
	) {
		return PgVectorStore.builder(
			jdbcTemplate, 
			embeddingModel
		).indexType(
			this.config.getIndexType() // PgIndexType.HNSW
		).distanceType(
			this.config.getDistanceType() // PgDistanceType.COSINE_DISTANCE
		).dimensions(
			this.config.getDimensions() // 1024
		).schemaName(
			this.config.getSchemaName() // PgVectorStore.DEFAULT_SCHEMA_NAME
		).vectorTableName(
			this.getTableName(
				this.config.getVectorTableName() // PgVectorStore.DEFAULT_TABLE_NAME // "vector_store"
			)
		).initializeSchema(
			this.config.isInitializeSchema() // true
		).removeExistingVectorStoreTable(
			this.config.isRemoveExistingVectorStoreTable() // false
		).build(); 
	}

}
