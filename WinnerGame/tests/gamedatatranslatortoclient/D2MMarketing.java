package gamedatatranslatortoclient;

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
import message.GameDataMessageToClient;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Benefit;
import server.Company;
import server.GameEngine;
import server.Location;

public class D2MMarketing {
	Company c;
	PurchaseFromClient purchase;
	DistributionFromClient distribution;
	ProductionFromClient production;
	HumanResourcesFromClient  hr;
	String name;
	int costsPerRound;
	ArrayList<GameDataMessageFromClient> gameDataMessages = new ArrayList<GameDataMessageFromClient>();
	
	
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		Benefit.initBenefits();
		GameEngine.getGameEngine();
		

		
	}

	@Before
	public void initializeTests() throws Exception {
		c = new Company(Location.getLocationByCountry("USA"),"OTTO");
		
		//Benefits werden gebucht durch 
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
		 
		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);
		 
		 
		
	}

	@Test
	public void getCompleteProductionOrder() throws Exception {
		
		 ArrayList<GameDataMessageToClient> gameDataMessageToClients = GameEngine.getGameEngine().startNextRound(gameDataMessages);
	
		 
		

	}


}
