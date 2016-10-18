package com.nurkiewicz.rxjava;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Ignore
public class R01_JustFrom {
	
	private static final Logger log = LoggerFactory.getLogger(R01_JustFrom.class);
	
	@Test
	public void shouldCreateObservableFromConstants() throws Exception {
		Observable<String> obs = Observable.just("A", null, "C");
		
		Observable<String> withoutNulls = obs.filter(x -> x != null);
		
		obs.subscribe(
				(x) -> System.out.println("Got: " + x)
		);
	}
	
	@Test
	public void should3() throws Exception {
		Observable<Date> dates = Observable.just(new Date());
		
		Observable<Date> datesPlusSecond = dates
				.map((Date d) -> {
					d.setTime(d.getTime() + 1_000);
					return d;
				});
		
		dates.subscribe();
	}
	
	
	@Test
	public void shouldEmitValues() throws Exception {
		Observable<String> obs = Observable
				.just("A", null, "C")
				.delay(1, TimeUnit.SECONDS);
		
		TestSubscriber<String> subscriber = new TestSubscriber<>();
		obs.subscribe(subscriber);

		//Awaitility
		await().until(() -> subscriber.assertValues("A", null, "C"));
		subscriber.assertValues("A", null, "C");
		subscriber.assertNoErrors();
	}
	
}
