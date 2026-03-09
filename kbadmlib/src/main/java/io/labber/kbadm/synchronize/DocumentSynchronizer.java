
// 
// Copyright (c) 2024, 2025, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.synchronize;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// import org.springframework.ai.document.Document;
// import org.springframework.ai.reader.tika.TikaDocumentReader;
// import org.springframework.ai.transformer.splitter.TokenTextSplitter;
// import org.springframework.core.io.FileSystemResource;

import io.labber.kbadm.KBException;
import io.labber.kbadm.driver.IDriver;
import io.labber.kbadm.model.Chunk;
import io.labber.kbadm.model.Document;
import io.labber.kbadm.model.DocumentChunk;
import io.labber.kbadm.store.IStore;
import io.labber.kbadm.validator.DocumentValidator;

/**
 * 
 * @author john
 *
 */
public class DocumentSynchronizer implements IDocumentSynchronizer {

	int chunkSize;
	int minChunkSizeChars;
	int minChunkLengthToEmbed;
	int maxNumChunks;
	boolean keepSeparator;

	// @Autowired
	// protected VectorStore vectorStore;

	IStore store = null;
	IDriver driver = null;

	/**
	 * 
	 * @param store
	 * @param driver
	 */
	public DocumentSynchronizer(
		IStore store, 
		IDriver driver
	) {
		this.store = store;
		this.driver = driver;
	}

	/**
	 * 
	 * @param file
	 * @throws KBException
	 */
	public void synchronize(
		File file
	) throws KBException {

		if( file == null ) {
			throw new KBException("File does not exist.");
		}

		if( !file.exists() ) {
			throw new KBException("File " + file.getAbsolutePath() + " does not exist.");
		}

		if( !file.isFile() ) {
			throw new KBException("File " + file.getAbsolutePath() + " is not a file.");
		}

		if( !file.canRead() ) {
			throw new KBException("File " + file.getAbsolutePath() + " is not readable.");
		}

		String document = file.getName();
		System.out.println(" !! ");
		System.out.println(document);

		Collection<Document> documents = this.check(document);
		System.out.println(documents);

		for( Document cdocument : documents ) {
			System.out.println(" Chunk check for document: " + document + ": " + cdocument.getStatus().toString() + ", " + cdocument.getReason().toString());
		}

//		List<Document> documents = new TikaDocumentReader(new FileSystemResource(file)).get();
//		for( Document largeDocument : documents ) {
//			TokenTextSplitter textSplitter = new TokenTextSplitter();
//			if( this.chunkSize > 0 ) {
//				// TokenTextSplitter
//				textSplitter = new TokenTextSplitter(
//					this.chunkSize,
//					this.minChunkSizeChars,
//					this.minChunkLengthToEmbed,
//					this.maxNumChunks,
//					this.keepSeparator
//				);
//			}
//			// else {
//			// TokenTextSplitter textSplitter = new TokenTextSplitter();
//			// }
//			this.driver.vectorStore().accept(
//				textSplitter.split(
//					largeDocument
//				)
//			);
//		}

	}

	/**
	 * 
	 * @param file
	 * @throws KBException
	 */
	public void synchronize(
		String file
	) throws KBException {

		if( file == null ) {
			throw new KBException("File does not exist.");
		}

		this.synchronize(new File(file));
	}

	/**
	 * 
	 * @param files
	 * @throws KBException
	 */
	public void synchronize(
		List<File> files
	) throws KBException {

		if( files == null ) {
			throw new KBException("Files does not exist.");
		}

		for( File file : files ) {
			this.synchronize(file);
		}
	}

	/**
	 * 
	 * @param files
	 * @throws KBException
	 */
	public void synchronize(
		String[] files
	) throws KBException {

		if( files == null ) {
			throw new KBException("Files does not exist.");
		}

		for( String file : files ) {
			this.synchronize(new File(file));
		}
	}

	/**
	 * 
	 * @return
	 */
	protected IDriver getDriver() {
		return this.driver;
	}

	/**
	 * 
	 * @param document
	 * @param chunks
	 * @param documentChunks
	 * @return
	 * @throws KBException
	 */
	protected Document check(
		Document document, 
		Collection<Chunk> chunks, 
		Collection<DocumentChunk> documentChunks
	) throws KBException {
		return new DocumentValidator(
			document, 
			chunks, 
			documentChunks
		).validate();
	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws KBException
	 */
	protected Collection<Document> check(
		String document
	) throws KBException {

		IDriver driver = this.getDriver();

		Collection<Document> checks = new ArrayList<Document>();

		if( document == null ) {
			return checks;
		}

		Collection<Document> documents = driver.getDocuments(document);
		if( (documents != null) && (documents.size() > 0) ) {
			for( Document cdocument : documents ) {
				if( (document != null) && (cdocument.getName() != null) && (cdocument.getId() != null) ) {
					Document check = this.check(
						cdocument, 
						driver.getChunks(cdocument), 
						driver.getDocumentChunks(cdocument)
					);
					checks.add(check);
				}
			}
		}

		return checks;
	}

}
