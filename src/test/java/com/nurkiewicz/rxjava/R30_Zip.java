package com.nurkiewicz.rxjava;

import org.junit.Ignore;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

@Ignore
public class R30_Zip {
	
	public static final Observable<String> LOREM_IPSUM = Observable.just("Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit");
	
	@Test
	public void zipTwoStreams() throws Exception {
		//given
		Observable<String> zipped = Observable
				.zip(
						LOREM_IPSUM,
						Observable.range(1, 1_000),
						(word, num) -> word + "-" + num)
				.take(3);
		TestSubscriber<String> subscriber = new TestSubscriber<>();
		
		//when
		zipped.subscribe(subscriber);
		
		//then
		subscriber.assertCompleted();
		subscriber.assertNoErrors();
		subscriber.assertValues("Lorem-1", "ipsum-2", "dolor-3");
	}
	
	/**
	 * Hint: Observable.range(1, 3).repeat()
	 * Hint: Pair.of(...)
	 * Hint: filter()
	 */
	@Test
	public void everyThirdWord() throws Exception {
		//given
		TestSubscriber<String> subscriber = new TestSubscriber<>();
		Observable<String> everyThirdWord = LOREM_IPSUM;
		
		//when
		everyThirdWord.subscribe(subscriber);
		
		//then
		subscriber.assertValues("dolor", "consectetur");
	}

}
