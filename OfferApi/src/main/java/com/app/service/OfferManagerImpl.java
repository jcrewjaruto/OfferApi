package com.app.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.Offer;
import com.app.model.type.Currency;
import com.app.repository.OfferRepository;

/*
 * 
 * Class that manages the transactions for Offers.
 * - Also holds in-memory representation of data in database.
 * 
 * */
@Service("offerManager")
public class OfferManagerImpl implements IOfferManager {
	
	@Autowired
	private OfferRepository repo;
	
	private Map<Long, Offer> offers;	//holding internal memory state
	
	public OfferManagerImpl() {
		offers = new HashMap<>();
	}

	@PostConstruct
	private void loadOffers(){
		offers.putAll(repo.getAllOffers());
	}
	
	@Override
	public Map<Long, Offer> getOffers() {
		return offers;
	}
	@Override
	public Offer getOfferById(long offerId) {
		return offers.get(offerId);
	}
	@Override
	public boolean cancelOffer(long offerId) {

		if(!offers.containsKey(offerId)) //assume there is no discrepancy between in-memory and database (normally i don't do this, but for testing sake)
			return false;
		
		boolean result = repo.cancelOffer(offerId);
		if(result && offers.containsKey(offerId))
			offers.get(offerId).setCancelled(true);
		
		//reload object if other attributes of the Offer object changes after being cancelled
		
		return result;
	}
	@Override
	public Offer createNewOffer(String description, double price, Currency currency, Date expiryDate, boolean cancelled) throws IllegalArgumentException {
		Offer newOffer = repo.createOffer(description, price, currency, expiryDate, cancelled);
		populateOffer(newOffer.getId(), newOffer);		
		return newOffer;
	}
	
	private void populateOffer(long offerId, Offer offer) {
		offers.put(offerId, offer);
	}
}
