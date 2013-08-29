package com.bestbuy.searchplatform.integrationtests.tests;

import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.REDIRECT_APPROVE;
import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.REDIRECT_CREATE;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bestbuy.searchplatform.integrationtests.model.Keywordredirects.Keywordredirect;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * Class to create and approve KeywordRedirects
 *
 */
public class KeywordRedirectTest extends BaseTest {
	private final static Logger log = Logger.getLogger("KeywordRedirectTest");

	/**
	 * @BeforeClass The annotated method will be run before the first test method in the
	 *  current class is invoked to read the data in xml 
	 */
	@BeforeClass(alwaysRun = true)
	public void setUpBeforeClass() throws Exception {

	}

	/**
	 * performs create redirect test
	 * @throws Exception
	 */
	@Test(dataProvider = "keywordredirectdata", dataProviderClass = com.bestbuy.searchplatform.integrationtests.commons.TestDataProvider.class)
	public void testCreate(Keywordredirect keywordredirect) throws Exception {
		String url = BT_REST_URL+REDIRECT_CREATE;
		createKeywordRedirects(url, keywordredirect);
	}
	
	/**
	 * Implementation of createKeywordRedirects Method
	 * @param url
	 * @param keywordredirectList
	 * @throws JSONException
	 */
	private void createKeywordRedirects(String url, Keywordredirect keywordredirect) throws JSONException {
		String redirectTerm = keywordredirect.getRedirectTerm();
		String dateType = "CD"; // CD - Current Date - Starting with CD
		for (int i = 0; i < 2; i++) { // looping twice for creating data with current date and future date
			XStream xStream = new XStream();
			Keywordredirect createKeywordRedirect = (Keywordredirect)xStream.fromXML(xStream.toXML(keywordredirect));
			if (dateType.equalsIgnoreCase("CD")) {
				createKeywordRedirect.setStartDate(getCurrentStartDate());
				createKeywordRedirect.setEndDate(getCurrentEndDate());
			} else {
				createKeywordRedirect.setStartDate(getFutureStartDate());
				createKeywordRedirect.setEndDate(getFutureEndDate());
			}
			createKeywordRedirect.setRedirectTerm(redirectTerm + dateType);
			HttpEntity<Keywordredirect> entity = new HttpEntity<Keywordredirect>(createKeywordRedirect, getHeaders());
			ResponseEntity<String> response = getRestTemplate().exchange(url, HttpMethod.POST, entity, String.class);
			assertNotNull(response);
			String redirectResponseString = response.getBody();
			JSONObject redirectResponse = new JSONObject(redirectResponseString);
			assertTrue(redirectResponse.has("data"));
			JSONObject data = (JSONObject) redirectResponse.get("data");
			assertTrue(data.has("redirectId"));
			Integer redirectId = (Integer) data.get("redirectId");
			assertNotNull(redirectId);
			log.info("created redirect = " + createKeywordRedirect.getRedirectTerm() + " with id = " + redirectId);
			getRedirectsMap().put(redirectId, createKeywordRedirect);
			dateType = "FD"; // Change the dateType to FD - Future Date for second iteration
		}
	}

	/**
	 * performs Approve Redirects Test
	 * @throws Exception
	 */
	@Test(dependsOnMethods = "testCreate")
	public void testApprove() throws Exception {
		String url = BT_REST_URL +REDIRECT_APPROVE+ "/";
		log.info("approve redirect resturi = " + url);
		for (Integer redirectId : getRedirectsMap().keySet()) {
			HttpEntity<Integer> entity = new HttpEntity<Integer>(getHeaders());
			ResponseEntity<String> response = getRestTemplate().exchange(
					url + redirectId, HttpMethod.PUT, entity, String.class);
			assertNotNull(response);
			String redirectResponseString = response.getBody();
			log.info("approve redirect response = " + redirectResponseString);
			JSONObject redirectResponse = new JSONObject(redirectResponseString);
			assertTrue(redirectResponse.has("data"));
			JSONObject data = (JSONObject) redirectResponse.get("data");
			assertTrue(data.has("statusId"));
			Integer statusId = (Integer) data.get("statusId");
			log.info("approve redirect status = " + redirectId + " status = "
					+ statusId);
			assertNotNull(statusId);
			assertTrue(statusId == 3);
		}
	}

	/**
	 * @AfterClass The annotated method will be run after all the test methods 
	 * in the current class have been run to set keywordredirects null 
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

}
