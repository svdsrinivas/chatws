package com.mia.ws.customimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import com.mia.ws.config.AppDefaults;
import com.mia.ws.service.ChatService;

public class ChatInterceptor extends ChannelInterceptorAdapter {

	@Autowired
	private ChatService chatService;

	@Autowired
	public SimpMessagingTemplate messagingTemplate;

	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
		
		// ignore non-STOMP messages like heartbeat messages
		if (sha.getCommand() == null) {
			return;
		}
		
		String sessionId = sha.getSessionId();
		String userId = null;
		List<String> userIdL = sha.getNativeHeader("userId");

		if (userIdL != null && userIdL.size() > 0) {
			userId = userIdL.get(0);
			System.out.println("UserID is " + userId);
		}

		switch (sha.getCommand()) {
			case CONNECT:
				System.out.println("STOMP Connect [sessionId: " + sessionId + "]");
				if (userId != null && sessionId != null)
					handleUserConnection(userId, sessionId);
				else {
					System.out.println("NULL");
				}
				break;
			case CONNECTED:
				System.out.println("STOMP Connected [sessionId: " + sessionId + "]");
				break;
			case DISCONNECT:
				System.out.println("STOMP Disconnect [sessionId: " + sessionId + "]");
				handleUserDisconnection(sessionId);
				break;
			default:
				System.out.println(sha.getCommand());
				break;
		}
	}

	private void handleUserConnection(String userId, String sessionId) {

		System.out.println("calling subscribeUserToChannels");

		String[] channels = new String[] { AppDefaults.REDIS_CHAT_CHANNEL, userId };

		chatService.subscribeUserToChannels(channels, sessionId, AppDefaults.CHAT_USER_SUBSCRIPTION_QUEUE,
				messagingTemplate);

	}

	private void handleUserDisconnection(String sessionId) {
		chatService.unsubscribeSession(sessionId);
	}

}
