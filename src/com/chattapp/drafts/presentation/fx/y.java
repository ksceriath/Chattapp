/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.chattapp.drafts.presentation.fx.FXMLController
 */
package com.chattapp.drafts.presentation.fx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class FXMLController
implements Runnable {
    private final /* synthetic */ OutputStream val$os;

    FXMLController(OutputStream outputStream) {
        this.val$os = outputStream;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            while ((i = System.in.read()) != -1) {
                this.val$os.write(i);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}