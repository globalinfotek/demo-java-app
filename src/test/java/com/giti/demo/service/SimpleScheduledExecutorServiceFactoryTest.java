package com.giti.demo.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ScheduledExecutorService;

import static org.junit.Assert.*;

public class SimpleScheduledExecutorServiceFactoryTest {

	private SimpleScheduledExecutorServiceFactory unitUnderTest;

	@Before
	public void before() {
		unitUnderTest = new SimpleScheduledExecutorServiceFactory();
	}

	@After
	public void after() {

	}

	@Test
	public void testCreate() {
		ScheduledExecutorService result = unitUnderTest.create();

		assertNotNull(result);
	}

}