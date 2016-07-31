package com.chattapp.drafts.presentation.console;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.chattapp.drafts.Controller;
import com.chattapp.drafts.Handler;

public class ConsoleClient {
	
	private static Handler sHandler = Controller.getInstance();
	private static List<PrintWriter> ostreams;
//	private static PipedOutputStream ost2;
	private static boolean running = false;
	static Boolean keepAlive = true;
	
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ostreams = Collections.synchronizedList(new ArrayList<PrintWriter>());
//		PrintWriter pw = null;
//		PrintWriter pw2 = null;
		while(keepAlive) {
			try {
				String line = br.readLine();
				 if(line.startsWith("/")) {
					 keepAlive = processCommand(line);
				 } else {
					 // pass on to the conversation handler
					 processMessage(line);
//					 if(ost!=null) {
//						 if(pw==null) {
//							 pw = new PrintWriter(ost);
//						 }
//						 pw.write(line);
//						 pw.flush();
//					 }
//					 if(ost2!=null) {
//						 if(pw2==null) {
//							 pw2 = new PrintWriter(ost2);
//						 }
//						 pw2.write(line);
//						 pw2.flush();
//					 }
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
		try {
			int messageStart = line.indexOf(' ');
			int index = Integer.parseInt(line.substring(0, messageStart));
			String message = line.substring(messageStart);
			PrintWriter pw = ostreams.get(index);
			pw.write(message);
			pw.flush();
		} catch(Exception E) {
			
		}
	}
	
	public static boolean processCommand(String line) {
		if(line.equalsIgnoreCase("/exit")) {
			sHandler.terminateAllConversations();
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
					ostreams.add(new PrintWriter(ost));
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
							while(host==null) {
								host = sHandler.addUnQueuedConnection(pis, System.out);
							}
							System.out.println(Thread.currentThread().getName()+
									":Added "+host+" at index "+ostreams.size());
							ostreams.add(new PrintWriter(ost2));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} else if(line.startsWith("/close")) {
			String[] segments = line.split(" ");
			sHandler.terminateConversation(segments[1]);
		}
		return true;
	}
}
