package com.nurkiewicz.rxjava;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class R00_SmokeTest {
	
	@Test
	public void shouldRunRxJava() throws Exception {
		//given
		Observable<Integer> obs = Observable.just(1, 2);
		TestObserver<Integer> subscriber = new TestObserver<>();
		
		//when
		obs.subscribe(subscriber);
		
		//then
		subscriber.assertValues(1, 2);
	}

	@Test
	public void shouldRunRxFlowable() throws Exception {
		//given
		Flowable<Integer> obs = Flowable.just(1, 2);
		TestSubscriber<Integer> subscriber = new TestSubscriber<>();
		
		//when
		obs.subscribe(subscriber);
		
		//then
		subscriber.assertValues(1, 2);
	}
}
