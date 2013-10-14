package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Der Beschaffungsmarkt ist für alle Spieler gleich und eins. Die Preise für
 * die Rohstoffe ändern sich nur rundenweise und nicht innerhalb einer Runde.
 * 
 * @author Tarek
 * 
 */
public class SupplierMarket {

	// Singleton Instanz
	private static SupplierMarket	market				= null;

	// -------------------- Verbindung zu anderen Abteilungen
	private ArrayList<Purchase>		listOfPurchase		= new ArrayList<Purchase>();

	// ------------- Liste aller Angebote
	private ArrayList<Request>		listOfRequest		= null;

	// -------------------- aktuelle Preislisten
	// Preisliste der Wafer
	private TreeSet<TResourcePrice>	waferPricelist		= null;

	// Preisliste der Gehäuse
	private TreeSet<TResourcePrice>	casePricelist		= null;

	// -------------------- Ursprungspreislisten
	// Auf diese werden die Spreads addiert
	// Preisliste der Wafer
	private int[]					waferPricelistBase	= new int[100];

	// Preisliste der Gehäuse
	private int[]					casePricelistBase	= new int[100];

	// ------------------ Verkaufsinformationen
	// Liste mit den Zuschlagssaetzen
	int[][]							spreadsWafer		= new int[5][100];

	// Liste mit den Zuschlagssaetzen
	int[][]							spreadsCase			= new int[5][100];

	// Rundenzaehler um die zu wissen welcher Zuschlagssatz ueberschrieben
	// werden soll
	int								roundCounter		= 0;

	/**
	 * Liefert die Instanz auf den Markt zurück. (Singleton)
	 * 
	 * @return SupplierMarket: Instanz des Marktes
	 */
	// TODO:
	public static SupplierMarket getMarket() {

		// Pruefe, ob der Markt bereits erstellt worden ist und geben es zurück
		// oder erstelle es neu
		return market = ((market == null) ? new SupplierMarket() : market);
	}

	/**
	 * Private Konstruktor zur Umsetzung des Singleton-Musters
	 */
	// TODO
	private SupplierMarket() {
		waferPricelist = new TreeSet<TResourcePrice>();
		casePricelist = new TreeSet<TResourcePrice>();

		// Startwerte der Waferpreisliste
		for (int i = 1; i <= 100; i++) {
			waferPricelist.add(new TResourcePrice(i, 300 + i * 2));
			waferPricelistBase[i - 1] = 300 + i * 2;
		}

		// Startwerte der Gehäusepreisliste
		for (int i = 1; i <= 100; i++) {
			casePricelist.add(new TResourcePrice(i, 4000 + i * 10));
			casePricelistBase[i - 1] = 4000 + i * 10;
		}
	}

	/**
	 * Registriert eine Einkaufsabteilung beim Markt
	 * 
	 * @param p
	 *            die zuregistrierende Einkaufsabteilung
	 * @return true: Wenn die Abteilung hinzugefügt wurde
	 *         false: Wenn die Abteilung bereits in der Liste existert
	 */
	public boolean addPurchase(Purchase p) {
		// Null-Referenzen abfangen
		if (p == null) {
			throw new NullPointerException("Pruchase-Referenz darf nicht null sein!");
		}

		// Pruefe ob Purchase noch nicht in der Liste steht
		if (listOfPurchase.indexOf(p) == -1) { // TODO: indexOf geht auf
			// equals (muss ggf. noch
			// implementiert werden)

			// Abteilung muss hinzugefügt werden
			listOfPurchase.add(p);

			return true;

		} else {
			// Abteilung exisitert beretis
			return false;
		}
	}

