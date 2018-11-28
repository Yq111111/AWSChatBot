package com.amazonaws.lambda.lex;

import com.amazonaws.lambda.lex.Message;

public class DialogAction {
    private final String type;
    private final String fulfillmentState;
    private final Message message;

    public DialogAction(String type, String fulfillmentState, Message message) {

        this.type = type;
        this.fulfillmentState = fulfillmentState;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getFulfillmentState() {
        return fulfillmentState;
    }

    public Message getMessage() {
        return message;
    }
}
