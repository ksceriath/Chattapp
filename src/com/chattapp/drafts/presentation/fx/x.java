package com.chattapp.drafts.presentation.fx;

import java.io.IOException;
import java.io.InputStream;

/*
 * Exception performing whole class analysis ignored.
 */
class FXMLControllerX implements Runnable {
    private final /* synthetic */ InputStream val$is;

    FXMLControllerX(InputStream inputStream) {
        this.val$is = inputStream;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            while ((i = this.val$is.read()) != -1) {
                com.chattapp.drafts.presentation.fx.FXMLController.access$0((com.chattapp.drafts.presentation.fx.FXMLController)FXMLControllerX.this).appendText(String.valueOf(i));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}