package com.amazonaws.lambda.demo;

public class UnstructuredMessage {
	String id;
    String text;
    String timestamp;
    
    public String getId() {
    	return this.id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }
    
    public String getText() {
    	return this.text;
    }
    
    public void setText(String text) {
    	this.text = text;
    }
    
    public String getTimestamp() {
    	return this.timestamp;
    }
    
    public void setTimestamp(String timestamp) {
    	this.timestamp = timestamp;
    }
   
    @Override
    public String toString() {
    	return this.id + "," + this.text + "," + this.timestamp;
    }

    public UnstructuredMessage(String id, String text, String timestamp) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
    }

    public UnstructuredMessage() {
    }
}
