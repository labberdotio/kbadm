//
//// 
//// Copyright (c) 2024, 2025, John Grundback
//// All rights reserved.
//// 
//
//package io.labber.kbadm.impl.pgvector;
//
//import java.util.Collection;
//import java.util.Iterator;
//
//import javax.sql.DataSource;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//
//import io.labber.kbadm.KBException;
//import io.labber.kbadm.model.Chunk;
//import io.labber.kbadm.model.Document;
//import io.labber.kbadm.model.DocumentChunk;
//
///**
// * 
// * @author john
// *
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
//// public class QueryTest2 extends TestCase {
//public class QueryTest2 {
//
//	@Configuration
//	static class ContextConfiguration {
//
//		// Bean definitions go here.
//
//		@Bean
//		public DataSource dataSource() {
//
//			DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//			dataSourceBuilder.driverClassName("org.postgresql.Driver");
//			dataSourceBuilder.url("jdbc:postgresql://10.88.88.197:5432/pgvector_db");
//			dataSourceBuilder.username("postgres");
//			dataSourceBuilder.password("postgres");
//
//			return dataSourceBuilder.build();
//		}
//
//		@Bean
//		public JdbcTemplate jdbcTemplate() {
//			return new JdbcTemplate(this.dataSource());
//		}
//
//	}
//
//	// Bean references go here.
//
//	@Autowired
//	DataSource dataSource;
//
//	@Autowired
//	JdbcTemplate jdbcTemplate;
//
//	/**
//	 * Create the test case
//	 *
//	 * @param testName name of the test case
//	 */
//	// public QueryTest2(String testName) {
//	// 	super(testName);
//	// }
//
//	/**
//	 * @return the suite of tests being tested
//	 */
//	// public static Test suite() {
//	// 	return new TestSuite(QueryTest2.class);
//	// }
//
//	/**
//	 * 
//	 */
//	// public void setUp() {
//	// 	// Configurator.setAllLevels("", Level.ALL); 
//	// }
//
//	// @Before
//	// public void setUp() {
//	// 	Configurator.setAllLevels("", Level.ALL);
//	// }
//
//	@Test
//	public void testDocumentQuery() throws KBException {
//
//		PgVectorStoreDriverImpl driver = new PgVectorStoreDriverImpl(
//			this.jdbcTemplate, 
//			new PgVectorStoreImpl(
//				new PgVectorStoreConfigImpl(), 
//				new PgVectorStoreMetadataImpl()
//			)
//		);
//
//		Collection<Document> list = driver.getDocuments("FreeBSD 12.2 Handbook.pdf");
//		System.out.println(list.size());
//		Iterator<Document> listiter = list.iterator();
//		while(listiter.hasNext()) {
//			Document document = listiter.next();
//			// System.out.println(document);
//			System.out.println(document.getId());
//			System.out.println(document.getName());
//		}
//
//	}
//
//	@Test
//	public void testChunkQuery() throws KBException {
//
//		PgVectorStoreDriverImpl driver = new PgVectorStoreDriverImpl(
//			this.jdbcTemplate, 
//			new PgVectorStoreImpl(
//				new PgVectorStoreConfigImpl(), 
//				new PgVectorStoreMetadataImpl()
//			)
//		);
//
//		Collection<Chunk> list = driver.getChunks("FreeBSD 12.2 Handbook.pdf");
//		System.out.println(list.size());
//		Iterator<Chunk> listiter = list.iterator();
//		while(listiter.hasNext()) {
//			Chunk chunk = listiter.next();
//			// System.out.println(chunk);
//			System.out.println(chunk.getId());
//			// System.out.println(chunk.getMetadata());
//			// System.out.println(chunk.getEmbedding());
//			// System.out.println(chunk.getContent());
//		}
//
//	}
//
//	@Test
//	public void testChunkQuery2() throws KBException {
//
//		PgVectorStoreDriverImpl driver = new PgVectorStoreDriverImpl(
//			this.jdbcTemplate, 
//			new PgVectorStoreImpl(
//				new PgVectorStoreConfigImpl(), 
//				new PgVectorStoreMetadataImpl()
//			)
//		);
//
//		Collection<Chunk> list = driver.getChunks(new Document().withId("2ed17236-7d87-400d-82a2-3078c511b0e5"));
//		System.out.println(list.size());
//		Iterator<Chunk> listiter = list.iterator();
//		while(listiter.hasNext()) {
//			Chunk chunk = listiter.next();
//			// System.out.println(chunk);
//			System.out.println(chunk.getId());
//			// System.out.println(chunk.getMetadata());
//			// System.out.println(chunk.getEmbedding());
//			// System.out.println(chunk.getContent());
//		}
//
//	}
//
//	@Test
//	public void testDocumentChunkQuery() throws KBException {
//
//		PgVectorStoreDriverImpl driver = new PgVectorStoreDriverImpl(
//			this.jdbcTemplate, 
//			new PgVectorStoreImpl(
//				new PgVectorStoreConfigImpl(), 
//				new PgVectorStoreMetadataImpl()
//			)
//		);
//
//		Collection<DocumentChunk> list = driver.getDocumentChunks(new Document().withId("2ed17236-7d87-400d-82a2-3078c511b0e5"));
//		System.out.println(list.size());
//		Iterator<DocumentChunk> listiter = list.iterator();
//		while(listiter.hasNext()) {
//			DocumentChunk chunk = listiter.next();
//			// System.out.println(chunk);
//			System.out.println(chunk.getDocumentId());
//			System.out.println(chunk.getVectorId());
//		}
//
//	}
//
//}
