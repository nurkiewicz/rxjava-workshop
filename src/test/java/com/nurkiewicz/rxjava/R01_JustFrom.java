package com.nurkiewicz.rxjava;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.observers.TestSubscriber;

@Ignore
public class R01_JustFrom {
	
	private static final Logger log = LoggerFactory.getLogger(R01_JustFrom.class);
	
	@Test
	public void shouldCreateObservableFromConstants() throws Exception {
		Observable<String> obs = Observable.just("A", null, "C");
		
		obs.subscribe(
				(x) -> System.out.println("Got: " + x)
		);
	}
	
	@Test
	public void shouldEmitValues() throws Exception {
		Observable<String> obs = Observable.just("A", null, "C");
		
		TestSubscriber<String> subscriber = new TestSubscriber<>();
		obs.subscribe(subscriber);
		
		subscriber.assertValues("A", null, "C");
	}
	
}
