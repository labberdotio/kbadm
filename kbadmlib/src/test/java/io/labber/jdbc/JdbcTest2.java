
// 
// Copyright (c) 2024, 2025, John Grundback
// All rights reserved.
// 

package io.labber.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * 
 * @author john
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
// public class JdbcTest2 extends TestCase {
public class JdbcTest2 {

	@Configuration
	static class ContextConfiguration {

		// Bean definitions go here.

		@Bean
		public DataSource dataSource() {

			DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
			dataSourceBuilder.driverClassName("org.postgresql.Driver");
			dataSourceBuilder.url("jdbc:postgresql://10.88.88.197:5432/pgvector_db");
			dataSourceBuilder.username("postgres");
			dataSourceBuilder.password("postgres");

			return dataSourceBuilder.build();
		}

		@Bean
		public JdbcTemplate jdbcTemplate() {
			return new JdbcTemplate(this.dataSource());
		}

	}

	// Bean references go here.

	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	// public JdbcTest(String testName) {
	// 	super(testName);
	// }

	/**
	 * @return the suite of tests being tested
	 */
	// public static Test suite() {
	// 	return new TestSuite(JdbcTest.class);
	// }

	/**
	 * 
	 */
	// public void setUp() {
	// 	// Configurator.setAllLevels("", Level.ALL); 
	// }

	// @Before
	// public void setUp() {
	// 	Configurator.setAllLevels("", Level.ALL);
	// }

	/**
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	protected Collection<Map<String, Object>> convert(ResultSet resultSet) throws SQLException {

		// long startTime = System.currentTimeMillis();

		ResultSet contentrs = resultSet;
		ResultSetMetaData contentrsmd = contentrs.getMetaData();

		Collection<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		while (contentrs.next()) {

			Map<String, Object> row = null;
			row = new HashMap<String, Object>();

			for (int i = 1; i <= contentrsmd.getColumnCount(); i++) {
				row.put(contentrsmd.getColumnName(i), contentrs.getObject(i));
			}

			rows.add(row);
		}

		// String pctx = "convert";
		// this.fnPerformanceTiming(pctx, startTime, state);

		return rows;
	}

	/**
	 * 
	 * @param statment
	 * @return
	 * @throws SQLException
	 */
	protected Collection<Map<String, Object>> runStatement(String statment) throws SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<Map<String, Object>> rsdata = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			stmt = conn.prepareStatement(statment);
			rs = stmt.executeQuery();
			rsdata = this.convert(rs);
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

		return rsdata;
	}

	/**
	 * 
	 * @param createTableSQL
	 * @return
	 * @throws SQLException
	 */
	protected boolean createTable(String createTableSQL) throws SQLException {

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
			throw e;
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
	 * @throws SQLException
	 */
	protected boolean dropTable(String dropTableSQL) throws SQLException {

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
			throw e;
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
	 * @throws SQLException
	 */
	public void createSchema() throws SQLException {

		/*
		 * 
		 */

		this.createTable(
			"CREATE TABLE IF NOT EXISTS " + "document_store" + " ("
					+ " id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,"
					+ " name VARCHAR(255) NOT NULL,"
					+ " description text"
					+ ");"
		);

		this.createTable(
			"CREATE TABLE IF NOT EXISTS " + "document_chunk" + " ("
					+ " document_id UUID REFERENCES document_store(id) NOT NULL,"
					+ " vector_id UUID REFERENCES vector_store(id) NOT NULL"
					+ ");"
		);

		/*
		 * 
		 */

		this.createTable(
			"ALTER TABLE document_chunk ALTER COLUMN document_id SET NOT NULL;"
		);

		this.createTable(
			"ALTER TABLE document_chunk ALTER COLUMN vector_id SET NOT NULL;"
		);

		/*
		 * 
		 */

		this.createTable(
			"ALTER TABLE document_store ADD COLUMN created TIMESTAMPTZ;"
		);

		this.createTable(
			"ALTER TABLE document_store ADD COLUMN modified TIMESTAMPTZ;"
		);

		this.createTable(
			"ALTER TABLE document_store ADD COLUMN timestamp TIMESTAMPTZ;"
		);

		this.createTable(
			"ALTER TABLE document_chunk ADD COLUMN timestamp TIMESTAMPTZ;"
		);

		/*
		 * 
		 */

		this.createTable(
			"ALTER TABLE document_store ADD COLUMN chunks int;"
		);

		this.createTable(
			"ALTER TABLE document_store ADD COLUMN total int;"
		);

		this.createTable(
			"ALTER TABLE document_store ADD COLUMN status VARCHAR(255);"
		);

		this.createTable(
			"ALTER TABLE document_store ADD COLUMN reason text;"
		);

	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void destroySchema() throws SQLException {
		this.dropTable(
			"DROP TABLE IF EXISTS " + "document_store" + ";"
		);
	}

	@Test
	public void testCreateSchema() throws SQLException {

		this.createSchema();

	}

}
