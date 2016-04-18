/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.TextArea
 */
package com.chattapp.drafts;

import java.io.IOException;
import java.io.InputStream;
import javafx.scene.control.TextArea;

public class TextAreaStream
implements Runnable {
    private InputStream inputStream;
    private TextArea textArea;

    public TextAreaStream(InputStream inputStream, TextArea textArea) {
        this.inputStream = inputStream;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        try {
            int i = this.inputStream.read();
            this.textArea.appendText(String.valueOf(i));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}