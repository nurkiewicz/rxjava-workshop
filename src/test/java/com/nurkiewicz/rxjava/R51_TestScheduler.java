package com.nurkiewicz.rxjava;

import com.nurkiewicz.rxjava.util.CloudClient;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class R51_TestScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(R51_TestScheduler.class);
	private static final BigDecimal FALLBACK = BigDecimal.ONE.negate();
	
	private CloudClient cloudClient = new CloudClient();
	
	/**
	 * Hint: timeout()
	 * Hint: Which operators accept `TestScheduler`?
	 */
	@Test
	public void retryingWithTestScheduler() throws Exception {
		//given
		TestScheduler clock = Schedulers.test();
		TestSubscriber<BigDecimal> subscriber = new TestSubscriber<>();
		
		//when
		cloudClient
				.pricing()
//				.timeout(3, TimeUnit.SECONDS, Observable.just(FALLBACK), clock)
				.timeout(3, TimeUnit.SECONDS, clock)
				.doOnError(ex -> log.warn("Error", ex))
				.onErrorReturn(ex -> FALLBACK)
				.subscribe(subscriber);
		
		//then
		subscriber.assertNoValues();
		subscriber.assertNoErrors();

		clock.advanceTimeBy(2_999, TimeUnit.MILLISECONDS);
		subscriber.assertNoValues();
		subscriber.assertNoErrors();

		clock.advanceTimeBy(1, TimeUnit.MILLISECONDS);
		subscriber.assertValue(FALLBACK);
		subscriber.assertNoErrors();
	}
	
	/**
	 * Hint: retryWhen()
	 * Hint: zipWith()
	 * Hint: Observable.range()
	 * Hint: IntMath.pow()
	 */
	@Test
	public void retryingWithExponentialBackoff() throws Exception {
		//given
		TestScheduler clock = Schedulers.test();
		TestSubscriber<BigDecimal> subscriber = new TestSubscriber<>();
		LongAdder subscriptionCounter = new LongAdder();
		
		//when
		cloudClient
				.broken()
				.onErrorReturn(error -> FALLBACK)
				.subscribe(subscriber);
		
		//then after initial request
		clock.advanceTimeBy(999, TimeUnit.MILLISECONDS);
		assertThat(subscriptionCounter.sum()).isEqualTo(1);
		
		//first retry after 1s
		clock.advanceTimeBy(1, TimeUnit.MILLISECONDS);
		assertThat(subscriptionCounter.sum()).isEqualTo(2);
		
		//second retry after 2s
		clock.advanceTimeBy(2_000 - 1, TimeUnit.MILLISECONDS);
		assertThat(subscriptionCounter.sum()).isEqualTo(2);
		clock.advanceTimeBy(1, TimeUnit.MILLISECONDS);
		assertThat(subscriptionCounter.sum()).isEqualTo(3);

		//third retry after 4s
		clock.advanceTimeBy(4_000 - 1, TimeUnit.MILLISECONDS);
		assertThat(subscriptionCounter.sum()).isEqualTo(3);
		clock.advanceTimeBy(1, TimeUnit.MILLISECONDS);
		assertThat(subscriptionCounter.sum()).isEqualTo(4);

		//fourth retry after 8s
		clock.advanceTimeBy(8_000 - 1, TimeUnit.MILLISECONDS);
		assertThat(subscriptionCounter.sum()).isEqualTo(4);
		clock.advanceTimeBy(1, TimeUnit.MILLISECONDS);
		assertThat(subscriptionCounter.sum()).isEqualTo(5);
		
		//no more retries
		clock.advanceTimeBy(1, TimeUnit.HOURS);
		assertThat(subscriptionCounter.sum()).isEqualTo(5);
		subscriber.assertNoValues();
		subscriber.assertNoErrors();
	}
	
}
