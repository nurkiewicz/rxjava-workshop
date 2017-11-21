package com.nurkiewicz.rxjava;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.nurkiewicz.rxjava.util.Sleeper;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
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
		Flowable<BigDecimal> obs = slowFromCallable();

		obs
				.subscribeOn(Schedulers.io())
				.subscribe(
						x -> log.info("Got: {}", x)
				);
		Sleeper.sleep(ofMillis(1_100));
	}

	@Test
	public void subscribeOnForEach() throws Exception {
		Flowable<BigDecimal> obs = slowFromCallable();

		obs
				.subscribeOn(Schedulers.io())
				.subscribe(
						x -> log.info("Got: {}", x)
				);
		Sleeper.sleep(ofMillis(1_100));
	}

	private Flowable<BigDecimal> slowFromCallable() {
		return Flowable.fromCallable(() -> {
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
		Sleeper.sleep(ofMillis(1_100));
	}

	/**
	 * TODO Create CustomExecutor
	 */
	@Test
	public void customExecutor() throws Exception {
		final TestSubscriber<BigDecimal> subscriber = slowFromCallable()
				.subscribeOn(myCustomScheduler())
				.test();
		await().until(() -> {
					Thread lastSeenThread = subscriber.lastThread();
					assertThat(lastSeenThread).isNotNull();
					assertThat(lastSeenThread.getName()).matches("CustomExecutor-\\d+");
				}
		);
	}

	/**
	 * Hint: Schedulers.from()
	 * Hint: ThreadFactoryBuilder
	 */
	private Scheduler myCustomScheduler() {
		final ThreadFactory threadFactory = new ThreadFactoryBuilder()
				.setNameFormat("CustomExecutor-%d")
				.build();
		return Schedulers.from(Executors.newFixedThreadPool(10, threadFactory));
	}
	
}
