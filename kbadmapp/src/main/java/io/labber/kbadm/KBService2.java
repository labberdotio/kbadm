package io.labber.kbadm;

import java.io.IOException;
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

import io.labber.kbadm.config.ConfigLoader;
import io.labber.kbadm.impl.pgvector.PgVectorStoreConfigLoader;

@Service
public class KBService2 {

	private static final Logger logger = LoggerFactory.getLogger(KBService2.class);

	// @Autowired
	// protected ChatClient customOllamaChatClient;

	// @Autowired
	// protected VectorStore vectorStore;

	// @Value("${app.resource}")
	// private Resource documentResource;

	@Autowired
	protected ConfigLoader configLoader;

	/**
	 * 
	 * @param kb
	 * @param question
	 * @return
	 * @throws IOException 
	 */
	public Answer askQuestion(
		String kb, 
		Question question
	) throws IOException {

		PgVectorStoreConfigLoader loader = new PgVectorStoreConfigLoader(
			kb, 
			configLoader.loadConfig()
		);

		logger.info(
			"Processing question: {}",
			question.question()
		);

		try {
			return askWithAdvisor(
				kb, 
				question
			);
		} catch( Exception e ) {
			logger.error(
				"Error processing question",
				e
			);
			return new Answer("Sorry, I encountered an error while processing your question.");
		}

	}

	/**
	 * 
	 * @param kb
	 * @param question
	 * @return
	 * @throws IOException 
	 */
	public Answer askWithAdvisor(
		String kb, 
		Question question
	) throws IOException {

		PgVectorStoreConfigLoader loader = new PgVectorStoreConfigLoader(
			kb, 
			configLoader.loadConfig()
		);

		// System.out.println(" ASK " + schema);
		// System.out.println(" ASK " + table);
		// System.out.println(" ASK " + question);

		// vectorStore.
		// SearchRequest.builder().query(question.question()).build();
		List<Document> relevantDocuments = loader.vectorStore().similaritySearch(
			SearchRequest.builder().query(question.question()).build()
		); // .withTopK(5));
		System.out.println(relevantDocuments);
		for( Document document : relevantDocuments ) {
			System.out.println(" >> RDOC: " + document.getId());
		}

		ChatResponse response = loader.chatClient().prompt()
			// .advisors(new QuestionAnswerAdvisor(vectorStore))
			.advisors(QuestionAnswerAdvisor.builder(loader.vectorStore()).build()) // Build and add the advisor
			.user(question.question()).call().chatResponse();

		if( response != null ) {
			String answer = response.getResult().getOutput().getText();
			return new Answer(answer);
		}

		return new Answer("Sorry, I couldn't find an answer to your question.");
	}

	/**
	 * 
	 * @param kb
	 * @param path
	 * @throws IOException 
	 */
	public void loadDocument(
		String kb, 
		String path
	) throws IOException {

		PgVectorStoreConfigLoader loader = new PgVectorStoreConfigLoader(
			kb, 
			configLoader.loadConfig()
		);

		// File file = new File(path);
		// if( file.exists() ) {
			System.out.println(" Loading " + path);

			// TikaDocumentReader documentReader = new TikaDocumentReader(path);
			TikaDocumentReader documentReader = new TikaDocumentReader(path);
			List<Document> documents = documentReader.get();
			// for( Document document : documents ) {
			// 	logger.info(document.getId());
			// }
			System.out.println(" Splitting " + path);
			// TokenTextSplitter textSplitter = new TokenTextSplitter(
			// 	500, 
			// 	100, 
			// 	5, 
			// 	1000, 
			// 	true
			// );
			TokenTextSplitter textSplitter = new TokenTextSplitter(
				800, // TokenTextSplitter.DEFAULT_CHUNK_SIZE, 
				350, // TokenTextSplitter.MIN_CHUNK_SIZE_CHARS, 
				5, // TokenTextSplitter.MIN_CHUNK_LENGTH_TO_EMBED, 
				10000, // TokenTextSplitter.MAX_NUM_CHUNKS, 
				true // TokenTextSplitter.KEEP_SEPARATOR
			);
			// TokenTextSplitter textSplitter = new TokenTextSplitter();
			List<Document> splitDocuments = textSplitter.apply(documents);
			// for( Document document : splitDocuments ) {
			// 	logger.info(document.getId());
			// }
			System.out.println(" Adding " + path);
			loader.vectorStore().add(splitDocuments);

			// // vectorStore.
			// // SearchRequest.builder().query(question.question()).build();
			// List<Document> relevantDocuments = this.vectorStore().similaritySearch(
			// 	SearchRequest.builder().query(question.question()).build()
			// ); // .withTopK(5));
			// for( Document document : relevantDocuments ) {
			// 	logger.info(document.getId());
			// }

			System.out.println(" Done " + path);
		// }

	}

	/**
	 * 
	 * @param kb
	 * @param paths
	 * @throws IOException 
	 */
	public void loadDocuments(
		String kb, 
		String[] paths
	) throws IOException {

		// PgVectorStoreConfigLoader loader = new PgVectorStoreConfigLoader(
		// 	kb, 
		// 	configLoader.loadConfig()
		// );

		for( String path : paths ) {
			if( path != null ) {
				this.loadDocument(
					kb, 
					path
				);
			}
		}

	}

	/**
	 * 
	 * @param kb
	 */
	public void loadDocuments(
		String kb
	) {

		// PgVectorStoreConfigLoader loader = new PgVectorStoreConfigLoader(
		// 	kb, 
		// 	configLoader.loadConfig()
		// );

		String[] books = {
			"Computing/FreeBSD/FreeBSD 12.2 Handbook.pdf", 
			"Computing/FreeBSD/FreeBSD 14.2 Handbook.pdf"
		};

		try {

			// this.createSchema(
			// this.initSchema(
			// 	kb
			// );

			for( String book : books ) {
				if( book != null ) {
					this.loadDocument(
						kb, 
						"Books/" + book
					);
				}
			}

		} catch( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
