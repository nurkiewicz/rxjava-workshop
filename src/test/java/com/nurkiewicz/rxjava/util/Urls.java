package com.nurkiewicz.rxjava.util;

import rx.Observable;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Urls {
	
	public static Observable<URL> all() {
		return all("urls.txt");
	}
	
	public static Observable<URL> all(String fileName) {
		return Observable.defer(() -> load(fileName));
	}
	
	private static Observable<URL> load(String fileName) {
		try (Stream<String> lines = classpathReaderOf(fileName).lines()) {
//			Observable<List<URL>> obs = Observable.fromCallable(() -> {
//				return lines
//						.map(line -> {
//							try {
//								return new URL(line);
//							} catch (MalformedURLException e) {
//								throw new IllegalArgumentException(line);
//							}
//						})
//						.collect(toList());
//			});
//			return obs
//					.flatMap((List<URL> x) -> Observable.from(x));
//			return obs
//					.concatMapIterable((List<URL> x) -> x);
			
			
//			Stream
//					.of(1,2,3)
//					.flatMap(x -> Arrays.asList(x, -x).stream())
			
			List<URL> urls = lines
					.map(line -> {
						try {
							return new URL(line);
						} catch (MalformedURLException e) {
							throw new IllegalArgumentException(line);
						}
					})
					.collect(toList());
			return Observable.from(urls);
		} catch (Exception e) {
			return Observable.error(e);
		}
	}
	
	private static BufferedReader classpathReaderOf(String fileName) throws IOException {
		URL input = Urls.class.getResource(fileName);
		if (input == null) {
			throw new FileNotFoundException(fileName);
		}
		InputStream is = input.openStream();
		return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
	}

}
