package com.nurkiewicz.rxjava;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class R00_SmokeTest {
	
	@Test
	public void shouldRunRxJava() throws Exception {
		//given
		rx.Observable<Integer> obs = rx.Observable.just(1, 2);
		rx.observers.TestSubscriber<Integer> subscriber = new rx.observers.TestSubscriber<>();
		
		//when
		obs.subscribe(subscriber);
		
		//then
		subscriber.assertValues(1, 2);
	}
}
