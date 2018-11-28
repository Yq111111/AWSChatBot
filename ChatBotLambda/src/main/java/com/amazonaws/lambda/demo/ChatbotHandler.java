package com.amazonaws.lambda.demo;

import java.util.ArrayList;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lexruntime.AmazonLexRuntime;
import com.amazonaws.services.lexruntime.AmazonLexRuntimeClientBuilder;
import com.amazonaws.services.lexruntime.model.PostTextRequest;
import com.amazonaws.services.lexruntime.model.PostTextResult;

public class ChatbotHandler implements RequestHandler<BotRequest, BotResponse> {

	// Global variable for the id generator in server, can be replaced by the
	// database auto generatedId in the future.
	int id = 0;
	private AmazonLexRuntime lexClient = AmazonLexRuntimeClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
	private PostTextRequest testRequest = new PostTextRequest();

	public BotResponse handleRequest(BotRequest request, Context context) {
		Message[] messages = request.getMessages();

		// Assume that we can only detect and reply for one message at one time
		Message userInput = messages[0];

		// do not get any message from client, return null and should be detected in the
		// frontend
		if (userInput == null || userInput.unstructured == null || userInput.unstructured.text == null) {
			return null;
		}

		testRequest.setBotName("DiningBot");
		testRequest.setBotAlias("dafeiyang");
		testRequest.setUserId("Tester");
		testRequest.setInputText(userInput.getUnstructured().text);
		PostTextResult textResult = lexClient.postText(testRequest);
		System.out.println(textResult.getIntentName());
		System.out.println(textResult.getMessage());
		System.out.println(textResult.getSlots());
		ArrayList<Message> replyMessages = new ArrayList<>();
		UnstructuredMessage um = new UnstructuredMessage(String.valueOf(id++), 
				textResult.getMessage(), java.time.LocalDate.now().toString());
		Message msg = new Message("autoReply", um);
		replyMessages.add(msg);
		// convert the list into array
		Message[] replyMessageArray = new Message[replyMessages.size()];
		for (int i = 0; i < replyMessages.size(); i++) {
			replyMessageArray[i] = replyMessages.get(i);
		}

		// return the response.
		BotResponse response = new BotResponse(replyMessageArray);
		return response;
		

	}
}
