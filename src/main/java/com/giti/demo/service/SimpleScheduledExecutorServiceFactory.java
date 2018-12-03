package com.giti.demo.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class SimpleScheduledExecutorServiceFactory implements ScheduledExecutorServiceFactory {

	private static final int DEFAULT_POOL_SIZE = 1;

	@Override
	public ScheduledExecutorService create() {
		return Executors.newScheduledThreadPool(DEFAULT_POOL_SIZE);
	}
}
