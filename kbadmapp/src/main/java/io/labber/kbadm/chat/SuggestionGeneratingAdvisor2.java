package io.labber.kbadm.chat;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;

public class SuggestionGeneratingAdvisor2 implements BaseAdvisor {

	private static final String DEFAULT_SUGGESTIONS_ADVISE_TEMPLATE = """
			{suggestions_input_query}
			
			Provide 3 helpful suggestions related to this query.
			""";

	private final String suggestionAdviseTemplate;

	private int order = 0;

	public SuggestionGeneratingAdvisor2() {
		this(DEFAULT_SUGGESTIONS_ADVISE_TEMPLATE);
	}

	public SuggestionGeneratingAdvisor2(String suggestionAdviseTemplate) {
		this.suggestionAdviseTemplate = suggestionAdviseTemplate;
	}

	@Override
	public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
		String augmentedUserText = PromptTemplate.builder()
			.template(this.suggestionAdviseTemplate)
			.variables(Map.of("suggestions_input_query", chatClientRequest.prompt().getUserMessage().getText()))
			.build()
			.render();

		return chatClientRequest.mutate()
			.prompt(chatClientRequest.prompt().augmentUserMessage(augmentedUserText))
			.build();
	}

	@Override
	public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
		return chatClientResponse;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	public SuggestionGeneratingAdvisor2 withOrder(int order) {
		this.order = order;
		return this;
	}

}
