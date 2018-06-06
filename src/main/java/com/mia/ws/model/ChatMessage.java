package com.mia.ws.model;

public class ChatMessage {

	private String from;
	private String[] toChannels;
	private String message;
	
	public ChatMessage() {
		super();
	}
	
	public ChatMessage(String from, String[] toChannels, String message) {
		super();
		this.from = from;
		this.toChannels = toChannels;
		this.message = message;
	}
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String[] getToChannels() {
		return toChannels;
	}
	public void setToChannel(String[] toChannels) {
		this.toChannels = toChannels;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
