package com.bestbuy.searchplatform.integrationtests.tests;


import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.FACET_APPROVE;
import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.FACET_CREATE;
import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.FACET_SEARCH;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bestbuy.searchplatform.integrationtests.model.Facets.Facet;
import com.bestbuy.searchplatform.integrationtests.model.Facets.Facet.ExcludeList;
import com.bestbuy.searchplatform.integrationtests.model.Facets.Facet.PromoteList;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * Class to create and approve Facets
 *
 */
public class FacetsTest extends BaseTest {
	private final static Logger log = Logger.getLogger("FacetsTest");

	/**
	 * @BeforeClass The annotated method will be run before the first test method in the
	 *  current class is invoked to read the data in xml 
	 */
	@BeforeClass(alwaysRun = true)
	public  void setUpBeforeClass() throws Exception {
		String attributeUrl= BT_REST_URL + "attributes";
		HttpEntity<Facet> entity = new HttpEntity<Facet>(getHeaders());
		ResponseEntity<String> attributeResponse =  getRestTemplate().exchange(attributeUrl, HttpMethod.GET, entity, String.class);
		log.info("attributeResponse:::"+attributeResponse);
		JSONObject facetAttributeResponseJson = new JSONObject(attributeResponse.getBody());
		JSONArray facetAttributeRows = (JSONArray)facetAttributeResponseJson.get("rows");
		for (int j = 0; j < facetAttributeRows.length(); j++) {
			JSONObject jsonValues = facetAttributeRows.getJSONObject(j);
			Integer attributeId = (Integer) jsonValues.get("id");
			String attributeName = (String) jsonValues.get("name");				
			getAttributeName().put(attributeName, attributeId);
		}
	}

