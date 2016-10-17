package com.nurkiewicz.rxjava.util;

public class Threads {
	
	public static Thread runInBackground(Runnable block) {
		Thread t = new Thread(block);
		return start(t);
	}
	
	public static Thread runInBackground(String threadName, Runnable block) {
		Thread t = new Thread(block, threadName);
		return start(t);
	}
	
	private static Thread start(Thread t) {
		t.start();
		return t;
	}
	
}
