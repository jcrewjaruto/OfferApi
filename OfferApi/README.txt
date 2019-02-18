----------------------------------------------------------
Environment setup:
----------------------------------------------------------
- Apache Tomcat 8.5
- JRE 8u201
- Java 1.8

----------------------------------------------------------
Project Background:
----------------------------------------------------------
Per Wikipedia, "an offer is a proposal to sell a specific product or
service under specific conditions". As a merchant I offer goods for
sale. I want to create an offer so that I can share it with my
customers.

All my offers have shopper friendly descriptions. I price all my offers
up front in a defined currency.

An offer is time-bounded, with the length of time an offer is valid for
defined as part of the offer, and should expire automatically. Offers may
also be explicitly cancelled before they expire.

You are required to create a simple RESTful software service that will
allow a merchant to create a new simple offer. Offers, once created, may be
queried. After the period of time defined on the offer it should expire and
further requests to query the offer should reflect that somehow. Before an offer
has expired users may cancel it.

----------------------------------------------------------
Project Assumptions:
----------------------------------------------------------
- Assumptions on application:
    - Ignore the need for user authentication and security.
    - Ignore the need for error page handling.
	- Assume there is only one application server running the application.
    - Offers are persisted in-memory, and the loading is hard-coded on startup (no database required).

- Assumptions on api:
    - Assume all users are allowed to view offers.
    - Assume only merchant-users are authorised to access/would be accessing the POST and PUT APIs.
	- All data content-type (passed in and out) are JSON only.
	
- Assumptions on offer:
    - Assume the merchant is only offering 'goods', not 'services'. The simple application will only handle one type.
    - It is not explicit on whether the merchant will provide the offer's Duration or Expiry-date. Chosen expiry-date, not duration.
    - Assume all timezones are read and set from one defined time-zone: the system timezone.

----------------------------------------------------------
Running project locally
----------------------------------------------------------
1. Checkout git project.
2. Open project from your chosen IDE.
3. Make sure you have a web-server (i.e. Tomcat Apache) setup on the IDE, so that this project can launch on it.
4. Run the project as Java Application.
5. Begin testing with browser (see below for API examples).
6. Testing has been written for controller, found under here: com.app.controller.OfferControllerTest.java


----------------------------------------------------------
Calling API examples:
----------------------------------------------------------
(replace "http://localhost:8080" with your appropriate domain setup)

1. Get all available offers (exclude expired and cancelled offers)
	
	GET URI: http://localhost:8080/api/offers


2. Get all offers (no restrictions on offer's state)
	
	GET URI: http://localhost:8080/api/alloffers


3. Get one specific offer by id (no restrictions on offer's state)
	
	GET URI: http://localhost:8080/api/offer/3


4. Create one new offer
	
	POST URI: http://localhost:8080/api/createoffer
	
	BODY:
	{
	    "description": "pear",
	    "price": 1,
	    "currency": "GBP",
	    "cancelled": false,
	    "expiryDate": "2019-07-19T12:00:00.000Z"
	}
	

5. Cancel an existing offer
	
	PUT URI: http://localhost:8080/api/canceloffer/1
	
