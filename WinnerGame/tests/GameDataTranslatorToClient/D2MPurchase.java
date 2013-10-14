package GameDataTranslatorToClient;

import static org.junit.Assert.assertEquals;

import java.awt.image.RescaleOp;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Message.GameDataMessageToClient;
import Server.Company;
import Server.GameDataTranslator;
import Server.GameEngine;
import Server.Location;
import Server.Request;
import Server.Resource;
import Server.SupplierMarket;
import Server.SupplierOffer;

public class D2MPurchase {
	
	Company c;
	int acceptedSupplierOfferQuality;
	
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		GameEngine.getGameEngine();

		
	}

	@Before
	public void initializeTests() throws Exception {
		c = new Company(Location.getLocationByCountry("USA"),"OTTO");
		Resource resource = new Resource(80, "Wafer", 0);
		c.getPurchase().createRequest(resource);
		SupplierMarket.getMarket().handleRequest();
		acceptedSupplierOfferQuality = c.getPurchase().getListOfRequest().get(0).getSupplierOffers()[0].getResource().getQuality();
		
		
		
	}

	@Test
	public void getRightName() throws Exception {
		
		 ArrayList<GameDataMessageToClient> gameDataMessageToClients = GameDataTranslator.getGameDataTranslator().createGameDataMessages();
		 assertEquals(80,gameDataMessageToClients.get(0).purchase.requests.get(0).quality);
		 assertEquals(acceptedSupplierOfferQuality,gameDataMessageToClients.get(0).purchase.requests.get(0).supplierOffers.get(0).quality);
		/*for(SupplierOffer supOf : c.getPurchase().getListOfRequest().get(0).getSupplierOffers()){
			System.out.println(supOf.getResource().getQuality() +"\n"+
							   supOf.getResource().getCosts() +"\n"+
							   supOf.getResource().getName());
		}
		
		 assertEquals("OTTO",gameDataMessageToClients.get(0).getPlayerName()); */
	}

	@After
	public void resetTests() {

	}


}
