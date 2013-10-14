package KIGegner;

import java.util.ArrayList;

import Message.GameDataMessageFromClient;
import Message.GameDataMessageFromClient.DistributionFromClient;
import Message.GameDataMessageFromClient.HumanResourcesFromClient;
import Message.GameDataMessageFromClient.ProductionFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient;
import Message.GameDataMessageFromClient.DistributionFromClient.OfferFromClient;
import Message.GameDataMessageFromClient.HumanResourcesFromClient.BenefitBookingFromClient;
import Message.GameDataMessageFromClient.ProductionFromClient.ProductionOrderFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient.AcceptedSupplierOfferFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient.RequestFromClient;

public class ClientToServerMessageCreator {
	// Liste aller bisher erstellten KI-Messages
	private static ArrayList<ClientToServerMessageCreator> listOfMessages = new ArrayList<ClientToServerMessageCreator>();

	// Name des Spielers:
	private String player;
	// Liste der requests für diese KI-Message
	private ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
	// Liste der annahmen für Requests der vorigen Message:
	private ArrayList<AcceptedSupplierOfferFromClient> accepted = new ArrayList<AcceptedSupplierOfferFromClient>();
	// Liste der Produktionsaufträge:
	private ArrayList<ProductionOrderFromClient> proOrder = new ArrayList<ProductionOrderFromClient>();
	// Liste der Angebote an den Markt
	private ArrayList<OfferFromClient> offerList = new ArrayList<OfferFromClient>();
	// Liste der Benefits die gekauft werden:
	private ArrayList<BenefitBookingFromClient> bBook = new ArrayList<BenefitBookingFromClient>();

	// Lohn in dieser Message:
	private int wage = 1;
	// Maschinenausbau erwünscht?
	private boolean machine = false;
	// Marktforschung erwünscht?
	private boolean marketResearch = true;

	public ClientToServerMessageCreator(String player) {
		listOfMessages.add(this);
		this.player = player;
	}

	/**
	 * fügt einen Request an die Liste der Anfragen an
	 * 
	 * @param ressourceName
	 *            : Name der Ressource (Wafer oder Gehäuse)
	 * @param requestedQuality
	 *            : angefragte Qualität
	 */
	public void addRequest(String ressourceName, int requestedQuality) {
		requests.add(new RequestFromClient(ressourceName, requestedQuality));
	}

	/**
	 * setzt den Lohn für diese Message
	 * 
	 * @param wage
	 */
	public void setWage(int wage) {
		this.wage = wage;
	}

	/**
	 * true, falls der MAschinen park ausgebaut werden soll
	 * 
	 * @param machine
	 */
	public void setMachine(boolean machine) {
		this.machine = machine;
	}

	/**
	 * nimmt ein Angebot an
	 * 
	 * @param ressourceName
	 *            : Wafer oder Gehäuse
	 * @param orderedQuality
	 *            :qualität die bestellt wird
	 * @param orderedQuantity
	 *            : menge, die bestellt wird
	 */
	public void addAccepted(String ressourceName, int orderedQuality,
			int orderedQuantity) {
		accepted.add(new AcceptedSupplierOfferFromClient(ressourceName,
				orderedQuality, orderedQuantity));
	}

	/**
	 * Soll die Marktforschung gekauft werden?
	 * 
	 * @param marketResearch
	 */
	public void setMarketResearch(boolean marketResearch) {
		this.marketResearch = marketResearch;
	}

	/**
	 * Erzeugt einen neuen Produktionsauftrag
	 * 
	 * @param waferQuality
	 *            Wafer die in der Produktion verwendet werden sollen
	 * @param caseQuality
	 *            Gehäuse für die Produktion
	 * @param quantity
	 *            Plan produktion
	 */
	public void addProductionOrder(int waferQuality, int caseQuality,
			int quantity) {
		proOrder.add(new ProductionOrderFromClient(waferQuality, caseQuality,
				quantity));
	}

	/**
	 * Erstellt ein neues Verkafsangebot an den Markt
	 * 
	 * @param panelQuality
	 *            Qualität, die verkauft werden soll
	 * @param quantityToSell
	 *            Menge, die verkauft werden soll
	 * @param priceToSell
	 *            Preis, zu dem verkauft werden soll
	 */
	public void addOffer(int panelQuality, int quantityToSell, int priceToSell) {
		offerList.add(new OfferFromClient(panelQuality, quantityToSell,
				priceToSell));
	}

	/**
	 * Bucht ein neues Benefit
	 * 
	 * @param benefitName
	 *            Name des Benefits
	 * @param rounds
	 *            Rundenanzahl, für die es gebucht wird.
	 */
	public void addBenefit(String benefitName, int rounds) {
		bBook.add(new BenefitBookingFromClient(benefitName, rounds));
	}

	/**
	 * erzeugt ein neues GameDataMessage Objekt zum versenden
	 * 
	 * @return
	 */
	public GameDataMessageFromClient getSendMessage() {
		// Mach ein paar checkroutinen:

		return new GameDataMessageFromClient(player, new PurchaseFromClient(
				requests, accepted), new ProductionFromClient(proOrder),
				new DistributionFromClient(offerList), machine,
				new HumanResourcesFromClient(bBook), wage, marketResearch);

	}

}
