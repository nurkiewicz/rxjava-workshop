package com.nurkiewicz.rxjava;

import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import static com.nurkiewicz.rxjava.R30_Zip.LOREM_IPSUM;

public class R31_WindowBuffer {
	
	/**
	 * Hint: use buffer()
	 */
	@Test
	public void everyThirdWordUsingBuffer() throws Exception {
		//given
		TestSubscriber<String> subscriber = new TestSubscriber<>();
		Observable<String> everyThirdWord = LOREM_IPSUM;
		
		//when
		everyThirdWord.subscribe(subscriber);
		
		//then
		subscriber.assertValues("dolor", "consectetur");
	}
	
	/**
	 * Hint: use window()
	 * Hint: use elementAt()
	 */
	@Test
	public void everyThirdWordUsingWindow() throws Exception {
		//given
		TestSubscriber<String> subscriber = new TestSubscriber<>();
		Observable<String> everyThirdWord = LOREM_IPSUM;
		
		//when
		everyThirdWord.subscribe(subscriber);
		
		//then
		subscriber.assertValues("dolor", "consectetur");
	}
	
}
