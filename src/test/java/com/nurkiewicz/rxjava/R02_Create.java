package com.nurkiewicz.rxjava;

import com.nurkiewicz.rxjava.util.Sleeper;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.nurkiewicz.rxjava.util.Threads.runInBackground;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@Ignore
public class R02_Create {

	private static final Logger log = LoggerFactory.getLogger(R02_Create.class);

	/**
	 * TODO Complete create() implementation
	 */
	@Test
	public void observableUsingCreate() throws Exception {
		Observable<String> obs = Observable.create(emitter -> {
			emitter.onNext("A");
			emitter.onNext("B");
			emitter.onComplete();
		});

		obs
				.test()
				.assertValues("A", "B")
				.assertComplete();
	}

	/**
	 * By default subscriber is in the same thread as Observable
	 */
	@Test
	public void sameThread() throws Exception {
		String curThreadName = Thread.currentThread().getName();

		Observable<String> obs = Observable.create(sub -> {
			sub.onNext(Thread.currentThread().getName());
			sub.onComplete();
		});

		obs
				.test()
				.assertValues(curThreadName);
	}

	@Test
	public void createCanBeBlocking() throws Exception {
		log.info("Start");
		Observable<String> obs = Observable.create(sub -> {
			log.info("In create()");
			Sleeper.sleep(Duration.ofSeconds(2));
			sub.onComplete();
			log.info("Completed");
		});
		log.info("Subscribing");
		obs.subscribe();
		log.info("Result");
	}

	@Test
	public void createLambdaIsInvokedManyTimes() throws Exception {
		DataSource ds = mock(DataSource.class);

		Observable<Integer> obs = queryDatabase(ds);

		obs.subscribe();
		obs.subscribe();

		verify(ds, times(2)).getConnection();
	}

	/**
	 * Hint: use cache() operator
	 */
	@Test
	public void cachingWhenCreateIsInvokedManyTimes() throws Exception {
		DataSource ds = mock(DataSource.class);

		Observable<Integer> obs = queryDatabase(ds).cache();

		obs.subscribe();
		obs.subscribe();

		verify(ds, times(1)).getConnection();
	}

	private Observable<Integer> queryDatabase(DataSource ds) {
		return Observable.create(sub -> {
			try (Connection conn = ds.getConnection()) {
				sub.onComplete();
			} catch (SQLException e) {
				sub.onError(e);
			}
		});
	}

	/**
	 * Hint: isDisposed()
	 */
	@Test
	public void infiniteObservable() throws Exception {
		Observable<Integer> obs = Observable.create(sub -> {
			int i = 0;
			while (!sub.isDisposed()) {
				sub.onNext(i++);
			}
		});

		TestObserver<Integer> subscriber = obs
				.skip(10)
				.take(3)
				.test();

		subscriber.assertValues(10, 11, 12);
	}

	/**
	 * Interrupt when no longer subscribed
	 */
	@Test
	public void infiniteObservableInBackground() throws Exception {
		Observable<Integer> obs = Observable.create(sub ->
				runInBackground(() -> {
							int i = 0;
							while (!sub.isDisposed()) {
								sub.onNext(i++);
							}
						}
				)
		);

		TestObserver<Integer> subscriber = obs
				.skip(10)
				.take(3)
				.test();
		await().until(() -> subscriber.assertValues(10, 11, 12));
	}

	/**
	 * RxJava contract broken
	 */
	@Test
	public void brokenObservable() throws Exception {
		Observable<Integer> obs = Observable.create(sub -> {
			runInBackground(() -> sub.onNext(3));
			runInBackground(() -> sub.onNext(6));
		});
	}

}