	/**
	 * Performs Create Facets Test
	 * @throws Exception
	 */
	@Test(dataProvider = "facetsdata", dataProviderClass=com.bestbuy.searchplatform.integrationtests.commons.TestDataProvider.class)
	public void testCreate(Facet facet) throws Exception{
		String createUrl = BT_REST_URL + FACET_CREATE;
		String searchUrl = BT_REST_URL + FACET_SEARCH;
		String attributeName = facet.getAttributeName();
		if (getAttributeName() != null) {
			Set<String> attributeNames = getAttributeName().keySet();
			for (String attributeNameKey : attributeNames) {
				if (attributeName.equalsIgnoreCase(attributeNameKey)) {
					String attributeValuesUrl= BT_REST_URL + "attributes/values/" + getAttributeName().get(attributeNameKey);
					HttpEntity<Facet> entity = new HttpEntity<Facet>(getHeaders());
					ResponseEntity<String> attributeValueResponse =  getRestTemplate().exchange(attributeValuesUrl, HttpMethod.GET, entity, String.class);
					//log.info("attributeResponse:::"+attributeValueResponse);
					JSONObject facetAttributeValueResponseJson = new JSONObject(attributeValueResponse.getBody());
					JSONArray facetAttributeValueRows = (JSONArray)facetAttributeValueResponseJson.get("rows");
					for (int j = 0; j < facetAttributeValueRows.length(); j++) {					
						JSONObject jsonValues = facetAttributeValueRows.getJSONObject(j);
						Integer attributeValueId = (Integer) jsonValues.get("attributeValueId");
						String attributeValue = (String) jsonValues.get("attributeValue");				
						getAttributeName().put(attributeValue, attributeValueId);
					}
					break;
				}
			}
		}		
		Set<String> keySet = getAttributeName().keySet();
		for (String key : keySet) {
			List<PromoteList> promoteLists = facet.getPromoteList();
			if(!CollectionUtils.isEmpty(promoteLists)){
				for (PromoteList promoteList : promoteLists) {
					String attributeValue = promoteList.getAttributeValue();
					if (key.equalsIgnoreCase(attributeValue)) {
						promoteList.setAttributeValueId(getAttributeName().get(key).toString());
					}
				}
			}			
			List<ExcludeList> excludeLists = facet.getExcludeList();
			if(!CollectionUtils.isEmpty(excludeLists)){
				for (ExcludeList excludeList : excludeLists) {
					String attributeValue = excludeList.getAttributeValue();
					if (key.equalsIgnoreCase(attributeValue)) {
						excludeList.setAttributeValueId(getAttributeName().get(key).toString());
					}
				}
			}
		}
		log.info("create facet resturi = "+createUrl);
		log.info("search facet resturi = "+searchUrl);
		String systemName = facet.getSystemName();
		for (String attributeNameValue : getAttributeName().keySet()) {
			Integer attributeIdValue = getAttributeName().get(attributeNameValue);
			String attributeId = attributeIdValue.toString();
			if(attributeName.equalsIgnoreCase(attributeNameValue)) {
				facet.setAttributeId(attributeId);
			}
		}
		String dateType = "CD"; // CD - Current Date - Starting with CD
		for (int i = 0; i < 2; i++) { // looping twice for creating data with current date and future date
			XStream xStream = new XStream();
			Facet createFacet = (Facet)xStream.fromXML(xStream.toXML(facet));
			if (dateType.equalsIgnoreCase("CD")) {
				createFacet.setStartDate(getCurrentStartDate());
				createFacet.setEndDate(getCurrentEndDate());
			} else {
				createFacet.setStartDate(getFutureStartDate());
				createFacet.setEndDate(getFutureEndDate());
			}
			createFacet.setDisplayName(systemName + dateType);
			createFacet.setSystemName(systemName + dateType);
			HttpEntity<Facet> entity = new HttpEntity<Facet>(createFacet,getHeaders());
			ResponseEntity<String> createResponse =  getRestTemplate().exchange(createUrl, HttpMethod.POST, entity, String.class);
			assertNotNull(createResponse);
			JSONObject facetCreateResponse = new JSONObject(createResponse.getBody());
			log.info("Facet create response : "+facetCreateResponse);
			String searchJSON = "{\"pageIndex\":1,\"rowsPerPage\":50,\"sortColumn\":\"modifiedDate\",\"sortOrder\":\"desc\"" +
			",\"searchColumnValues\":[{\"key\":\"systemName\",\"value\":\"" + createFacet.getSystemName() + "\"}],\"searchOper\":\"AND\"}";
			HttpEntity<String> searchEntity = new HttpEntity<String>(searchJSON,getHeaders());
			ResponseEntity<String> searchResponse =  getRestTemplate().exchange(searchUrl, HttpMethod.POST, searchEntity, String.class);
			JSONObject facetSearchResponse = new JSONObject(searchResponse.getBody());
			log.info("Facet search response : "+facetSearchResponse);
			assertTrue(facetSearchResponse.has("rows"));
			JSONArray facetDataArray = (JSONArray)facetSearchResponse.get("rows");
			assertNotNull(facetDataArray);
			JSONObject facetData = (JSONObject)facetDataArray.get(0);
			assertTrue(facetData.has("facetId"));
			Integer facetId = (Integer)facetData.get("facetId");
			log.info("facetId : "+facetId);
			assertNotNull(facetId);
			getFacetsMap().put(facetId, createFacet);
			dateType = "FD"; // Change the dateType to FD - Future Date for second iteration
		}
	}

	/**
	 * Performs Approve Facets Test
	 * @throws Exception
	 */
	@Test(dependsOnMethods = "testCreate")
	public void testApprove() throws Exception{
		String url = BT_REST_URL+FACET_APPROVE+"/?id=";
		log.info("approve facet resturi = "+url);
		for (Integer facetId : getFacetsMap().keySet()) {
			HttpEntity<Integer> entity = new HttpEntity<Integer>(getHeaders());
			ResponseEntity<String> response =  getRestTemplate().exchange(url+facetId, HttpMethod.PUT, entity, String.class);
			assertNotNull(response);
			String facetResponseString =  response.getBody();
			log.info("approve facet response = "+facetResponseString);
			JSONObject facetResponse = new JSONObject(facetResponseString);
			assertTrue(facetResponse.has("status"));
			String status = (String)facetResponse.get("status");
			assertEquals(status, "SUCCESS");
		}
	}

	/**
	 * @AfterClass The annotated method will be run after all the test methods 
	 * in the current class have been run to set facets null. 
	 * @throws Exception
	 */
	@AfterClass(alwaysRun = true)
	public static void tearDownAfterClass() throws Exception {
	}
}
