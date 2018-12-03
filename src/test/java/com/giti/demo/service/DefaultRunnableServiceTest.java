package com.giti.demo.service;

import com.giti.demo.domain.ApplicationDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRunnableServiceTest {

	private static final String EXPECTED_PATH = "/updates";
	private static final String NAME = "test";
	private static final String VERSION = "1.0.0";
	private static final String HOST = "localhost";
	private static final int PORT = 8080;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private DefaultRunnableService unitUnderTest;

	@Before
	public void before() {
		ReflectionTestUtils.setField(unitUnderTest, "serviceName", NAME);
		ReflectionTestUtils.setField(unitUnderTest, "serviceVersion", VERSION);
		ReflectionTestUtils.setField(unitUnderTest, "updateHost", HOST);
		ReflectionTestUtils.setField(unitUnderTest, "updatePort", PORT);
	}

	@After
	public void after() {
		verifyNoMoreInteractions(restTemplate);
	}

	@Test
	public void testRun() throws Exception {
		ApplicationDetails expectedDetails = new ApplicationDetails(NAME, currentHost(), VERSION);

		unitUnderTest.run();

		ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
		verify(restTemplate).postForLocation(uriCaptor.capture(), eq(expectedDetails));
		URI captured = uriCaptor.getValue();
		assertNotNull(captured);
		assertEquals(HOST, captured.getHost());
		assertEquals(PORT, captured.getPort());
		assertEquals(EXPECTED_PATH, captured.getPath());
	}

	@Test
	public void testRun_rest_exception() throws Exception {
		ApplicationDetails expectedDetails = new ApplicationDetails(NAME, currentHost(), VERSION);
		when(restTemplate.postForLocation(isA(URI.class), eq(expectedDetails))).thenThrow(new RuntimeException("bad"));

		try {
			unitUnderTest.run();
		} catch (Exception e) {
			fail("The exception should be suppressed in the runnable!");
		}

		verify(restTemplate).postForLocation(isA(URI.class), eq(expectedDetails));
	}

	private String currentHost() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}

}