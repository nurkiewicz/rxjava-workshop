package com.nurkiewicz.rxjava;

import com.nurkiewicz.rxjava.util.UrlDownloader;
import com.nurkiewicz.rxjava.util.Urls;
import org.junit.Ignore;
import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;

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
				.concatMap(u -> UrlDownloader.downloadAsync(u, Schedulers.io()))
				.toBlocking()
				.last();
	}
	
	@Test
	public void concatIsLazy() throws Exception {
		AtomicBoolean firstSubscribed = new AtomicBoolean();
		AtomicBoolean secondSubscribed = new AtomicBoolean();

		Observable<Integer> first = listOfInts(firstSubscribed, 1, 2, 3);
		Observable<Integer> second = listOfInts(secondSubscribed, 4, 5, 6);


		//first   || second
		//1, 2, 3 || 4, 5, 6
		
		//TODO Change take(N) to 0, 1, 4, 7
		
		Observable.concat(
				first, second/*, third, fourth*/
		);
		
		first.concatWith(second).take(3).toBlocking().subscribe();
		assertThat(firstSubscribed.get()).describedAs("Should subscribe to first").isTrue();
		assertThat(secondSubscribed.get()).describedAs("Should not subscribe to second").isFalse();
	}
	
	private Observable<Integer> listOfInts(AtomicBoolean subscriptionFlag, Integer... values) {
		return Observable
				.fromCallable(() -> Arrays.asList(values))
				.subscribeOn(Schedulers.io())
				.doOnSubscribe(() -> subscriptionFlag.set(true))
				//TODO What's the point of this concatMapIterable?
				.concatMapIterable(x -> x);
	}
	
	@Test
	public void mergeIsEager() throws Exception {
		AtomicBoolean firstSubscribed = new AtomicBoolean();
		AtomicBoolean secondSubscribed = new AtomicBoolean();
		
		//TODO Change take(N) to 0
		
		Observable
				.merge(     //Also mergeWith
						listOfInts(firstSubscribed, 1, 2, 3),
						listOfInts(secondSubscribed, 4, 5, 6)
				)
				.take(1)
				.toBlocking()
				.subscribe();
		assertThat(firstSubscribed.get()).describedAs("Should subscribe to first").isTrue();
		assertThat(secondSubscribed.get()).describedAs("Should subscribe to second").isTrue();
	}
	
	
}
