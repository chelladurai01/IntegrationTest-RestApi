package com.bestbuy.searchplatform.integrationtests.commons;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Environment {

    private String name;
    private String desc;
    private boolean defaultEnv;
    private List<Configuration> configurations = new ArrayList<Configuration>(8);
    
	public Environment() {
	}

	public Environment(String name, String desc, boolean defaultEnv, List<String> appliesTo) {
		this.name = name;
		this.desc = desc;
		this.defaultEnv = defaultEnv;
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

	public boolean isDefaultEnv() {
		return defaultEnv;
	}

	public void setDefaultEnv(boolean defaultEnv) {
		this.defaultEnv = defaultEnv;
	}

	

	public List<Configuration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<Configuration> configurations) {
		this.configurations = configurations;
	}

	@Override
	public String toString() {
		return "Environment [name=" + name + ", desc=" + desc + ", defaultEnv="
				+ defaultEnv + ", configurations=" + configurations + "]";
	}
}
