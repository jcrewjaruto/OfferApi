package com.app.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.app.model.Offer;
import com.app.model.type.Currency;
import com.app.service.IOfferManager;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 
 * n.b. could have broken down the testing into smaller classes, but decided it was not necessary yet
 * 
 * */
@RunWith(SpringRunner.class)
@WebMvcTest(value = OfferController.class, secure = false)
public class OfferControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private IOfferManager offerManager;
	
	private ObjectMapper objectMapper		= getObjectMapper();
	
	private String getAllAvailableOffers	= "/api/offers";
	private String getAllOffers				= "/api/alloffers";
	private String getOfferById				= "/api/offer";
	private String createOffer				= "/api/createoffer";
	private String cancelOffer				= "/api/canceloffer";
	
	private Offer mockValidOffer			= new Offer(1l, "Apple", 0.40, Currency.GBP, getFutureExpiryDate(1), false);
	private Offer mockCancelledOffer		= new Offer(1l, "Apple", 0.40, Currency.USD, getFutureExpiryDate(1), true);
	private Offer mockExpiredOffer			= new Offer(1l, "Apple", 0.40, Currency.GBP, getPastExpiryDate(1), false);
	

	//=====================================================
	// General test
	//=====================================================
	@Test
	public void testUnknownMapping() throws Exception {
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get("/api/randomunknownmapping")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		Assert.assertEquals(MockHttpServletResponse.SC_NOT_FOUND, result.getResponse().getStatus());
	}
	
	@Test
	public void invalidRequestType() throws Exception {
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.post(getAllAvailableOffers)
				.accept(MediaType.APPLICATION_JSON)
				.content("{}")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		Assert.assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, result.getResponse().getStatus());
	}
	
	//=====================================================
	// Test getAllAvailableOffers
	//=====================================================
	@Test
	public void testGetAllAvailableOffers_ValidOfferOnly() throws Exception {
		
		Map<Long, Offer> offerMap = new HashMap();
		offerMap.put(mockValidOffer.getId(), mockValidOffer);
		
		Mockito
			.when(offerManager.getOffers())
			.thenReturn(offerMap);
		
		String expectedString = objectMapper.writeValueAsString(offerMap.values());
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getAllAvailableOffers)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), true);
	}
	
	@Test
	public void testGetAllAvailableOffers_ExpiredOfferOnly() throws Exception {
		
		Map<Long, Offer> offerMap = new HashMap();
		offerMap.put(mockExpiredOffer.getId(), mockExpiredOffer);
		
		Mockito
			.when(offerManager.getOffers())
			.thenReturn(offerMap);
		
		String expectedString = objectMapper.writeValueAsString(new ArrayList<Offer>());
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getAllAvailableOffers)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), true);
	}
	
	@Test
	public void testGetAllAvailableOffers_MixedOffers() throws Exception {
		
		Map<Long, Offer> offerMap = new HashMap();
		offerMap.put(mockValidOffer.getId(), mockValidOffer);
		offerMap.put(mockExpiredOffer.getId(), mockExpiredOffer);
		offerMap.put(mockCancelledOffer.getId(), mockCancelledOffer);
		
		Mockito
			.when(offerManager.getOffers())
			.thenReturn(offerMap);
		
		String expectedString = objectMapper.writeValueAsString(offerMap.values());
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getAllAvailableOffers)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertNotEquals(expectedString, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testGetAllAvailableOffers_Empty() throws Exception {
		
		Mockito
			.when(offerManager.getOffers())
			.thenReturn(new HashMap<Long, Offer>());
		
		String expectedString = objectMapper.writeValueAsString(new ArrayList<Offer>());
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getAllAvailableOffers)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), true);
	}
	
	//=====================================================
	// Test getAllOffers
	//=====================================================
	@Test
	public void testGetAllOffers_ValidOfferOnly() throws Exception {
		
		Map<Long, Offer> offerMap = new HashMap();
		offerMap.put(mockValidOffer.getId(), mockValidOffer);
		
		Mockito
			.when(offerManager.getOffers())
			.thenReturn(offerMap);
		
		String expectedString = objectMapper.writeValueAsString(offerMap.values());
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getAllOffers)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), true);
	}
	
	@Test
	public void testGetAllOffers_ExpiredOfferOnly() throws Exception {
		
		Map<Long, Offer> offerMap = new HashMap();
		offerMap.put(mockExpiredOffer.getId(), mockExpiredOffer);
		
		Mockito
			.when(offerManager.getOffers())
			.thenReturn(offerMap);
		
		String expectedString = objectMapper.writeValueAsString(offerMap.values());
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getAllOffers)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), true);
	}
	
	@Test
	public void testGetAllOffers_MixedOffers() throws Exception {
		
		Map<Long, Offer> offerMap = new HashMap();
		offerMap.put(mockValidOffer.getId(), mockValidOffer);
		offerMap.put(mockExpiredOffer.getId(), mockExpiredOffer);
		offerMap.put(mockCancelledOffer.getId(), mockCancelledOffer);
		
		Mockito
			.when(offerManager.getOffers())
			.thenReturn(offerMap);
		
		String expectedString = objectMapper.writeValueAsString(offerMap.values());
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getAllOffers)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), true);
	}
	
	@Test
	public void testGetAllOffers_Empty() throws Exception {
		
		Mockito
			.when(offerManager.getOffers())
			.thenReturn(new HashMap<Long, Offer>());
		
		String expectedString = objectMapper.writeValueAsString(new ArrayList<Offer>());
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getAllOffers)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), true);
	}
	
	//=====================================================
	// Test GetOfferById
	//=====================================================
	
	@Test
	public void testGetOfferById_ValidOffer() throws Exception {
		
		Mockito
			.when(offerManager.getOfferById(Mockito.anyLong()))
			.thenReturn(mockValidOffer);
		
		String expectedString = objectMapper.writeValueAsString(mockValidOffer);
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getOfferById+"/1")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void testGetOfferById_CancelledOffer() throws Exception {
		
		Mockito
			.when(offerManager.getOfferById(Mockito.anyLong()))
			.thenReturn(mockCancelledOffer);
		
		String expectedString = objectMapper.writeValueAsString(mockCancelledOffer);
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getOfferById+"/1")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), true);
	}
	
	@Test
	public void testGetOfferById_ExpiredOffer() throws Exception {
		
		Mockito
			.when(offerManager.getOfferById(Mockito.anyLong()))
			.thenReturn(mockExpiredOffer);
		
		String expectedString = objectMapper.writeValueAsString(mockExpiredOffer);
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getOfferById+"/1")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), true);
	}
	
	@Test
	public void testGetOfferById_InvalidParameter() throws Exception {
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getOfferById+"/idsomeone")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		Assert.assertEquals(MockHttpServletResponse.SC_BAD_REQUEST, result.getResponse().getStatus());
	}
	@Test
	public void testGetOfferById_EmptyParameter() throws Exception {
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get(getOfferById)
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		Assert.assertEquals(MockHttpServletResponse.SC_NOT_FOUND, result.getResponse().getStatus());
	}
	
	//=====================================================
	// Test CreateOffer
	//=====================================================
	@Test
	public void testCreateOffer_FutureOffer() throws Exception {
		
		String expectedString = objectMapper.writeValueAsString(mockValidOffer);
		
		Mockito
			.when(offerManager.createNewOffer(Mockito.anyString(), Mockito.anyDouble(), Mockito.any(Currency.class), Mockito.any(), Mockito.anyBoolean()))
			.thenReturn(mockValidOffer);
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.post(createOffer)
				.accept(MediaType.APPLICATION_JSON)
				.content(expectedString)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testCreateOffer_ExpiredOffer() throws Exception {
		
		String expectedString = objectMapper.writeValueAsString(mockExpiredOffer);
		
		Mockito
			.when(offerManager.createNewOffer(Mockito.anyString(), Mockito.anyDouble(), Mockito.any(Currency.class), Mockito.any(), Mockito.anyBoolean()))
			.thenReturn(mockExpiredOffer);
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.post(createOffer)
				.accept(MediaType.APPLICATION_JSON)
				.content(expectedString)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testCreateOffer_InsufficientArgs() throws Exception {
		
		String expectedString = "{description: \"abc\"}";
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.post(createOffer)
				.accept(MediaType.APPLICATION_JSON)
				.content(expectedString)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		Assert.assertEquals(MockHttpServletResponse.SC_BAD_REQUEST , result.getResponse().getStatus());
	}
	

	@Test
	public void testCreateOffer_BadMapping() throws Exception {
		
		String expectedString = "{}";
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.post(createOffer+"/abc")
				.accept(MediaType.APPLICATION_JSON)
				.content(expectedString)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		Assert.assertEquals(MockHttpServletResponse.SC_NOT_FOUND, result.getResponse().getStatus());
	}
	
	//=====================================================
	// Test CancelOffer
	//=====================================================
	@Test
	public void testCancelOffer_Valid() throws Exception {
		
		Mockito
			.when(offerManager.cancelOffer(Mockito.anyLong()))
			.thenReturn(true);
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.put(cancelOffer+"/1")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals("{status: true}", result.getResponse().getContentAsString(), true);
	}
	
	@Test
	public void testCancelOffer_Failed() throws Exception {
		
		Mockito
			.when(offerManager.cancelOffer(Mockito.anyLong()))
			.thenReturn(false);
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.put(cancelOffer+"/1")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		JSONAssert.assertEquals("{status: false}", result.getResponse().getContentAsString(), true);
	}
	
	@Test
	public void testCancelOffer_InsufficientArgs() throws Exception {
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.put(cancelOffer)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		Assert.assertEquals(MockHttpServletResponse.SC_NOT_FOUND , result.getResponse().getStatus());
	}
	

	@Test
	public void testCancelOffer_BadMapping() throws Exception {
		
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.put(cancelOffer+"/abc")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(reqBuilder).andReturn();
		Assert.assertEquals(MockHttpServletResponse.SC_BAD_REQUEST, result.getResponse().getStatus());
	}
	
	//=====================================================
	// Misc.
	//=====================================================
	private ObjectMapper getObjectMapper() {
		ObjectMapper obj = new ObjectMapper();
		obj.setDateFormat(new SimpleDateFormat(Offer.DATE_OUTPUT_FORMAT));
		return obj;
	}
	
	private Date getFutureExpiryDate(int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	
	private Date getPastExpiryDate(int day) {
		if(day >= 0)
			day *= -1;
			
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	
}
