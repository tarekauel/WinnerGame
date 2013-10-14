package KIGegner;

import java.text.NumberFormat;
import java.util.ArrayList;

import Client.Connection.Client;
import Client.UI.ClientGameUIStart;
import Constant.Constant;
import Message.GameDataMessageToClient;
import Message.GameDataMessageToClient.DistributionToClient.OfferToClient;
import Message.GameDataMessageToClient.PurchaseToClient.RequestToClient;
import Message.GameDataMessageToClient.PurchaseToClient.RequestToClient.SupplierOfferToClient;
import Message.GameDataMessageToClient.ReportingToClient.FixCostToClient;
import Message.GameDataMessageToClient.StorageToClient.StorageElementToClient;
import Message.LoginConfirmationMessage;
import Message.LoginMessage;

public class KITarek extends Thread {
		
	private Client c;
	
	private String playerName;
	
	private int quality = 60;
	
	private int stopRound = 0;
	private int toBuy = 200;
	
	public ArrayList<String> historie = new ArrayList<String>();
	
	private NumberFormat formatter = NumberFormat.getCurrencyInstance();
	
	public static GameDataMessageToClient reply; 
	public static ClientToServerMessageCreator m;
	
	public static void main(String[] args) {
		new KITarek( 10 );
	}
	
	public KITarek(int round) {
		stopRound = round;
		if( login()) {
			System.out.println("Login-OK");
			start();
		}
	}
	
	private boolean login() {
		this.c = new Client();
		this.c.connect("127.0.0.1", Constant.Server.TCP_PORT);
		playerName = "KiTarek";
		// Sende die Daten an den Server
		c.writeMessage(new LoginMessage(playerName, "KI-Programmed",
				"usa"));
		// Empfange die Daten
		LoginConfirmationMessage msg = (LoginConfirmationMessage) c
				.readMessage();
		// Gib zurück ob eingeloggt wurde.
		return msg.getSuccess();

	}
	
	@Override
	public void run() {
		//c.readMessage();
		doFirstRound();	
		reply = (GameDataMessageToClient) c.readMessage();
		m = new ClientToServerMessageCreator(playerName);
		doSecondRound();
		while(true) {
			System.out.println( String.format("Runde: %d  Guthaben: "+formatter.format(reply.cash / 100.0), reply.round   ) );
			double sumFixCosts = 0.0;
			for(FixCostToClient fix : reply.reporting.fixCosts) {
				sumFixCosts += fix.costs;
			}
			System.out.println( "Fixkosten: " + formatter.format(sumFixCosts/100.0));
			double sumSales = 0.0;
			double sumCosts = 0.0;
			int countSales=0;
			for(OfferToClient offer : reply.distribution.offers ) {
				if( offer.round == reply.round-1) {
					sumSales += offer.price * offer.quantitySold;
					sumCosts += offer.costs * offer.quantityToSell;
					countSales += offer.quantitySold;
				}
			}
			System.out.println("Anzahl der Verkäufe: " + countSales);
			System.out.println("Umsatz der Verkäufe: " + formatter.format(sumSales/100.0));
			System.out.println("Kosten der Herrstellung: " + formatter.format(sumCosts/100.0));
			System.out.println("");
			reply = (GameDataMessageToClient) c.readMessage();
			m = new ClientToServerMessageCreator(
					playerName);
			play();
			if ( reply.round == stopRound) {
				ClientGameUIStart.main(null);
				break;
			}
			sendData(m);
		}
		for(String s:historie) {
			System.out.println(s);
		}

	}
	
	private void doFirstRound() {
		// Erzeuge neue KI-Message
		m = new ClientToServerMessageCreator(
				playerName);

		// Frage Wafer an:
		m.addRequest("Wafer", quality);

		// Frage Gehäuse an:
		m.addRequest("Gehäuse", quality);

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
	
	private void doSecondRound() {
		acceptOffer();
		sendData(m);
	}
	
	private void acceptOffer() {
		RequestToClient reqA = reply.purchase.requests.get(reply.purchase.requests.size()-1);
		RequestToClient reqB = reply.purchase.requests.get(reply.purchase.requests.size()-2);
		
		int toProduce = toBuy;				
		toBuy *= 1.14;
		double plA = 0.0;
		SupplierOfferToClient toAcceptA = null;
		for( SupplierOfferToClient offer : reqA.supplierOffers ) {
			if( ((double) offer.price) / offer.quality  > plA ) {
				toAcceptA = offer;
				plA = ((double) offer.price) / offer.quality;
			}
		}
		
		int quantityA = (toAcceptA.name.equals("Wafer")) ? toProduce * Constant.Production.WAFERS_PER_PANEL : toProduce; 
		m.addAccepted(toAcceptA.name, toAcceptA.quality, quantityA);
		
		double plB = 0.0;
		SupplierOfferToClient toAcceptB = null;
		for( SupplierOfferToClient offer : reqB.supplierOffers ) {
			if( ((double) offer.price) / offer.quality  > plB ) {
				toAcceptB = offer;
				plB = ((double) offer.price) / offer.quality;
			}
		}
		
		int quantityB = (toAcceptB.name.equals("Wafer")) ? toProduce * Constant.Production.WAFERS_PER_PANEL : toProduce; 
		m.addAccepted(toAcceptB.name, toAcceptB.quality, quantityB);
		
	}
	
	private void play() {
		acceptOffer();
		sendSales();
		m.addRequest("Wafer", quality);
		m.addRequest("Gehäuse", quality);
		int toProduce = 0;
		int waferQuality = 0;
		int maxWaferCount = 0;
		int caseQuality = 0;
		int maxCaseCount = 0;
		for( StorageElementToClient elem : reply.storage.storageElements) {
			if( elem.type.equals("Wafer")) {
				if( elem.quantity > maxWaferCount) {
					toProduce = elem.quantity / Constant.Production.WAFERS_PER_PANEL;
					maxWaferCount = elem.quantity;
					waferQuality = elem.quality;
				}				
			} else if( elem.type.equals("Gehäuse")) {
				if ( elem.quantity > maxCaseCount ) {
					maxCaseCount = elem.quantity;
					caseQuality = elem.quality;
				}
			}
		}
		toProduce--;
		if( waferQuality > 0 && caseQuality > 0 && toProduce > 0)
			m.addProductionOrder(waferQuality, caseQuality, toProduce);
		m.setWage((int) Math.floor( Math.random() * 6) + 7); // Lohn zwischen 7 und 13
	}
	
	private void sendSales() {
		for( StorageElementToClient elem : reply.storage.storageElements) {
			if(elem.type.equals("Panel")) {
				int menge =  (elem.quantity-1);
				menge = (menge > 1) ? menge / 2 : menge;
				if( menge >= 1)
					m.addOffer(elem.quality, menge, elem.costs*2);
			}
		}
	}
	
	private void sendData(ClientToServerMessageCreator s) {
		c.writeMessage(s.getSendMessage());
	}

}
