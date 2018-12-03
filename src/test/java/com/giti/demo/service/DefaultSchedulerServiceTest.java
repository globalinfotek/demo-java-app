package com.giti.demo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSchedulerServiceTest {

	private static final long EXPECTED_DELAY = 1;
	private static final long EXPECTED_PERIOD = 5;
	private static final TimeUnit EXPECTED_TIME_UNIT = TimeUnit.SECONDS;

	@Mock
	private RunnableService runnableService;

	@Mock
	private ScheduledExecutorServiceFactory scheduledExecutorServiceFactory;

	@InjectMocks
	private DefaultSchedulerService unitUnderTest;

	@Test
	public void testSchedule() {
		ScheduledExecutorService mockExecutorService = mock(ScheduledExecutorService.class);
		when(scheduledExecutorServiceFactory.create()).thenReturn(mockExecutorService);

		unitUnderTest.schedule();

		verify(scheduledExecutorServiceFactory).create();
		verify(mockExecutorService).scheduleAtFixedRate(runnableService, EXPECTED_DELAY, EXPECTED_PERIOD, EXPECTED_TIME_UNIT);
	}

}