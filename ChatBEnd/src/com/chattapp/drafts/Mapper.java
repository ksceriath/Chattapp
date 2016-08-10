package com.chattapp.drafts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Mapper implements Runnable {

	private InputStream fromStream;
	private OutputStream toStream;
	private Conversation context;
	
	public Mapper(Conversation ctx, InputStream is, OutputStream os) {
		context = ctx;
		fromStream = is;
		toStream = os;
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+":starting new map.");
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				synchronized(context) {
//					while(context.keepAlive()) {
//						try {
//							context.wait();
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				};
//				try {
//					if("outgoingDelivery".equals(Thread.currentThread().getName())) {
//						toStream.close();
//						System.out.println(Thread.currentThread()
//								.getName()+": closed tostream.");
//					} else if("incomingDelivery".equals(Thread.currentThread().getName())) {
//						fromStream.close();
//						System.out.println(Thread.currentThread()
//								.getName()+": closed fromstream.");
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		},Thread.currentThread().getName()+"Closer").start();
		int x = 0;
		/* Since we are closing the socket, not sure if this should be
		 * required?
		 */
		boolean isAlive = true;
		while(isAlive && !context.isDead()) {
			try {
				x = fromStream.read();
				if(x==-1) {			// when the source stream has been closed.
					throw new SocketException();
				}
				toStream.write(x);
				toStream.flush();
			} catch(SocketTimeoutException ste) {
				// do nothing...
				/* Socket timeout has been defined during the creation
				 * time of the socket. The purpose is to unblock the 
				 * read operation, and do any 'should-continue' checks.
				 */
			} catch(SocketException se) {
				isAlive = false;
				/* This happens when the socket from which data
				 * is being read has been closed.
				 * This is a valid scenario and happens due to
				 * TerminateConnection.
				 */
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		context.kill();			// Whatever the reason, we don't want to continue the
								// conversation if either one of the Mappers is shutting down.
	}
}
