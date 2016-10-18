package com.nurkiewicz.rxjava;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscriber;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

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
	
	@Test
	public void should() throws Exception {
		CompletableFuture<String> future = CompletableFuture.completedFuture("abc");
		
//		String s1 = future.get();
//		int length = s1.length();
		
		future.handle((s, ex) -> {return null;});
		CompletableFuture<Double> doubFut = future
				.thenApply((String s) -> s.length())  //map
				.thenApply((Integer x) -> x * 2.0);
		
		CompletableFuture<BigDecimal> decFut = doubFut
				.thenCompose(db -> load(db));//flatMap
	}
	
	CompletableFuture<BigDecimal> load(double d) {
		return CompletableFuture.completedFuture(BigDecimal.ZERO);
	}
	
	private static final Logger log = LoggerFactory.getLogger(R00_SmokeTest.class);
	
	@Test
	public void should2() throws Exception {
		rx.Observable<String> obs = Observable.just("a", "b", "c");
		
		Observable<Double> doubles = obs
				.map(str -> str.length())
				.filter(x -> x < 10)
				.map(x -> x * 2.0);
		
		obs.subscribe();
		obs.subscribe(new Subscriber<String>() {
			@Override
			public void onCompleted() {
				//...
			}
			
			@Override
			public void onError(Throwable e) {
				
			}
			
			@Override
			public void onNext(String s) {
				System.out.println(s);
			}
		});
		obs.subscribe(
				str -> System.out.println(str),
				ex -> log.error("Error", ex),
				() -> System.out.println("Done")
				);
		//later
	}
	
	// Hot vs cold Observable
	
	
}
