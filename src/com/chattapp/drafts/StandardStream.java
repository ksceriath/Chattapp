/*
 * Decompiled with CFR 0_114.
 */
package com.chattapp.drafts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class StandardStream
implements Runnable {
    private InputStream inputStream;
    private OutputStream outputStream;

    public StandardStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.outputStream = System.out;
    }

    public StandardStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.inputStream = System.in;
    }

    @Override
    public void run() {
        try {
            int ch = this.inputStream.read();
            while (ch != -1) {
                this.outputStream.write(ch);
                this.outputStream.flush();
                ch = this.inputStream.read();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}