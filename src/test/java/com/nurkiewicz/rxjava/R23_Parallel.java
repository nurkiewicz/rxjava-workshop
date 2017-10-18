package com.nurkiewicz.rxjava;

import com.nurkiewicz.rxjava.util.UrlDownloader;
import com.nurkiewicz.rxjava.util.Urls;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class R23_Parallel {
	
	@Test
	public void parallelOperator() throws Exception {
		//given
		Flowable<URL> urls = Urls.all();

		//when
		//Use UrlDownloader.downloadBlocking()
		List<String> bodies = urls
				.parallel(100)
				.runOn(Schedulers.io())
				.map(UrlDownloader::downloadBlocking)
				.sequential()
				.toList()
				.blockingGet();

		//then
		assertThat(bodies).hasSize(996);
		assertThat(bodies).contains("<html>www.twitter.com</html>", "<html>www.aol.com</html>", "<html>www.mozilla.org</html>");
	}

}
