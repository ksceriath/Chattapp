package com.chattapp.drafts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Conversation {
	private Socket socket;
    private InputStream inboundMsg;
    private OutputStream outboundMsg;
    private InputStream readFromLocal;
    private OutputStream writeToLocal;
    public Mapper inPipe;
    public Mapper outPipe;
    Conversation(Controller.ConBag bag) {
    	try {
    		socket = bag.socket;
    		inboundMsg = bag.socket.getInputStream();
    		outboundMsg = bag.socket.getOutputStream();
    		readFromLocal = bag.readFrom;
    		writeToLocal = bag.writeTo;
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public boolean isDead() {
    	return socket.isInputShutdown() || socket.isOutputShutdown();
    }
    
    public void kill() {
    	try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	synchronized(this) {
//    		keepAlive = state;
//    		this.notifyAll();
//    	}
    }

    public void startStreamMappers() {
    	inPipe = new Mapper(this,inboundMsg,writeToLocal);
    	outPipe = new Mapper(this,readFromLocal,outboundMsg);
    	new Thread(inPipe,"incomingDelivery").start();
    	new Thread(outPipe,"outgoingDelivery").start();
//        try {
//            Thread t2 = new Thread((Runnable)new StandardStream(this.socket.getOutputStream()));
//            t2.start();
//            try {
//                t2.join();
//                this.socket.close();
//            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}