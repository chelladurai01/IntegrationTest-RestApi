package com.bestbuy.searchplatform.integrationtests.tests;

import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.BANNER_APPROVE;
import static com.bestbuy.searchplatform.integrationtests.commons.IntegationTestConstants.BANNER_CREATE;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Map.Entry;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bestbuy.searchplatform.integrationtests.model.Banners.Banner;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * Class to create and approve Banners
 *
 */
public class BannersTest extends BaseTest {

	private final static Logger log = Logger.getLogger("BannersTest");

	/**
	 * @BeforeClass The annotated method will be run before the first test method in the
	 *  current class is invoked to read the data in xml
	 */
	@BeforeClass (alwaysRun = true)
	public void setUpBeforeClass() throws Exception {

	}

	/**
	 * performs create Banners test
	 * @throws Exception
	 */
	@Test(dataProvider = "bannersdata", dataProviderClass=com.bestbuy.searchplatform.integrationtests.commons.TestDataProvider.class)
	public void testCreate(Banner banner) throws Exception{
		String url = BT_REST_URL+BANNER_CREATE;
		log.info("create banner resturi = "+url);
		String bannerName = banner.getBannerName();
		String dateType = "CD"; // CD - Current Date - Starting with CD
		for (int i = 0; i < 2; i++) { // looping twice for creating data with current date and future date
			XStream xStream = new XStream();
			Banner createBanner = (Banner)xStream.fromXML(xStream.toXML(banner));
			if (dateType.equalsIgnoreCase("CD")) {
				createBanner.setStartDate(getCurrentStartDate());
				createBanner.setEndDate(getCurrentEndDate());
			} else {
				createBanner.setStartDate(getFutureStartDate());
				createBanner.setEndDate(getFutureEndDate());
			}
			createBanner.setBannerName(bannerName + dateType);
			HttpEntity<Banner> entity = new HttpEntity<Banner>(createBanner,getHeaders());
			ResponseEntity<String> response =  getRestTemplate().exchange(url, HttpMethod.POST, entity, String.class);
			assertNotNull(response);
			String bannerResponseString =  response.getBody();
			JSONObject bannerResponse = new JSONObject(bannerResponseString);
			assertTrue(bannerResponse.has("data"));
			JSONObject data = (JSONObject)bannerResponse.get("data");
			assertTrue(data.has("bannerId"));
			Integer bannerId =(Integer)data.get("bannerId");
			assertNotNull(bannerId);
			log.info("created banner = "+createBanner.getBannerName()+" with id = "+bannerId);
			
			getBannersMap().put(bannerId, createBanner);
			dateType = "FD"; // Change the dateType to FD - Future Date for second iteration

		}
	}

	/**
	 * performs Approve Banners Test
	 * @throws Exception
	 */
	@Test(dependsOnMethods = "testCreate")
	public void testApprove() throws Exception{
		String url = BT_REST_URL+BANNER_APPROVE+"/";
		log.info("approve banner resturi = "+url);
		for (Entry<Integer, Banner> entry : getBannersMap().entrySet()) {
			log.info("-----------------------------------------------------------------------");
			log.info("testApprove Banner Id: "+entry.getKey());
			log.info("testApprove Banner NAme: "+getBannersMap().get(entry.getKey()).getBannerName());
			log.info("-----------------------------------------------------------------------");
		}
		for (Integer bannerId : getBannersMap().keySet()) {
			HttpEntity<Integer> entity = new HttpEntity<Integer>(getHeaders());
			ResponseEntity<String> response =  getRestTemplate().exchange(url+bannerId, HttpMethod.PUT, entity, String.class);
			assertNotNull(response);
			String bannerResponseString =  response.getBody();
			log.info("approve banner response = "+bannerResponseString);
			JSONObject bannerResponse = new JSONObject(bannerResponseString);
			assertTrue(bannerResponse.has("data"));
			JSONObject data = (JSONObject)bannerResponse.get("data");
			assertTrue(data.has("statusId"));
			Integer statusId = (Integer)data.get("statusId");
			log.info("approve banner status = "+bannerId+" status = "+statusId);
			assertNotNull(statusId);
			assertTrue(statusId == 3);
		}
	}

	/**
	 * @AfterClass The annotated method will be run after all the test methods 
	 * in the current class have been run to set banners null. 
	 * @throws Exception
	 */
	@AfterClass (alwaysRun = true)
	public void tearDownAfterClass() throws Exception {
	}
}
