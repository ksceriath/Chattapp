package com.chattapp.drafts.presentation.console;

import java.io.*;

public class Dummy {
	
	public static void main(String[] args) throws IOException {
		final PipedOutputStream pos = new PipedOutputStream();
		PipedInputStream pis = new PipedInputStream(pos);
		new Thread(new Runnable() {
			@Override
			public void run() {
				int b=0;
				while(b<100) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						pos.write(b++);
						pos.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					pos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		int i;
		while((i = pis.read())!=-1) {
			System.out.print(i);
		}
		pis.close();
	}
}