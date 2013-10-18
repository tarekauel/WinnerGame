package scenraiotest;

/**
 * Dieser Test gibt alle M�glichen Eingabedaten an den Server und pr�ft, ob die Antwort korrekt ist.
 */
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import kigegner.ClientToServerMessageCreator;
import message.GameDataMessageFromClient;
import message.GameDataMessageToClient;
import message.GameDataMessageToClient.PurchaseToClient.RequestToClient.SupplierOfferToClient;

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

public class TestRound1 {
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

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void requestWafer0() throws Exception {
		// Senden vorbereiten
		msg.addRequest("Wafer", 0);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void requestCase0() throws Exception {
		// Senden vorbereiten
		msg.addRequest("Geh�use", 0);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void requestWafer101() throws Exception {
		// Senden vorbereiten
		msg.addRequest("Wafer", 101);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void requestCase101() throws Exception {
		// Senden vorbereiten
		msg.addRequest("Geh�use", 101);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void requestWrongWafer0() throws Exception {
		// Senden vorbereiten
		msg.addRequest("4123", 0);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void requestWrongCase0() throws Exception {
		// Senden vorbereiten
		msg.addRequest("asdfe", 0);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void requestWrongWafer101() throws Exception {
		// Senden vorbereiten
		msg.addRequest("Waegfer", 101);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void requestWrongCase101() throws Exception {
		// Senden vorbereiten
		msg.addRequest("hgr", 101);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);

	}

	@Test
	public void requestWafer100() throws Exception {
		// Senden vorbereiten
		msg.addRequest("Wafer", 100);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		// Speichern in simpler Message.
		received = toReceive.remove(0);
		// Analyse der Antwort:
		assertEquals(100, received.purchase.requests.get(0).quality);
		assertEquals("Wafer", received.purchase.requests.get(0).name);
		for (SupplierOfferToClient so : received.purchase.requests.get(0).supplierOffers) {
			// Die Angebote d�rfen nicht um mehr als 10 Punkte abweichen.
			assertEquals(true, so.quality > 90);
			assertEquals(true, so.quality < 101);
		}

	}

	@Test
	public void requestCase100() throws Exception {
		// Senden vorbereiten
		msg.addRequest("Geh�use", 100);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		// Speichern in simpler Message.
		received = toReceive.remove(0);
		// TODO:
		// FIXME: Warum zur h�lle kriege ich 4 Antworten?!
		// Analyse der Antwort:
		assertEquals(100, received.purchase.requests.get(0).quality);
		assertEquals("Geh�use", received.purchase.requests.get(0).name);
		for (SupplierOfferToClient so : received.purchase.requests.get(0).supplierOffers) {
			// Die Angebote d�rfen nicht um mehr als 10 Punkte abweichen.
			assertEquals(true, so.quality > 90);
			assertEquals(true, so.quality < 101);
		}

	}

	@Test
	public void requestWafer50() throws Exception {
		// Senden vorbereiten
		msg.addRequest("Wafer", 50);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		// Speichern in simpler Message.
		received = toReceive.remove(0);
		// Analyse der Antwort:
		assertEquals(100, received.purchase.requests.get(0).quality);
		assertEquals("Wafer", received.purchase.requests.get(0).name);
		for (SupplierOfferToClient so : received.purchase.requests.get(0).supplierOffers) {
			// Die Angebote d�rfen nicht um mehr als 10 Punkte abweichen.
			assertEquals(true, so.quality > 40);
			assertEquals(true, so.quality < 60);
		}

	}

	@Test
	public void requestCase50() throws Exception {
		// Senden vorbereiten
		msg.addRequest("Geh�use", 50);

		// Hinzuf�gen zu einem Array
		toSend.add(msg.getSendMessage());
		// Senden an GameEngine und entgegennehmen der Antwort
		toReceive = GameEngine.getGameEngine().startNextRound(toSend);
		// Speichern in simpler Message.
		received = toReceive.remove(0);
		// Analyse der Antwort:
		assertEquals(100, received.purchase.requests.get(0).quality);
		assertEquals("Geh�use", received.purchase.requests.get(0).name);
		for (SupplierOfferToClient so : received.purchase.requests.get(0).supplierOffers) {
			// Die Angebote d�rfen nicht um mehr als 10 Punkte abweichen.
			assertEquals(true, so.quality > 40);
			assertEquals(true, so.quality < 60);
		}

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void wageNegative() throws Exception {
		// set wage
		msg.setWage(-1);
		// hinzuf�gen zum Array
		toSend.add(msg.getSendMessage());
		// senden an GameEngine
		GameEngine.getGameEngine().startNextRound(toSend);
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void wage0() throws Exception {
		// set wage
		msg.setWage(0);
		// hinzuf�gen zum Array
		toSend.add(msg.getSendMessage());
		// senden an GameEngine
		GameEngine.getGameEngine().startNextRound(toSend);
	}
	
	@Test
	public void benefitBooking() throws Exception {
		// set wage
		msg.addBenefit("Sport", 15);;
		// hinzuf�gen zum Array
		toSend.add(msg.getSendMessage());
		// senden an GameEngine
		GameEngine.getGameEngine().startNextRound(toSend);
	}
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void benefitBookingWrongName() throws Exception {
		// set wage
		msg.addBenefit("Sporsdet", 15);;
		// hinzuf�gen zum Array
		toSend.add(msg.getSendMessage());
		// senden an GameEngine
		GameEngine.getGameEngine().startNextRound(toSend);
	}
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void benefitBookingNullName() throws Exception {
		// set wage
		msg.addBenefit(null, 15);;
		// hinzuf�gen zum Array
		toSend.add(msg.getSendMessage());
		// senden an GameEngine
		GameEngine.getGameEngine().startNextRound(toSend);
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void benefitBookingEmptyName() throws Exception {
		// set wage
		msg.addBenefit("", 15);;
		// hinzuf�gen zum Array
		toSend.add(msg.getSendMessage());
		// senden an GameEngine
		GameEngine.getGameEngine().startNextRound(toSend);
	}
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void benefitBookingNegativeRound() throws Exception {
		// set wage
		msg.addBenefit("Sport", -1);;
		// hinzuf�gen zum Array
		toSend.add(msg.getSendMessage());
		// senden an GameEngine
		GameEngine.getGameEngine().startNextRound(toSend);
	}
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void benefitBookingRound0() throws Exception {
		// set wage
		msg.addBenefit("Sport", 0);;
		// hinzuf�gen zum Array
		toSend.add(msg.getSendMessage());
		// senden an GameEngine
		GameEngine.getGameEngine().startNextRound(toSend);
	}
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void benefitBookingRoundOver() throws Exception {
		GameEngine.getGameEngine().startNextRound(toSend);
		GameEngine.getGameEngine().startNextRound(toSend);
		// set wage
		msg.addBenefit("Sport", 1);;
		// hinzuf�gen zum Array
		toSend.add(msg.getSendMessage());
		// senden an GameEngine
		GameEngine.getGameEngine().startNextRound(toSend);
	}

}
