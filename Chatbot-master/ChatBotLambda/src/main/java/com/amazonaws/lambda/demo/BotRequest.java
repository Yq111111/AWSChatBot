package com.amazonaws.lambda.demo;

public class BotRequest {
	Message[] messages;
	
    public Message[] getMessages() {
        return this.messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }

    public BotRequest() {
    }
}
