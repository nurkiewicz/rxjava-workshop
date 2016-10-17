package com.nurkiewicz.rxjava;

import com.nurkiewicz.rxjava.util.InfiniteReader;
import com.nurkiewicz.rxjava.util.NumberSupplier;
import com.nurkiewicz.rxjava.util.Sleeper;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.schedulers.Schedulers;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


@Ignore
public class R70_Backpressure {

	private static final Logger log = LoggerFactory.getLogger(R70_Backpressure.class);

	@Test
	public void missingBackpressure() throws Exception {
		Flowable
				.interval(5, TimeUnit.MILLISECONDS)
				.doOnNext(x -> log.trace("Emitted: {}", x))
				.observeOn(Schedulers.computation())
				.doOnNext(x -> log.trace("Handling: {}", x))
				.subscribe(x -> Sleeper.sleep(Duration.ofMillis(6)));

		TimeUnit.SECONDS.sleep(30);
	}

	@Test
	public void loadingDataFromInfiniteReader() throws Exception {
		//given
		Flowable<String> numbers = Flowable.create(sub -> pushNumbersToSubscriber(sub), BackpressureStrategy.ERROR);

		//then
		numbers
				.take(4)
				.test()
				.assertValues("0", "1", "2", "3");
	}

	@Test
	public void backpressureIsNotAproblemIfTheSameThread() throws Exception {
		Flowable<String> numbers = Flowable.create(sub -> pushNumbersToSubscriber(sub), BackpressureStrategy.ERROR);

		numbers
				.doOnNext(x -> log.info("Emitted: {}", x))
				.subscribe(x -> Sleeper.sleep(Duration.ofMillis(6)));
	}

	/**
	 * TODO Reimplement `numbers` so that lines are pulled by subscriber, not pushed to subscriber
	 */
	@Test
	public void missingBackpressureIfCrossingThreads() throws Exception {
		Iterable<String> iterable = new Iterable<String>() {
			Reader reader = new InfiniteReader(NumberSupplier.lines());
			BufferedReader lines = new BufferedReader(reader);

			@Override
			public Iterator<String> iterator() {
				return lines.lines().iterator();
			}
		};

		Flowable.fromIterable(iterable)
				.observeOn(Schedulers.io())
				.blockingSubscribe(x -> Sleeper.sleep(Duration.ofMillis(6)));
	}

	private void pushNumbersToSubscriber(FlowableEmitter<? super String> sub) {
		try (Reader reader = new InfiniteReader(NumberSupplier.lines())) {
			BufferedReader lines = new BufferedReader(reader);
			while (!sub.isCancelled()) {
				sub.onNext(lines.readLine());
			}
		} catch (IOException e) {
			sub.onError(e);
		}
	}

}
