package com.giti.demo;

import com.giti.demo.domain.ApplicationDetails;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTest {

	private static final long MAX_WAIT_SECONDS = 10;

	@Value("${service.name}")
	private String expectedName;

	@Value("${service.version}")
	private String getExpectedVersion;

	@Test
	public void testMessagesArePublished() throws Exception {
		StopWatch stopWatch = new StopWatch();
		long seconds = 0;
		while (seconds <= MAX_WAIT_SECONDS) {
			if (areMessagesPublished()) {
				return;
			}
			seconds = stopWatch.getTime(TimeUnit.SECONDS);
		}
		fail("No requests were published by the application!");
	}

	private boolean areMessagesPublished() throws Exception {
		if (RequestHolderSingleton.INSTANCE.threadSafeDetails.isEmpty()) {
			return false;
		}
		ApplicationDetails published = RequestHolderSingleton.INSTANCE.threadSafeDetails.get(0);
		assertNotNull(published);
		assertEquals(expectedName, published.getName());
		assertEquals(InetAddress.getLocalHost().getHostName(), published.getHostName());
		assertEquals(getExpectedVersion, published.getVersion());
		return true;
	}
}
