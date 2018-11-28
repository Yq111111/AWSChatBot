package com.amazonaws.lambda.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.yelp.YelpHelper;


public class DinningInfoHandler implements RequestHandler<Object, String> {
    final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    final String myQueueUrl = "https://sqs.us-east-1.amazonaws.com/092474612954/KrisDinningSqs";
    final String myDynamoDBTable = "DinningSuggestionKris";
    private final YelpHelper yelpHelper = new YelpHelper();
    
    @Override
    public String handleRequest(Object input, Context context) {
        
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build(); 
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(myDynamoDBTable);

        List<Message> messages = sqs.receiveMessage(myQueueUrl).getMessages();

        for (final Message message : messages) {
            final Map<String, Object> infoMap = new HashMap<String, Object>();

            String[] info = message.getBody().split(",");
            if (info.length == 6) {
                infoMap.put("city", info[0]);
                infoMap.put("cuisine", info[1]);
                infoMap.put("number", info[3]);
                infoMap.put("phone", info[4]);
                infoMap.put("date", info[2]);
                infoMap.put("time", info[5]);
                //get suggestion
                String suggestions = yelpHelper.search(infoMap);
                sendSMSMessage("+1" + info[4], suggestions);
                try {
                    table.putItem(new Item()
                            .withPrimaryKey("messageID", message.getMessageId())
                            .withMap("info", infoMap));
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            final String messageReceiptHandle = message.getReceiptHandle();
            sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));
        }
        

        return "fuck aws";
    }
    
    public void sendSMSMessage(String phoneNumber, String messageContext) {
        AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
        Map<String, MessageAttributeValue> smsAttributes = 
                new HashMap<String, MessageAttributeValue>();
        
        snsClient.publish(new PublishRequest()
                .withMessage(messageContext)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
    }
    

}
