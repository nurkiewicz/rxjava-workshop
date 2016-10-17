package com.nurkiewicz.rxjava.util;

import org.junit.Test;

import java.io.BufferedReader;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class InfiniteReaderTest {
	
	@Test
	public void shouldReturnSubsequentNumbers() throws Exception {
		//given
		InfiniteReader infinite = new InfiniteReader(NumberSupplier.lines());
		
		//when
		BufferedReader reader = new BufferedReader(infinite);
		
		//then
		assertThat(reader.readLine()).isEqualTo("0");
		assertThat(reader.readLine()).isEqualTo("1");
		assertThat(reader.readLine()).isEqualTo("2");
		assertThat(reader.readLine()).isEqualTo("3");
		assertThat(reader.readLine()).isEqualTo("4");
		assertThat(reader.readLine()).isEqualTo("5");
	}
	
	@Test
	public void shouldWorkWithLargeNumbers() throws Exception {
		InfiniteReader infinite = new InfiniteReader(NumberSupplier.lines());
		
		//when
		BufferedReader reader = new BufferedReader(infinite);
		
		//then
		List<String> lines = reader.lines().skip(1_000_000).limit(4).collect(toList());
		assertThat(lines).containsExactly("1000000", "1000001", "1000002", "1000003");
	}
	
	
}

