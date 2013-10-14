package GameDataTranslatorFromClient;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Message.GameDataMessageFromClient;
import Message.GameDataMessageFromClient.DistributionFromClient;
import Message.GameDataMessageFromClient.DistributionFromClient.OfferFromClient;
import Message.GameDataMessageFromClient.HumanResourcesFromClient;
import Message.GameDataMessageFromClient.HumanResourcesFromClient.BenefitBookingFromClient;
import Message.GameDataMessageFromClient.ProductionFromClient;
import Message.GameDataMessageFromClient.ProductionFromClient.ProductionOrderFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient.AcceptedSupplierOfferFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient.RequestFromClient;
import Server.Company;
import Server.GameDataTranslator;
import Server.GameEngine;
import Server.Location;
import Server.Resource;
import Server.SupplierMarket;
import Server.SupplierOffer;

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
	public void benefitsNull() throws Exception {
		//erstellen der Listen
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();;
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
		ArrayList<BenefitBookingFromClient> benefits = null;
		
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
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
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
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
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
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
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
	
	@Test (expected = java.lang.NullPointerException.class)
	public void humanresourcesNull() throws Exception {
		//erstellen der Listen
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
		ArrayList<BenefitBookingFromClient> benefits =  new ArrayList<BenefitBookingFromClient>();
		
		//erstellen der Objekte mit den Listen gefuellt
		PurchaseFromClient purchase = new PurchaseFromClient(requests, acceptedSupplierOffers);
		ProductionFromClient production = new ProductionFromClient(orders);
		DistributionFromClient distribution = new DistributionFromClient(offers);
		HumanResourcesFromClient  hr = null;
		//erstellen der Message
		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);
		
		GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
	}
		


	@After
	public void resetTests() {

	}

}
