package com.giti.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class HealthService {

	private static final Logger logger = LoggerFactory.getLogger(HealthService.class);

	private static final String HEALTH_FILE_NAME = "health";
	private static final String MESSAGE = "The app is running!";

	@EventListener(value = ApplicationStartedEvent.class)
	public void onApplicationStartedEvent() {
		try {
			String tmpdir = System.getProperty("java.io.tmpdir");
			Files.write(Paths.get(tmpdir + "/" + HEALTH_FILE_NAME), MESSAGE.getBytes());
		} catch (Exception e) {
			logger.error("An exception occurred when attempting to write the health file.", e);
		}
	}
}
