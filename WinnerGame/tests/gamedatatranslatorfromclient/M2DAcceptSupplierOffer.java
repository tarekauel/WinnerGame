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
import server.Resource;
import server.SupplierMarket;
import server.SupplierOffer;


	public class M2DAcceptSupplierOffer {
		
		Company c;
		ArrayList<GameDataMessageFromClient> gameDataMessages = new ArrayList<GameDataMessageFromClient>();
		int supplierOfferQuality;
		
		@BeforeClass
		public static void setUpBeforeClass() throws Exception {
			Location.initLocations();
			GameEngine.getGameEngine();
			
			
		}

		@Before
		public void initializeTests() throws Exception {
			c = new Company(Location.getLocationByCountry("USA"),"OTTO");
			Resource wafer = new Resource(80, "Wafer",1000);
			c.getPurchase().createRequest(wafer);
			SupplierMarket.getMarket().handleRequest();
			SupplierOffer[] supplierOffers = c.getPurchase().getListOfRequest().get(0).getSupplierOffers();
			supplierOfferQuality = supplierOffers[0].getResource().getQuality();
			//wird ausgefuehrt da sich der translator auf die Requests of last round bezieht
			c.getPurchase().prepareForNewRound(2);
	
			//werden teilweise absichtlich leer gelassen da in anderem Test geprueft
			ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
			ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
			ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
			ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
			ArrayList<BenefitBookingFromClient> benefits = new ArrayList<BenefitBookingFromClient>();
			
			//hier findet die erzeugung der relevanten Daten innerhalb der Message statt
			//
			AcceptedSupplierOfferFromClient acceptedSupOf = new AcceptedSupplierOfferFromClient("Wafer", supplierOfferQuality, 100);
			acceptedSupplierOffers.add(acceptedSupOf);
			PurchaseFromClient purchase = new PurchaseFromClient(requests, acceptedSupplierOffers);
			
			
			//weitere Erstellung der Message
			ProductionFromClient production = new ProductionFromClient(orders);
			DistributionFromClient distribution = new DistributionFromClient(offers);
			HumanResourcesFromClient  hr = new HumanResourcesFromClient(benefits);
			
			GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
			gameDataMessages.add(gameDataMessage);
			
			
		}

		@Test
		public void convertAcceptedSupplierOffer() throws Exception {
			GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
			
			for(SupplierOffer r : c.getPurchase().getListOfLastRoundRequests().get(0).getSupplierOffers()){
				if(r.getResource().getQuality() == supplierOfferQuality){
					assertEquals(100,r.getOrderedQuantity());
				}
			}

			
			
		}

		@After
		public void resetTests() {

		}

	}


