package com.app.service;

import java.util.Date;
import java.util.Map;

import com.app.model.Offer;
import com.app.model.type.Currency;

public interface IOfferManager {
	public Map<Long, Offer> getOffers();
	public Offer getOfferById(long offerId);
	public Offer createNewOffer(String description, double price, Currency currency, Date expiryDate, boolean cancelled) throws IllegalArgumentException;
	public boolean cancelOffer(long offerId);
}
