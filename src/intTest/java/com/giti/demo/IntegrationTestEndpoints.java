package com.giti.demo;

import com.giti.demo.domain.ApplicationDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class IntegrationTestEndpoints {

	/**
	 * This endpoint, created specifically for automated testing purposes, receives updates that are published from the
	 * scheduled service in the application. When Requests are received on this endpoint, it means that the application
	 * is working as designed.
	 * @param applicationDetails  non-null {@link ApplicationDetails} instance that has been deserialized from the HTTP
	 *                            request
	 */
	@PostMapping(value = "/updates")
	public void update(@RequestBody ApplicationDetails applicationDetails) {
		RequestHolderSingleton.INSTANCE.threadSafeDetails.add(applicationDetails);
	}
}
