package com.chattapp.drafts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Mapper implements Runnable {

	private InputStream fromStream;
	private OutputStream toStream;
	public boolean keepAlive=true;
	
	public Mapper(InputStream is, OutputStream os) {
		fromStream = is;
		toStream = os;
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+":starting new map.");
		try {
			while(keepAlive) {
				int x = fromStream.read();
				toStream.write(x);
				toStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
