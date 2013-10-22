package scenarien;

/**
 * Dieser Test gibt alle Möglichen Eingabedaten an den Server und prüft, ob die Antwort korrekt ist.
 */
import java.util.ArrayList;

import kigegner.ClientToServerMessageCreator;
import message.GameDataMessageFromClient;
import message.GameDataMessageToClient;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Benefit;
import server.Company;
import server.CustomerMarket;
import server.GameEngine;
import server.Location;
import server.MarketData;
import server.SupplierMarket;
import annotation.FakeSupplierMarketOfferQualities;

public class TestRound2 {
	Company c1;
	ClientToServerMessageCreator msg;
	ArrayList<GameDataMessageFromClient> toSend;
	ArrayList<GameDataMessageToClient> toReceive;
	GameDataMessageToClient received;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		Benefit.initBenefits();
	}

	@Before
	@FakeSupplierMarketOfferQualities(differences = { -10, 0, 10 })
	public void initializeTests() throws Exception {
		// erstelle die messageobjekte
		msg = new ClientToServerMessageCreator("Tester-1");

		// erstelle die Firmenobjekte
		c1 = new Company(Location.getLocationByCountry("Deutschland"),
				"Tester-1");

		// Anmelden im CustomerMarket
		CustomerMarket.getMarket().addDistribution(c1.getDistribution());

		// Anmelden im SupplierMarket
		SupplierMarket.getMarket().addPurchase(c1.getPurchase());

		// Anmelden an den Marktdaten
		MarketData.getMarketData().addHR(c1.getHumanResources());

		// Initialisiere die Listen
		toReceive = new ArrayList<GameDataMessageToClient>();
		toSend = new ArrayList<GameDataMessageFromClient>();
		received = null;

		// Hole Angebote vom Markt mit bestimmten Parametern.
		// erstelle dazu ein MessageObject, sende es an den Server und lade die
		// Antwort in Received

		msg.addRequest("Wafer", 50);
		msg.addRequest("Gehäuse", 50);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		received = toReceive.remove(toReceive.size() - 1);

		// initialisiere die gebrauchten Listen, damit der Test beginnen kann.
		toReceive = new ArrayList<GameDataMessageToClient>();
		toSend = new ArrayList<GameDataMessageFromClient>();

	}
	
	@Test
	public void acceptWafer() throws Exception{
		msg.addAccepted("Wafer", 50, 5400);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void acceptWaferIllegalQuality() throws Exception{
		msg.addAccepted("Wafer", 54, 5400);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void acceptWaferNegative() throws Exception{
		msg.addAccepted("Wafer", 54, -1);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		
	}
	@Test (expected = IllegalArgumentException.class)
	public void acceptWaferIllegalAll() throws Exception{
		msg.addAccepted("Waf24r", 54, -1);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		
	}
	@Test (expected = IllegalArgumentException.class)
	public void acceptWaferQuantity0() throws Exception{
		msg.addAccepted("Wafer", 50, 0);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		
	}
	@Test (expected = IllegalArgumentException.class)
	public void acceptNull() throws Exception{
		msg.addAccepted(null, 50,10);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		
	}
	@Test
	public void acceptCase() throws Exception{
		msg.addAccepted("Gehäuse", 50, 5400);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void acceptCaseIllegalQuality() throws Exception{
		msg.addAccepted("Gehäuse", 54, 5400);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void acceptCaseNegative() throws Exception{
		msg.addAccepted("Gehäuse", 54, -1);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		
	}
	@Test (expected = IllegalArgumentException.class)
	public void acceptCaseIllegalAll() throws Exception{
		msg.addAccepted("Gehäuasdese", 54, -1);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		
	}
	@Test (expected = IllegalArgumentException.class)
	public void acceptCaseQuantity0() throws Exception{
		msg.addAccepted("Gehäuse", 50, 0);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		
	}

}
