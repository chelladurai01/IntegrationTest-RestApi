package com.bestbuy.searchplatform.integrationtests.rest;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 
 * Class to declare variables for login method
 *  
 */

public class RestClient {
	private String host = "";
	private String port = "";
	private String applicationPath = "";
	private String loginPath = "j_spring_security_check";
	private String logoutPath = "logout";
	private final String usernameInputFieldName = "j_username";
	private final String passwordInputFieldName = "j_password";
	private final StatefullRestTemplate template = new StatefullRestTemplate();

	public RestClient(String host, String port, String applicationPath) {
		this.host = host;
		this.port = port;
		this.applicationPath = applicationPath;
	}
	/**
	 * Method to login 
	 * @param username
	 * @param password
	 * @return
	 */
	
	public String login(String username, String password) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.add(usernameInputFieldName, username);
		form.add(passwordInputFieldName, password);
		
		MultiValueMap<String, String> urlVariables = new LinkedMultiValueMap<String, String>();
		urlVariables.add("Content-Type", "application/x-www-form-urlencoded");
		
		this.template.getHttpContext().setAttribute("Content-Type", "application/x-www-form-urlencoded");
		URI location = this.template.postForLocation(loginUrl(), form, urlVariables);
		String locationStr = location.toString();
		String sessionId = locationStr.substring(locationStr.indexOf("jsessionid=") + 11);
		return sessionId;
	}
	
	public ResponseEntity<String> logout() {
		return this.template.getForEntity(logoutUrl(), String.class);
	}

	public String applicationUrl(String relativePath) {
		return applicationUrl() + "/" + relativePath;
	}

	public StatefullRestTemplate getTemplate() {
		return template;
	}

	public String serverUrl() {
		return "http://" + host + ":" + port;
	}

	public String applicationUrl() {
		return serverUrl() + "/" + applicationPath;
	}

	public String loginUrl() {
		return applicationUrl(loginPath);
	}

	public String logoutUrl() {
		return applicationUrl(logoutPath);
	}

	public void setLogoutPath(String logoutPath) {
		this.logoutPath = logoutPath;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getApplicationPath() {
		return applicationPath;
	}

	public void setApplicationPath(String contextPath) {
		this.applicationPath = contextPath;
	}

	public String getLoginPath() {
		return loginPath;
	}

	public void setLoginPath(String loginPath) {
		this.loginPath = loginPath;
	}

	public String getLogoutPath() {
		return logoutPath;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RestClient [\n serverUrl()=");
		builder.append(serverUrl());
		builder.append(", \n applicationUrl()=");
		builder.append(applicationUrl());
		builder.append(", \n loginUrl()=");
		builder.append(loginUrl());
		builder.append(", \n logoutUrl()=");
		builder.append(logoutUrl());
		builder.append(", \n apiUrl()=");
		builder.append("\n]");
		return builder.toString();
	}
}