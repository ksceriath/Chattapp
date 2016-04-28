package com.chattapp.drafts.presentation.fx;

import com.chattapp.drafts.Main;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class FXMLController {
    @FXML
    private Text actionTarget;
    @FXML
    private TextArea testing;
    InputStream is;
    OutputStream os;

    public void setStreams(final InputStream is, final OutputStream os) {
        this.is = is;
        this.os = os;
        new Thread(new Runnable(){

            @Override
            public void run() {
                int i = 0;
                try {
                    while ((i = is.read()) != -1) {
                        FXMLController.this.testing.appendText(String.valueOf(i));
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable(){

            @Override
            public void run() {
                int i = 0;
                try {
                    while ((i = System.in.read()) != -1) {
                        os.write(i);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @FXML
    protected void initialize() {
        this.testing.appendText("abracadabra");
    }

    @FXML
    protected void handleConnectButtonAction(ActionEvent event) {
        this.actionTarget.setText("button pressed!");
        try {
            Socket s = new Socket("localhost", 8080);
            Main.addConnection((InetAddress)s.getInetAddress(), (Socket)s);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TextArea getTextArea() {
        return this.testing;
    }

}
