package com.nurkiewicz.rxjava.util;

import com.google.common.collect.AbstractIterator;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Supplier;

public class InfiniteReader extends Reader {
	
	private final CharSupplyingIterator characters;
	
	public InfiniteReader(Supplier<String> source) {
		this.characters = new CharSupplyingIterator(source);
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		for(int i = off; i < off + len; ++i) {
			cbuf[i] = characters.next();
		}
		return len;
	}
	
	@Override
	public void close() throws IOException {
		
	}
}

class CharSupplyingIterator extends AbstractIterator<Character> {
	
	private final Supplier<String> source;
	private String current = "";
	
	CharSupplyingIterator(Supplier<String> source) {
		this.source = source;
	}
	
	@Override
	protected Character computeNext() {
		while (current.isEmpty()) {
			current = source.get();
		}
		char result = current.charAt(0);
		current = current.substring(1);
		return result;
	}
}