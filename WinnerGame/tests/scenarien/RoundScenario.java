package scenarien;

import static org.junit.Assert.*;

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
import server.FinishedGood;
import server.GameEngine;
import server.Location;
import server.MarketData;
import server.StorageElement;
import server.SupplierMarket;
import annotation.FakeSupplierMarketOfferQualities;

public class RoundScenario {
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

	}

	@Test
	@FakeSupplierMarketOfferQualities(differences = { -10, 0, 10 })
	public void completeTest() throws Exception {
		// Stelle Anfragen an den Markt (Qualität 50)
		doFirstRound();
		// Kauf Wafer und Gehäuse der Qualität 50 für 100 Panels
		doSecondRound();
		// Produziere 100 Panels 
		doThirdRound();
		//Verkaufe 10 Panels für je 400€
		doFourthRound();
	}

	private void doFirstRound() throws Exception {
		// Senden vorbereiten
		msg.addRequest("Wafer", 50);
		msg.addRequest("Gehäuse", 50);
		// Hinzufügen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

	}

	private void doSecondRound() throws Exception {
		// Senden vorbereiten
		msg.addAccepted("Wafer", 50, 5400);
		msg.addAccepted("Gehäuse", 50, 100);
		// Hinzufügen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

	}

	private void doThirdRound() throws Exception {
		//Senden vorbereiten
		msg.addProductionOrder(50, 50, 100);
		//Hinzufügen zu einem Array
		toSend.add(msg.getSendMessage());
		//Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

	}

	private void doFourthRound() throws Exception {
		
		FinishedGood g = c1.getStorage().getAllFinishedGoods().get(0);
		//Verkaufe 10 Panels für 400€
		msg.addOffer(g.getQuality(), 10, 40000);
		ArrayList<StorageElement> se = c1.getStorage().getAllStorageElements();
		StorageElement sePanel = null;
		for (StorageElement e : se) {
			if (e.getProduct().getName().equals("Panel")) {
				sePanel = e;
				break;
			}
		}
		if (sePanel == null) {
			fail();
			return;
		}
		//wieviele panels hatten wir vorher?
		int before = sePanel.getQuantity();
		//senden an Gameengine
		toSend.add(msg.getSendMessage());
		GameEngine.getGameEngine().startNextRound(toSend);
		//finaler Test:
		//vergleich der anzahl der panels
		if(sePanel.getQuantity() >= before){
			fail("Es wurde nichts verkauft!");
		}

	}

}