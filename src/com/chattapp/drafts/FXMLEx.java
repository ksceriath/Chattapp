/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.chattapp.drafts.Main
 *  javafx.application.Application
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.stage.Stage
 */
package com.chattapp.drafts;

import com.chattapp.drafts.Main;
import com.chattapp.drafts.presentation.fx.FXMLController;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLEx
extends Application {
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxmlEx.fxml"));
        Parent root = (Parent)loader.load();
        primaryStage.setTitle("Welcome to chattapp stage");
        primaryStage.setScene(new Scene(root, 300.0, 300.0));
        primaryStage.show();
        new Thread(new Runnable(){

            @Override
            public void run() {
                while (Main.getConnections().isEmpty()) {
                }
                Socket s = (Socket)Main.getConnections().values().iterator().next();
                try {
                    InputStream is = s.getInputStream();
                    OutputStream os = s.getOutputStream();
                    FXMLController ctrl = (FXMLController)loader.getController();
                    ctrl.setStreams(is, os);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void connectionHandler() {
        while (Main.getConnections().isEmpty()) {
        }
        Socket s = (Socket)Main.getConnections().values().iterator().next();
        try {
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxmlEx.fxml"));
            FXMLController ctrl = (FXMLController)loader.getController();
            ctrl.setStreams(is, os);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch((Class)FXMLEx.class, (String[])args);
    }

}