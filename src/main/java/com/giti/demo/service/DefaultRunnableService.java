package com.giti.demo.service;

import com.giti.demo.domain.ApplicationDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.InetAddress;
import java.net.URI;

@Service
public class DefaultRunnableService implements RunnableService {

	private static final Logger logger = LoggerFactory.getLogger(DefaultRunnableService.class);

	@Value("${service.name}")
	private String serviceName;

	@Value("${service.version}")
	private String serviceVersion;

	@Value("${service.updateHost}")
	private String updateHost;

	@Value("${service.updatePort}")
	private int updatePort;

	private RestTemplate restTemplate;

	public DefaultRunnableService() {
		restTemplate = new RestTemplate();
	}

	@Override
	public void run() {
		try {
			String hostName = InetAddress.getLocalHost().getHostName();
			ApplicationDetails details = new ApplicationDetails(serviceName, hostName, serviceVersion);
			URI uri = buildUri();
			logger.info("Sending an update message to the listening server [ {} ]. ", uri);
			restTemplate.postForLocation(uri, details);
		} catch (Exception e) {
			logger.error("An exception occurred while submitting the request.", e);
		}
	}

	private URI buildUri() {
		return new DefaultUriBuilderFactory().builder()
				.scheme("http")
				.host(updateHost)
				.port(updatePort)
				.path("updates")
				.build();
	}
}
