package com.nurkiewicz.rxjava;

import com.nurkiewicz.rxjava.util.UrlDownloader;
import com.nurkiewicz.rxjava.util.Urls;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class R21_FlatMap {

	/**
	 * Hint: UrlDownloader.download()
	 * Hint: flatMap(), maybe concatMap()?
	 * Hint: Pair.of(...)
	 * Hint: Flowable.toMap()
	 */
	@Test
	public void shouldDownloadAllUrls() throws Exception {
		//given
		Flowable<URL> urls = Urls.all();

		//when
		Map<URI, String> bodies = urls
				.flatMap(url ->
						UrlDownloader
								.download(url)
								.subscribeOn(Schedulers.io())
								.map(body -> Pair.of(toUri(url), body)))
				.toMap(Pair::getKey, Pair::getValue)
				.blockingGet();

		//then
		assertThat(bodies).hasSize(996);
		assertThat(bodies).containsEntry(new URI("http://www.twitter.com"), "<html>www.twitter.com</html>");
		assertThat(bodies).containsEntry(new URI("http://www.aol.com"), "<html>www.aol.com</html>");
		assertThat(bodies).containsEntry(new URI("http://www.mozilla.org"), "<html>www.mozilla.org</html>");
	}

	/**
	 * Hint: flatMap with int parameter
	 */
	@Test
	public void downloadThrottled() throws Exception {
		//given
		Flowable<URL> urls = Urls.all().take(20);

		//when
		//Use UrlDownloader.downloadThrottled()
		Map<URI, String> bodies = urls
				.flatMap(url ->
								UrlDownloader
										.downloadThrottled(url)
										.subscribeOn(Schedulers.io())
										.map(body -> Pair.of(toUri(url), body)),
						UrlDownloader.TOTAL)
				.toMap(Pair::getKey, Pair::getValue)
				.blockingGet();

		//then
		assertThat(bodies).containsEntry(new URI("http://www.twitter.com"), "<html>www.twitter.com</html>");
		assertThat(bodies).containsEntry(new URI("http://www.adobe.com"), "<html>www.adobe.com</html>");
		assertThat(bodies).containsEntry(new URI("http://www.bing.com"), "<html>www.bing.com</html>");
	}

	private URI toUri(URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
