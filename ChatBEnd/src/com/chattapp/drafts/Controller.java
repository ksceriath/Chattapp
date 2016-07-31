package com.chattapp.drafts;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.chattapp.drafts.ClientSocketManager.ConBag;

public class Controller implements Runnable, Handler{
    private Map<String, Conversation> runningConversations = new ConcurrentHashMap<>();
    private Deque<ClientSocketManager.ConBag> queuedConBags = new ConcurrentLinkedDeque<>();
    private Deque<ClientSocketManager.ConBag> unQueuedConBags = new ConcurrentLinkedDeque<>();
    private ClientSocketManager csm;
    private ServerSocketManager ssm;
    private int mPort;
    private Boolean serverStatus=null;
    
    public static boolean keepAlive;
    
    private Thread controllerThread;
    private static Controller controller = new Controller();
    private Controller() { }
    public static Handler getInstance() {
    	return controller;
    }

    public Boolean getServerStatus() {
    	return serverStatus;
    }
    
    public void setServerStatus(boolean s) {
    	this.serverStatus = s;
    }
    
    public boolean addConnection(ClientSocketManager.ConBag s) {
    	if(!runningConversations.containsKey(s.socket.getInetAddress())) {
    		return queuedConBags.offer(s);
    	}
    	return true;
    }
    
	public boolean addUnConnection(ConBag conBag) {
		return unQueuedConBags.offer(conBag);
	}
	
	public String addUnQueuedConnection(InputStream readFrom, OutputStream writeTo) {
		ClientSocketManager.ConBag x = unQueuedConBags.pollFirst();
		if(x!=null) {
			x.readFrom = readFrom;
			x.writeTo = writeTo;
			if(addConnection(x)) {
				System.out.println(Thread.currentThread().getName()+":Queued "+x.host);
			}
			return x.host+x.port;
		}
		return null;
	}
    
    @Override
    public void terminateConversation(String connTo) {
    	System.out.println(Thread.currentThread().getName()+":Terminate "+connTo+" - request received.");
    	try {
			runningConversations.get(InetAddress.getAllByName(connTo)).keepAlive(false);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }

	@Override
	public boolean execute(int port) {
		System.out.println(Thread.currentThread().getName()+":Execute - request received.");
		if(controllerThread==null || !controllerThread.isAlive()) {
			mPort = port;
			System.out.println(Thread.currentThread().getName()+":Starting new Controller Thread.");
			controllerThread = new Thread(this,"controllerThread");
			controllerThread.start();
		}
		synchronized(this) {
			while(getServerStatus()==null) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// Do nothing.
				}
			};
		}
		System.out.println(Thread.currentThread().getName()+":Execute finished - server status : "+getServerStatus());
		return getServerStatus();
	}

	@Override
	public void createNewConnection(String host, int port, InputStream readFrom, OutputStream writeTo) {
		System.out.println(Thread.currentThread().getName()+":Create new connection - request received.");
		csm.newRequest(host, port, readFrom, writeTo);
	}

	@Override
	public void terminateAllConversations() {
		System.out.println(Thread.currentThread().getName()+":Terminate connections - request received.");
		keepAlive = false;
	}
	
	@Override
	public void run(){
		try {
			System.out.println(Thread.currentThread().getName()+":Starting execution.");
//            int PORT = Integer.parseInt(args[0]);	// server port
			int PORT = mPort;
			keepAlive = true;
			ssm = new ServerSocketManager(PORT,this);
			csm = new ClientSocketManager(this);
			System.out.println(Thread.currentThread().getName()+":Starting new Listener Thread");
			new Thread(ssm, "listenerThread").start();
			synchronized(this) {
				while(getServerStatus()==null) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						// Do nothing.
					}
				};
			}
			if(!getServerStatus()) {
				return;
			}
			System.out.println(Thread.currentThread().getName()+":Starting new Initiator Thread");
			new Thread(csm,"initiatorThread").start();
//            t.join();
			
			while(keepAlive) {
				ClientSocketManager.ConBag x;
				if((x = queuedConBags.pollFirst()) != null 
						&& x.socket != null) {
					Conversation conv;
					if(runningConversations.containsKey(x.host)) {
						conv = runningConversations.get(x.host);
					} else {
						System.out.println(Thread.currentThread().getName()+":Starting new conversation with "+x.host);
						conv = new Conversation(x);
						runningConversations.put(x.host, conv);
					}
					conv.startStreamMappers();
				}
			}
			System.out.println(Thread.currentThread().getName()+":Stopping processing.");
			csm.keepAlive = false;
			ssm.keepAlive = false;
			for(Conversation conv : runningConversations.values()) {
				conv.keepAlive(false);
			}
		}
		catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			e.printStackTrace();
		}
	}
}