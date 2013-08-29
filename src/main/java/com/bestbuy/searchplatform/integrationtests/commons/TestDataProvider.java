package com.bestbuy.searchplatform.integrationtests.commons;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.bestbuy.searchplatform.integrationtests.model.Banners;
import com.bestbuy.searchplatform.integrationtests.model.Banners.Banner;
import com.bestbuy.searchplatform.integrationtests.model.BoostsAndBlocks;
import com.bestbuy.searchplatform.integrationtests.model.BoostsAndBlocks.BoostsAndBlock;
import com.bestbuy.searchplatform.integrationtests.model.Facets;
import com.bestbuy.searchplatform.integrationtests.model.Facets.Facet;
import com.bestbuy.searchplatform.integrationtests.model.Keywordredirects;
import com.bestbuy.searchplatform.integrationtests.model.Keywordredirects.Keywordredirect;
import com.bestbuy.searchplatform.integrationtests.model.Promos;
import com.bestbuy.searchplatform.integrationtests.model.Promos.Promo;
import com.bestbuy.searchplatform.integrationtests.model.Synonyms;
import com.bestbuy.searchplatform.integrationtests.model.Synonyms.Synonym;

public class TestDataProvider<E> {

	@DataProvider(name = "synonymsdata")
	public static Iterator<Object[]> synonymsDataProvider (ITestContext context) throws JAXBException {

		String inputFile = context.getCurrentXmlTest().getParameter("synonymdataXml");
		Synonyms synonyms = getFileContentList(inputFile, Synonyms.class);
		List<Synonym> synonym = synonyms.getSynonym();
		return getDataObject(synonym);

	}
	
	@DataProvider(name = "keywordredirectdata")
	public static Iterator<Object[]> keywordRedirectDataProvider (ITestContext context) throws JAXBException {

		String inputFile = context.getCurrentXmlTest().getParameter("keywordredirectdataXml");
		Keywordredirects keywordredirects = getFileContentList(inputFile, Keywordredirects.class);
		List<Keywordredirect> keywordredirect = keywordredirects.getKeywordredirect();
		return getDataObject(keywordredirect);

	}
	
	@DataProvider(name = "promosdata")
	public static Iterator<Object[]> promosDataProvider (ITestContext context) throws JAXBException {

		String inputFile = context.getCurrentXmlTest().getParameter("promodataXml");
		Promos promos = getFileContentList(inputFile, Promos.class);
		List<Promo> promo = promos.getPromo();
		return getDataObject(promo);

	}
	
	@DataProvider(name = "facetsdata")
	public static Iterator<Object[]> facetsDataProvider (ITestContext context) throws JAXBException {

		String inputFile = context.getCurrentXmlTest().getParameter("facetdataXml");
		Facets facets = getFileContentList(inputFile, Facets.class);
		List<Facet> facet = facets.getFacet();
		return getDataObject(facet);

	}
	
	@DataProvider(name = "bannersdata")
	public static Iterator<Object[]> bannersDataProvider (ITestContext context) throws JAXBException {

		String inputFile = context.getCurrentXmlTest().getParameter("bannerdataXml");
		Banners banners = getFileContentList(inputFile, Banners.class);
		List<Banner> banner = banners.getBanner();
		return getDataObject(banner);

	}
	
	@DataProvider(name = "boostandblockdata")
	public static Iterator<Object[]> boostAndBlockDataProvider (ITestContext context) throws JAXBException {

		String inputFile = context.getCurrentXmlTest().getParameter("boostandblockdataXml");
		BoostsAndBlocks boostsAndBlocks = getFileContentList(inputFile, BoostsAndBlocks.class);
		List<BoostsAndBlock> boostsAndBlock = boostsAndBlocks.getBoostsAndBlock();
		return getDataObject(boostsAndBlock);

	}

	private static <T> Iterator<Object[]> getDataObject(List<T> objList) {
		List<Object[]> dataToBeReturned = new ArrayList<Object[]>();
		for (T userData : objList) {
			dataToBeReturned.add(new Object[] { userData } );
		}

		return dataToBeReturned.iterator();
	}
	
	

	@SuppressWarnings("unchecked")
	private static <T> T getFileContentList(String filenamePath, Class<T> clazz) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		InputStream resourceAsStream = clazz.getResourceAsStream(filenamePath);
		return (T) jaxbUnmarshaller.unmarshal(resourceAsStream);
	}
}
