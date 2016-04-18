/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.chattapp.drafts.presentation.fx.FXMLEx
 *  javafx.application.Application
 */
package com.chattapp.drafts;

import com.chattapp.drafts.presentation.fx.FXMLEx;
import javafx.application.Application;

public class Client
implements Runnable {
    @Override
    public void run() {
        Application.launch((Class)FXMLEx.class, (String[])new String[]{""});
    }

    public static void main(String[] args) throws Exception {
        Client myCli = new Client();
        myCli.run();
    }
}