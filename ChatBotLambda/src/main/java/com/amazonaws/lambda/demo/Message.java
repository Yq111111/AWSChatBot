package com.amazonaws.lambda.demo;

public class Message {
	String type;
	UnstructuredMessage unstructured;
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public UnstructuredMessage getUnstructured() {
		return this.unstructured;
	}
	
	public void setUnstructured(UnstructuredMessage message) {
		this.unstructured = message;
	}
	
    @Override
    public String toString() {
    	return this.type + "," + this.unstructured.toString();
    }
	
	public Message(String type, UnstructuredMessage um) {
		this.type = type;
		this.unstructured = um;
	}
	
	public Message() {
		
	}

}
