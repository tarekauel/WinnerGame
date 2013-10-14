package GameDataTranslatorFromClient;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Message.GameDataMessageFromClient;
import Message.GameDataMessageFromClient.DistributionFromClient;
import Message.GameDataMessageFromClient.HumanResourcesFromClient;
import Message.GameDataMessageFromClient.ProductionFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient;
import Message.GameDataMessageFromClient.DistributionFromClient.OfferFromClient;
import Message.GameDataMessageFromClient.HumanResourcesFromClient.BenefitBookingFromClient;
import Message.GameDataMessageFromClient.ProductionFromClient.ProductionOrderFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient.AcceptedSupplierOfferFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient.RequestFromClient;
import Server.*;

public class M2DRequests {
	
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
/*		Resource wafer = new Resource(80, "Wafer", 1000);
		Resource cases = new Resource(50, "Gehäuse", 30000);
		c.getPurchase().createRequest(cases);
*/		
		//werden teilweise absichtlich leer gelassen da in anderem Test geprueft
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
		ArrayList<BenefitBookingFromClient> benefits = new ArrayList<BenefitBookingFromClient>();
		
		//hier findet die erzeugung der relevanten Daten innerhalb der Message statt
		RequestFromClient request =  new RequestFromClient("Wafer", 80);
		requests.add(request);		
		request =  new RequestFromClient("Gehäuse", 50);
		requests.add(request);
		PurchaseFromClient purchase = new PurchaseFromClient(requests, acceptedSupplierOffers); 
		
		//weitere Erstellung der Message
		ProductionFromClient production = new ProductionFromClient(orders);
		DistributionFromClient distribution = new DistributionFromClient(offers);
		HumanResourcesFromClient  hr = new HumanResourcesFromClient(benefits);
		
		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);
		
		
	}

	@Test
	public void convertRequest() throws Exception {
		GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
		assertEquals(80,c.getPurchase().getListOfRequest().get(0).getRequestedResource().getQuality());
		assertEquals(true,c.getPurchase().getListOfRequest().get(0).getRequestedResource().getName().equals("Wafer"));
		assertEquals(50,c.getPurchase().getListOfRequest().get(1).getRequestedResource().getQuality());
		assertEquals(true,c.getPurchase().getListOfRequest().get(1).getRequestedResource().getName().equals("Gehäuse"));
	}

	@After
	public void resetTests() {

	}

}
