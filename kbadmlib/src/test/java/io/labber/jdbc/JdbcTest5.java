
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import io.labber.kbadm.model.Document;
import io.labber.kbadm.model.rowmapper.DocumentRowMapper;

/**
 * 
 * @author john
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
// public class JdbcTest5 extends TestCase {
public class JdbcTest5 {

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

	// @Test
	public void testSimple() throws SQLException {

		try {

			PreparedStatement ps = jdbcTemplate
				.getDataSource()
				.getConnection()
				.prepareStatement("SELECT * FROM document_store LIMIT 100;");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString(1));
			}
			rs.close();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// @Test
	public void testSimple2() throws SQLException {

		Collection<Map<String, Object>> rsdata = this.runStatement("SELECT * FROM document_store LIMIT 100;");
		Iterator<Map<String, Object>> rsiter = rsdata.iterator();
		while(rsiter.hasNext()) {
			System.out.println(
				rsiter.next().keySet()
			);
		}

	}

	// @Test
	public void testSimple3() throws SQLException {

		Collection<Document> list = this.runDocumentStatement("SELECT * FROM document_store LIMIT 100;");
		Iterator<Document> listiter = list.iterator();
		while(listiter.hasNext()) {
			Document document = listiter.next();
			System.out.println(document);
			System.out.println(document.getId());
			System.out.println(document.getName());
		}

	}

	@Test
	public void testSimple4() throws SQLException {

		Collection<Document> list = this.runDocumentStatement("SELECT * FROM document_store WHERE name = 'FreeBSD 12.2 Handbook.pdf' LIMIT 100;");
		Iterator<Document> listiter = list.iterator();
		while(listiter.hasNext()) {
			Document document = listiter.next();
			System.out.println(document);
			System.out.println(document.getId());
			System.out.println(document.getName());
		}

	}

}
