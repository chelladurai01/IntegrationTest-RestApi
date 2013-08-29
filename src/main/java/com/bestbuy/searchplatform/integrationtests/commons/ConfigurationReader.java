package com.bestbuy.searchplatform.integrationtests.commons;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * Class to declare the variables used to read the env-config.xml 
 * 
 *
 */
public class ConfigurationReader {

	private static final Map<String, Element> ENV_MAP = new LinkedHashMap<String, Element>();
	private static String defaultEnvironment = null;
	private Document document = null;


	/**
	 * ConfigReader single instance created by configuration xml input stream
	 * @param xmlStream
	 * @return
	 * @throws Exception
	 */
	public ConfigurationReader(InputStream xmlStream) throws Exception {
		initXML(xmlStream);
	}
	
	/**
	 * loads the configuration xml as input stream
	 * @param xmlStream
	 * @throws ConfigurationException 
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws Exception
	 */
	protected void initXML(InputStream xmlStream) throws ConfigurationException {
		try {
			if (xmlStream == null) {
				xmlStream = this.getClass().getClassLoader().getResourceAsStream("configuration.xml");
			}
			parseXML(xmlStream);
		} finally {
			try {
				if (xmlStream != null) {
					xmlStream.close();
				}
			} catch (IOException e) {
				throw new ConfigurationException(e);
			}
		}
	}
	
	/**
	 * Creating Dom object to parse the configuration xml
	 * @param xmlStream
	 * @throws ConfigurationException 
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws Exception
	 */
	private void parseXML(InputStream xmlStream) throws ConfigurationException  {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(false);
		DocumentBuilder builder;
		try {
			builder = domFactory.newDocumentBuilder();
			document = builder.parse(xmlStream);
		} catch (ParserConfigurationException e) {
			throw new ConfigurationException(e);
		} catch (SAXException e) {
			throw new ConfigurationException(e);
		} catch (IOException e) {
			throw new ConfigurationException(e);
		}
		parseDocument(document);
	}
	
	/**
	 * parse the configuration xml
	 * @param documen
	 */
	private void parseDocument(Document document) {
		//get a nodelist of environments
		NodeList environmentList = document.getElementsByTagName("environment");
		ENV_MAP.clear();

		for(int i = 0 ; i < environmentList.getLength(); i++) {

			//get the environment element
			Element environment = (Element) environmentList.item(i);
			String envName = environment.getAttribute("name");
			boolean defaultEnv = Boolean.parseBoolean(environment.getAttribute("default"));
			if (defaultEnv) {
				defaultEnvironment = envName;
			}
			//add environment element to map
			ENV_MAP.put(envName, environment);
		}
	}

	/**
	 * Returns the defaut environment name
	 * @return
	 */
	public String getDefaultEnvName() {
		return defaultEnvironment;
	}

	/**
	 * Returns the Configurations of given environments
	 * @param envName - Environment name
	 * @return
	 */
	@SuppressWarnings("unused")
	public List<Configuration> getConfigByEnv(String envName) {
		List<Configuration> configurations = new ArrayList<Configuration>();
		Element environment = getEnvironment(envName);
		if (environment != null) {
			NodeList configNodes = environment.getChildNodes();
			for (int i = 0; i < configNodes.getLength(); i++) {
				Node node = configNodes.item(i);
				if (configNodes.item(i).getNodeType() !=  Element.TEXT_NODE && configNodes.item(i).getNodeType() !=  Element.COMMENT_NODE
						&& configNodes.item(i).getNodeType() !=  Element.CDATA_SECTION_NODE) {
					Element configNode = (Element) configNodes.item(i);
					String configType = configNode.getNodeName();
					String configName = configNode.getAttribute("name");
					String configDesc = configNode.getAttribute("desc");
					Properties properties = getProperties(configNode);
					Configuration config = new Configuration(configName, configDesc, envName, configType, properties);
					configurations.add(config);
				}
			}
		}
		return configurations;
	}
	
	/**
	 * return the environment element for the given Environment name
	 * @param envName
	 * @return
	 */
	protected Element getEnvironment(String envName) {
		return ENV_MAP.get(envName);
	}

	/**
	 * return the property of the given configuration
	 * @param configNode
	 * @return
	 */
	private Properties getProperties(Element configNode) {
		Properties props = new Properties();
		NodeList propNodes = configNode.getChildNodes();
		for(int i = 0 ; i < propNodes.getLength(); i++) {
			if (propNodes.item(i).getNodeType() !=  Element.TEXT_NODE && propNodes.item(i).getNodeType() !=  Element.COMMENT_NODE
					&& propNodes.item(i).getNodeType() !=  Element.CDATA_SECTION_NODE) {
				//get the environment element
				Element propNode = (Element) propNodes.item(i);
				String propName = propNode.getNodeName();
				String propValue = propNode.getTextContent();
				props.put(propName, propValue);
			}
		}
		return props;
	}
	
	/**
	 * Returns the Configuration of given environment, configuration type, configuration name
	 * @param envName
	 * @param configType
	 * @return
	 */
	public Configuration getConfiguration(String envName, String configType, String configName) {
		List<Configuration> configurations = getConfigByEnv(envName);
		for (Configuration configuration : configurations) {
			if (configuration.getType().equalsIgnoreCase(configType) && configuration.getName().equalsIgnoreCase(configName)) {
				return configuration;
			}
		}
		return null;
	}

}
