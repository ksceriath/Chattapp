package com.chattapp.drafts;

import com.chattapp.drafts.FXMLEx;
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