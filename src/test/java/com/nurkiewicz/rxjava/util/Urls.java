package com.nurkiewicz.rxjava.util;

import rx.Observable;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class Urls {
	
	public static Observable<URL> all() {
		return all("urls.txt");
	}
	
	public static Observable<URL> all(String fileName) {
		return Observable.defer(() -> load(fileName));
	}
	
	private static Observable<URL> load(String fileName) {
		try (Stream<String> lines = classpathReaderOf(fileName).lines()) {
			return Observable.empty();
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
