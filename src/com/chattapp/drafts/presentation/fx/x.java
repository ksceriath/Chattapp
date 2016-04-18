/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.chattapp.drafts.presentation.fx.FXMLController
 */
package com.chattapp.drafts.presentation.fx;

import java.io.IOException;
import java.io.InputStream;

/*
 * Exception performing whole class analysis ignored.
 */
class FXMLController
implements Runnable {
    private final /* synthetic */ InputStream val$is;

    FXMLController(InputStream inputStream) {
        this.val$is = inputStream;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            while ((i = this.val$is.read()) != -1) {
                com.chattapp.drafts.presentation.fx.FXMLController.access$0((com.chattapp.drafts.presentation.fx.FXMLController)FXMLController.this).appendText(String.valueOf(i));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}