	public void handleRequest() throws Exception {

		listOfRequest = new ArrayList<Request>();

		for (Purchase p : listOfPurchase) {
			for (Request r : p.getListOfLatestRequest()) {
				listOfRequest.add(r);
			}
		}

		for (Request r : listOfRequest) {
			int reqQuality = r.getRequestedResource().getQuality();
			String reqName = r.getRequestedResource().getName();
			int[] offerQualities = getOfferQualities( reqQuality);
			if (reqName.equals("Gehäuse")) {
				for( int i=0; i<offerQualities.length; i++) {
					r.addSupplierOffer(new SupplierOffer(new Resource(offerQualities[i], reqName, casePricelist.ceiling(
							new TResourcePrice(offerQualities[i], 1)).getPrice())));
				}
			} else {
				for( int i=0; i<offerQualities.length; i++) {
					r.addSupplierOffer(new SupplierOffer(new Resource(offerQualities[i], reqName, waferPricelist.ceiling(
							new TResourcePrice(offerQualities[i], 1)).getPrice())));
				}
			}

		}
		
		recalculatePrices();
	}
	
	/**
	 * Liefert drei Qualitaeten zurueck, die unterschiedlich sind
	 * @param reqQuality Nachgefrage Qualitaet
	 * @return Array mit 3 unterschiedlichen Qualitaeten
	 */
	private int[] getOfferQualities( int reqQuality ) {
		int[] out = new int[3];
		
		for( int i=0; i < out.length; i++) {
			
			// Sucht bis eine neue Zufallszahl gefunden wurde			
			while(true) {
				int variance = 14;
				int newQuality = reqQuality + (int) Math.floor( Math.random() * variance+1) - variance/2;
				if( newQuality > 0) {
					boolean unique = true;
					for( int j=0; j<out.length; j++) {
						if( out[j] == newQuality ) {
							unique = false;
							break;
						}
					}				
					if(unique) {
						out[i] = newQuality;
						break;
					}
				}
			}
		}
		
		return out;
	}

