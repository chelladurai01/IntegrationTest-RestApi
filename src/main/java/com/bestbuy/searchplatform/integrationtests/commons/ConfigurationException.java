package com.bestbuy.searchplatform.integrationtests.commons;
/**
 * 
 * Class to read the exception message.
 *
 */
public class ConfigurationException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfigurationException() {

	}

	public ConfigurationException(String errorMessage) {
		super(errorMessage);
	}

	public ConfigurationException(Throwable throwable) {
		super(throwable);
	}

	public ConfigurationException(String errorMessage, 
			Throwable throwable) {
		super(errorMessage, throwable);
	}
}
