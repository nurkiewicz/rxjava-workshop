package com.nurkiewicz.rxjava;

import com.nurkiewicz.rxjava.util.UrlDownloader;
import com.nurkiewicz.rxjava.util.Urls;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class R22_ConcatMap {
	
	@Test
	public void downloadSequentially() throws Exception {
		//TODO Change flatMap to concatMap
		Urls
				.all()
				.flatMap(u -> UrlDownloader.downloadAsync(u, Schedulers.io()))
				.test()
				.await();
	}
	
	@Test
	public void concatIsLazy() throws Exception {
		AtomicBoolean firstSubscribed = new AtomicBoolean();
		AtomicBoolean secondSubscribed = new AtomicBoolean();
		Flowable<Integer> first = listOfInts(firstSubscribed, 1, 2, 3);
		Flowable<Integer> second = listOfInts(secondSubscribed, 4, 5, 6);
		
		//TODO Change take(N) to 1, 4, 7
		first.concatWith(second).take(3).blockingSubscribe();
		assertThat(firstSubscribed.get()).describedAs("Should subscribe to first").isTrue();
		assertThat(secondSubscribed.get()).describedAs("Should not subscribe to second").isFalse();
	}
	
	private Flowable<Integer> listOfInts(AtomicBoolean subscriptionFlag, Integer... values) {
		return Flowable
				.fromCallable(() -> Arrays.asList(values))
				.subscribeOn(Schedulers.io())
				.doOnSubscribe(d -> subscriptionFlag.set(true))
				//TODO What's the point of this concatMapIterable?
				.concatMapIterable(x -> x);
	}
	
	@Test
	public void mergeIsEager() throws Exception {
		AtomicBoolean firstSubscribed = new AtomicBoolean();
		AtomicBoolean secondSubscribed = new AtomicBoolean();
		
		//TODO Change take(N) to 0
		Flowable
				.merge(     //Also mergeWith
						listOfInts(firstSubscribed, 1, 2, 3),
						listOfInts(secondSubscribed, 4, 5, 6)
				)
				.take(1)
				.blockingSubscribe();
		assertThat(firstSubscribed.get()).describedAs("Should subscribe to first").isTrue();
		assertThat(secondSubscribed.get()).describedAs("Should subscribe to second").isTrue();
	}
	
	
}
