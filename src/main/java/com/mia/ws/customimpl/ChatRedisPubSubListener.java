package com.mia.ws.customimpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import io.lettuce.core.pubsub.RedisPubSubListener;

public class ChatRedisPubSubListener implements RedisPubSubListener<String, String> {
	
	String sessionId, destination;
	SimpMessageHeaderAccessor headerAccessor;
	SimpMessagingTemplate messagingTemplate;
	Set<String> channelsSubscribedTo;
	
	public ChatRedisPubSubListener(String sessionId, String destination, 
			SimpMessagingTemplate messagingTemplate, String[] channels){
		
		super();
		
		this.sessionId = sessionId;
		this.destination = destination;
		this.messagingTemplate = messagingTemplate;
		
		this.headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		this.headerAccessor.setSessionId(sessionId);
		this.headerAccessor.setLeaveMutable(Boolean.TRUE);
		
		channelsSubscribedTo = new HashSet<String>(Arrays.asList(channels));
	}
	
	public void message(String channel, String message) {
		
		if( this.channelsSubscribedTo.contains(channel) )
			this.messagingTemplate.convertAndSendToUser(this.sessionId, this.destination, message, this.headerAccessor.getMessageHeaders());
		
	}
	public void message(String pattern, String channel, String message) {
		// TODO Auto-generated method stub
		
	}
	public void subscribed(String channel, long count) {
		// TODO Auto-generated method stub
		
	}
	public void psubscribed(String pattern, long count) {
		// TODO Auto-generated method stub
		
	}
	public void unsubscribed(String channel, long count) {
		// TODO Auto-generated method stub
		
	}
	public void punsubscribed(String pattern, long count) {
		// TODO Auto-generated method stub
		
	}
	
	

}
