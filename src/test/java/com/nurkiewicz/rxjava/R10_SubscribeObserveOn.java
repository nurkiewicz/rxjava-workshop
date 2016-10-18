package com.nurkiewicz.rxjava;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.nurkiewicz.rxjava.util.Sleeper;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Scheduler;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Ignore
public class R10_SubscribeObserveOn {
	
	private static final Logger log = LoggerFactory.getLogger(R10_SubscribeObserveOn.class);
	
	@Test
	public void subscribeOn() throws Exception {
		Observable<BigDecimal> obs = slowFromCallable();
		
		obs
				.subscribeOn(Schedulers.io())  //nie polecam
				.subscribe(
						x -> log.info("Got: {}", x)
				);
		log.info("Subscribed");
		Sleeper.sleep(ofMillis(1_100));
	}
	
	@Test
	public void subscribeOnForEach() throws Exception {
		Observable<BigDecimal> obs = slowFromCallable();
		
		obs
				.subscribeOn(Schedulers.io())
				.toBlocking()
				.forEach(
						x -> log.info("Got: {}", x)
				);
		Sleeper.sleep(ofMillis(1_100));
	}
	
	private Observable<BigDecimal> slowFromCallable() {
		return Observable.fromCallable(() -> {
			log.info("Starting");
			Sleeper.sleep(ofSeconds(1));
			log.info("Done");
			return BigDecimal.TEN;
		});
	}
	
	@Test
	public void observeOn() throws Exception {
		slowFromCallable()
				.subscribeOn(Schedulers.io())
				.doOnNext(x -> log.info("A: {}", x))
				.observeOn(Schedulers.computation())
				.doOnNext(x -> log.info("B: {}", x))
				.observeOn(Schedulers.newThread())
				.doOnNext(x -> log.info("C: {}", x))
				.subscribe(
						x -> log.info("Got: {}", x)
				);
		log.info("Blocked?");
		Sleeper.sleep(ofMillis(1_100));
	}
	
	@Test
	public void customExecutor() throws Exception {
		TestSubscriber<BigDecimal> subscriber = new TestSubscriber<>();
		slowFromCallable()
				.subscribeOn(myCustomScheduler())
				.subscribe(subscriber);
		await().until(() -> {
					Thread lastSeenThread = subscriber.getLastSeenThread();
					assertThat(lastSeenThread).isNotNull();
					assertThat(lastSeenThread.getName()).startsWith("CustomExecutor-");
				}
		);
	}
	
	/**
	 * Hint: Schedulers.from()
	 * Hint: ThreadFactoryBuilder
	 */
	private Scheduler myCustomScheduler() {
		ThreadFactory threadFactory = new ThreadFactoryBuilder()
				.setNameFormat("CustomExecutor-%d")
				.build();
		ExecutorService executor = Executors.newFixedThreadPool(10, threadFactory);
		return Schedulers.from(executor);
	}
	
	
}
