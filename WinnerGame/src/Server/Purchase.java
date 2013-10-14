package Server;

import java.io.IOException;
import java.util.ArrayList;

import Constant.Constant;

/**
 * Die Purchase-Klasse stellt die Einkaufsabteilung des Unternehmens dar. Sie
 * kann an den beschaffungsmarkt neue Anfragen stellen und die Antworten
 * daraufhin mit der Angabe der gewuenschten Menge annehmen.
 * 
 * @author Felix
 * 
 */

public class Purchase extends DepartmentRoundSensitive {

	// Liste alle Requests, die jemals gestellt worden sind
	private ArrayList<Request> listOfRequests = new ArrayList<Request>();

	// Liste aller Request dieser Runde
	private ArrayList<Request> listOfLatestRequests = new ArrayList<Request>();
	
	// Liste aller Requests der Vorrunde
	private ArrayList<Request> listOfLastRoundRequests = new ArrayList<Request>();

	// Liste aller akzeptierten SupplierOffers dieser Runde ( für den Markt )
	private ArrayList<SupplierOffer> listOfLatestAcceptedSupplierOffers = new ArrayList<SupplierOffer>();

	/**
	 * Name wird vom Konstruktor gesetzt.
	 * 
	 * @param c
	 *            Company, Unternehmensreferenz
	 * @param fix
	 *            FixKosten
	 * @throws Exception
	 *             falls keine Korrekte erstellung
	 */
	public Purchase(Company c) throws Exception {
		super(c, "Einkauf", Constant.DepartmentFixcost.PURCHASE);
		SupplierMarket.getMarket().addPurchase(this);
	}

	/**
	 * privater Konstruktor, dass niemand einen falschen Abteilungsnamen eingibt
	 * 
	 * @param c
	 *            Company = Unternehmen
	 * @param n
	 *            Name = Abteilungsname
	 * @param f
	 *            fixCosts = Fix Kosten der Abteilung
	 * @throws Exception
	 *             falls die Abteilung nicht korrekt erstellt werden konnte
	 */
	private Purchase(Company c, String n, int f) throws Exception {
		super(c, n, f);
		
	}

	/**
	 * erstellt eine neue Anfrage an den Beschaffungsmarkt
	 * 
	 * @param resource
	 *            : Für eine spezielle resource(Wafer oder Case inklusive
	 *            Qualität)
	 * @throws Exception
	 *             : falls die Resource null ist
	 */

	public void createRequest(Resource resource) throws Exception {
		if (resource == null) {
			throw new NullPointerException(
					"Resource is null! Class Purchase Method createRequest");
		}

		Request request = new Request(resource);
		// Request request = Request.create(resource);
		listOfRequests.add(request);
		listOfLatestRequests.add(request);
	}

	/**
	 * Die SupplierOffer wird unter Angabe eine gewuenschten Menge angenommen
	 * 
	 * @param supplierOffer
	 *            : Offer auf die sich bezogen wird
	 * @param quantity
	 *            : Menge, die gewuenscht ist
	 * @throws Exception
	 *             : falls SupplierOffer null ist oder die Anzahl kleiner null.s
	 */

	public void acceptSupplierOffer(SupplierOffer supplierOffer, int quantity)
			throws Exception {
		if (supplierOffer == null) {
			throw new NullPointerException(
					"supplierOffer is null! Class Purchase Method acceptSupplierOffer");
		}

		if (quantity <= 0) {
			throw new IOException(
					"Quantity is 0 or lower! Class Purchase Method acceptSupplierOffer");
		}

		Resource resource = supplierOffer.getResource();
		if (resource == null) {
			throw new NullPointerException(
					"resource of supplierOffer is null! Class Purchase Method acceptSupplierOffer");
		}

		Storage storage = this.getCompany().getStorage();

		// Bucht den Betrag vom Konto ab. Nur wenn der Betrag abgebucht werden
		// kann, wird das Lager erhöht!
		if (getCompany().getBankAccount().decreaseBalance(
			(long)	quantity * (long) supplierOffer.getResource().getCosts())) {
			storage.store(resource, quantity);
			supplierOffer.setOrderedQuantity(quantity);
			listOfLatestAcceptedSupplierOffers.add(supplierOffer);
		}
	}

	public ArrayList<Request> getListOfRequest() {
		return listOfRequests;
	}

	/**
	 * Liefert eine Liste der neuen Request dieser Runde zurück
	 * 
	 * @return Liste der Request
	 */
	public ArrayList<Request> getListOfLatestRequest() {
		return listOfLatestRequests;
	}

	public ArrayList<SupplierOffer> getListOfAcceptedSupplierOffer() {
		return listOfLatestAcceptedSupplierOffers;
	}
	
	/**
	 * Liefert alle Requests zurueck, die in der Vorrunde erzeugt worden sind (und in der aktuellen angenommen werden koennen)
	 * @return Liste der Requests
	 */
	public ArrayList<Request> getListOfLastRoundRequests() {
		return listOfLastRoundRequests;
	}

	@Override
	public void prepareForNewRound(int round) {
		listOfLastRoundRequests = listOfLatestRequests;
		listOfLatestRequests = new ArrayList<Request>();
		listOfLatestAcceptedSupplierOffers = new ArrayList<SupplierOffer>();
	}

}
