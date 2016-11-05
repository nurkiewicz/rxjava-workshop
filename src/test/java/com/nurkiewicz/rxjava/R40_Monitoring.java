package com.nurkiewicz.rxjava;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.nurkiewicz.rxjava.util.UrlDownloader;
import com.nurkiewicz.rxjava.util.Urls;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Ignore
public class R40_Monitoring {
	
	private Slf4jReporter reporter;
	private Counter counter;
	private MetricRegistry metricRegistry;
	
	@Before
	public void setupMetrics() {
		metricRegistry = new MetricRegistry();
		reporter = Slf4jReporter
				.forRegistry(metricRegistry)
				.outputTo(LoggerFactory.getLogger(R40_Monitoring.class))
				.build();
		reporter.start(1, TimeUnit.SECONDS);
		counter = new Counter();
		metricRegistry.register("counter", counter);
	}
	
	@After
	public void cleanupMetrics() {
		reporter.close();
		metricRegistry.remove("counter");
	}
	
	/**
	 * TODO Limit the number of concurrent downloads, prove with metric it worked
	 */
	@Test
	public void ongoingRequests() throws Exception {
		TestSubscriber<String> subscriber = new TestSubscriber<>();
		Urls
				.all()
				.flatMap(url -> UrlDownloader.downloadAsync(url, Schedulers.io()))
				.blockingSubscribe(subscriber);
	}
}
