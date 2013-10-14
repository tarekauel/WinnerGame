package GameDataTranslatorToClient;

import static org.junit.Assert.assertEquals;

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
import Server.Resource;
import Server.SupplierMarket;

public class D2MProduction {
	
	Company c;
	int acceptedSupplierOfferQuality;
	int panelQuality;
	int producedQuantity;
	int costs;
	
	

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
		panelQuality = c.getStorage().getAllFinishedGoods().get(0).getQuality();
		producedQuantity = c.getProduction().getListOfAllProductionOrders().get(0).getProduced();
		costs =  c.getProduction().getListOfAllProductionOrders().get(0).getPanel().getCosts();
		
		
		
		
	}

	@Test
	public void getCompleteProductionOrder() throws Exception {
		
		 ArrayList<GameDataMessageToClient> gameDataMessageToClients = GameDataTranslator.getGameDataTranslator().createGameDataMessages();
		 assertEquals(100,gameDataMessageToClients.get(0).production.orders.get(0).quantity);
		 assertEquals(80,gameDataMessageToClients.get(0).production.orders.get(0).qualityWafer);
		 assertEquals(50,gameDataMessageToClients.get(0).production.orders.get(0).qualityCase);
		 assertEquals(panelQuality,gameDataMessageToClients.get(0).production.orders.get(0).qualityPanel);
		 assertEquals(producedQuantity,gameDataMessageToClients.get(0).production.orders.get(0).producedQuantity);
		 assertEquals(costs,gameDataMessageToClients.get(0).production.orders.get(0).costs);
		

	}

	@After
	public void resetTests() {

	}


}
