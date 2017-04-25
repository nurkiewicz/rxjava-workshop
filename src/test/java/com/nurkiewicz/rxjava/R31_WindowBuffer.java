package com.nurkiewicz.rxjava;

import io.reactivex.Flowable;
import org.junit.Ignore;
import org.junit.Test;

import static com.nurkiewicz.rxjava.R30_Zip.LOREM_IPSUM;

@Ignore
public class R31_WindowBuffer {
	
	/**
	 * Hint: use buffer()
	 */
	@Test
	public void everyThirdWordUsingBuffer() throws Exception {
		//given
		Flowable<String> everyThirdWord = LOREM_IPSUM
				.buffer(3)
				.filter(list -> list.size() == 3)
				.map(list -> list.get(2));

		//then
		everyThirdWord
				.test()
				.assertValues("dolor", "consectetur");
	}
	
	/**
	 * Hint: use window()
	 * Hint: use elementAt()
	 */
	@Test
	public void everyThirdWordUsingWindow() throws Exception {
		//given
		Flowable<String> everyThirdWord = LOREM_IPSUM
				.window(3)
				.flatMap(obs -> obs.elementAt(2).toFlowable());

		//then
		everyThirdWord
				.test()
				.assertValues("dolor", "consectetur");
	}
	
}
