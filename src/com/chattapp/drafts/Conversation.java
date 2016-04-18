/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.chattapp.drafts.StandardStream
 */
package com.chattapp.drafts;

import com.chattapp.drafts.StandardStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Conversation
implements Runnable {
    private Socket socket = null;

    Conversation(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Thread t2 = new Thread((Runnable)new StandardStream(this.socket.getOutputStream()));
            t2.start();
            try {
                t2.join();
                this.socket.close();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}