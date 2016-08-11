package com.chattapp.drafts;

import com.chattapp.drafts.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerSocketManager implements Runnable {
    private int PORT;
    public boolean keepAlive=true;
    private Controller controller;

    ServerSocketManager() {
    }

    ServerSocketManager(int PORT, Controller cont) {
        this.PORT = PORT;
        this.controller = cont;
    }

    public static void main(String[] args) throws Exception {
        ServerSocketManager myServ = new ServerSocketManager();
        myServ.run();
    }

    @Override
    public void run() {
    	System.out.println(Thread.currentThread().getName()+":Starting execution.");
        ServerSocket serverSocket = null;
        try {
        	serverSocket = new ServerSocket(this.PORT);
        	serverSocket.setSoTimeout(5000);	// Keeping this just to get the keepAlive check re-evaluated.
        	synchronized(controller) {
        		controller.setServerStatus(true);
        		controller.notifyAll();
        	}
        	System.out.println(Thread.currentThread().getName()+":I have updated the status.");
        	while (keepAlive) {
        		try {
        			Socket socket = serverSocket.accept();
        			socket.setSoTimeout(1);
        			System.out.println(Thread.currentThread().getName()+":"+socket.getInetAddress()+" requesting connection.");
        			controller.addUnConnection(new Controller.ConBag(socket));
        			continue;
        		} catch (SocketTimeoutException ste) {
        			// Do nothing
        		}
        	}
        } catch(IOException e) {
        	e.printStackTrace();
        	controller.setServerStatus(false);
        } finally {
        	if(serverSocket!=null) {
        		try {
					serverSocket.close();
				} catch (IOException e) {
					// This is the only thread which waits for this socket's accept.
					// This should never happen.
					e.printStackTrace();
				}
        	}
        }
    }
}