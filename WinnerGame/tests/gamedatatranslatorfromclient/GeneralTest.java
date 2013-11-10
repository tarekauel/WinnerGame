package gamedatatranslatorfromclient;

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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Company;
import server.GameDataTranslator;
import server.GameEngine;
import server.Location;

public class GeneralTest {
	
	Company c;
	ArrayList<GameDataMessageFromClient> gameDataMessages = new ArrayList<GameDataMessageFromClient>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		GameEngine.getGameEngine();
	}

	@Before
	public void initializeTests() throws Exception {
		c = new Company(Location.getLocationByCountry("USA"),"OTTO");	
	}

	@Test (expected = java.lang.NullPointerException.class)
	public void requestNull() throws Exception {
		//werden teilweise absichtlich leer gelassen da in anderem Test geprueft
		ArrayList<RequestFromClient> requests = null;
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
		ArrayList<BenefitBookingFromClient> benefits = new ArrayList<BenefitBookingFromClient>();
		
		//hier findet die erzeugung der relevanten Daten innerhalb der Message statt
		//
		
		
		
		
		//weitere Erstellung der Message
		PurchaseFromClient purchase = new PurchaseFromClient(requests, acceptedSupplierOffers);
		ProductionFromClient production = new ProductionFromClient(orders);
		DistributionFromClient distribution = new DistributionFromClient(offers);
		HumanResourcesFromClient  hr = new HumanResourcesFromClient(benefits);
		
		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);
		
			GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
	}
	
	
	@Test (expected = java.lang.NullPointerException.class)
	public void acceptedSupplierOfferNull() throws Exception {
		
		//erstellen der Listen
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = null;
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
		ArrayList<BenefitBookingFromClient> benefits = new ArrayList<BenefitBookingFromClient>();
		
		//erstellen der Objekte mit den Listen gefuellt
		PurchaseFromClient purchase = new PurchaseFromClient(requests, acceptedSupplierOffers);
		ProductionFromClient production = new ProductionFromClient(orders);
		DistributionFromClient distribution = new DistributionFromClient(offers);
		HumanResourcesFromClient  hr = new HumanResourcesFromClient(benefits);
		//erstellen der Message
		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);
		
		GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
	}

	
	@Test (expected = java.lang.NullPointerException.class)
	public void ordersNull() throws Exception {
		//erstellen der Listen
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
		ArrayList<ProductionOrderFromClient> orders = null;
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
		ArrayList<BenefitBookingFromClient> benefits = new ArrayList<BenefitBookingFromClient>();
		
		//erstellen der Objekte mit den Listen gefuellt
		PurchaseFromClient purchase = new PurchaseFromClient(requests, acceptedSupplierOffers);
		ProductionFromClient production = new ProductionFromClient(orders);
		DistributionFromClient distribution = new DistributionFromClient(offers);
		HumanResourcesFromClient  hr = new HumanResourcesFromClient(benefits);
		//erstellen der Message
		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);
		
		GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
	}
	
	@Test (expected = java.lang.NullPointerException.class)
	public void offersNull() throws Exception {
		//erstellen der Listen
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
		ArrayList<OfferFromClient> offers = null;
		ArrayList<BenefitBookingFromClient> benefits = new ArrayList<BenefitBookingFromClient>();
		
		//erstellen der Objekte mit den Listen gefuellt
		PurchaseFromClient purchase = new PurchaseFromClient(requests, acceptedSupplierOffers);
		ProductionFromClient production = new ProductionFromClient(orders);
		DistributionFromClient distribution = new DistributionFromClient(offers);
		HumanResourcesFromClient  hr = new HumanResourcesFromClient(benefits);
		//erstellen der Message
		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);
		
		GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
	}
	
	@Test (expected = java.lang.NullPointerException.class)
	public void purchaseNull() throws Exception {
		//erstellen der Listen
		
		
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
		ArrayList<BenefitBookingFromClient> benefits =  new ArrayList<BenefitBookingFromClient>();
		
		//erstellen der Objekte mit den Listen gefuellt
		PurchaseFromClient purchase = null;
		ProductionFromClient production = new ProductionFromClient(orders);
		DistributionFromClient distribution = new DistributionFromClient(offers);
		HumanResourcesFromClient  hr = new HumanResourcesFromClient(benefits);
		//erstellen der Message
		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);
		
		GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
	}
	
	@Test (expected = java.lang.NullPointerException.class)
	public void productionNull() throws Exception {
		//erstellen der Listen
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
		ArrayList<BenefitBookingFromClient> benefits =  new ArrayList<BenefitBookingFromClient>();
		
		//erstellen der Objekte mit den Listen gefuellt
		PurchaseFromClient purchase = new PurchaseFromClient(requests, acceptedSupplierOffers);
		ProductionFromClient production = null;
		DistributionFromClient distribution = new DistributionFromClient(offers);
		HumanResourcesFromClient  hr = new HumanResourcesFromClient(benefits);
		//erstellen der Message
		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);
		
		GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
	}
	
	@Test (expected = java.lang.NullPointerException.class)
	public void distributionNull() throws Exception {
		//erstellen der Listen
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
		ArrayList<BenefitBookingFromClient> benefits =  new ArrayList<BenefitBookingFromClient>();
		
		//erstellen der Objekte mit den Listen gefuellt
		PurchaseFromClient purchase = new PurchaseFromClient(requests, acceptedSupplierOffers);
		ProductionFromClient production = new ProductionFromClient(orders);
		DistributionFromClient distribution = null;
		HumanResourcesFromClient  hr = new HumanResourcesFromClient(benefits);
		//erstellen der Message
		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);
		
		GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
	}
	
	
}
