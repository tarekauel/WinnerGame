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
import message.GameDataMessageToClient;
import message.GameDataMessageToClient.ReportingToClient.FixCostToClient;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Company;
import server.GameEngine;
import server.Location;

public class Xyz {

	static Company c;

	PurchaseFromClient purchase;
	DistributionFromClient distribution;
	ProductionFromClient production;
	HumanResourcesFromClient hr;
	ArrayList<GameDataMessageFromClient> gameDataMessages = new ArrayList<GameDataMessageFromClient>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		GameEngine.getGameEngine();
		c = new Company(Location.getLocationByCountry("USA"), "OTTO");

	}

	@Before
	public void initializeTests() throws Exception {

		// werden teilweise absichtlich leer gelassen da in anderem Test
		// geprueft
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers = new ArrayList<AcceptedSupplierOfferFromClient>();
		ArrayList<ProductionOrderFromClient> orders = new ArrayList<ProductionOrderFromClient>();
		ArrayList<OfferFromClient> offers = new ArrayList<OfferFromClient>();
		ArrayList<BenefitBookingFromClient> benefits = new ArrayList<BenefitBookingFromClient>();

		// weitere Erstellung der Message
		purchase = new PurchaseFromClient(requests, acceptedSupplierOffers);
		production = new ProductionFromClient(orders);
		distribution = new DistributionFromClient(offers);
		hr = new HumanResourcesFromClient(benefits);

		GameDataMessageFromClient gameDataMessage = new GameDataMessageFromClient(c.getName(), purchase, production, distribution, false, hr, 7, false);
		gameDataMessages.add(gameDataMessage);

	}

	@Test
	public void increaseMachineryLevel() throws Exception {
		
		ArrayList<GameDataMessageToClient> gameDataM2Client= GameEngine.getGameEngine().startNextRound(gameDataMessages);
		GameDataMessageToClient toClient = gameDataM2Client.get(0);
		double sum = 0;
		for(FixCostToClient fix : toClient.reporting.fixCosts){
			sum = sum + fix.costs;
		}
		System.out.println(sum);

	}

	@After
	public void resetTests() {

	}
}
