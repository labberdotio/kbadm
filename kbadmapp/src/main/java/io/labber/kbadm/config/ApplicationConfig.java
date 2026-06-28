
//
// Copyright (c) 2019, 2023, 2024, 2025, John Grundback
// All rights reserved.
//

package io.labber.kbadm.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
// import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EntityScan(basePackages = { "io.labber", "io.labber.app", "io.labber.kbadm" })
@ComponentScan(basePackages = { "io.labber", "io.labber.app", "io.labber.kbadm" })
public class ApplicationConfig {

	@Bean
	ConfigLoader configLoader() {
		return new ConfigLoader();
	}

//	@Bean
//	public Driver driver() {
//	    return GraphDatabase.driver("neo4j://<host>:<bolt-port>",
//	            AuthTokens.basic("<username>", "<password>"));
//	}
//
//	@Bean
//	public VectorStore vectorStore(Driver driver, EmbeddingModel embeddingModel) {
//	    return Neo4jVectorStore.builder(driver, embeddingModel)
//	        .databaseName("neo4j")                // Optional: defaults to "neo4j"
//	        .distanceType(Neo4jDistanceType.COSINE) // Optional: defaults to COSINE
//	        .embeddingDimension(1536)                      // Optional: defaults to 1536
//	        .label("Document")                     // Optional: defaults to "Document"
//	        .embeddingProperty("embedding")        // Optional: defaults to "embedding"
//	        .indexName("custom-index")             // Optional: defaults to "spring-ai-document-index"
//	        .initializeSchema(true)                // Optional: defaults to false
//	        .batchingStrategy(new TokenCountBatchingStrategy()) // Optional: defaults to TokenCountBatchingStrategy
//	        .build();
//	}
//
//	// This can be any EmbeddingModel implementation
//	@Bean
//	public EmbeddingModel embeddingModel() {
//	    return new OpenAiEmbeddingModel(new OpenAiApi(System.getenv("OPENAI_API_KEY")));
//	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NonNull CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins("*");
			}
		};
	}

}
