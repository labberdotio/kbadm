
// 
// Copyright (c) 2024, 2025, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.google.gson.Gson;

/**
 * 
 * @author john
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
// public class ConfigTest extends TestCase {
public class ConfigTest {

	@Configuration
	static class ContextConfiguration {

		// Bean definitions go here.

		

	}

	// Bean references go here.

	@Autowired
	private ResourceLoader resourceLoader;

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	// public ConfigTest(String testName) {
	// 	super(testName);
	// }

	/**
	 * @return the suite of tests being tested
	 */
	// public static Test suite() {
	// 	return new TestSuite(ConfigTest.class);
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
	public void testReadConfig() throws IOException {

		Config config = new Gson().fromJson(
			resourceLoader.getResource(
				"classpath:" + "config.json"
			).getContentAsString(
				Charset.defaultCharset()
			),
			Config.class
		);

		System.out.println(config);
		System.out.println(config.getEndpoints());

	}

}
