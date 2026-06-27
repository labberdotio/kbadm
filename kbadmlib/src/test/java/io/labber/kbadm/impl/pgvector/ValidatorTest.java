//
//// 
//// Copyright (c) 2024, 2025, John Grundback
//// All rights reserved.
//// 
//
//package io.labber.kbadm.impl.pgvector;
//
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Collection;
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
//import io.labber.kbadm.validator.DocumentValidator;
//
///**
// * 
// * @author john
// *
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
//// public class ValidatorTest extends TestCase {
//public class ValidatorTest {
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
//	// public ValidatorTest(String testName) {
//	// 	super(testName);
//	// }
//
//	/**
//	 * @return the suite of tests being tested
//	 */
//	// public static Test suite() {
//	// 	return new TestSuite(ValidatorTest.class);
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
//	public PgVectorStoreDriverImpl getDriver() {
//
//		PgVectorStoreDriverImpl driver = new PgVectorStoreDriverImpl(
//			this.jdbcTemplate, 
//			new PgVectorStoreImpl(
//				new PgVectorStoreConfigImpl(), 
//				new PgVectorStoreMetadataImpl()
//			)
//		);
//
//		return driver;
//	}
//
//	public Document checkDocument(
//		Document document, 
//		Collection<Chunk> chunks, 
//		Collection<DocumentChunk> documentChunks
//	) throws SQLException, KBException {
//
//		System.out.println(" Chunk check for document: " + document.getName() + ", " + document.getId());
//		System.out.println(chunks.size());
//		System.out.println(documentChunks.size());
//
//		DocumentValidator validator = new DocumentValidator(
//			document, 
//			chunks, 
//			documentChunks
//		);
//
//		// return 
//		Document check = validator.validate();
//
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println(" Chunk check for document: " + document + ": " + check.getStatus().toString() + ", " + check.getReason().toString() + ", " + dateFormat.format(check.getTimestamp()));
//
//		return check;
//	}
//
//	public Collection<Document> checkDocument(
//			String documentName
//		) throws SQLException, KBException {
//
//		PgVectorStoreDriverImpl driver = this.getDriver();
//
//		Collection<Document> checks = new ArrayList<Document>();
//
//		if( documentName == null ) {
//			return checks;
//		}
//
//		Collection<Document> documents = driver.getDocuments(documentName);
//		if( (documents != null) && (documents.size() > 0) ) {
//			for( Document document : documents ) {
//				if( (document != null) && (document.getName() != null) && (document.getId() != null) ) {
//					Document check = this.checkDocument(
//						document, 
//						driver.runChunkStatement(
//							"SELECT * FROM vector_store WHERE metadata->>'source' = '" + document.getName().replace("'", "''") + "';"
//						), 
//						driver.runDocumentChunkStatement(
//							"SELECT * FROM document_chunk WHERE document_id = '" + document.getId().replace("'", "''") + "';"
//						)
//					);
//					checks.add(check);
//				}
//			}
//		}
//
//		return checks;
//	}
//
//	@Test
//	public void testValidateDocument() throws SQLException, KBException {
//
//		PgVectorStoreDriverImpl driver = this.getDriver();
//
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//		String documentName = "FreeBSD 12.2 Handbook.pdf";
//		Collection<Document> checks = this.checkDocument(documentName);
//		if( checks != null ) {
//			for( Document check : checks ) {
//				System.out.println(" Chunk check for document: " + documentName + ": " + check.getStatus().toString() + ", " + check.getReason().toString() + ", " + dateFormat.format(check.getTimestamp()));
//			}
//		}
//
//	}
//
//}
