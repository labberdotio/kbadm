//
//// 
//// Copyright (c) 2024, 2025, John Grundback
//// All rights reserved.
//// 
//
//package io.labber.kbadm.impl.pgvector;
//
//import javax.sql.DataSource;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.embedding.EmbeddingModel;
//import org.springframework.ai.ollama.OllamaChatModel;
//import org.springframework.ai.ollama.OllamaEmbeddingModel;
//import org.springframework.ai.ollama.api.OllamaApi;
//import org.springframework.ai.ollama.api.OllamaChatOptions;
//import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;
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
//import io.labber.kbadm.synchronize.DocumentSynchronizer;
//
///**
// * 
// * @author john
// *
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
//// public class SynchronizerTest extends TestCase {
//public class SynchronizerTest {
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
//		@Bean
//		public OllamaApi customOllamaApi() {
//			return OllamaApi.builder().baseUrl("http://10.88.88.180:11434").build();
//		}
//
//		@Bean
//		public ChatClient customOllamaChatClient() {
//			return ChatClient.builder(
//				OllamaChatModel.builder().ollamaApi(
//					customOllamaApi()
//				).defaultOptions(
//					OllamaChatOptions.builder().model(
//						"gpt-oss"
//					).build()
//				).build()
//			).build();
//		}
//
//		@Bean
//		public EmbeddingModel embeddingModel() {
//			return OllamaEmbeddingModel.builder().ollamaApi(
//				customOllamaApi()
//			).defaultOptions(
//				OllamaEmbeddingOptions.builder().model(
//					"nomic-embed-text"
//				).build()
//			).build();
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
//	// public SynchronizerTest(String testName) {
//	// 	super(testName);
//	// }
//
//	/**
//	 * @return the suite of tests being tested
//	 */
//	// public static Test suite() {
//	// 	return new TestSuite(SynchronizerTest.class);
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
//	public PgVectorStoreConfigImpl getConfig() {
//		return new PgVectorStoreConfigImpl();
//	}
//
//	public PgVectorStoreMetadataImpl getMetadata() {
//		return new PgVectorStoreMetadataImpl();
//	}
//
//	public PgVectorStoreImpl getStore() {
//
//		PgVectorStoreImpl store = new PgVectorStoreImpl(
//			this.getConfig(), 
//			this.getMetadata()
//		);
//
//		return store;
//	}
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
//	public DocumentSynchronizer getSynchronizer() {
//
//		DocumentSynchronizer synchronizer = new DocumentSynchronizer(
//			this.getStore(), 
//			this.getDriver()
//		);
//
//		return synchronizer;
//	}
//
//	@Test
//	public void testSynchronizeDocument() throws KBException {
//
//		DocumentSynchronizer synchronizer = this.getSynchronizer();
//
//		String documentName = "FreeBSD 12.2 Handbook.pdf";
//		String documentPath = "/tmp/library/Library/Books/Computing/FreeBSD/" + documentName;
//
//		synchronizer.synchronize(documentPath);
//
//	}
//
//}
