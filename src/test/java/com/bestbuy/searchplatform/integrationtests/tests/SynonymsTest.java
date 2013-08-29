package com.bestbuy.searchplatform.integrationtests.tests;

import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.SYNONYM_APPROVE;
import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.SYNONYM_CREATE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bestbuy.searchplatform.integrationtests.model.Synonyms.Synonym;

/**
 * 
 * Class to create and approve Synonyms
 *
 */
public class SynonymsTest extends BaseTest {
	private final static Logger log = Logger.getLogger("SynonymsTest");

	/**
	 * @BeforeClass The annotated method will be run before the first test method in the
	 *  current class is invoked to read the data in xml  
	 */
	@BeforeClass(alwaysRun = true)
	public void setUpBeforeClass() throws Exception {

	}


	/**
	 * performs create synonyms test
	 * @throws Exception
	 */
	@Test(dataProvider = "synonymsdata", dataProviderClass=com.bestbuy.searchplatform.integrationtests.commons.TestDataProvider.class)
	public void testCreate(Synonym synonym) throws Exception{
		String url = BT_REST_URL+SYNONYM_CREATE;
		log.info("create synonym resturi = "+url);
		HttpEntity<Synonym> entity = new HttpEntity<Synonym>(synonym,getHeaders());
		ResponseEntity<String> response =  getRestTemplate().exchange(url, HttpMethod.POST, entity, String.class);
		assertNotNull(response);
		String synonymResponseString =  response.getBody();
		JSONObject synonymResponse = new JSONObject(synonymResponseString);
		assertTrue(synonymResponse.has("data"));
		JSONObject data = (JSONObject)synonymResponse.get("data");
		assertTrue(data.has("synonymId"));
		Integer synonymId =(Integer)data.get("synonymId");
		assertNotNull(synonymId);
		log.info("created synonym = "+synonym.getPrimaryTerm()+" with id = "+synonymId);
		getSynonymsMap().put(synonymId,synonym);
	}

	/**
	 * performs Approve synonyms Test
	 * @throws Exception
	 */
	@Test(dependsOnMethods = "testCreate")
	public void testApprove() throws Exception{
		String url = BT_REST_URL+SYNONYM_APPROVE+"/";
		log.info("approve promo resturi = "+url);
		for (Integer synonymId : getSynonymsMap().keySet()) {
			HttpEntity<Integer> entity = new HttpEntity<Integer>(getHeaders());
			ResponseEntity<String> response =  getRestTemplate().exchange(url+synonymId, HttpMethod.PUT, entity, String.class);
			assertNotNull(response);
			String synonymResponseString =  response.getBody();
			log.info("approve synonym response = "+synonymResponseString);
			JSONObject synonymResponse = new JSONObject(synonymResponseString);
			assertTrue(synonymResponse.has("data"));
			JSONObject data = (JSONObject)synonymResponse.get("data");
			assertTrue(data.has("status"));
			String status = (String)data.get("status");
			log.info("approve synonym status = "+synonymId+" status = "+status);
			assertNotNull(status);
			assertEquals(status,"Approved");
		}
	}

	/**
	 * @AfterClass The annotated method will be run after all the test methods 
	 * in the current class have been run to set synonyms null. 
	 * @throws Exception
	 */
	@AfterClass(alwaysRun = true)
	public void tearDownAfterClass() throws Exception {
	}
}
