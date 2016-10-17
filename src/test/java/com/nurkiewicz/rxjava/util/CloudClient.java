package com.nurkiewicz.rxjava.util;

import rx.Observable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class CloudClient {
	
	public Observable<BigDecimal> pricing() {
		return Observable
				.timer(30, TimeUnit.SECONDS)
				.flatMap(x -> Observable.error(new IOException("Service unavailable")));
	}
	
	public Observable<BigDecimal> broken() {
		return Observable.error(new RuntimeException("Out of service"));
	}
}
