package scenarien;

import static org.junit.Assert.assertEquals;

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
import server.Resource;
import server.StorageElement;
import server.SupplierMarket;
import annotation.FakeSupplierMarketOfferQualities;

public class TestRound3 {
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
		// initialisiere die Listen, damit der Test beginnen kann.
		toReceive = new ArrayList<GameDataMessageToClient>();
		toSend = new ArrayList<GameDataMessageFromClient>();

		// erstelle die Firmenobjekte
		c1 = new Company(Location.getLocationByCountry("Deutschland"),
				"Tester-1");

		// Sorge für quasi unendlich Geld(Fehler Quelle vermeiden)
		// 10 Millionen GE mehr auf Konto:
		c1.getBankAccount().increaseBalance(999999999);

		// Anmelden im CustomerMarket
		CustomerMarket.getMarket().addDistribution(c1.getDistribution());

		// Anmelden im SupplierMarket
		SupplierMarket.getMarket().addPurchase(c1.getPurchase());

		// Anmelden an den Marktdaten
		MarketData.getMarketData().addHR(c1.getHumanResources());

		// Hole Angebote vom Markt mit bestimmten Parametern.
		// erstelle dazu ein MessageObject, sende es an den Server und lade die
		// Antwort in Received

		msg.addRequest("Wafer", 50);
		msg.addRequest("Gehäuse", 50);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		received = toReceive.remove(toReceive.size() - 1);

		/**
		 * Akzeptiere die Angebote (Qualitäten durch FakeOffers bekannt): Geld
		 * genug vorhanden (+10 Millionen zum eigentlichen Kapital reichen)
		 */
		msg.addAccepted("Wafer", 50, 5400);
		msg.addAccepted("Gehäuse", 50, 1000);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

		// initialisiere die gebrauchten Listen, damit der Test beginnen kann.
		toReceive = new ArrayList<GameDataMessageToClient>();
		toSend = new ArrayList<GameDataMessageFromClient>();

	}

	@Test
	public void checkIfInStorage() {
		// Check if correct Quality in Storage
		for (Resource r : c1.getStorage().getAllResources()) {
			if (r.getName().equals("Wafer")) {
				assertEquals(50, r.getQuality());
			} else if (r.getName().equals("Gehäuse")) {
				assertEquals(50, r.getQuality());
			}
		}
		for (StorageElement se : c1.getStorage().getAllStorageElements()) {
			if (se.getProduct().getName().equals("Wafer")) {
				assertEquals(54000, se.getQuantity());
			} else if (se.getProduct().getName().equals("Gehäuse")) {
				assertEquals(1000, se.getQuantity());
			}
		}
	}
}
