package com.bestbuy.searchplatform.integrationtests.commons;

import java.util.Properties;
/**
 * 
 * Class to declare the variables used to read the Configuration 
 *
 */
public class Configuration {

	private String name;
	private String desc;
	private String type;
	private String envName;
	private Properties properties;

	public Configuration() { // Added for Action class to get Configuration as json from jsp
		super();
	}

	public Configuration(String name, String desc, String envName, String type, Properties properties) {
		super();
		this.name = name;
		this.desc = desc;
		this.envName = envName;
		this.type = type;
		this.properties = properties;
	}

	public Configuration(String name, String desc, String type, Properties properties) {
		this (name, desc, null, type, properties);
	}

	public Configuration(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "Configuration [name=" + name + ", type=" + type
				+ ", properties=" + properties + "]";
	}

}

