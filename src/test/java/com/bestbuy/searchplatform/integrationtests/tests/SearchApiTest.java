package com.bestbuy.searchplatform.integrationtests.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import junit.framework.AssertionFailedError;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.bestbuy.searchplatform.integrationtests.model.Banners.Banner;
import com.bestbuy.searchplatform.integrationtests.model.BoostsAndBlocks.BoostsAndBlock;
import com.bestbuy.searchplatform.integrationtests.model.Facets.Facet;
import com.bestbuy.searchplatform.integrationtests.model.Facets.Facet.CategoryWrapper;
import com.bestbuy.searchplatform.integrationtests.model.Facets.Facet.ExcludeList;
import com.bestbuy.searchplatform.integrationtests.model.Facets.Facet.PromoteList;
import com.bestbuy.searchplatform.integrationtests.model.Keywordredirects.Keywordredirect;
import com.bestbuy.searchplatform.integrationtests.model.Synonyms.Synonym;

@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class SearchApiTest extends BaseTest {
	private final static Logger log = Logger.getLogger("SearchApiTest");
	private static String publishType = "";
	private static String saasAFUrl = "";
	private static String saasCFUrl = "";
	private static String promoAttributeValue = "";
	private static String excludeAttributeValue = "";

	@Parameters({ "publishType" })
	@BeforeClass(alwaysRun = true)
	public static void setUpBeforeClass(String aPublishType) throws Exception {
		publishType = aPublishType;
		log.info("Publish Type : " + publishType);
		if (publishType.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
			long publishCFInterval = Long
			.parseLong(BT_CF_PUBLISHING_INTERVAL);
			Thread.sleep(publishCFInterval);
		} else {
			long publishAFInterval = Long
			.parseLong(BT_AF_PUBLISHING_INTERVAL);
			Thread.sleep(publishAFInterval);
		}
		saasAFUrl = SAAS_AF_REST_URL;
		saasCFUrl = SAAS_CF_REST_URL;
	}

	@AfterClass(alwaysRun = true)
	public void tearDownAfterClass() throws Exception {
	}

	@Test(groups = { "AF", "CF" })
	public void testKeywordRedirectAF() throws Exception {
		log.info("Executing to verify the KeywordRedirect is published in saasAFUrl "
				+ saasAFUrl);
		validateKeywordRedirects(saasAFUrl, AF_PUBLISH_TYPE);
	}

	@Test(groups = "CF")
	public void testKeywordRedirectCF() throws Exception {
		log.info("Executing to verify the KeywordRedirect is published in saasCFUrl "
				+ saasCFUrl);
		validateKeywordRedirects(saasCFUrl, CF_PUBLISH_TYPE);
	}

	private void validateKeywordRedirects(String saasUrl, String testGroup)
	throws Exception {
		Set<Entry<Integer,Keywordredirect>> entrySet = getRedirectsMap().entrySet();
		for (Entry<Integer, Keywordredirect> entry : entrySet) {
			HttpEntity entity = new HttpEntity(getHeaders());
			Keywordredirect keywordredirect = entry.getValue();
			String redirectTerm = keywordredirect.getRedirectTerm();
			String queryUrl = saasUrl + redirectTerm;
			if (redirectTerm.endsWith("CD")) {
				if (!testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
					queryUrl = queryUrl + getCDQuery();
				}
				log.info("SaaS Redirect search url for current date : "+queryUrl);
				validateKeywordRedirectDateRanges(keywordredirect, entity, queryUrl, testGroup);
			} else {
				if (!testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
					queryUrl = queryUrl + getFDQuery();
				}
				log.info("SaaS Redirect search url for future date : "+queryUrl);
				validateKeywordRedirectDateRanges(keywordredirect, entity, queryUrl, testGroup);
			}
		}
	}

	private void validateKeywordRedirectDateRanges(
			Keywordredirect keywordredirect, HttpEntity entity,
			String queryUrl, String testGroup) throws JSONException {
		ResponseEntity<String> response = getRestTemplate().exchange(queryUrl,
				HttpMethod.GET, entity, String.class);
		assertNotNull(response);
		String redirectResponseString = response.getBody();
		// log.info("keyword redirect saas response = " +
		// redirectResponseString);
		JSONObject redirectResponse = new JSONObject(redirectResponseString);
		assertTrue(redirectResponse.has("redirect"));
		JSONArray redirectUrl = redirectResponse.getJSONArray("redirect");
		if ((keywordredirect.getRedirectTerm().endsWith("FD"))
				&& testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
			log.info("redirectUrl.get(0) : " + redirectUrl.get(0));
			assertNotEquals(redirectUrl.get(0),
					keywordredirect.getRedirectUrl());
		} else {
			assertEquals(redirectUrl.get(0), keywordredirect.getRedirectUrl());
		}
	}

	@Test(groups = { "AF", "CF" })
	private void testSynonymsAF() throws Exception {
		log.info("Executing to verify the Synonyms data is published in saasAFUrl "
				+ saasAFUrl);
		validateSynonyms(saasAFUrl);
	}

	@Test(groups = "CF")
	private void testSynonymsCF() throws Exception {
		log.info("Executing to verify the Synonyms data is published in saasCFUrl "
				+ saasCFUrl);
		validateSynonyms(saasCFUrl);
	}

	private void validateSynonyms(String saasUrl) throws Exception {
		Set<Entry<Integer, Synonym>> entrySet = getSynonymsMap().entrySet();
		for (Entry<Integer, Synonym> entry : entrySet) {
			String productName = "";
			try {
				HttpEntity entity = new HttpEntity(getHeaders());
				log.info("SaaS Synonyms search url : " + saasUrl
						+ entry.getValue().getPrimaryTerm());
				ResponseEntity<String> response = getRestTemplate().exchange(
						saasUrl + entry.getValue().getPrimaryTerm(),
						HttpMethod.GET, entity, String.class);
				assertNotNull(response);
				String synonymResponseString = response.getBody();
				// log.info("synonymResponseString : "+synonymResponseString);
				JSONObject synonymResponse = new JSONObject(
						synonymResponseString);
				assertTrue(synonymResponse.has("documents"));
				JSONArray documents = synonymResponse.getJSONArray("documents");
				assertNotNull(documents.get(0));
				JSONObject document = (JSONObject) documents.get(0);
				assertTrue(document.has("productname"));
				productName = (String) document.get("productname");
				assertTrue(productName.toLowerCase().contains(
						entry.getValue().getTerm().get(0).toLowerCase()));
			} catch (AssertionFailedError e) {
				log.info("Assertion failed for " + saasUrl
						+ entry.getValue().getPrimaryTerm());
				log.info("Response productName :" + productName);
				log.info("Synonym term : "
						+ entry.getValue().getTerm().get(0).toLowerCase());
				throw e;
			}
		}
	}

	@Test(groups = { "AF", "CF" })
	private void testBannersAF() throws Exception {
		log.info("Executing to verify the banner data is published in saasAFUrl "
				+ saasAFUrl);
		validateBanners(saasAFUrl, AF_PUBLISH_TYPE);
	}

	@Test(groups = { "CF" })
	private void testBannersCF() throws Exception {
		log.info("Executing to verify the banner data is published in saasCFUrl "
				+ saasCFUrl);
		validateBanners(saasCFUrl, CF_PUBLISH_TYPE);
	}

	private void validateBanners(String saasUrl, String testGroup)
	throws Exception {
		Set<Entry<Integer, Banner>> entrySet = getBannersMap().entrySet();
		for (Entry<Integer, Banner> entry : entrySet) {
			HttpEntity entity = new HttpEntity(getHeaders());
			Banner banner = entry.getValue();
			log.info("Banner Id: " + entry.getKey());
			log.info("Banner Name: " + banner.getBannerName());
			String keywords = banner.getContexts().get(0).getKeywords();
			String queryUrl = saasUrl + keywords;
			queryUrl = appendCategory(queryUrl, banner.getContexts().get(0)
					.getContextPathId(), banner.getContexts().get(0)
					.getCategoryPath());
			if (banner.getBannerName().endsWith("CD")) {
				if (!testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
					queryUrl = queryUrl + getCDQuery();
				}
				log.info("SaaS Banners search url for current date : "
						+ queryUrl);
				validateBannersDateRanges(banner, entity, queryUrl, testGroup);
			} else {
				if (!testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
					queryUrl = queryUrl + getFDQuery();
				}
				log.info("SaaS Banners search url for future date : "
						+ queryUrl);
				validateBannersDateRanges(banner, entity, queryUrl, testGroup);
			}
		}
	}

	private void validateBannersDateRanges(Banner banner, HttpEntity entity,
			String queryUrl, String testGroup) throws Exception {
		ResponseEntity<String> response = getRestTemplate().exchange(queryUrl,
				HttpMethod.GET, entity, String.class);
		assertNotNull(response);
		String bannerResponseString = response.getBody();
		JSONObject bannerResponse = new JSONObject(bannerResponseString);
		assertTrue(bannerResponse.has("banners"));
		JSONArray banners = bannerResponse.getJSONArray("banners");
		log.info("banners : " + banners);
		String bannersStr = banners.toString();
		if ((banner.getBannerName().endsWith("FD"))
				&& testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
			assertFalse(bannersStr.toLowerCase().contains(
					banner.getBannerName().toLowerCase()));
		} else {
			assertTrue(bannersStr.toLowerCase().contains(
					banner.getBannerName().toLowerCase()));
		}
	}

	@Test(groups = { "AF", "CF" })
	private void testFacetsAF() throws Exception {
		log.info("Executing to verify the facet data is published in saasAFUrl "
				+ saasAFUrl);
		validateFacets(saasAFUrl, AF_PUBLISH_TYPE);
	}

	@Test(groups = { "CF" })
	private void testFacetsCF() throws Exception {
		log.info("Executing to verify the facet data is published in saasCFUrl "
				+ saasCFUrl);
		validateFacets(saasCFUrl, CF_PUBLISH_TYPE);
	}


	private void validateFacets(String saasUrl, String testGroup) throws Exception {
		Set<Entry<Integer,Facet>> entrySet = getFacetsMap().entrySet();
		for (Entry<Integer, Facet> entry : entrySet) {
			HttpEntity entity = new HttpEntity(getHeaders());
			Facet facet = entry.getValue();
			System.out.println("facet::"+facet);
			String queryUrl = saasUrl + "sony";
			List<CategoryWrapper> categoryWrapper = facet.getCategoryWrapper();
			String categoryId = categoryWrapper.get(0).getCategoryId();
			String categoryPath = categoryWrapper.get(0).getCategoryPath();
			queryUrl = appendCategory(queryUrl, categoryId,categoryPath);
			List<PromoteList> promoList = facet.getPromoteList();
			List<ExcludeList> excludeList = facet.getExcludeList(); 
			if (!CollectionUtils.isEmpty(promoList)&& facet.getSystemName().endsWith("CD")) {
				assertNotNull(promoList);
				promoAttributeValue = promoList.get(0).getAttributeValue();
				if (!testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
					queryUrl = queryUrl + getCDQuery();
				}
				log.info("SaaS Promo Facets search url for current date : "+queryUrl);
				validatePromoFacetsDateRanges(facet, entity, queryUrl, testGroup);

			}else if (!CollectionUtils.isEmpty(promoList)&& facet.getSystemName().endsWith("FD")) {
				assertNotNull(promoList);
				promoAttributeValue = promoList.get(0).getAttributeValue();
				if (!testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
					queryUrl = queryUrl + getFDQuery();
				}
				log.info("SaaS Promo Facets search url for future date : "+queryUrl);
				validatePromoFacetsDateRanges(facet, entity, queryUrl, testGroup);

			}else if (!CollectionUtils.isEmpty(excludeList) &&facet.getSystemName().endsWith("CD")) {
				assertNotNull(excludeList);
				if (!testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
					queryUrl = queryUrl + getCDQuery();
				}
				if(excludeList.size()>1)
				{
					log.info("SaaS ExcludeAll Facets search url for current date : "+queryUrl);					
					System.out.println("excludeAttributeValue************"+excludeAttributeValue);
					validateExcludeAllFacetsDateRanges(facet, entity, queryUrl, testGroup);
				}
				else
				{
					log.info("SaaS  Exclude Facets search url for current date : "+queryUrl);
					excludeAttributeValue = excludeList.get(0).getAttributeValue();
					validateExcludeFacetsDateRanges(facet, entity, queryUrl, testGroup);
				}
			}else if (!CollectionUtils.isEmpty(excludeList)&& facet.getSystemName().endsWith("FD")) {
				assertNotNull(excludeList);
				if (!testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
					queryUrl = queryUrl + getFDQuery();
				}
				if(excludeList.size()>1)
				{
					log.info("SaaS ExcludeAll Facets search url for future date : "+queryUrl);
					System.out.println("excludeAttributeValue************"+excludeAttributeValue);
					validateExcludeAllFacetsDateRanges(facet, entity, queryUrl, testGroup);
				}
				else
				{
					log.info("SaaS  Exclude Facets search url for future date : "+queryUrl);
					excludeAttributeValue = excludeList.get(0).getAttributeValue();
					validateExcludeFacetsDateRanges(facet, entity, queryUrl, testGroup);
				}
			}else if (CollectionUtils.isEmpty(promoList) && CollectionUtils.isEmpty(excludeList) && facet.getSystemName().endsWith("CD")) {
				if (!testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
					queryUrl = queryUrl + getCDQuery();
				}
				log.info("SaaS Facets search url for current date : "+queryUrl);
				validateFacetsDateRanges(facet, entity, queryUrl, testGroup);
			}else if (CollectionUtils.isEmpty(promoList) && CollectionUtils.isEmpty(excludeList) && facet.getSystemName().endsWith("FD")) {
				if (!testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
					queryUrl = queryUrl + getFDQuery();
				}
				log.info("SaaS Facets search url for future date : "+queryUrl);
				validateFacetsDateRanges(facet, entity, queryUrl, testGroup);
			}
		}
	}

	private void validateFacetsDateRanges( Facet facet,
			HttpEntity entity, String queryUrl, String testGroup) throws Exception {
		ResponseEntity<String> response =  getRestTemplate().exchange(queryUrl, HttpMethod.GET, entity, String.class);
		assertNotNull(response);
		String facetResponseString =  response.getBody();
		JSONObject facetResponse = new JSONObject(facetResponseString);
		assertTrue(facetResponse.has("facets"));
		JSONArray facets = facetResponse.getJSONArray("facets");
		assertNotNull(facets.get(0));	
		log.info("facets : "+facets);
		String facetsStr = facets.toString();
		if ((facet.getSystemName().endsWith("FD")) && testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
			assertFalse(facetsStr.toLowerCase().contains(facet.getSystemName().toLowerCase()));
		} 
		else {
			assertTrue(facetsStr.toLowerCase().contains(facet.getSystemName().toLowerCase()));	
		}
	}


	private void validatePromoFacetsDateRanges( Facet facet,
			HttpEntity entity, String queryUrl, String testGroup) throws Exception {
		ResponseEntity<String> response =  getRestTemplate().exchange(queryUrl, HttpMethod.GET, entity, String.class);
		assertNotNull(response);
		String facetResponseString =  response.getBody();
		JSONObject facetResponse = new JSONObject(facetResponseString);
		assertTrue(facetResponse.has("facets"));
		JSONArray facets = facetResponse.getJSONArray("facets");
		assertNotNull(facets.get(0));	
		log.info("facets : "+facets);
		String facetsStr = facets.toString();
		if ((facet.getSystemName().endsWith("FD")) && testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
			assertFalse(facetsStr.toLowerCase().contains(facet.getSystemName().toLowerCase()));
		} 
		else {
			assertTrue(facetsStr.toLowerCase().contains(facet.getSystemName().toLowerCase()));	
			for (int i = 0; i < facets.length(); i++) {
				JSONObject jsonObject = facets.getJSONObject(i);
				String systemName = (String) jsonObject.get("systemName");
				if (systemName.equals(facet.getSystemName())) {
					JSONArray values = jsonObject.getJSONArray("values");
					for (int j = 0; j < values.length(); j++) {
						JSONObject jsonValues = values.getJSONObject(j);
						String promoValue = (String) jsonValues.get("value");
						assertEquals(promoValue,promoAttributeValue);
						if (promoAttributeValue.contains(promoValue)) {
							break;
						}
					}
					break;
				}
			}
		}
	}

	private void validateExcludeAllFacetsDateRanges( Facet facet,
			HttpEntity entity, String queryUrl, String testGroup) throws Exception {
		ResponseEntity<String> response =  getRestTemplate().exchange(queryUrl, HttpMethod.GET, entity, String.class);
		assertNotNull(response);
		String facetResponseString =  response.getBody();
		JSONObject facetResponse = new JSONObject(facetResponseString);
		assertTrue(facetResponse.has("facets"));
		JSONArray facets = facetResponse.getJSONArray("facets");
		assertNotNull(facets.get(0));	
		log.info("facets : "+facets);
		String facetsStr = facets.toString();
		if ((facet.getSystemName().endsWith("FD")) && testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
			assertFalse(facetsStr.toLowerCase().contains(facet.getSystemName().toLowerCase()));
		} 
		else {
			assertTrue(facetsStr.toLowerCase().contains(facet.getSystemName().toLowerCase()));
			for (int i = 0; i < facets.length(); i++) {
				JSONObject jsonObject = facets.getJSONObject(i);
				System.out.println("facets.getJSONObject(i):::"+i+"::::::::::::"+facets.getJSONObject(i));
				String systemName = (String) jsonObject.get("systemName");
				System.out.println("systemName::"+i+"::::"+systemName);
				if (systemName.equals(facet.getSystemName())) {
					JSONArray values = (JSONArray) jsonObject.get("values");
					boolean foundTerm = false;
					if(values.length()<=0)
					{
						foundTerm = true;
					}
					Assert.assertTrue(foundTerm);
				}
			}
		}
	}

	private void validateExcludeFacetsDateRanges( Facet facet,
			HttpEntity entity, String queryUrl, String testGroup) throws Exception {
		ResponseEntity<String> response =  getRestTemplate().exchange(queryUrl, HttpMethod.GET, entity, String.class);
		assertNotNull(response);
		String facetResponseString =  response.getBody();
		JSONObject facetResponse = new JSONObject(facetResponseString);
		assertTrue(facetResponse.has("facets"));
		JSONArray facets = facetResponse.getJSONArray("facets");
		assertNotNull(facets.get(0));	
		log.info("facets : "+facets);
		String facetsStr = facets.toString();
		if ((facet.getSystemName().endsWith("FD")) && testGroup.equalsIgnoreCase(CF_PUBLISH_TYPE)) {
			assertFalse(facetsStr.toLowerCase().contains(facet.getSystemName().toLowerCase()));
		} 
		else {
			assertTrue(facetsStr.toLowerCase().contains(facet.getSystemName().toLowerCase()));
			for (int i = 0; i < facets.length(); i++) {
				JSONObject jsonObject = facets.getJSONObject(i);
				String systemName = (String) jsonObject.get("systemName");
				if (systemName.equals(facet.getSystemName())) {
					JSONArray values = (JSONArray) jsonObject.get("values");
					String valueString = values.toString();
					assertFalse(valueString.toLowerCase().contains(excludeAttributeValue.toLowerCase()));
				}
			}
		}
	}

	private String appendCategory(String queryUrl, String categoryId,
			String categoryPath) {
		if (!"cat00000".equals(categoryId)) {
			int count = StringUtils.countOccurrencesOf(categoryPath, "|");
			int addCount = count + 1;
			queryUrl = queryUrl + "/category_facet=" + addCount + "|"
			+ categoryPath + "|" + categoryId;
		}
		return queryUrl;
	}

	@Test(groups = { "AF", "CF" })
	public void testBoostAndBlockAF() throws Exception {
		log.info("Executing to verify the BoostAndBlock is published in saasAFUrl "
				+ saasAFUrl);
		validateBoostAndBlock(saasAFUrl);
	}

	@Test(groups = "CF")
	public void testBoostAndBlockCF() throws Exception {
		log.info("Executing to verify the BoostAndBlock is published in saasCFUrl "
				+ saasCFUrl);
		validateBoostAndBlock(saasCFUrl);
	}

	private void validateBoostAndBlock(String saasUrl) throws Exception {
		Set<Entry<String,BoostsAndBlock>> entrySet = getBoostSkuId().entrySet();
		for (Entry<String, BoostsAndBlock> entry : entrySet) {
			String responseSkuID = "";
			try {
				HttpEntity entity = new HttpEntity(getHeaders());
				log.info("SaaS BoostsAndBlocks search url : "+saasUrl+entry.getValue().getSearchTerm());
				String boostSaasUrl=saasUrl+entry.getValue().getSearchTerm()+"?rows=99&page=1";
				ResponseEntity<String> response =  getRestTemplate().exchange(boostSaasUrl, HttpMethod.GET, entity, String.class);
				assertNotNull(response);
				String boostsAndBlockResponseString =  response.getBody();
				JSONObject boostResponse = new JSONObject(boostsAndBlockResponseString);
				assertTrue(boostResponse.has("documents"));
				JSONArray documents = boostResponse.getJSONArray("documents");
				assertNotNull(documents.get(0));
				JSONObject document = (JSONObject) documents.get(0);
				assertTrue(document.has("skuid"));
				responseSkuID = (String)document.get("skuid");	
				String actualSkuId=entry.getKey();
				assertEquals(actualSkuId, responseSkuID);
			} catch (AssertionFailedError e) {
				log.info("Assertion failed for "+saasUrl+entry.getValue().getSearchTerm());
				log.info("Response skuId :"+responseSkuID);
				throw e;
			}
		}
	}

	private String getCDQuery() throws Exception {
		Date csd = new SimpleDateFormat("MM-dd-yyyy hh:MM:ss")
		.parse(getCurrentStartDate());
		Calendar cal = Calendar.getInstance();
		cal.setTime(csd);
		return "/?querydate=" + cal.get(Calendar.YEAR) + "-"
		+ (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
	}

	private String getFDQuery() throws Exception {
		Date fsd = new SimpleDateFormat("MM-dd-yyyy hh:MM:ss")
		.parse(getFutureStartDate());
		Calendar cal = Calendar.getInstance();
		cal.setTime(fsd);
		return "/?querydate=" + cal.get(Calendar.YEAR) + "-"
		+ (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
	}
}
