package com.giti.demo.domain;

import java.util.Objects;

public class ApplicationDetails {

	private String name;
	private String hostName;
	private String version;

	public ApplicationDetails() {}

	public ApplicationDetails(String name, String hostName, String version) {
		this.name = name;
		this.hostName = hostName;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ApplicationDetails)) {
			return false;
		}
		ApplicationDetails details = (ApplicationDetails) o;
		return Objects.equals(name, details.name) &&
				Objects.equals(hostName, details.hostName) &&
				Objects.equals(version, details.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, hostName, version);
	}

	@Override
	public String toString() {
		return "ApplicationDetails{" +
				"name='" + name + '\'' +
				", hostName='" + hostName + '\'' +
				", version='" + version + '\'' +
				'}';
	}
}
