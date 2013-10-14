package aaaaa;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Client.UI.ClientGameUIController;
import Client.UI.ClientUIStart;
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
import Message.GameDataMessageToClient;
import Message.GameDataMessageToClient.DistributionToClient.OfferToClient;
import Message.GameDataMessageToClient.HumanResourcesToClient.BenefitBookingToClient;
import Message.GameDataMessageToClient.ProductionToClient.ProductionOrderToClient;
import Message.GameDataMessageToClient.PurchaseToClient.RequestToClient;
import Message.GameDataMessageToClient.PurchaseToClient.RequestToClient.SupplierOfferToClient;
import Server.Benefit;
import Server.Company;
import Server.GameEngine;
import Server.Location;

public class GameTestConsole {

	private static BufferedReader		console		= new BufferedReader(new InputStreamReader(System.in));

	private static GameEngine			game		= GameEngine.getGameEngine();

	private static ArrayList<String>	playernames	= new ArrayList<String>();

	public static GameDataMessageToClient data;
	
	public static void main(String[] args) throws Exception {

		startGame();

		while (true) {
			play();
		}

	}

	public static void startGame() throws Exception {
		Benefit.initBenefits();
		Location.initLocations();

		System.out.println("Willkommen beim Planspiel...");
		System.out.println("Zunächst muessen die Spieler erstellt werden. Keine Eingabe beendet das Erstellen.");
		System.out.println("Folgende Länder sind verfügbar");

		String locations = "";
		for (Location l : Location.getListOfLocations()) {
			locations += l.getCountry() + ", ";
		}

		System.out.println(locations.substring(0, locations.length() - 2));

		int counter = 0;

		while (true) {
			System.out.print("Spielername: ");
			String name = console.readLine();
			if (name.equals("")) {
				System.out.println("Spieler hinzufuegen wurde beendet");
				break;
			}
			System.out.print("Land: ");
			String location = console.readLine();
			new Company(Location.getLocationByCountry(location), name);
			++counter;
			playernames.add(name);
		}
		if (counter < 1) {
			throw new IllegalArgumentException("Spiel wurde mit nur einem Spieler gestartet!");
		}

	}

