package com.nurkiewicz.rxjava;

import com.nurkiewicz.rxjava.util.Urls;
import org.junit.Ignore;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class R20_ObservableFromFile {
	
	
	/**
	 * Hint: Observable.defer or Observable.fromCallable
	 */
	@Test
	public void shouldNotFailWithoutSubscription() throws Exception {
		//when
		Observable<URL> all = Urls.all("urrrrrls.txt");
		
		//then
		//no exception thrown
	}
	
	@Test
	public void shouldFailWhenMissingFile() throws Exception {
		//given
		Observable<URL> all = Urls.all("urrrrrls.txt");
		TestSubscriber<URL> subscriber = new TestSubscriber<>();
		
		//when
		all.subscribe(subscriber);
		
		//then
		subscriber.assertNoValues();
		subscriber.assertError(FileNotFoundException.class);
		assertThat(subscriber.getOnErrorEvents().get(0)).hasMessageContaining("urrrrrls.txt");
	}
	
	@Test
	public void shouldFailWhenBrokenFile() throws Exception {
		Observable<URL> all = Urls.all("urls_broken.txt");
		TestSubscriber<URL> subscriber = new TestSubscriber<>();
		
		//when
		all.subscribe(subscriber);
		
		//then
		subscriber.assertError(IllegalArgumentException.class);
		assertThat(subscriber.getOnErrorEvents().get(0)).hasMessageContaining("john@gmail.com");
	}
	
	@Test
	public void shouldParseAllUrls() throws Exception {
		//given
		Observable<URL> all = Urls.all("urls.txt");
		
		//when
		List<URL> list = all
				.toList()
				.toBlocking()
				.single();
		
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
