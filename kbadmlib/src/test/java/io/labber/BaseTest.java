
// 
// Copyright (c) 2024, 2025, John Grundback
// All rights reserved.
// 

package io.labber;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
public abstract class BaseTest {

	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(BaseTest.class);

	protected String dbHost = null;
	protected String dbPort = null;
	protected String dbUser = null;
	protected String dbPass = null;
	protected String dbName = null;
	protected String dbType = ""; // "";

	/**
	 * 
	 * @param dbHost
	 * @param dbPort
	 * @param dbUser
	 * @param dbPass
	 * @param dbName
	 */
	public BaseTest(
		String dbHost, 
		String dbPort, 
		String dbUser, 
		String dbPass, 
		String dbName
	) {
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
		this.dbName = dbName;
		this.dbType = ""; // "";
	}

	/**
	 * 
	 * @param dbHost
	 * @param dbPort
	 * @param dbUser
	 * @param dbPass
	 * @param dbName
	 * @param dbType
	 */
	public BaseTest(
		String dbHost, 
		String dbPort, 
		String dbUser, 
		String dbPass, 
		String dbName, 
		String dbType
	) {
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
		this.dbName = dbName;
		this.dbType = dbType;
	}

	@Configuration
	@EntityScan(basePackages = { "io.labber", "io.labber.web" })
	@ComponentScan(basePackages = { "io.labber", "io.labber.web" })
	// static class ContextConfiguration {
	static class ContextConfiguration {

		@SuppressWarnings("unused")
		private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
				.getLogger(ContextConfiguration.class);

		protected String dbHost = "";
		protected String dbPort = "";
		protected String dbUser = "";
		protected String dbPass = "";
		protected String dbName = "";
		protected String dbType = ""; // "";

		// Bean definitions go here.

	}

	// Bean references go here.

	/**
	 * 
	 */
	@Before
	public void setUp() {
		/*
		 * ClassCast class org.apache.logging.slf4j.SLF4JLoggerContext cannot be cast to class 
		 * org.apache.logging.log4j.core.LoggerContext (org.apache.logging.slf4j.SLF4JLoggerContext 
		 * and org.apache.logging.log4j.core.LoggerContext ...
		 */
		// Configurator.setAllLevels("", Level.ALL);
	}

}
