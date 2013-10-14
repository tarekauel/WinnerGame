package GameDataTranslatorToClient;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Message.GameDataMessageToClient;
import Server.Company;
import Server.CustomerMarket;
import Server.GameDataTranslator;
import Server.GameEngine;
import Server.Location;
import Server.Resource;

public class D2MDistribution {
	

	Company c;
	int quantitySold;
	int panelQuality;
	int price;
	int quantityToSell;
	int costs;
	int round;
	
	
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		GameEngine.getGameEngine();

		
	}

	@Before
	public void initializeTests() throws Exception {
		c = new Company(Location.getLocationByCountry("USA"),"OTTO");
		Resource wafer = new Resource(80, "Wafer", 500);
		c.getStorage().store(wafer, 10000);
		Resource cases = new Resource(50, "Gehäuse", 10000);
		c.getStorage().store(cases, 1000);
		c.getProduction().createProductionOrder(wafer, cases, 100);
		c.getProduction().produce();
		int quality = c.getProduction().getListOfAllProductionOrders().get(0).getPanel().getQuality();
		c.getDistribution().createOffer(quality, 50, costs+10000);
		CustomerMarket.getMarket().handleAllOffers();
		panelQuality = c.getProduction().getListOfAllProductionOrders().get(0).getPanel().getQuality();
		quantityToSell = c.getDistribution().getListOfOffers().get(0).getQuantityToSell();
		quantitySold =  c.getDistribution().getListOfOffers().get(0).getQuantitySold();
		price =  c.getDistribution().getListOfOffers().get(0).getPrice();
		round =  c.getDistribution().getListOfOffers().get(0).getRound();
		costs =  c.getDistribution().getListOfOffers().get(0).getStorageElement().getProduct().getCosts();
		
		
		
		
		
	}

	@Test
	public void getCompleteProductionOrder() throws Exception {
		
		 ArrayList<GameDataMessageToClient> gameDataMessageToClients = GameDataTranslator.getGameDataTranslator().createGameDataMessages();
		 assertEquals(panelQuality,gameDataMessageToClients.get(0).distribution.offers.get(0).quality);
		 assertEquals(quantityToSell,gameDataMessageToClients.get(0).distribution.offers.get(0).quantityToSell);
		 assertEquals(quantityToSell,gameDataMessageToClients.get(0).distribution.offers.get(0).quantitySold);
		 assertEquals(price,gameDataMessageToClients.get(0).distribution.offers.get(0).price);
		 assertEquals(round,gameDataMessageToClients.get(0).distribution.offers.get(0).round);
		 assertEquals(costs,gameDataMessageToClients.get(0).distribution.offers.get(0).costs);
		

	}

	@After
	public void resetTests() {

	}

}
