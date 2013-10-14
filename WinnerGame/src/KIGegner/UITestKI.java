package KIGegner;

import java.util.ArrayList;

import Client.Connection.Client;
import Client.UI.ClientGameUIStart;
import Client.UI.ClientUIStart;
import Message.GameDataMessageToClient;
import Message.GameDataMessageToClient.Loser;
import Message.GameDataMessageToClient.StorageToClient.StorageElementToClient;
import Message.LoginConfirmationMessage;
import Message.LoginMessage;
import Server.Connection.Server;

public class UITestKI extends Thread {
	private Client c;
	// Die KI versucht diese Qualität zu erreichen, oder zu überbieten
	private int qualityTry;
	private String playerName;
	private ArrayList<AmountObject> bankAmounts = new ArrayList<AmountObject>();
	private static int counter = 1;
	private final int id;
	public static GameDataMessageToClient data;

	public static void main(String[] args) {
		// starte den Server
		// Server.main(null);
		// in welchen Sektor soll die KI?
		// Je niedriger die Zahl, desto mehr ist es im billigen Secotr:
		new UITestKI(60);

	}

	private UITestKI(int sector) {
		id = counter;
		System.out.println("KI-" + id + " wurde gestartet!");

		this.qualityTry = sector;
		this.playerName = "KI-Solar" + id;

		counter++;

		if (Login()) {
			// Login erfolgreich

			System.out.println("KI-" + id + " hat sich erfolgreich angemeldet");
			// Starte in die Prozessierung
			this.start();
		}

	}

	/**
	 * kümmert sich um den generellen Verbindungsaufbau und Login prozess
	 * 
	 * @return gibt den Loginstatus zurück
	 */

	private boolean Login() {
		// initialisiere den Client
		c = new Client();
		// erstelle die TCP-Verbindung
		c.connect("127.0.0.1", Constant.Constant.Server.TCP_PORT);
		// Sende die Daten an den Server
		c.writeMessage(new LoginMessage(playerName, "KI-Programmed", "deutschland"));
		// Empfange die Daten
		LoginConfirmationMessage msg = (LoginConfirmationMessage) c
				.readMessage();
		// Gib zurück ob eingeloggt wurde.
		return msg.getSuccess();

	}

