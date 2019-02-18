package com.app.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.app.model.type.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Offer {
	
	public final static String DATE_OUTPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	private long id;
	@NotNull(message = "description can not be null")
	private String description;
	@NotNull(message = "price can not be null")
	private double price;
	@NotNull(message = "currency can not be null")
	private Currency currency;
	private boolean cancelled;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_OUTPUT_FORMAT)
    @DateTimeFormat(pattern=DATE_OUTPUT_FORMAT)
	private Date expiryDate;
	
	public Offer(long id, String description, double price, Currency currency, Date expiryDate, boolean cancelled) {
		this.id = id;
		this.description = description;
		this.price = price;
		this.currency = currency;
		this.expiryDate = expiryDate;
		this.cancelled = cancelled;
	}
	
	public long getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public double getPrice() {
		return price;
	}
	public Currency getCurrency() {
		return currency;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	public boolean isAvailable(Date currentTime) {
		return !cancelled && (expiryDate == null || !currentTime.after(expiryDate) ); //assume we could have non-expirable offers 
	}
}
