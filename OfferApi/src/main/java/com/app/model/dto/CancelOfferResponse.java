package com.app.model.dto;

public class CancelOfferResponse {
	private boolean status;
	
	public CancelOfferResponse(boolean status) {
		this.status = status;
	}
	
	public boolean getStatus() {
		return status;
	}
}