	private static void play() throws Exception {
		ArrayList<GameDataMessageFromClient> messages = new ArrayList<GameDataMessageFromClient>();
		for (String player : playernames) {
			System.out.println();
			System.out.println();
			System.out.println("Spieler: " + player);
			System.out.println("Zunaechst folgen die Bestellungen der Rohstoffe z.B. \"Wafer;20\"");
			ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();

			while (true) {
				String input = console.readLine();
				if (input.equals("")) {
					break;
				}
				try {
					requests.add(new RequestFromClient(input.split(";")[0], Integer.parseInt(input.split(";")[1])));
					System.out.println("--Entry added-- [Enter um Block zu beenden]");
				} catch (IndexOutOfBoundsException e) {
					System.err.println("Ungueltige Eingabe wurde verworfen!");
				}
			}

			ArrayList<AcceptedSupplierOfferFromClient> accepted = new ArrayList<AcceptedSupplierOfferFromClient>();
			System.out.println("Nun muessen Angebote akzeptiert werden z.B. \"Wafer;20;200\"");
			while (true) {
				String input = console.readLine();
				if (input.equals("")) {
					break;
				}
				try {
					String[] splitInput = input.split(";");
					accepted.add(new AcceptedSupplierOfferFromClient(splitInput[0], Integer.parseInt(splitInput[1]),
							Integer.parseInt(splitInput[2])));
					System.out.println("--Entry added-- [Enter um Block zu beenden]");
				} catch (IndexOutOfBoundsException e) {
					System.err.println("Ungueltige Eingabe wurde verworfen!");
				}
			}

			ArrayList<ProductionOrderFromClient> proOrder = new ArrayList<ProductionOrderFromClient>();
			System.out
					.println("Nun muessen Produktionsauftraege gestartet werden z.B. \"20;20;200\" (Qualitaet Wafer;Qualitaet Gehäuse;Anzahl");
			while (true) {
				String input = console.readLine();
				if (input.equals("")) {
					break;
				}
				try {
					String[] splitInput = input.split(";");
					proOrder.add(new ProductionOrderFromClient(Integer.parseInt(splitInput[0]), Integer
							.parseInt(splitInput[1]), Integer.parseInt(splitInput[2])));
					System.out.println("--Entry added-- [Enter um Block zu beenden]");
				} catch (IndexOutOfBoundsException e) {
					System.err.println("Ungueltige Eingabe wurde verworfen!");
				}
			}

			ArrayList<OfferFromClient> offerList = new ArrayList<OfferFromClient>();
			System.out.println("Nun muessen Angebote verschickt werden z.B. \"20;20;200\" (Qualitaet;Anzahl;Preis");
			while (true) {
				String input = console.readLine();
				if (input.equals("")) {
					break;
				}
				try {
					String[] splitInput = input.split(";");
					offerList.add(new OfferFromClient(Integer.parseInt(splitInput[0]), Integer.parseInt(splitInput[1]),
							Integer.parseInt(splitInput[2])));
					System.out.println("--Entry added-- [Enter um Block zu beenden]");
				} catch (IndexOutOfBoundsException e) {
					System.err.println("Ungueltige Eingabe wurde verworfen!");
				}
			}

			ArrayList<BenefitBookingFromClient> bBook = new ArrayList<BenefitBookingFromClient>();

			System.out.println("Bitte gib die Benefits an die du Buchen moechtest und die Dauer z.B. \"Sport;2\"");
			System.out.println("Folgende Benefits sind verfügbar");

			String benefits = "";
			for (Benefit b : Benefit.getBookableBenefits()) {
				benefits += b.getName() + " (" + b.getCostsPerRound() + "), ";
			}

			System.out.println(benefits.substring(0, benefits.length() - 2));
			while (true) {
				String input = console.readLine();
				if (input.equals("")) {
					break;
				}
				try {
					bBook.add(new BenefitBookingFromClient(input.split(";")[0], Integer.valueOf(input.split(";")[1])));
					System.out.println("--Entry added-- [Enter um Block zu beenden]");
				} catch (IndexOutOfBoundsException e) {
					System.err.println("Ungueltige Eingabe wurde verworfen!");
				}
			}
			Integer wage = null;
			while (wage == null) {
				System.out.print("Bitte gib den neuen Lohn an: ");
				try { wage = Integer.valueOf(console.readLine()); } catch(Exception e) { System.out.println("Lohn ungueltig!"); }
			}
			System.out.println("Moechtest du in der naechsten Runde Marketin kaufen? [Y/N] ");
			boolean marketing = (console.readLine().toUpperCase().equals("Y") ? true : false);

			System.out.println("Moechtest du in der naechsten Runde deinen Maschinenparkt erweitern? [Y/N] ");
			boolean maschine = (console.readLine().toUpperCase().equals("Y") ? true : false);

			messages.add(new GameDataMessageFromClient(player, new PurchaseFromClient(requests, accepted),
					new ProductionFromClient(proOrder), new DistributionFromClient(offerList), maschine,
					new HumanResourcesFromClient(bBook), wage, marketing));
		}

		for (GameDataMessageToClient answer : game.startNextRound(messages)) {
			//TODO
			
			data = answer;
			ClientUIStart.main(null);
			//END OF WoRK
			System.out.println("Auswertung Spieler" + answer.getPlayerName());
			
			System.out.println("Antwort vom Purchase");
			for (int i = answer.purchase.requests.size() - 1; i >= 0; i--) {
				RequestToClient req = answer.purchase.requests.get(i);
				System.out.println("Anfrage: #" + i + " " + req.name + " " + req.quality);
				for (int j = 0; j < req.supplierOffers.size(); j++) {
					SupplierOfferToClient offer = req.supplierOffers.get(j);
					System.out.println("Offer: #" + j + " " + offer.name + " " + offer.quality);
				}
			}
			
			System.out.println("Antwort der Produktion");
			for( int i = answer.production.orders.size() - 1; i>=0; i--)  {
				ProductionOrderToClient order = answer.production.orders.get(i);
				System.out.println("Order: #"+i+" W: " + order.qualityWafer + " C: " + order.qualityCase + " Anzahl:" + order.quantity );
			}
			
			System.out.println("Antwort vom HR"); 
			for( int i=answer.humanResources.benefits.size() - 1; i>=0; i--) {
				BenefitBookingToClient b = answer.humanResources.benefits.get(i);
				System.out.println(b.name + " Restlaufzeit: " + b.remainingRounds);
			}
			System.out.println("Aktueller Lohn: " + answer.humanResources.myWage + " Avg.: " + answer.humanResources.averageWage);
			System.out.println("Lohnkosten der letzten Runde " + answer.humanResources.wageCosts + " bei " + answer.humanResources.countEmployees + " Mitarbeitern");
			
			System.out.println("Antwort vom Verkauf");
			for( int i=answer.distribution.offers.size()- 1; i>=0; i--) {
				OfferToClient o = answer.distribution.offers.get(i);
				System.out.println("Angebot #" + i + " Q. " + o.quality + " Price " + o.price + "  Verkauft " + o.quantitySold + " von " + o.quantityToSell);
			}
			
			
			
		}
	}

}
