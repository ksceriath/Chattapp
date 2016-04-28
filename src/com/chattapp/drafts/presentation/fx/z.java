/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.chattapp.drafts.Main
 *  com.chattapp.drafts.presentation.fx.FXMLEx
 *  javafx.fxml.FXMLLoader
 */
package com.chattapp.drafts.presentation.fx;

import com.chattapp.drafts.Main;
import com.chattapp.drafts.presentation.fx.FXMLController;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import javafx.fxml.FXMLLoader;

class FXMLExZ implements Runnable {
    private final /* synthetic */ FXMLLoader val$loader;

    FXMLExZ(FXMLLoader fXMLLoader) {
        this.val$loader = fXMLLoader;
    }

    @Override
    public void run() {
        while (Main.getConnections().isEmpty()) {
        }
        Socket s = (Socket)Main.getConnections().values().iterator().next();
        try {
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();
            FXMLController ctrl = (FXMLController)this.val$loader.getController();
            ctrl.setStreams(is, os);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}