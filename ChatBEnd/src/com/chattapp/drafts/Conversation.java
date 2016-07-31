package com.chattapp.drafts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Conversation {
    private InputStream inboundMsg;
    private OutputStream outboundMsg;
    private InputStream readFromLocal;
    private OutputStream writeToLocal;
    public Mapper inPipe;
    public Mapper outPipe;
    
    Conversation(ClientSocketManager.ConBag bag) {
    	try {
    		inboundMsg = bag.socket.getInputStream();
    		outboundMsg = bag.socket.getOutputStream();
    		readFromLocal = bag.readFrom;
    		writeToLocal = bag.writeTo;
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void keepAlive(boolean state) {
    	inPipe.keepAlive = false;
    	outPipe.keepAlive = false;
    }

    public void startStreamMappers() {
    	inPipe = new Mapper(inboundMsg,writeToLocal);
    	outPipe = new Mapper(readFromLocal,outboundMsg);
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