package com.nurkiewicz.rxjava;

import io.reactivex.Flowable;
import org.junit.Test;

import static com.nurkiewicz.rxjava.R30_Zip.LOREM_IPSUM;

public class R31_WindowBuffer {
	
	/**
	 * Hint: use buffer()
	 */
	@Test
	public void everyThirdWordUsingBuffer() throws Exception {
		//given
		Flowable<String> everyThirdWord = LOREM_IPSUM;
		
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
		Flowable<String> everyThirdWord = LOREM_IPSUM;
		
		//then
		everyThirdWord
				.test()
				.assertValues("dolor", "consectetur");
	}
	
}
