package com.giti.demo;

import com.giti.demo.domain.ApplicationDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This enum provides a thread-safe way of keeping track of requests that are submitted by the scheduled application
 * service.
 */
public enum RequestHolderSingleton {

	INSTANCE;

	List<ApplicationDetails> threadSafeDetails = Collections.synchronizedList(new ArrayList<>());

}
