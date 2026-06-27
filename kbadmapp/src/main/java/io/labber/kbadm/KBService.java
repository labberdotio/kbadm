package io.labber.kbadm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.labber.kbadm.config.Config;
import io.labber.kbadm.config.ConfigLoader;
import io.labber.kbadm.impl.neo4j.Neo4jStoreConfigLoader;
// import io.labber.kbadm.impl.pgvector.PgVectorStoreConfigLoader;

@Service
public class KBService {

	private static final Logger logger = LoggerFactory.getLogger(KBService.class);

	// @Autowired
	// protected ChatClient customOllamaChatClient;

	// @Autowired
	// protected VectorStore vectorStore;

	// @Value("${app.resource}")
	// private Resource documentResource;

	@Autowired
	protected ConfigLoader configLoader;

	public Answer findRefs(
		String kb, 
		Question question
	) throws Exception {

		Config config = null;
		// PgVectorStoreConfigLoader loader = null;
		Neo4jStoreConfigLoader loader = null;

		try {

			config = configLoader.loadConfig();

			// loader = new PgVectorStoreConfigLoader(
			loader = new Neo4jStoreConfigLoader(
				kb, 
				config
			);
	
			// vectorStore.
			// SearchRequest.builder().query(question.question()).build();
			List<Document> relevantDocuments = loader.driver().vectorStore().similaritySearch(
				SearchRequest.builder().query(question.question()).build()
			); // .withTopK(5));
			System.out.println(relevantDocuments);
			for( Document document : relevantDocuments ) {
				System.out.println(" >> RDOC: " + document.getId());
			}
	
			return new Answer(".");

		} catch( Exception e ) {
			throw e;
		} finally {
			if( loader != null ) {
				loader.close();
			}
		}

	}

	/**
	 * 
	 * @param kb
	 * @param question
	 * @return
	 * @throws Exception 
	 */
	public Answer runPrompt(
		String kb, 
		Question question
	) throws Exception {

		Config config = null;
		// PgVectorStoreConfigLoader loader = null;
		Neo4jStoreConfigLoader loader = null;

		try {

			config = configLoader.loadConfig();

			// loader = new PgVectorStoreConfigLoader(
			loader = new Neo4jStoreConfigLoader(
				kb, 
				config
			);

			ChatResponse response = loader.driver().chatClient().prompt()
				.advisors(QuestionAnswerAdvisor.builder(loader.driver().vectorStore()).build())
				.user(question.question()).call().chatResponse();

			if( response != null ) {
				String answer = response.getResult().getOutput().getText();
				return new Answer(answer);
			}

			return new Answer("Sorry, I couldn't find an answer to your question.");

		} catch( Exception e ) {
			throw e;
		} finally {
			if( loader != null ) {
				loader.close();
			}
		}

	}

	/**
	 * 
	 * @param kb
	 * @throws Exception
	 */
	public void loadDocuments(
		String kb
	) throws Exception {

		Config config = null;
		// PgVectorStoreConfigLoader loader = null;
		Neo4jStoreConfigLoader loader = null;

		try {

			config = configLoader.loadConfig();

			// loader = new PgVectorStoreConfigLoader(
			loader = new Neo4jStoreConfigLoader(
				kb, 
				config
			);

			String[] books = {
				"Computing/FreeBSD/FreeBSD 12.2 Handbook.pdf", 
				"Computing/FreeBSD/FreeBSD 14.2 Handbook.pdf"
			};

			for( String book : books ) {
				if( book != null ) {

					// TikaDocumentReader documentReader = new TikaDocumentReader("Books/" + book);
					TikaDocumentReader documentReader = new TikaDocumentReader("Books/" + book);
					List<Document> documents = documentReader.get();

					TokenTextSplitter textSplitter = new TokenTextSplitter(
						800, 
						350, 
						5, 
						10000, 
						true
					);

					// TokenTextSplitter textSplitter = new TokenTextSplitter();
					List<Document> splitDocuments = textSplitter.apply(documents);

					loader.driver().vectorStore().add(splitDocuments);

				}
			}

		} catch( Exception e ) {
			throw e;
		} finally {
			if( loader != null ) {
				loader.close();
			}
		}

	}

}
