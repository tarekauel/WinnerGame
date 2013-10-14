package Server;

import java.util.ArrayList;
import java.util.Collections;

import Constant.Constant;

public class Distribution extends DepartmentRoundSensitive {
	
	// Liste aller jemals erstellten Offers
	private ArrayList<Offer> listOfOffers = new ArrayList<Offer>();
	
	// Liste aller Offers dieser Runde
	private ArrayList<Offer> listOfLatestOffers = new ArrayList<Offer>();
	
	/**
	 * Regulaerer Konstruktor der Distribution, 
	 * 
	 * @param c
	 *            Referenz des Unternehmens
	 * @param fix
	 *            Fixkosten
	 * @throws ExceptionO
	 *             falls Abteilung nicht erstellt werden konnte
	 */
	public Distribution(Company c)throws Exception{
		super(c,"Verkauf",Constant.DepartmentFixcost.DISTRIBUTION);
		
		CustomerMarket.getMarket().addDistribution(this);
		
	}
	
	/**
	 * privater Konstruktor damit niemand eine Falsche Abteilung erzeugen kann
	 * (name falsch)
	 * 
	 * @param c
	 *            Referenz des Unternehmens
	 * @param n
	 *            Name der Abteilung
	 * @param f
	 *            Fixkosten der Abteilung
	 * @throws Exception
	 */
	private Distribution(Company c, String n, int f) throws Exception {
		super(c, n, f);		
		
	}

/**
 * erstellt ein Offer fuer ein Fertigprodukt mit der uebergebenen Qualitaet, Preis und
 * Menge die verkauft werden soll
 * @param quality Qualitaet des Fertigprodukts
 * @param quantityToSell Menge die verkauft werden soll
 * @param price Preis zu dem verkauft werden soll
 * @throws Exception 
 */
	public void createOffer(int quality, int quantityToSell, int price) throws Exception{
		
		Storage storage = this.getCompany().getStorage();
		StorageElement storageElement = storage
				.getFinishedGoodByQuality(quality);

		if (storageElement == null) {
			throw new IllegalArgumentException(
					"StorageElement could not found! Class Distribution Method createOffer");
		}

		int round = GameEngine.getGameEngine().getRound(); 
		int sold = 0; // die verkaufte Menge ist beim erstellen eines Offers
						// immer 0.
		
			Offer offer = new Offer(quantityToSell, price, round, sold,
					storageElement, this);
			listOfOffers.add(offer);
			listOfLatestOffers.add(offer);
	
		getCompany().getBankAccount().decreaseBalance(Constant.Distribution.DISTRIBUTION_OFFER_COSTS_PER_PANEL*quantityToSell);
		
		
	}// createOffer

	/**
	 * 
	 * @return liefert eine Liste aller Angebote
	 */
	public ArrayList<Offer> getListOfOffers() {
		
		return listOfOffers;
	}// getListOfOffers
	/**
	 * 
	 * @return liefert eine sortierte Liste aller Angebote dieser Runde
	 */
	public ArrayList<Offer> getSortedListOfLatestOffers() {
		ArrayList<Offer> listOfLatestOffers = new ArrayList<Offer>();
		for (Offer elem : listOfOffers) {
			if (elem.getRound() == GameEngine.getGameEngine().getRound()) {
				listOfLatestOffers.add(elem);
			}
		}
		Collections.sort(listOfLatestOffers);
		
		
		
		return listOfLatestOffers;
	}
	
	/**
	 * Liefert eine Liste der aktuellen Offer zurueck
	 * @return Liste der aktuellen Offer
	 */
	public ArrayList<Offer> getListOfLatestOffers() {
		
		return listOfLatestOffers;
	}
	
	@Override
	public void prepareForNewRound( int round ) {
		
		listOfLatestOffers = new ArrayList<Offer>();
	}
}
