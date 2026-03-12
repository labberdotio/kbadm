
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.impl.pgvector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import io.labber.kbadm.KBException;
import io.labber.kbadm.driver.Driver;
import io.labber.kbadm.model.Chunk;
import io.labber.kbadm.model.Document;
import io.labber.kbadm.model.DocumentChunk;
import io.labber.kbadm.model.rowmapper.ChunkRowMapper;
import io.labber.kbadm.model.rowmapper.DocumentChunkRowMapper;
import io.labber.kbadm.model.rowmapper.DocumentRowMapper;

/**
 * 
 * @author john
 *
 */
public class PgVectorStoreDriverImpl extends Driver {

	private PgVectorStoreImpl store;
	private PgVectorStoreConfigImpl config;
	private PgVectorStoreMetadataImpl metadata;

	private DataSource dataSource = null;
	private JdbcTemplate jdbcTemplate;

	// private OllamaApi ollamaApi = null;
	private ChatClient chatClient = null;
	private VectorStore vectorStore = null;

	// private EmbeddingModel embeddingModel = null;

	/**
	 * 
	 * @param jdbcTemplate
	 * @param chatClient
	 * @param vectorStore
	 * @param store
	 */
	public PgVectorStoreDriverImpl(
		JdbcTemplate jdbcTemplate, 
		ChatClient chatClient, 
		VectorStore vectorStore, 
		PgVectorStoreImpl store
	) {
		super(store);
		this.store = store;
		this.config = store.getConfig();
		this.metadata = store.getMetadata();
		this.jdbcTemplate = jdbcTemplate;
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;
	}

	/**
	 * 
	 * @throws KBException
	 */
	public void init() throws KBException {

		// 

	}

	/**
	 * 
	 * @throws KBException
	 */
	public void close() throws KBException {

		// 

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
	 * @return
	 */
	public JdbcTemplate jdbcTemplate() {
		return this.jdbcTemplate;
	}

	/**
	 * 
	 * @return
	 */
	public ChatClient chatClient() {
		return this.chatClient;
	}

	/**
	 * 
	 * @return
	 */
	public VectorStore vectorStore() {
		return this.vectorStore;
	}

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
	 */
	public void initSchema() throws KBException {

		this.jdbcTemplate().execute(String.format("CREATE SCHEMA IF NOT EXISTS %s", this.getSchemaName()));

		// Remove existing VectorStoreTable
		// if (this.removeExistingVectorStoreTable) {
		// 	this.jdbcTemplate().execute(String.format("DROP TABLE IF EXISTS %s", this.getFullyQualifiedTableName(schema, table)));
		// }

		this.jdbcTemplate().execute(String.format("""
				CREATE TABLE IF NOT EXISTS %s (
					id %s PRIMARY KEY,
					content text,
					metadata json,
					embedding vector(%d)
				)
				""", this.getFullyQualifiedTableName(), this.getColumnTypeName(), this.embeddingDimensions()));

		// if (this.createIndexMethod != PgIndexType.NONE) {
			this.jdbcTemplate().execute(String.format("""
					CREATE INDEX IF NOT EXISTS %s ON %s USING %s (embedding %s)
					""", this.getVectorIndexName(), this.getFullyQualifiedTableName(), this.createIndexMethod(),
					this.getDistanceType().index));
		// }

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
	 * @return
	 */
	public String getSchemaName() {
		return this.config.getSchemaName();
	}

	/**
	 * 
	 * @return
	 */
	public String getVectorTableName() {
		return this.getTableName(
			this.config.getVectorTableName() // PgVectorStore.DEFAULT_TABLE_NAME // "vector_store"
		);
	}

	/**
	 * 
	 * @return
	 */
	public String getFullyQualifiedTableName() {
		return this.getSchemaName() + "." + this.getVectorTableName();
	}

	/**
	 * 
	 * @return
	 */
	public String getColumnTypeName() {
		// TODO
		// return this.config.getColumnTypeName();
		return "uuid";
	}

	/**
	 * 
	 * @return
	 */
	public int embeddingDimensions() {
		return this.config.getDimensions();
	}

	/**
	 * 
	 * @return
	 */
	public String getVectorIndexName() {
		String vectorTableName = this.getVectorTableName();
		String vectorIndexName = vectorTableName.equals(PgVectorStore.DEFAULT_TABLE_NAME) ? PgVectorStore.DEFAULT_VECTOR_INDEX_NAME
			: vectorTableName + "_index";
		return vectorIndexName;
	}

	/**
	 * 
	 * @return
	 */
	public PgIndexType createIndexMethod() {
		return this.config.getIndexType();
	}

	/**
	 * 
	 * @return
	 */
	public PgDistanceType getDistanceType() {
		return this.config.getDistanceType();
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
	 * @param statment
	 * @return
	 * @throws SQLException
	 */
	protected Collection<Document> runDocumentStatement(String statment) throws SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<Document> list = null;
		try {

			JdbcTemplate tmpl = this.jdbcTemplate;
			PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareCall(statment);
					return ps;
				}
			};

			list = tmpl.query(psc, new DocumentRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
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

		return list;
	}

	/**
	 * 
	 * @param statment
	 * @return
	 * @throws SQLException
	 */
	protected Collection<Chunk> runChunkStatement(String statment) throws SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<Chunk> list = null;
		try {

			JdbcTemplate tmpl = this.jdbcTemplate;
			PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareCall(statment);
					return ps;
				}
			};

			list = tmpl.query(psc, new ChunkRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
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

		return list;
	}

	/**
	 * 
	 * @param statment
	 * @return
	 * @throws SQLException
	 */
	protected Collection<DocumentChunk> runDocumentChunkStatement(String statment) throws SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<DocumentChunk> list = null;
		try {

			JdbcTemplate tmpl = this.jdbcTemplate;
			PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareCall(statment);
					return ps;
				}
			};

			list = tmpl.query(psc, new DocumentChunkRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
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

		return list;
	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<Document> getDocuments(
		String document
	) throws KBException {
		try {
			return this.runDocumentStatement(
				"SELECT * FROM document_store WHERE name = '" + document.replace("'", "''") + "';"
			);
		} catch( SQLException e ) {
			throw new KBException(e);
		}
	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<Chunk> getChunks(
		String document
	) throws KBException {
		try {
			return this.runChunkStatement(
				"SELECT * FROM vector_store WHERE metadata->>'source' = '" + document.replace("'", "''") + "';"
			);
		} catch( SQLException e ) {
			throw new KBException(e);
		}
	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<Chunk> getChunks(
		Document document
	) throws KBException {
		try {
			return this.runChunkStatement(
				"SELECT * FROM vector_store WHERE metadata->>'parent_document_id' = '" + document.getId().replace("'", "''") + "';"
			);
		} catch( SQLException e ) {
			throw new KBException(e);
		}
	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	public Collection<DocumentChunk> getDocumentChunks(
		Document document
	) throws KBException {
		try {
			return this.runDocumentChunkStatement(
				"SELECT * FROM document_chunk WHERE document_id = '" + document.getId().replace("'", "''") + "';"
			);
		} catch( SQLException e ) {
			throw new KBException(e);
		}
	}

}