	private boolean noLoser(GameDataMessageToClient data) {
		for (Loser l : data.listOfLosers) {
			if (l.name.equals(playerName)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void run() {
		boolean noLoser = false;
		// Bereite Requests und sonstiges für die Erste Runde vor!
		doFirstRound();
		// zweite Runde
		GameDataMessageToClient data = (GameDataMessageToClient) c
				.readMessage();
		if (noLoser(data)) {
			doSecondRound(data);
			noLoser = true;
		}

		// nteRunde
		try {

			while (noLoser) {
				data = (GameDataMessageToClient) c.readMessage();
				if (noLoser(data)) {
					doJob(data);
				} else {
					noLoser = false;
					System.out.println("KI-" + id+ " hat verloren! (Kein Geld mehr)");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("KI-" + id + " wurde beendet");
		for (int i = 0; i < bankAmounts.size(); i++) {

			System.out.println("KI-" + id + " meldet:"
					+ bankAmounts.get(i).toString());
		}

	}

	/**
	 * erstellt eine Antwort für die nte Runde
	 * 
	 * @param readMessage
	 */

	private void doJob(GameDataMessageToClient readMessage) throws Exception {

		data = readMessage;
		
		if (data.round == 6) {
			ClientGameUIStart.main(null);
			this.stop();
		}
		
		if (data.round == 100) {
			throw new Exception("Runde!");
			
		}
	
		if (readMessage == null) {
			throw new Exception("Fehler bei der Nachricht");

		}
		// Protokoll zum Bankkonto
		bankAmounts.add(new AmountObject(readMessage.cash, readMessage.round));

		// Abbruchbedingung zum Testen
		// if (readMessage.cash < 0) {
		// throw new Exception("Negatives Konto");
		// }

		// Erzeuge neue KI-Message
		ClientToServerMessageCreator m = new ClientToServerMessageCreator(
				playerName);
		/******************************
		 * SECTION BESCHAFFUNG
		 */
		// Frage Wafer an:
		m.addRequest("Wafer", qualityTry);

		// Frage Gehäuse an:
		m.addRequest("Gehäuse", qualityTry);

		// Setze den Lohn:
		m.setWage(1000);

		// Intelligenter Ausbau der Maschine (bis Marktsättigung)
		m.setMachine(true);
		for (StorageElementToClient s : readMessage.storage.storageElements) {
			// Es liegen noch Panels auf Lager, wir produzieren also zuviel.
			if (s.type.equals("Panel")) {
				m.setMachine(false);
				break;
			}
		}
		// Maximal produzierende Menge immer ausnutzen
		int toBuy = readMessage.reporting.machinery.maxCapacity;

		// Kaufe keine MarketResearch
		m.setMarketResearch(false);
		/**
		 * SECTION: EINKAUF
		 */
		// Schleife über alle Anfragen:
		boolean waferFound = false;

		int waferPrice = 0;
		int waferQuality = 0;

		boolean caseFound = false;

		int casePrice = 0;
		int caseQuality = 0;
		
		
		for (int i = 0; i < readMessage.purchase.requests.size(); i++) {
			// index des bisher besten angebots zur anfrage:
			int index = 0;
			// PreisLeistung des besten:
			double quotient = 0.0;
			// Schleife über alle Angebote des Marktes zu den Anfragen:
			for (int j = 0; j < readMessage.purchase.requests.get(i).supplierOffers
					.size(); j++) {
				double tmp = readMessage.purchase.requests.get(i).supplierOffers
						.get(j).quality
						/ (readMessage.purchase.requests.get(i).supplierOffers
								.get(j).price * 1.0);

				if (tmp > quotient) {
					quotient = tmp;
					index = j;
				}
			}

			// das beste liegt also an position "index"

			if (readMessage.purchase.requests.get(i).supplierOffers.get(index).name
					.equals("Wafer") && !waferFound) {

				waferPrice = readMessage.purchase.requests.get(i).supplierOffers
						.get(index).price;
				waferQuality = readMessage.purchase.requests.get(i).supplierOffers
						.get(index).quality;
				waferFound = true;
			} else if (readMessage.purchase.requests.get(i).supplierOffers
					.get(index).name.equals("Gehäuse") && !caseFound) {

				casePrice = readMessage.purchase.requests.get(i).supplierOffers
						.get(index).price;
				caseQuality = readMessage.purchase.requests.get(i).supplierOffers
						.get(index).quality;
				caseFound = true;
			}

		}
		// Berechnen der maximalen Stücke mit dem momentanen Geld
		int maxByMoney = (int) ((readMessage.cash * 1.0) / (casePrice + waferPrice
				* Constant.Constant.Production.WAFERS_PER_PANEL));
		// Entscheidung (toBuy ist hier maxByMachine
		toBuy = (toBuy < maxByMoney) ? toBuy : maxByMoney;
		boolean marketFull = false;
		for (StorageElementToClient s : readMessage.storage.storageElements) {
			// Es liegen noch Panels auf Lager, wir produzieren also zuviel.
			if (s.type.equals("Panel")) {
				m.setMachine(true);
				break;
			}
		}
		if (marketFull){
			toBuy = toBuy / 10;
		}
		// Tatsächliche Bestellung
		m.addAccepted("Wafer", waferQuality, (toBuy
				* Constant.Constant.Production.WAFERS_PER_PANEL));
		m.addAccepted("Gehäuse", caseQuality, (toBuy));

		/*************************************
		 * SECTION : PRODUCE + SELLING
		 */

		int indexWafer = -1;
		int noOfPanelsWafer = 0;
		int indexCase = -1;
		for (int i = 0; i < readMessage.storage.storageElements.size(); i++) {
			if (readMessage.storage.storageElements.get(i).type.equals("Panel")) {
				// Vertick die fertigen Panels
				double newCosts = (readMessage.storage.storageElements.get(i).costs * 1.1);
				m.addOffer(
						readMessage.storage.storageElements.get(i).quality,
						readMessage.storage.storageElements.get(i).quantity,
						(int) newCosts );

			} else if (readMessage.storage.storageElements.get(i).type
					.equals("Wafer")) {

				// Absicherung, dass wenigsten fuer ein Panel genug Wafer da
				// sind!
				if ((int) (readMessage.storage.storageElements.get(i).quantity / Constant.Constant.Production.WAFERS_PER_PANEL) > noOfPanelsWafer) {

					noOfPanelsWafer = (int) (readMessage.storage.storageElements
							.get(i).quantity / Constant.Constant.Production.WAFERS_PER_PANEL);
					indexWafer = i;
				}

			} else if (readMessage.storage.storageElements.get(i).type
					.equals("Gehäuse")) {
				indexCase = i;

			}
		}
		if (indexWafer != -1 && indexCase != -1) {
			// Es wurden also genug Wafer und Cases gefunden:
			int maxProduction = (noOfPanelsWafer > readMessage.storage.storageElements
					.get(indexCase).quantity) ? readMessage.storage.storageElements
					.get(indexCase).quantity : noOfPanelsWafer;

			m.addProductionOrder(
					readMessage.storage.storageElements.get(indexWafer).quality,
					readMessage.storage.storageElements.get(indexCase).quality,
					maxProduction);
		}

		// Sende daten zurück an Server:
		sendData(m);

	}

	/**
	 * kümmert sich um die Antwort auf die erste Runde
	 * 
	 * @param readMessage
	 */
	private void doSecondRound(GameDataMessageToClient readMessage) {
		// Protokolliere das Bank konto mit
		bankAmounts.add(new AmountObject(readMessage.cash, readMessage.round));

		// Erzeuge neue KI-Message
		ClientToServerMessageCreator m = new ClientToServerMessageCreator(
				playerName);

		// Frage Wafer an:
		m.addRequest("Wafer", qualityTry);

		// Frage Gehäuse an:
		m.addRequest("Gehäuse", qualityTry);

		// Setze den Lohn:
		// Erste Runde brauchen wir ja kaum Lohn
		m.setWage(1000);

		// Keine Maschinenerweiterung
		m.setMachine(true);

		// Kaufe keine MarketResearch
		m.setMarketResearch(false);

		// Maximale Produktionsauslastung!:
		int toBuy = readMessage.reporting.machinery.maxCapacity;

		// Schleife über alle Anfragen:
		for (int i = 0; i < readMessage.purchase.requests.size(); i++) {
			// index des bisher besten angebots zur anfrage:
			int index = 0;
			// PreisLeistung des besten:
			double quotient = 0.0;
			// Schleife über alle Angebote des Marktes zu den Anfragen:
			for (int j = 0; j < readMessage.purchase.requests.get(i).supplierOffers
					.size(); j++) {
				double tmp = readMessage.purchase.requests.get(i).supplierOffers
						.get(j).quality
						/ (readMessage.purchase.requests.get(i).supplierOffers
								.get(j).price * 1.0);

				if (tmp > quotient) {
					quotient = tmp;
					index = j;
				}
			}

			// das beste liegt also an position "index"

			if (readMessage.purchase.requests.get(i).supplierOffers.get(index).name
					.equals("Wafer")) {
				// Bestelle Wafer
				m.addAccepted(
						readMessage.purchase.requests.get(i).supplierOffers
								.get(index).name, readMessage.purchase.requests
								.get(i).supplierOffers.get(index).quality,
						toBuy * Constant.Constant.Production.WAFERS_PER_PANEL);
			} else {
				// Bestelle Cases
				m.addAccepted(
						readMessage.purchase.requests.get(i).supplierOffers
								.get(index).name, readMessage.purchase.requests
								.get(i).supplierOffers.get(index).quality,
						toBuy);
			}

		}
		// Sende daten zurück an Server:
		sendData(m);

	}

	/**
	 * kümmert sich um die User eingaben der ersten Runde
	 */
	private void doFirstRound() {
		// Erzeuge neue KI-Message
		ClientToServerMessageCreator m = new ClientToServerMessageCreator(
				playerName);

		// Frage Wafer an:
		m.addRequest("Wafer", qualityTry);

		// Frage Gehäuse an:
		m.addRequest("Gehäuse", qualityTry);

		// Setze den Lohn:
		// Erste Runde brauchen wir ja kaum Lohn
		m.setWage(1);

		// Erweitere die Maschine
		m.setMachine(true);

		// Kaufe keine MarketResearch
		m.setMarketResearch(false);

		// Sende daten zurück an Server:
		sendData(m);
	}

	private void sendData(ClientToServerMessageCreator s) {
		c.writeMessage(s.getSendMessage());
	}
}
