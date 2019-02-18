package com.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.app.model.Offer;
import com.app.model.dto.CancelOfferResponse;
import com.app.service.IOfferManager;

/*
 * 
 * API controller for Offers
 * 
 * */

@RestController("offerController")
@RequestMapping("/api")
public class OfferController {
	
	@Autowired
	private IOfferManager offerManager;
	
	@GetMapping(value="/offer/{id}")
	public Offer getOfferById(@Valid @NotNull @PathVariable("id") long offerId) {
		return offerManager.getOfferById(offerId);
	}
	
	@GetMapping(value="/offers")
	public List<Offer> getAllAvailableOffers() {
		Date currentTime = new Date();
		
		return offerManager.getOffers().values()
			.stream()
			.filter(x -> x.isAvailable(currentTime))
			.collect(Collectors.toList());
	}
	
	@GetMapping(value="/alloffers")
	public List<Offer> getAllOffers() {
		return new ArrayList<>(offerManager.getOffers().values());
	}
	
	@PostMapping(value="/createoffer", produces = "application/json")
	public Offer createOffer(@RequestBody Offer offer) {
		try {
			return offerManager.createNewOffer(offer.getDescription(), offer.getPrice(), offer.getCurrency(), offer.getExpiryDate(), offer.isCancelled());
		}
		catch(IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient argument to create offer. Please check API doc.", ex);
		}
	}
	
	@PutMapping(value="/canceloffer/{id}")
	public CancelOfferResponse cancelOffer(@Valid @NotNull @PathVariable("id") long offerId) {
		
		return new CancelOfferResponse(offerManager.cancelOffer(offerId));
	}
}
