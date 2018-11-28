package com.amazonaws.lambda.demo;

public class Error {
	int code;
	String message;
	
	public int getCode() {
		return this.code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Error(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public Error() {
		
	}

}
