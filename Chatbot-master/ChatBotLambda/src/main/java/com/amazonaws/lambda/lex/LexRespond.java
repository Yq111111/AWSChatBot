package com.amazonaws.lambda.lex;

public class LexRespond {
    private DialogAction dialogAction;

    public LexRespond(DialogAction dialogAction) {

        this.dialogAction = dialogAction;
    }

    public DialogAction getDialogAction() {
        return dialogAction;
    }
}
