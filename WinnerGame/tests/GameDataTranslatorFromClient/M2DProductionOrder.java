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

public class M2DProductionOrder {

	Company c;
	ArrayList<GameDataMessageFromClient> gameDataMessages = new ArrayList<GameDataMessageFromClient>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		GameEngine.getGameEngine();

	}

	@Before
	public void initializeTests() throws Exception {
		//einrichten der Company mit den erforderlichen Eigenschaften fuer diesen Test
		c = new Company(Location.getLocationByCountry("USA"), "OTTO");

		Resource wafer = new Resource(80, "Wafer", 10000);
		Resource cases = new Resource(50, "Gehäuse", 30000);
		c.getStorage().store(wafer, 10000);
		c.getStorage().store(cases, 10000);
		
		// Erstellen der Message Objekte
		// Listen werden teilweise absichtlich leer gelassen da sie in anderen Tests
		// geprueft werden aber notwendig sind
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
		ArrayList<BenefitBookingFromClient> benefits = new ArrayList<BenefitBookingFromClient>();

		// hier findet die erzeugung der relevanten Testdaten innerhalb der Message
		// statt
		ProductionOrderFromClient prodOrder = new ProductionOrderFromClient(80,50, 100);
		orders.add(prodOrder);
		ProductionFromClient production = new ProductionFromClient(orders);

		// weitere Erstellung der Message
		PurchaseFromClient purchase = new PurchaseFromClient(requests,
				acceptedSupplierOffers);
		DistributionFromClient distribution = new DistributionFromClient(offers);
		HumanResourcesFromClient hr = new HumanResourcesFromClient(benefits);

		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(
				c.getName(), purchase, production, distribution, false, hr, 7,
				false);
		gameDataMessages.add(gameDataMessage);

	}

	@Test
	public void convertProductionOrder() throws Exception {
		GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
		assertEquals(100,c.getProduction().getListOfAllProductionOrders().get(0)
						.getRequested());
	}

	@After
	public void resetTests() {

	}

}
