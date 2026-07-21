package io.labber.kbadm.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.ai.chat.messages.Message;
//import org.springframework.util.Assert;

/**
 * 
 */
public class ChatMemoryRepository implements org.springframework.ai.chat.memory.ChatMemoryRepository {

	Map<String, List<Message>> chatMemoryStore = new ConcurrentHashMap<>();

	@Override
	public List<String> findConversationIds() {
		return new ArrayList<>(this.chatMemoryStore.keySet());
	}

	@Override
	public List<Message> findByConversationId(String conversationId) {
		// Assert.hasText(conversationId, "conversationId cannot be null or empty");
		List<Message> messages = this.chatMemoryStore.get(conversationId);
		return messages != null ? new ArrayList<>(messages) : List.of();
	}

	@Override
	public void saveAll(String conversationId, List<Message> messages) {
		// Assert.hasText(conversationId, "conversationId cannot be null or empty");
		// Assert.notNull(messages, "messages cannot be null");
		// Assert.noNullElements(messages, "messages cannot contain null elements");
		this.chatMemoryStore.put(conversationId, messages);
	}

	@Override
	public void deleteByConversationId(String conversationId) {
		// Assert.hasText(conversationId, "conversationId cannot be null or empty");
		this.chatMemoryStore.remove(conversationId);
	}

}
