package com.giti.demo.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationDetailsTest {

	private static final String EXPECTED_NAME = "test-name";
	private static final String EXPECTED_HOST_NAME = "host.name";
	private static final String EXPECTED_VERSION = "test-version";

	@Test
	public void testConstructors() {
		ApplicationDetails details = new ApplicationDetails();
		assertNull(details.getName());
		assertNull(details.getVersion());

		details = new ApplicationDetails(EXPECTED_NAME, EXPECTED_HOST_NAME, EXPECTED_VERSION);
		assertEquals(EXPECTED_NAME, details.getName());
		assertEquals(EXPECTED_HOST_NAME, details.getHostName());
		assertEquals(EXPECTED_VERSION, details.getVersion());
	}

	@Test
	public void testSettersGetters() {
		ApplicationDetails details = new ApplicationDetails();
		details.setName(EXPECTED_NAME);
		details.setVersion(EXPECTED_VERSION);

		assertEquals(EXPECTED_NAME, details.getName());
		assertEquals(EXPECTED_VERSION, details.getVersion());
	}

	@Test
	public void testEquals() {
		ApplicationDetails first = new ApplicationDetails(EXPECTED_NAME, EXPECTED_HOST_NAME, EXPECTED_VERSION);
		ApplicationDetails second = new ApplicationDetails(EXPECTED_NAME, EXPECTED_HOST_NAME, EXPECTED_VERSION);
		ApplicationDetails third = new ApplicationDetails(EXPECTED_NAME, EXPECTED_HOST_NAME, EXPECTED_VERSION);
		ApplicationDetails other = new ApplicationDetails();
		ApplicationDetails differentHost = new ApplicationDetails(EXPECTED_NAME, "other", EXPECTED_VERSION);
		ApplicationDetails differenVersion = new ApplicationDetails(EXPECTED_NAME, EXPECTED_HOST_NAME, "other");

		// Null input
		assertFalse(first.equals(null));

		// Reflexive
		assertEquals(first, first);

		// Symmetric
		assertEquals(first, second);
		assertEquals(second, first);

		// Transitive
		assertEquals(first, second);
		assertEquals(second, third);
		assertEquals(first, third);

		// Not Equal
		assertNotEquals(first, other);
		assertNotEquals(first, differentHost);
		assertNotEquals(first, differenVersion);
	}

	@Test
	public void testHashCode() {
		ApplicationDetails first = new ApplicationDetails(EXPECTED_NAME, EXPECTED_HOST_NAME, EXPECTED_VERSION);
		ApplicationDetails second = new ApplicationDetails(EXPECTED_NAME, EXPECTED_HOST_NAME, EXPECTED_VERSION);
		ApplicationDetails other = new ApplicationDetails();


		assertEquals(first.hashCode(), first.hashCode());
		assertEquals(first.hashCode(), second.hashCode());
		assertNotEquals(first.hashCode(), other.hashCode());
	}

	@Test
	public void testToString() {
		ApplicationDetails details = new ApplicationDetails();

		String result = details.toString();

		assertTrue(result.contains("name"));
		assertTrue(result.contains("hostName"));
		assertTrue(result.contains("version"));
	}

}