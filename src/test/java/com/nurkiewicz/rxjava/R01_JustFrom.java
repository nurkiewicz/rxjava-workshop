package com.nurkiewicz.rxjava;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class R01_JustFrom {
	
	@Test
	public void shouldCreateFlowableFromConstants() throws Exception {
		Flowable<String> obs = Flowable.just("A", "B", "C");
		
		obs.subscribe(
				(String x) -> System.out.println("Got: " + x)
		);
	}
	
	@Test
	public void shouldEmitValues() throws Exception {
		Flowable<String> obs = Flowable.just("A", "B", "C");
		
		final TestSubscriber<String> subscriber = obs.test();
		
		subscriber.assertValues("A", "B", "C");
	}
	
}
