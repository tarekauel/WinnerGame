package GameDataTranslatorFromClient;

import static org.junit.Assert.assertEquals;

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
import Message.GameDataMessageToClient.DistributionToClient.OfferToClient;
import Server.Company;
import Server.FinishedGood;
import Server.GameDataTranslator;
import Server.GameEngine;
import Server.Location;
import Server.Resource;

public class M2DOffer {

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

		FinishedGood fg = FinishedGood.create(80, 67000);
		c.getStorage().store(fg, 100);
		
		
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
		OfferFromClient offer = new OfferFromClient(80, 50, 80000);
		offers.add(offer);
		DistributionFromClient distribution = new DistributionFromClient(offers);

		// weitere Erstellung der Message
		ProductionFromClient production = new ProductionFromClient(orders);
		PurchaseFromClient purchase = new PurchaseFromClient(requests, acceptedSupplierOffers);
		
		HumanResourcesFromClient hr = new HumanResourcesFromClient(benefits);

		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(
				c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);

	}

	@Test
	public void convertOffer() throws Exception {
		GameDataTranslator.getGameDataTranslator().convertGameDataMessage2Objects(gameDataMessages);
		assertEquals(80000,c.getDistribution().getListOfLatestOffers().get(0).getPrice());
	}

	@After
	public void resetTests() {

	}
	
}
