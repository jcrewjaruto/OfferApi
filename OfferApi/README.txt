----------------------------------------------------------
Environment setup:
----------------------------------------------------------
- Apache Tomcat 8.5
- JRE 8u201

----------------------------------------------------------
Project Assumptions:
----------------------------------------------------------
- Assumptions on application:
    - Ignore the need for user authentication and security.
    - Ignore the need for error page handling.
	- Assume there is only one application server running the application.
    - Offers are persisted in-memory, and the loading is hard-coded on startup.

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
3. Make sure you have a web-server (i.e. Tomcat Apache) setup on the IDE, so that this project can run on.
4. Run the project: should be able to run application as a typical Java Application.
5. Begin testing with browser.
6. Test is found under: com.app.controller.OfferControllerTest.java

----------------------------------------------------------
Calling API examples:
----------------------------------------------------------
(replace "http://localhost:8080" with your appropriate domain setup)

- Get all available offers (exclude expired and cancelled offers)
	
	GET URI: http://localhost:8080/api/offers


- Get all offers (no restrictions on offer's state)
	
	GET URI: http://localhost:8080/api/alloffers


- Get one specific offer by id (no restrictions on offer's state)
	
	GET URI: http://localhost:8080/api/offer/3


- Create one new offer
	
	POST URI: http://localhost:8080/api/createoffer
	
	BODY:
	{
	    "description": "pear",
	    "price": 1,
	    "currency": "GBP",
	    "cancelled": false,
	    "expiryDate": "2019-07-19T12:00:00.000Z"
	}
	

- Cancel an existing offer
	
	PUT URI: http://localhost:8080/api/canceloffer/1
	
