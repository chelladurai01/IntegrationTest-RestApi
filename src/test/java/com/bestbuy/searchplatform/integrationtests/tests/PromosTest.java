package com.bestbuy.searchplatform.integrationtests.tests;

import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.PROMO_APPROVE;
import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.PROMO_CREATE;
import static org.testng.Assert.assertNotNull;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bestbuy.searchplatform.integrationtests.model.Promos;
import com.bestbuy.searchplatform.integrationtests.model.Promos.Promo;

/**
 * 
 * Class to create and approve Promos
 *
 */
public class PromosTest extends BaseTest {
	private final static Logger log = Logger.getLogger("PromosTest");
	private static Promos promos; 
	
	/**
	 * @BeforeClass The annotated method will be run before the first test method in the
	 *  current class is invoked to read the data in xml  
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Promos.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		InputStream resourceAsStream = PromosTest.class.getResourceAsStream("/data/PositivePromoTests.xml");
		promos = (Promos) jaxbUnmarshaller.unmarshal(resourceAsStream);
	}
	
	/**
	 * performs create promos test
	 * @throws Exception
	 */
	@Test(priority = 1)
	public void testCreate() throws Exception{
		   String url = BT_REST_URL + PROMO_CREATE;
		   log.info("create promo resturi = "+url);
		   List<Promo> promoList = promos.getPromo();
		   for (Promo promo : promoList) {
				  HttpEntity<Promo> entity = new HttpEntity<Promo>(promo,getHeaders());
				  ResponseEntity<String> response =  getRestTemplate().exchange(url, HttpMethod.POST, entity, String.class);
				  AssertJUnit.assertNotNull(response);
				  String promoResponseString =  response.getBody();
				  JSONObject promoResponse = new JSONObject(promoResponseString);
				  AssertJUnit.assertTrue(promoResponse.has("data"));
				  JSONObject data = (JSONObject)promoResponse.get("data");
				  AssertJUnit.assertTrue(data.has("promoId"));
				  Integer promoId =(Integer)data.get("promoId");
				  assertNotNull(promoId);
				  log.info("created promo = "+promo.getPromoName()+" with id = "+promoId);
				  getPromosMap().put(promoId, promo.getPromoName());
			}
	}
	
	/**
	 * performs Approve promos Test
	 * @throws Exception
	 */
	@Test(priority = 2)
	public void testApprove() throws Exception{
		 String url = BT_REST_URL + PROMO_APPROVE + "/";
		 log.info("approve promo resturi = "+url);
		 for (Integer promoId : getPromosMap().keySet()) {
			 HttpEntity<Integer> entity = new HttpEntity<Integer>(getHeaders());
			 ResponseEntity<String> response =  getRestTemplate().exchange(url+promoId, HttpMethod.PUT, entity, String.class);
			 AssertJUnit.assertNotNull(response);
			 String promoResponseString =  response.getBody();
			 log.info("approve promo response = "+promoResponseString);
			 JSONObject promoResponse = new JSONObject(promoResponseString);
			 AssertJUnit.assertTrue(promoResponse.has("data"));
			 JSONObject data = (JSONObject)promoResponse.get("data");
			 AssertJUnit.assertTrue(data.has("statusId"));
			 Integer statusId = (Integer)data.get("statusId");
			 log.info("approve promo status = "+promoId+" status = "+statusId);
			 assertNotNull(statusId);
			 AssertJUnit.assertTrue(statusId == 3);
		 }
	}
	
	/**
	 * @AfterClass The annotated method will be run after all the test methods 
	 * in the current class have been run to set promos null. 
	 * @throws Exception
	 */
	@AfterClass
	public void tearDownAfterClass() throws Exception {
		promos=null;
	}

}
