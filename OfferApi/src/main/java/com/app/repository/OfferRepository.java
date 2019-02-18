package com.app.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.app.model.Offer;
import com.app.model.type.Currency;

@Repository("offerRepository")
public class OfferRepository {

	private long tempIncrOfferPk = 10;	//id counter for new offers that get added
	
	public Map<Long, Offer> getAllOffers(){
		 Map<Long, Offer> result = new HashMap();
		 
		/* Result:
		 * 1. Apple
		 * 2. Banana
		 * 3. Orange (Cancelled)
		 * 4. Watermelon (Expired)
		 * */
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1); //set expiry-date ahead 1 day
		
		result.put(1l, new Offer(1l, "Apple",  0.40, Currency.GBP, cal.getTime(), false));
		result.put(2l, new Offer(2l, "Banana", 0.30, Currency.USD, cal.getTime(), false));
		result.put(3l, new Offer(3l, "Orange", 0.20, Currency.GBP, cal.getTime(), true));

		cal.add(Calendar.DATE, -3); //set date to be expired
		
		result.put(4l, new Offer(4l, "Watermelon", 1.50, Currency.USD, cal.getTime(), false));
		
		return result;
	}
	
	public Offer createOffer(String description, double price, Currency currency, Date expiryDate, boolean cancelled) throws IllegalArgumentException {
		
		if(expiryDate == null)
			throw new IllegalArgumentException("Expiry date cannot be null");
		
		//TODO: Send create transaction to database
		
		long newOfferId	= tempIncrOfferPk++;
		return new Offer(newOfferId, description, price, currency, expiryDate, cancelled);
	}
	
	public boolean cancelOffer(long offerId) {
		//TODO: Send update transaction to database
		return true;
	}
}
