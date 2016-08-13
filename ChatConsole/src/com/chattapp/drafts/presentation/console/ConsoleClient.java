package com.chattapp.drafts.presentation.console;

import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import com.chattapp.drafts.Controller;
import com.chattapp.drafts.Handler;

public class ConsoleClient {
	
	private static Handler sHandler = Controller.getInstance();
	private static Map<String,PrintWriter> ostreams;
	private static Map<String,String> connections = new HashMap<>();
	private static boolean running = false;
	volatile static Boolean keepAlive = true;
	
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ostreams = Collections.synchronizedMap(new HashMap<String,PrintWriter>());
		while(keepAlive) {
			try {
				String line = br.readLine();
				 if(line.startsWith("/")) {
					 keepAlive = processCommand(line);
				 } else {
					 // pass on to the conversation handler
					 processMessage(line);
				 }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void processMessage(String line) {
		PrintWriter pw = null;
		try {
			int messageStart = line.indexOf(' ');
			String index = line.substring(0, messageStart);
			String message = line.substring(messageStart);
			pw = ostreams.get(index);
			pw.write(message);
			pw.flush();
		} catch(Exception E) {
			
		}
	}
	
	public static boolean processCommand(String line) {
		if(line.equalsIgnoreCase("/exit")) {
			sHandler.terminateAllConversations();
			connections.clear();
			closeAllOStreams();
			return false;
		} else if(line.startsWith("/connect")) {
			String[] segments = line.split(" ");
			try {
//				String name = segments[1];		// Ignore name for now...
				String host = segments[1];
				int port = Integer.parseInt(segments[2]);
				PipedOutputStream ost = new PipedOutputStream();
				try {
					InputStream is = new PipedInputStream(ost);
					sHandler.createNewConnection(host, port, is, System.out);
					System.out.println(Thread.currentThread().getName()+
							":Added "+host+" at index "+ostreams.size());
					connections.put(ostreams.size()+"", host+":"+port);
					ostreams.put(ostreams.size()+"",new PrintWriter(ost));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				// just ignore the command..
			}
		} else if(line.startsWith("/start") && !running) {
			// TODO handle case with multiple /start commands
			String[] segments = line.split(" ");
			try{
				int port = Integer.parseInt(segments[1]);
				running = sHandler.execute(port);
				
			} catch(ArrayIndexOutOfBoundsException e) {
				// just ignore the command..
			} catch(NumberFormatException e) {
				// just ignore the command..
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while(ConsoleClient.keepAlive) {
							PipedOutputStream ost2 = new PipedOutputStream();
							PipedInputStream pis = new PipedInputStream(ost2);
							String host = null;
							while(host==null && ConsoleClient.keepAlive) {
								host = sHandler.addUnQueuedConnection(pis, System.out);
							}
							if(host!=null) {
								System.out.println(Thread.currentThread().getName()+
										":Added "+host+" at index "+ostreams.size());
								connections.put(ostreams.size()+"", host);
								ostreams.put(ostreams.size()+"",new PrintWriter(ost2));
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} else if(line.startsWith("/close")) {
			String[] segments = line.split(" ");
			String host = connections.get(segments[1]).split(":")[0];
			sHandler.terminateConversation(host);
			connections.remove(segments[1]);
			ostreams.get(segments[1]).close();
			ostreams.remove(segments[1]);
		}
		return true;
	}
	
	public static void closeAllOStreams() {
		for(PrintWriter pw : ostreams.values()) {
			pw.close();
		}
		ostreams.clear();
	}
	
}
