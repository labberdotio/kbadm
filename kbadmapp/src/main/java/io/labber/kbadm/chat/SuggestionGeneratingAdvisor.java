package io.labber.kbadm.chat;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.prompt.Prompt;

import reactor.core.publisher.Flux;

// public class SuggestionGeneratingAdvisor implements CallAroundAdvisor {
public class SuggestionGeneratingAdvisor implements CallAdvisor, StreamAdvisor {

	private int order = 0;

	@Override
	// public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
	public ChatClientResponse adviseCall(ChatClientRequest advisedRequest, CallAdvisorChain chain) {
		
		// 1. Intercept and examine the user's original input
		// String originalUserText = advisedRequest.userText();
		String originalUserText = advisedRequest.prompt().getUserMessage().getText();
		
		// 2. Generate or inject suggestions before sending the prompt
		String enhancedUserText = originalUserText + "\n\nProvide 3 helpful suggestions related to this query.";
		
		// 3. Create a modified request with your enhanced text
		// AdvisedRequest modifiedRequest = AdvisedRequest.from(advisedRequest)
		// ChatClientRequest modifiedRequest = ChatClientRequest.from(advisedRequest)
		// 		.withUserText(enhancedUserText)
		// 		.build();
		ChatClientRequest modifiedRequest = ChatClientRequest.builder()
				.prompt(Prompt.builder().content(enhancedUserText).build())
				.build();

		// 4. Pass the modified request down the chain
		return chain.nextCall(modifiedRequest);
	}

	@Override
	// public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
	public Flux<ChatClientResponse> adviseStream(ChatClientRequest advisedRequest, StreamAdvisorChain chain) {
		// Implement stream logic similarly if you need to intercept streaming responses
		// return chain.nextCallStream(advisedRequest);
		return chain.nextStream(advisedRequest);
	}
	
	@Override
	public String getName() {
		return "SuggestionGeneratingAdvisor";
	}

	@Override
	public int getOrder() {
		// return 0;
		return this.order;
	}

	public SuggestionGeneratingAdvisor withOrder(int order) {
		this.order = order;
		return this;
	}

}
