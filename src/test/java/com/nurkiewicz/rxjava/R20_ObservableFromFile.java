package com.nurkiewicz.rxjava;

import com.nurkiewicz.rxjava.util.Urls;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class R20_ObservableFromFile {
	
	
	/**
	 * TODO: Implement Urls.all()
	 * Hint: Flowable.defer or Flowable.fromCallable
	 */
	@Test
	public void shouldNotFailWithoutDisposable() throws Exception {
		//when
		Flowable<URL> all = Urls.all("urrrrrls.txt");
		
		//then
		//no exception thrown
	}
	
	@Test
	public void shouldFailWhenMissingFile() throws Exception {
		//given
		Flowable<URL> all = Urls.all("urrrrrls.txt");
		
		//when
		final TestSubscriber<URL> observer = all.test();
		
		//then
		observer.assertNoValues();
		observer.assertError(FileNotFoundException.class);
		assertThat(observer.errors().get(0)).hasMessageContaining("urrrrrls.txt");
	}

	@Test
	public void shouldParseAllUrls() throws Exception {
		//given
		Flowable<URL> all = Urls.all("urls.txt");
		
		//when
		List<URL> list = all
				.toList()
				.blockingGet();
		
		//then
		assertThat(list).hasSize(996);
		assertThat(list).startsWith(
				new URL("http://www.google.com"),
				new URL("http://www.youtube.com"),
				new URL("http://www.facebook.com"));
		assertThat(list).endsWith(
				new URL("http://www.king.com"),
				new URL("http://www.virginmedia.com"));
	}

}
