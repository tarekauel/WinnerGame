package gamedatatranslatorfromclient;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import message.GameDataMessageFromClient;
import message.GameDataMessageFromClient.DistributionFromClient;
import message.GameDataMessageFromClient.DistributionFromClient.OfferFromClient;
import message.GameDataMessageFromClient.HumanResourcesFromClient;
import message.GameDataMessageFromClient.HumanResourcesFromClient.BenefitBookingFromClient;
import message.GameDataMessageFromClient.ProductionFromClient;
import message.GameDataMessageFromClient.ProductionFromClient.ProductionOrderFromClient;
import message.GameDataMessageFromClient.PurchaseFromClient;
import message.GameDataMessageFromClient.PurchaseFromClient.AcceptedSupplierOfferFromClient;
import message.GameDataMessageFromClient.PurchaseFromClient.RequestFromClient;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Company;
import server.GameDataTranslator;
import server.GameEngine;
import server.Location;

public class M2DWage {
	
	static Company c;
	
	PurchaseFromClient purchase;
	DistributionFromClient distribution;
	ProductionFromClient production;
	HumanResourcesFromClient  hr;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		GameEngine.getGameEngine();
		c = new Company(Location.getLocationByCountry("USA"),"OTTO");
		
		
	}

	@Before
	public void initializeTests() throws Exception {

	
		//werden teilweise absichtlich leer gelassen da in anderem Test geprueft
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
		ArrayList<BenefitBookingFromClient> benefits = new ArrayList<BenefitBookingFromClient>();
		
		//weitere Erstellung der Message
		 purchase = new PurchaseFromClient(requests, acceptedSupplierOffers);
		 production = new ProductionFromClient(orders);
		 distribution = new DistributionFromClient(offers);
		 hr = new HumanResourcesFromClient(benefits);
		 

		

		
	}
	@Test
	public void makeTests() throws Exception {
		int wageBefore = c.getHumanResources().getWagesPerHour().getAmount();
		int wage = 10;
		ArrayList<GameDataMessageFromClient> gameDataMessages = new ArrayList<GameDataMessageFromClient>();
		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, wage,false);
		gameDataMessages.add(gameDataMessage);
		GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
		int wageAfter = c.getHumanResources().getWagesPerHour().getAmount();
		System.out.println(wageBefore);
		System.out.println(wageAfter);
		assertEquals(true, wageBefore!=wageAfter);
		
	}

	@After
	public void resetTests() {

	}


}
