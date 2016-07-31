package com.chattapp.drafts;

import java.io.*;

public interface Handler {
	
	public boolean execute(int port) ;
	
	public void createNewConnection(String host, int port, InputStream readFrom, OutputStream writeTo);
	
	public void terminateAllConversations();
	
	public void terminateConversation(String host);
	
	public String addUnQueuedConnection(InputStream readFrom, OutputStream writeTo);
	
}
