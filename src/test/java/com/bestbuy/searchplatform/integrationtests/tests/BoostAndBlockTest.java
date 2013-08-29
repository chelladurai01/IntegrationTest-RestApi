package com.bestbuy.searchplatform.integrationtests.tests;

import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.BOOSTBLOCK_APPROVE;
import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.BOOSTBLOCK_CREATE;
import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.BOOSTBLOCK_SEARCH;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bestbuy.searchplatform.integrationtests.model.BoostsAndBlocks.BoostsAndBlock;
import com.bestbuy.searchplatform.integrationtests.model.BoostsAndBlocks.BoostsAndBlock.BoostProduct;

public class BoostAndBlockTest extends BaseTest {	

	private final static Logger log = Logger.getLogger("BoostsAndBlocksTest");

	@BeforeClass(alwaysRun = true)
	public void setUpBeforeClass() throws Exception {

	}

	/**
	 * performs testFetchSkuId test
	 * 
	 * @throws Exception
	 */
	@Test(dataProvider = "boostandblockdata", dataProviderClass = com.bestbuy.searchplatform.integrationtests.commons.TestDataProvider.class)
	public void testFetchSkuId(BoostsAndBlock boostAndBlock) throws Exception {
		String getBoostSkuIdUrl = SAAS_AF_REST_URL + boostAndBlock.getSearchTerm()+"?rows=99&page=3";
		HttpEntity<BoostsAndBlock> entity = new HttpEntity<BoostsAndBlock>(boostAndBlock, getHeaders());	
		log.info("BoostsAndBlocks GetSkuIDsearch url : "+getBoostSkuIdUrl);
		ResponseEntity<String> response =  getRestTemplate().exchange(getBoostSkuIdUrl, HttpMethod.GET, entity, String.class);
		assertNotNull(response);
		String boostResponseString =  response.getBody();
		JSONObject boostsResponse = new JSONObject(boostResponseString);
		assertTrue(boostsResponse.has("documents"));
		JSONArray documents = boostsResponse.getJSONArray("documents");
		assertNotNull(documents.get(0));
		JSONObject document = (JSONObject) documents.get(0);
		assertTrue(document.has("skuid"));
		String boostedskuid = (String)document.get("skuid");
		String productName = document.getString("productname");
		BoostProduct boostProduct = new BoostProduct();
		boostProduct.setSkuId(boostedskuid);
		boostProduct.setProductName(productName);
		boostProduct.setPosition(1);
		boostAndBlock.setBoostProduct(Collections.singletonList(boostProduct));
		getBoostSkuId().put(boostedskuid, boostAndBlock);
	}


	/**
	 * performs create BoostAndBlock test
	 * @throws Exception
	 */

	@SuppressWarnings("unused")
	@Test(dataProvider = "boostandblockdata", dataProviderClass=com.bestbuy.searchplatform.integrationtests.commons.TestDataProvider.class,dependsOnMethods = {"testFetchSkuId"})
	public void testCreate(BoostsAndBlock boostAndBlock) throws Exception{
		for (Entry<String, BoostsAndBlock> entry : getBoostSkuId().entrySet()) {
			BoostsAndBlock mapBoostAndBlock = entry.getValue();
			if (boostAndBlock.getSearchTerm().equals(mapBoostAndBlock.getSearchTerm())) {
				boostAndBlock.setBoostProduct(mapBoostAndBlock.getBoostProduct());
			}
		}
		String url = BT_REST_URL + BOOSTBLOCK_CREATE;
		String searchUrl = BT_REST_URL + BOOSTBLOCK_SEARCH;
		log.info("create boost resturi = "+url);
		HttpEntity<BoostsAndBlock> createEntity = new HttpEntity<BoostsAndBlock>(boostAndBlock,getHeaders());
		ResponseEntity<String> createResponse =  getRestTemplate().exchange(url, HttpMethod.POST, createEntity, String.class);
		assertNotNull(createResponse);
		String boostResponseString1 =  createResponse.getBody();
		JSONObject boostResponse = new JSONObject(boostResponseString1);
		String searchJSON = "{\"pageIndex\":1,\"rowsPerPage\":50,\"sortColumn\":\"modifiedDate\",\"sortOrder\":\"desc\"" +
		",\"searchColumnValues\":[{\"key\":\"searchTerm\",\"value\":\"" + boostAndBlock.getSearchTerm()+ "\"}],\"searchOper\":\"AND\"}";
		HttpEntity<String> searchEntity = new HttpEntity<String>(searchJSON,getHeaders());
		ResponseEntity<String> searchResponse =  getRestTemplate().exchange(searchUrl, HttpMethod.POST, searchEntity, String.class);
		JSONObject boostSearchResponse = new JSONObject(searchResponse.getBody());
		log.info("Boost search response : "+boostSearchResponse);
		assertTrue(boostSearchResponse.has("rows"));
		JSONArray boostDataArray = (JSONArray)boostSearchResponse.get("rows");
		assertNotNull(boostDataArray);
		JSONObject boostData = (JSONObject)boostDataArray.get(0);
		assertTrue(boostData.has("boostBlockId"));
		Integer boostBlockId = (Integer)boostData.get("boostBlockId");
		log.info("boostBlockId : "+boostBlockId);
		assertNotNull(boostBlockId);
		getBoostAndBlocksMap().put(boostBlockId, boostAndBlock);
	}

	/**
	 * performs Approve BoostAndBlock Test
	 * @throws Exception
	 */
	@Test(dependsOnMethods = "testCreate")
	public void testApprove() throws Exception{
		String url = BT_REST_URL + BOOSTBLOCK_APPROVE+"/";
		log.info("approve boostandblock resturi = "+url);
		for (Integer boostBlockId : getBoostAndBlocksMap().keySet()) {
			HttpEntity<Integer> entity = new HttpEntity<Integer>(getHeaders());
			ResponseEntity<String> response =  getRestTemplate().exchange(url+boostBlockId, HttpMethod.PUT, entity, String.class);
			assertNotNull(response);
			String boostResponseString =  response.getBody();
			log.info("approve boost response = "+boostResponseString);
			JSONObject boostResponse = new JSONObject(boostResponseString);
			assertTrue(boostResponse.has("data"));
			JSONObject data = (JSONObject)boostResponse.get("data");
			assertTrue(data.has("status"));
			String status = (String)data.get("status");
			log.info("approve boost status = "+boostBlockId+" status = "+status);
			assertNotNull(status);
			assertEquals(status,"Approved");
		}
	}

	@AfterClass
	public void tearDownAfterClass() throws Exception {
	}
}
