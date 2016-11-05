package com.nurkiewicz.rxjava;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class R30_Zip {

	public static final Flowable<String> LOREM_IPSUM = Flowable.just("Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit");

	@Test
	public void zipTwoStreams() throws Exception {
		//given
		Flowable<String> zipped = Flowable
				.zip(
						LOREM_IPSUM,
						Flowable.range(1, 1_000),
						(word, num) -> word + "-" + num)
				.take(3);

		//when
		final TestSubscriber<String> subscriber = zipped.test();

		//then
		subscriber.assertComplete();
		subscriber.assertNoErrors();
		subscriber.assertValues("Lorem-1", "ipsum-2", "dolor-3");
	}

	/**
	 * Hint: Flowable.range(1, 3).repeat()
	 * Hint: Pair.of(...)
	 * Hint: filter()
	 */
	@Test
	public void everyThirdWord() throws Exception {
		//given
		Flowable<String> everyThirdWord = LOREM_IPSUM;

		//when
		final TestSubscriber<String> subscriber = everyThirdWord.test();

		//then
		subscriber.assertValues("dolor", "consectetur");
	}

}
