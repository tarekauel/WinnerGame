package scenarien;

import static org.junit.Assert.*;

import java.util.ArrayList;

import kigegner.ClientToServerMessageCreator;
import message.GameDataMessageFromClient;
import message.GameDataMessageToClient;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.javafx.scene.layout.region.Margins.Converter;

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

public class TestRound4 {
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
		
		toSend = new ArrayList<GameDataMessageFromClient>();
		
		msg = new ClientToServerMessageCreator("Tester-1");
		/**
		 * Akzeptiere die Angebote (Qualitäten durch FakeOffers bekannt): Geld
		 * genug vorhanden (+10 Millionen zum eigentlichen Kapital reichen)
		 */
		msg.addAccepted("Wafer", 50, 5400);
		msg.addAccepted("Gehäuse", 50, 100);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

		// initialisiere die gebrauchten Listen, damit der Test beginnen kann.
		toReceive = new ArrayList<GameDataMessageToClient>();
		toSend = new ArrayList<GameDataMessageFromClient>();
		msg = new ClientToServerMessageCreator("Tester-1");
		//Zahlen sind aus der init Test bekannt
		msg.addProductionOrder(50, 50, 100);
		toSend.add(msg.getSendMessage());
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		


	}

	
	
	@Test
	public void sellToHigh() throws Exception{
		msg = new ClientToServerMessageCreator("Tester-1");
		FinishedGood g = c1.getStorage().getAllFinishedGoods().get(0);
		msg.addOffer(g.getQuality(), 10, 1000 * g.getCosts());
		ArrayList<StorageElement> se = c1.getStorage().getAllStorageElements();
		StorageElement sePanel = null;
		
		for(StorageElement e: se){
			if (e.getProduct().getName()=="Panel"){
				sePanel = e;
				break;
			}
		}
		if (sePanel == null){
			fail();
			return;
		}
		
		int before = sePanel.getQuantity();
		
		toSend.add(msg.getSendMessage());
		GameEngine.getGameEngine().startNextRound(toSend);
		assertEquals(before,sePanel.getQuantity());
		
	}
	@Test
	public void sell() throws Exception{
		FinishedGood g = c1.getStorage().getAllFinishedGoods().get(0);
		msg.addOffer(g.getQuality(), 10, 1);
		ArrayList<StorageElement> se = c1.getStorage().getAllStorageElements();
		StorageElement sePanel = null;
		toSend = new ArrayList<GameDataMessageFromClient>();
		for(StorageElement e: se){
			if (e.getProduct().getName()=="Panel"){
				sePanel = e;
				break;
			}
		}
		if (sePanel == null){
			fail("Keine Panele");
			return;
		}
		
		int before = sePanel.getQuantity();
		
		toSend.add(msg.getSendMessage());
		GameEngine.getGameEngine().startNextRound(toSend);
		assertEquals(true,sePanel.getQuantity()<before);
		
	}
}
