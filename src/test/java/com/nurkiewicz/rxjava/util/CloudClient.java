package com.nurkiewicz.rxjava.util;

import io.reactivex.Flowable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class CloudClient {
	
	public Flowable<BigDecimal> pricing() {
		return Flowable
				.timer(30, TimeUnit.SECONDS)
				.flatMap(x -> Flowable.error(new IOException("Service unavailable")));
	}
	
	public Flowable<BigDecimal> broken() {
		return Flowable.error(new RuntimeException("Out of service"));
	}
}
