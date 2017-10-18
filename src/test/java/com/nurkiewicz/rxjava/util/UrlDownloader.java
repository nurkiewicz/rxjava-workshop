package com.nurkiewicz.rxjava.util;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.concurrent.Semaphore;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;

public class UrlDownloader {
	
	private static final Logger log = LoggerFactory.getLogger(UrlDownloader.class);
	
	public static final int TOTAL = 10;
	private static final Semaphore counter = new Semaphore(TOTAL);
	
	public static Flowable<String> downloadAsync(URL url, Scheduler scheduler) {
		return Flowable
				.fromCallable(() -> downloadBlocking(url))
				.subscribeOn(scheduler);
	}
	
	public static Flowable<String> download(URL url) {
		return Flowable.fromCallable(() -> downloadBlocking(url));
	}
	
	/**
	 * Fails if more than `TOTAL` number of concurrent connections
	 */
	public static Flowable<String> downloadThrottled(URL url) {
		return Flowable.fromCallable(() -> {
			if (counter.tryAcquire()) {
				try {
					return downloadBlocking(url);
				} finally {
					counter.release();
				}
			} else {
				throw new RuntimeException("Too many concurrent connections: " + TOTAL);
			}
		});
	}
	
	public static String downloadBlocking(URL url) {
		log.trace("Downloading: {}", url);
		Sleeper.sleep(ofSeconds(1), ofMillis(500));
		log.trace("Done: {}", url);
		return "<html>" + url.getHost() + "</html>";
	}
	
}
