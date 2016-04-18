/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.chattapp.drafts.Client
 *  com.chattapp.drafts.Server
 */
package com.chattapp.drafts;

import com.chattapp.drafts.Client;
import com.chattapp.drafts.Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static Map<InetAddress, Socket> connections = new HashMap<InetAddress, Socket>();
    public static boolean keepAlive;

    public static void addConnection(InetAddress a, Socket s) {
        connections.put(a, s);
    }

    public static Map getConnections() {
        return connections;
    }

    public static void main(String[] args) throws IOException {
        try {
            int PORT = Integer.parseInt(args[0]);
            keepAlive = true;
            new Thread((Runnable)new Server(PORT)).start();
            Thread t = new Thread((Runnable)new Client());
            t.start();
            t.join();
            keepAlive = false;
        }
        catch (ArrayIndexOutOfBoundsException | InterruptedException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}