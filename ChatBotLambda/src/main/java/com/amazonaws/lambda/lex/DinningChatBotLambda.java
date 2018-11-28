package com.amazonaws.lambda.lex;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.lambda.lex.Message;

import java.util.Map;

public class DinningChatBotLambda implements RequestHandler<Map<String, Object>, Object> {
	private final static String SQS_URL = "https://sqs.us-east-1.amazonaws.com/953325521927/RestaurantQueue";
;	private final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    @Override
    public Object handleRequest(Map<String, Object> input, Context context) {
        Map<String, Object> currentIntent = (Map<String, Object>) input.get("currentIntent");
        String intentName = (String)currentIntent.get("name");
        String content = "";
        if (intentName.equals("GreetingIntent")) {
        	content = "Hi there, how can I help?";
        }
        else if (intentName.equals("ThankYouIntent")) {
        	content = "Thank you!";
        }
        else if (intentName.equals("DiningSuggestionsIntent")){
        	content = "You are all set!";
        	//get msg
        	Map<String, Object> slots = (Map<String, Object>) currentIntent.get("slots");
            StringBuilder sb = new StringBuilder();
            sb.append((String) slots.get("city")).append(",");
            sb.append((String) slots.get("cuisine")).append(",");
            sb.append((String) slots.get("date")).append(",");
            sb.append((String) slots.get("number")).append(",");
            sb.append((String) slots.get("phone")).append(",");
            sb.append((String) slots.get("time"));
            sqs.sendMessage(new SendMessageRequest(SQS_URL, sb.toString()));
        }
        Message message = new Message("PlainText", content);
        DialogAction dialogAction = new DialogAction("Close", "Fulfilled", message);
        return new LexRespond(dialogAction);
    }
}
