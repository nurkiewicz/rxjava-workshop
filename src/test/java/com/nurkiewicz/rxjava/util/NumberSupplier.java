package com.nurkiewicz.rxjava.util;

import java.math.BigInteger;
import java.util.function.Supplier;

public class NumberSupplier implements Supplier<BigInteger> {
	
	private BigInteger current = BigInteger.ZERO;
	
	@Override
	public BigInteger get() {
		BigInteger next = current;
		current = current.add(BigInteger.ONE);
		return next;
	}
	
	public static Supplier<String> lines() {
		NumberSupplier numbers = new NumberSupplier();
		return () -> numbers.get().toString() + "\n";
	}
}
