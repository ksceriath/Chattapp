package com.chattapp.drafts;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ClientSocketManager implements Runnable {
	
	static class ConBag {
		String host;
		int port;
		InputStream readFrom;
		OutputStream writeTo;
		Socket socket;
		ConBag(Socket x) {
			socket = x;
			host = x.getInetAddress().toString().substring(1);
			port = x.getPort();
		}
		ConBag(String host, int port, InputStream ip, OutputStream op) {
			try {
				this.host = InetAddress.getByName(host).toString();
				this.host = this.host.substring(this.host.indexOf('/')+1);
				this.port = port;
				this.readFrom = ip;
				this.writeTo = op;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	private Deque<ConBag> requests = new ConcurrentLinkedDeque<>();
	public boolean keepAlive = true;
	private Controller controller;
	
	public ClientSocketManager() { }
	
	public ClientSocketManager(Controller controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {
    	System.out.println(Thread.currentThread().getName()+":Starting processing.");
        while(keepAlive) {
        	ConBag request = requests.pollFirst();
        	try {
        		if(request!=null) {
        			System.out.print(Thread.currentThread().getName()+":request found.");
        		}
				if(request!=null && 
						request.host!=null && 
						InetAddress.getByName(request.host)!=null) {
					System.out.println(Thread.currentThread().getName()+":Connecting to "+request.host);
					request.socket = new Socket(InetAddress.getByName(request.host), request.port);
					request.socket.setSoTimeout(1);
					if(controller.addConnection(request)) {
						System.out.println(Thread.currentThread().getName()+":Queued "+request.host);
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        System.out.println(Thread.currentThread().getName()+":Stopping processing.");
    }

    public void newRequest(String host, int port, InputStream ip, OutputStream op) {
    	System.out.println(Thread.currentThread().getName()+":Adding to requests queue.");
    	requests.add(new ConBag(host,port,ip,op));
    	System.out.println(Thread.currentThread().getName()+":requests queue size:"+requests.size());
    }
    
    public static void main(String[] args) throws Exception {
    	System.out.println(InetAddress.getByName("127.0.0.1").toString().substring(InetAddress.getByName("127.0.0.1").toString().indexOf('/')+1));
//        ClientSocketManager myCli = new ClientSocketManager();
//        myCli.run();
    }
    
}