	private void recalculatePrices() {
		// Summe des Umsatzes aus Sicht des Beschaffungsmarkt in der Runde
		long sumSalesWafer = 0;
		// Summe des Umsatzes aus Sicht des Beschaffungsmarkt in der Runde
		long sumSalesCase = 0;

		// Liste mit den Qualität und den Umsatz, der Qualität
		long[] boughtQualityWafer = new long[100];

		// Liste mit den Qualität und den Umsatz, der Qualität
		long[] boughtQualityCase = new long[100];

		ArrayList<SupplierOffer> acceptedSupplierOffer = new ArrayList<SupplierOffer>();
		// for(Purchase p : listOfPruchase) {
		for (int i = 0; i < listOfPurchase.size(); i++) {
			Purchase p = listOfPurchase.get(i);
			// TODO: WARUM SIND IN DER LISTE NULL DINGER?!?!?!?!
			ArrayList<SupplierOffer> listSupOffers = p.getListOfAcceptedSupplierOffer();
			for (int j = 0; j < listSupOffers.size(); j++) {
				SupplierOffer s = listSupOffers.get(j);
				// for (SupplierOffer s : p.getListOfAcceptedSupplierOffer()) {
				acceptedSupplierOffer.add(s);
				if (s.getResource().getName().equals("Wafer")) {
					sumSalesWafer += s.getResource().getCosts() * s.getOrderedQuantity();
					// Umsatz dieses Offers in der HashMap hinzufügen
					boughtQualityWafer[s.getResource().getQuality() - 1] += s.getResource().getCosts()
							* s.getOrderedQuantity();
				} else if (s.getResource().getName().equals("Gehäuse")) {
					sumSalesCase += s.getResource().getCosts() * s.getOrderedQuantity();
					// Umsatz dieses Offers in der HashMap hinzufügen
					boughtQualityCase[s.getResource().getQuality() - 1] += s.getResource().getCosts()
							* s.getOrderedQuantity();

				}
			}
		}

		// Array mit den neuen Preisspreads (in Prozent)
		int[] newSpreadsWafer = new int[boughtQualityWafer.length];

		// Durch den Array gehen und alle Qualitaeten auswerten
		for (int i = 0; i < boughtQualityWafer.length; i++) {
			// Anteil der Qualitaet am gesamtMarkt berechnen
			int share = (int) Math.floor(boughtQualityWafer[i] * 100.0 / sumSalesWafer);

			// Spread fuer die Qualitaeten, um den Anteil erhoehen
			// Die fuenf davor bzw. danach werden linear erhoeht (20-40-60-80
			// 100(gekaufte Qualität) 80-60-40-20
			for (int j = i - 9; j <= i + 9; j++) {
				// Falls j nicht im Array-Bereich liegt verwerfen
				if (j < 0 || j >= newSpreadsWafer.length) {
					continue;
				}

				// Spread berechnen und addieren fuer j < i
				if (j < i) {
					newSpreadsWafer[j] += share - share * (i - j) * 0.1;
				} else {
					newSpreadsWafer[j] += share - share * (j - i) * 0.1;
				}
			}

		}

		spreadsWafer[roundCounter] = newSpreadsWafer;
		
		waferPricelist = new TreeSet<TResourcePrice>();

		for (int i = 0; i < waferPricelistBase.length; i++) {
			double spread = 1 + spreadsWafer[roundCounter][i] / 100;
			for (int j = 1; j < 5; j++) {
				if (spreadsWafer[(roundCounter + j) % 5] != null) {
					spread = spread * (1 + spreadsWafer[(roundCounter + j) % 5][i] / 100);
				}
			}
			int price = (int) Math.floor(waferPricelistBase[i] * spread);
			waferPricelist.add(new TResourcePrice(i + 1, price));
		}

		// Array mit den neuen Preisspreads (in Prozent)
		int[] newSpreadsCase = new int[boughtQualityCase.length];

		// Durch den Array gehen und alle Qualitaeten auswerten
		for (int i = 0; i < boughtQualityCase.length; i++) {
			// Anteil der Qualitaet am gesamtMarkt berechnen
			int share = (int) Math.floor(boughtQualityCase[i] * 100.0 / sumSalesCase);

			// Spread fuer die Qualitaeten, um den Anteil erhoehen
			// Die fuenf davor bzw. danach werden linear erhoeht (20-40-60-80
			// 100(gekaufte Qualität) 80-60-40-20
			for (int j = i - 9; j <= i + 9; j++) {
				// Falls j nicht im Array-Bereich liegt verwerfen
				if (j < 0 || j >= newSpreadsCase.length) {
					continue;
				}

				// Spread berechnen und addieren fuer j < i
				// TODO eventuell Einfluss auf Nachbarn anders berechnen
				if (j < i) {
					newSpreadsCase[j] += share - share * (i - j) * 0.1;
				} else {
					newSpreadsCase[j] += share - share * (j - i) * 0.1;
				}
			}

		}

		spreadsCase[roundCounter] = newSpreadsCase;

		casePricelist = new TreeSet<TResourcePrice>();

		for (int i = 0; i < casePricelistBase.length; i++) {
			double spread = 1 + spreadsCase[roundCounter][i] / 100;
			for (int j = 1; j < 5; j++) {
				if (spreadsCase[(roundCounter + j) % 5] != null) {
					spread = spread * (1 + spreadsCase[(roundCounter + j) % 5][i] / 100);
				}
			}
			int price = (int) Math.floor(casePricelistBase[i] * spread);
			casePricelist.add(new TResourcePrice(i + 1, price));
		}

		// Rundenzaehler um eins erhoehen
		roundCounter = ++roundCounter % 5;
	}

	/**
	 * Liefert die Preisliste der Wafer zurück
	 * 
	 * @return
	 */
	public TreeSet<TResourcePrice> getWaferPricelist() {
		return waferPricelist;
	}

	/**
	 * Liefert die Preisliste der Gehäuse zurück
	 * 
	 * @return
	 */
	public TreeSet<TResourcePrice> getCasePricelist() {
		return casePricelist;
	}
	
	public boolean removePurchase(Purchase p){
		if (p == null) {
			throw new NullPointerException("Pruchase-Referenz darf nicht null sein!");
		}
		
		return listOfPurchase.remove(p);
		
	}
}
