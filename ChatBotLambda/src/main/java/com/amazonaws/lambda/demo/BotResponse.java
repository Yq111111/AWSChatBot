package com.amazonaws.lambda.demo;

public class BotResponse {
	Message[] messages;
	
    public Message[] getMessages() {
        return this.messages;
    }

    public void setMessage(Message[] messages) {
        this.messages = messages;
    }

    public BotResponse(Message[] messages) {
    	this.messages = messages;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	for (Message m : messages) {
    		sb.append(m.toString() + "\n");
    	}
    	return sb.toString();
    }
    
    public BotResponse() {
    }
}
