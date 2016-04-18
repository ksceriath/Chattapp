/*
 * Decompiled with CFR 0_114.
 */
package com.chattapp.drafts;

import com.chattapp.drafts.Main;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server
implements Runnable {
    private int PORT;

    Server() {
    }

    Server(int PORT) {
        this.PORT = PORT;
    }

    public static void main(String[] args) throws Exception {
        Server myServ = new Server();
        myServ.run();
    }

    @Override
    public void run() {
        block15 : {
            ServerSocket serverSocket = null;
            try {
                try {
                    serverSocket = new ServerSocket(this.PORT);
                    serverSocket.setSoTimeout(5000);
                    while (Main.keepAlive) {
                        try {
                            Socket socket = serverSocket.accept();
                            Main.addConnection(socket.getInetAddress(), socket);
                            continue;
                        }
                        catch (SocketTimeoutException socket) {
                            // empty catch block
                        }
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    if (serverSocket == null) break block15;
                    try {
                        serverSocket.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}