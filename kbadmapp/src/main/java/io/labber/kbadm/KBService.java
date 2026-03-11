//package io.labber.kbadm;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.List;
//
//import javax.sql.DataSource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.embedding.EmbeddingModel;
//import org.springframework.ai.ollama.OllamaChatModel;
//import org.springframework.ai.ollama.OllamaEmbeddingModel;
//import org.springframework.ai.ollama.api.OllamaApi;
//import org.springframework.ai.ollama.api.OllamaChatOptions;
//import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;
//import org.springframework.ai.reader.tika.TikaDocumentReader;
//import org.springframework.ai.transformer.splitter.TokenTextSplitter;
//import org.springframework.ai.vectorstore.SearchRequest;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
//import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType;
//import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KBService {
//
//	private static final Logger logger = LoggerFactory.getLogger(KBService.class);
//
//	// @Autowired
//	// protected ChatClient customOllamaChatClient;
//
//	// @Autowired
//	// protected VectorStore vectorStore;
//
//	// @Value("${app.resource}")
//	// private Resource documentResource;
//
//	// @Bean
//	public DataSource dataSource() {
//
//		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//		dataSourceBuilder.driverClassName("org.postgresql.Driver");
//		dataSourceBuilder.url("jdbc:postgresql://10.88.88.197:5432/pgvector_db");
//		dataSourceBuilder.username("postgres");
//		dataSourceBuilder.password("postgres");
//
//		return dataSourceBuilder.build();
//	}
//
//	// @Bean
//	public JdbcTemplate jdbcTemplate() {
//		return new JdbcTemplate(this.dataSource());
//	}
//
//	// @Bean
//	public OllamaApi ollamaApi() {
//		return OllamaApi.builder().baseUrl("http://10.88.88.180:11434").build();
//	}
//
//	// @Bean
//	public ChatClient chatClient() {
//		OllamaChatModel chatModel = OllamaChatModel.builder().ollamaApi(
//			ollamaApi()
//		).defaultOptions(
//			OllamaChatOptions.builder().model(
//				"gpt-oss:20b" // "gpt-oss"
//			).build()
//		).build();
//		return ChatClient.builder(
//			chatModel
//		).build();
//	}
//
//	// @Bean
//	public EmbeddingModel embeddingModel() {
//		return OllamaEmbeddingModel.builder().ollamaApi(
//			ollamaApi()
//		).defaultOptions(
//			OllamaEmbeddingOptions.builder().model(
//				"nomic-embed-text" // "mxbai-embed-large" // "nomic-embed-text-v2-moe" // "nomic-embed-text"
//			).build()
//		).build();
//	}
//
//	// @Bean
//	public VectorStore vectorStore(
//		String schema, 
//		String table
//	) {
//		// The PgVectorStore uses a default table name 'vector_store'.
//		// You can use a builder to customize schema, table name, distance type, etc.
//		return PgVectorStore.builder(
//			this.jdbcTemplate(), 
//			this.embeddingModel()
//		).indexType(
//			PgIndexType.HNSW
//		).distanceType(
//			PgDistanceType.COSINE_DISTANCE
//		).dimensions(
//			768 // 1024 // 768 // 1024
//		).schemaName(
//			schema // PgVectorStore.DEFAULT_SCHEMA_NAME
//		).vectorTableName(
//			table // "vector_store"
//		).initializeSchema(
//			false // false // true
//		).removeExistingVectorStoreTable(
//			false // false // false
//		).build(); 
//	}
//
//	/**
//	 * 
//	 * @param schema
//	 * @param table
//	 * @param question
//	 * @return
//	 */
//	public Answer askQuestion(
//		String schema, 
//		String table, 
//		Question question
//	) {
//
//		logger.info(
//			"Processing question: {}",
//			question.question()
//		);
//
//		try {
//			return askWithAdvisor(
//				schema, 
//				table, 
//				question
//			);
//		} catch( Exception e ) {
//			logger.error(
//				"Error processing question",
//				e
//			);
//			return new Answer("Sorry, I encountered an error while processing your question.");
//		}
//
//	}
//
//	/**
//	 * 
//	 * @param schema
//	 * @param table
//	 * @param question
//	 * @return
//	 */
//	public Answer askWithAdvisor(
//		String schema, 
//		String table, 
//		Question question
//	) {
//
//		System.out.println(" ASK " + schema);
//		System.out.println(" ASK " + table);
//		System.out.println(" ASK " + question);
//
//		// vectorStore.
//		// SearchRequest.builder().query(question.question()).build();
//		List<Document> relevantDocuments = this.vectorStore(
//			schema, 
//			table
//		).similaritySearch(
//			SearchRequest.builder().query(question.question()).build()
//		); // .withTopK(5));
//		System.out.println(relevantDocuments);
//		for( Document document : relevantDocuments ) {
//			System.out.println(" >> RDOC: " + document.getId());
//		}
//
//		ChatResponse response = this.chatClient().prompt()
//			// .advisors(new QuestionAnswerAdvisor(vectorStore))
//			.advisors(QuestionAnswerAdvisor.builder(this.vectorStore(
//				schema, 
//				table
//			)).build()) // Build and add the advisor
//			.user(question.question()).call().chatResponse();
//
//		if( response != null ) {
//			String answer = response.getResult().getOutput().getText();
//			return new Answer(answer);
//		}
//
//		return new Answer("Sorry, I couldn't find an answer to your question.");
//	}
//
//	/**
//	 * 
//	 * @param createTableSQL
//	 * @return
//	 * @throws KBException
//	 */
//	protected boolean createTable(String createTableSQL) throws KBException {
//
//		/*
//		 * 
//		 */
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		try {
//			conn = this.jdbcTemplate().getDataSource().getConnection();
//			stmt = conn.createStatement();
//			stmt.executeUpdate(createTableSQL);
//			return true;
//		} catch( SQLException e ) {
//			throw new KBException(e);
//			// return false;
//		} finally {
//			if( rs != null ) {
//				try { rs.close(); } catch (SQLException e) { /* log error */ }
//			}
//			if( stmt != null ) {
//				try { stmt.close(); } catch (SQLException e) { /* log error */ }
//			}
//			if( conn != null ) {
//				try { conn.close(); } catch (SQLException e) { /* log error */ }
//			}
//		}
//
//	}
//
//	/**
//	 * 
//	 * @throws KBException
//	 */
//	public void createSchema(
//		String schema, 
//		String table
//	) throws KBException {
//		this.createTable(
//			"CREATE SCHEMA IF NOT EXISTS " + schema + ";"
//		);
//		// this.createTable(
//		// 	"CREATE TABLE IF NOT EXISTS " + table + " ("
//		// 			+ " id SERIAL PRIMARY KEY,"
//		// 			+ " name VARCHAR(255) NOT NULL"
//		// 			+ ");"
//		// );
//	}
//
//	protected String getSchemaName(
//		String schema, 
//		String table
//	) {
//		return schema;
//	}
//
//	protected String getFullyQualifiedTableName(
//		String schema, 
//		String table
//	) {
//		return schema + "." + table;
//	}
//
//	protected String getColumnTypeName() {
//		return "uuid";
//	}
//
//	protected int embeddingDimensions() {
//		return 768; // 1024; // 768; // 1024;
//	}
//
//	protected String getVectorIndexName() {
//		// this.vectorIndexName = this.vectorTableName.equals(DEFAULT_TABLE_NAME) ? DEFAULT_VECTOR_INDEX_NAME
//		// 	: this.vectorTableName + "_index";
//		return PgVectorStore.DEFAULT_VECTOR_INDEX_NAME;
//	}
//
//	protected PgIndexType createIndexMethod() {
//		return PgIndexType.HNSW;
//	}
//
//	protected PgDistanceType getDistanceType() {
//		return PgDistanceType.COSINE_DISTANCE;
//	}
//
//	/**
//	 * 
//	 */
//	public void initSchema(
//		String schema, 
//		String table
//	) {
//
////		String columnTypeName = "uuid";
////		int embeddingDimensions = 1024;
////
////		String vectorIndexName = "spring_ai_vector_index";
////		String fullyQualifiedTableName = schema + "." + table;
//
//		this.jdbcTemplate().execute(String.format("CREATE SCHEMA IF NOT EXISTS %s", this.getSchemaName(schema, table)));
//
//		// Remove existing VectorStoreTable
//		// if (this.removeExistingVectorStoreTable) {
//		// 	this.jdbcTemplate().execute(String.format("DROP TABLE IF EXISTS %s", this.getFullyQualifiedTableName(schema, table)));
//		// }
//
//		this.jdbcTemplate().execute(String.format("""
//				CREATE TABLE IF NOT EXISTS %s (
//					id %s PRIMARY KEY,
//					content text,
//					metadata json,
//					embedding vector(%d)
//				)
//				""", this.getFullyQualifiedTableName(schema, table), this.getColumnTypeName(), this.embeddingDimensions()));
//
//		// if (this.createIndexMethod != PgIndexType.NONE) {
//			this.jdbcTemplate().execute(String.format("""
//					CREATE INDEX IF NOT EXISTS %s ON %s USING %s (embedding %s)
//					""", this.getVectorIndexName(), this.getFullyQualifiedTableName(schema, table), this.createIndexMethod(),
//					this.getDistanceType().index));
//		// }
//
//	}
//
//	/**
//	 * 
//	 * @param schema
//	 * @param table
//	 * @param path
//	 */
//	public void loadDocument(
//		String schema, 
//		String table, 
//		String path
//	) {
//		// File file = new File(path);
//		// if( file.exists() ) {
//			System.out.println(" Loading " + path);
//
//			// TikaDocumentReader documentReader = new TikaDocumentReader(path);
//			TikaDocumentReader documentReader = new TikaDocumentReader(path);
//			List<Document> documents = documentReader.get();
//			// for( Document document : documents ) {
//			// 	logger.info(document.getId());
//			// }
//			System.out.println(" Splitting " + path);
//			// TokenTextSplitter textSplitter = new TokenTextSplitter(
//			// 	500, 
//			// 	100, 
//			// 	5, 
//			// 	1000, 
//			// 	true
//			// );
//			TokenTextSplitter textSplitter = new TokenTextSplitter(
//				800, // TokenTextSplitter.DEFAULT_CHUNK_SIZE, 
//				350, // TokenTextSplitter.MIN_CHUNK_SIZE_CHARS, 
//				5, // TokenTextSplitter.MIN_CHUNK_LENGTH_TO_EMBED, 
//				10000, // TokenTextSplitter.MAX_NUM_CHUNKS, 
//				true // TokenTextSplitter.KEEP_SEPARATOR
//			);
//			// TokenTextSplitter textSplitter = new TokenTextSplitter();
//			List<Document> splitDocuments = textSplitter.apply(documents);
//			// for( Document document : splitDocuments ) {
//			// 	logger.info(document.getId());
//			// }
//			System.out.println(" Adding " + path);
//			this.vectorStore(
//				schema, 
//				table
//			).add(splitDocuments);
//
//			// // vectorStore.
//			// // SearchRequest.builder().query(question.question()).build();
//			// List<Document> relevantDocuments = this.vectorStore().similaritySearch(
//			// 	SearchRequest.builder().query(question.question()).build()
//			// ); // .withTopK(5));
//			// for( Document document : relevantDocuments ) {
//			// 	logger.info(document.getId());
//			// }
//
//			System.out.println(" Done " + path);
//		// }
//
//	}
//
//	/**
//	 * 
//	 * @param schema
//	 * @param table
//	 * @param paths
//	 */
//	public void loadDocuments(
//		String schema, 
//		String table, 
//		String[] paths
//	) {
//
//		for( String path : paths ) {
//			if( path != null ) {
//				this.loadDocument(
//					schema, 
//					table, 
//					path
//				);
//			}
//		}
//
//	}
//
//	/**
//	 * 
//	 * @param schema
//	 * @param table
//	 */
//	public void loadDocuments(
//		String schema, 
//		String table
//	) {
//
//		String[] books = {
//			"Computing/FreeBSD/FreeBSD 12.2 Handbook.pdf", 
//			"Computing/FreeBSD/FreeBSD 14.2 Handbook.pdf"
//		};
//
//		try {
//
//			// this.createSchema(
//			this.initSchema(
//				schema, 
//				table
//			);
//
//			for( String book : books ) {
//				if( book != null ) {
//					this.loadDocument(
//						schema, 
//						table, 
//						"Books/" + book
//					);
//				}
//			}
//
//		} catch( Exception e ) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//}
