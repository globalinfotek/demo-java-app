package com.giti.demo.service;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class HealthServiceTest {

	private HealthService unitUnderTest;

	@Before
	public void before() {
		unitUnderTest = new HealthService();
	}

	@Test
	public void testOnApplicationStartedEvent() throws Exception {
		String tmpdir = System.getProperty("java.io.tmpdir");
		Path path = Paths.get(tmpdir + "/health");
		Files.delete(path);

		unitUnderTest.onApplicationStartedEvent();

		List<String> content = Files.readAllLines(path);
		assertNotNull(content);
		assertFalse(content.isEmpty());
	}

}