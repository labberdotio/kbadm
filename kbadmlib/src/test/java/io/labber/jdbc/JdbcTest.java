
// 
// Copyright (c) 2024, 2025, John Grundback
// All rights reserved.
// 

package io.labber.jdbc;

import java.sql.SQLException;

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
// public class JdbcTest extends TestCase {
public class JdbcTest {

	@Configuration
	static class ContextConfiguration {

		// Bean definitions go here.

		@Bean
		public DataSource dataSource() {

			DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
			dataSourceBuilder.driverClassName("org.postgresql.Driver");
			dataSourceBuilder.url("jdbc:postgresql://localhost:5432/your_database_name");
			dataSourceBuilder.username("your_username");
			dataSourceBuilder.password("your_password");

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

	@Test
	public void testSimple() throws SQLException {

		// 

	}

}
