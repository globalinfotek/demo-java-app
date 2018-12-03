package com.giti.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class DefaultSchedulerService implements SchedulerService {

	private static final long INITIAL_DELAY = 1;
	private static final long PERIOD = 5;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	private RunnableService runnableService;
	private ScheduledExecutorServiceFactory scheduledExecutorServiceFactory;

	@Autowired
	public DefaultSchedulerService(ScheduledExecutorServiceFactory scheduledExecutorServiceFactory,
								   RunnableService defaultRunnableService) {
		this.scheduledExecutorServiceFactory = scheduledExecutorServiceFactory;
		this.runnableService = defaultRunnableService;
	}

	@Override
	@EventListener(value = ApplicationStartedEvent.class)
	public void schedule() {
		ScheduledExecutorService scheduler = scheduledExecutorServiceFactory.create();
		scheduler.scheduleAtFixedRate(runnableService, INITIAL_DELAY, PERIOD, TIME_UNIT);
	}
}
