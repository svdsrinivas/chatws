package com.mia.ws.model;

import java.sql.Timestamp;

public class OutputMessage {

	private String from;
    private String text;
    private Timestamp timestamp;
    
	public OutputMessage(String from, String text, Timestamp timestamp2) {
		super();
		this.from = from;
		this.text = text;
		this.timestamp = timestamp2;
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
    
}
