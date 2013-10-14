package Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import Constant.Constant;

/**
 * Der CustomerMarket existiert f�r alle Spieler gemeinsam. Er arbeitet die
 * Angebote der Spieler ab und entscheidet f�r jeden K�ufer welches Angebot er
 * ausw�hlt
 * 
 * @author Tarek
 * TODO Maximale Peak-Verschiebung
 */
public class CustomerMarket {

	// Singleton Instanz
	private static CustomerMarket		market					= null;							// TODO

	// --------------------------------------- A-Markt
	// --------------------------------------------------------------
	// aktueller Nachfragepeak im A-Markt
	// Standardwert: Q 7,5;
	private int							aMarketPeak;

	// aktuelle Nachfragemenge im A-Markt
	// Standardwert: 100 ME;
	private int							aMarketQuantity;											// TODO

	// Standardabweichung zur Berechnung im A-Markt
	private final double				aMarketVariance;											// TODO

	// Faktor zum Wachstum vom A-Markt
	// F�r X bediente Kunden kommt einer hinzug
	private final double				aMarketIncreaseFactor;										// TODO

	// Faktor zur Abnahme vom A-Markt
	// F�r X nicht bediente Kunden geht einer
	private final double				aMarketDecreaseFactor;										// TODO

	// --------------------------------------- C-Markt
	// --------------------------------------------------------------
	// aktueller Nachfragepeak im C-Markt
	// Standardwert: Q 2,5
	private int							cMarketPeak;

	// aktuelle Nachfragemenge im C-Markt
	// Standardwert: 200 ME;
	private int							cMarketQuantity;											// TODO

	// Standardabweichung zur Berechnung im C-Markt
	private final double				cMarketVariance;											// TODO

	// Faktor zum Wachstum vom C-Markt
	// F�r X bediente Kunden kommt einer hinzug
	private final double				cMarketIncreaseFactor;										// TODO

	// Faktor zur Abnahme vom C-Markt
	// F�r X nicht bediente Kunden geht einer
	private final double				cMarketDecreaseFactor;										// TODO

	// --------------------------------------- Preisberechnung
	// --------------------------------------------------------------
	// Durchschnittlicher Preis der gekauften Artikel in der letzten Runde im
	// A-Markt
	private int							aMarketAvgPriceLastRound;									// TODO

	// Durchschnittliche Qualit�t der gekauften Artikel in der letzten Runde im
	// A-Markt
	private int							aMarketAvgQualityLastRound;								// TODO

	// Durchschnittlicher Preis der gekauften Artikel in der letzten Runde im
	// C-Markt
	private int							cMarketAvgPriceLastRound;									// TODO

	// Durchschnittliche Qualit�t der gekauften Artikel in der letzten Runde im
	// C-Markt
	private int							cMarketAvgQualityLastRound;								// TODO

	// Mittelpunkt zwischen den Durchschnittsqualit�ten der letzten Runde
	private int							marketMiddleQualityLastRound;								// TODO

	// Mittelpunkt zwischen den Durchschnittspreisen der letzten Runde (um 50
	// der L�cke angehoben)
	private int							marketMiddlePriceLastRound;								// TODO

	// --------------------------------------- Verbindung zu
	// Abteilungen------------------------------------------------------
	// Liste aller Verkaufsabteilung
	private ArrayList<Distribution>		listOfDistributions		= new ArrayList<Distribution>();	// TODO

	// Liste aller Angebote, die f�r diese Runde gemacht worden sind
	private ArrayList<Offer>			listOfAllOffers			= null;

	// ------------------------------------------ Market Shares
	private HashMap<Company, Long>	listOfSales				= new HashMap<Company, Long>();	// TODO

	// --------------------------------------- Logging zur �berpr�fung
	// --------------------------------------------------------------
	// HashMap zum Speichern der Nachgefragten Qualit�ten
	private HashMap<Integer, Integer>	logRequestedQualities	= new HashMap<Integer, Integer>();	// TODO

	// HashMap zum Speichern der Akzeptierten Preise
	private HashMap<Integer, String>	logAcceptedPrices		= new HashMap<Integer, String>();	// TODO

	/**
	 * Liefert die Instanz auf den Markt zur�ck. (Singleton)
	 * 
	 * @return CusomterMarket: Marktinstanz
	 */
	// TODO
	public static CustomerMarket getMarket() {

		// Pruefe, ob der Markt bereits erstellt worden ist und geben es zur�ck
		// oder erstelle es neu
		return market = ((market == null) ? new CustomerMarket() : market);
	}

	/**
	 * Private Konstruktor zur Umsetzung von Singleton
	 */
	// TODO
	private CustomerMarket() {
		// Initialisierungsvariablen setzen
		this.aMarketPeak = Constant.CustomerMarket.aMarketPeak;
		this.aMarketQuantity = Constant.CustomerMarket.aMarketQuantity;
		this.aMarketVariance = Constant.CustomerMarket.aMarketVariance;
		this.aMarketIncreaseFactor = Constant.CustomerMarket.aMarketIncreaseFactor;
		this.aMarketDecreaseFactor = Constant.CustomerMarket.aMarketDecreaseFactor;
		this.cMarketPeak = Constant.CustomerMarket.cMarketPeak;
		this.cMarketQuantity = Constant.CustomerMarket.cMarketQuantity;
		this.cMarketVariance = Constant.CustomerMarket.cMarketVariance;
		this.cMarketIncreaseFactor = Constant.CustomerMarket.cMarketIncreaseFactor;
		this.cMarketDecreaseFactor = Constant.CustomerMarket.cMarketDecreaseFactor;
		this.aMarketAvgPriceLastRound = Constant.CustomerMarket.aMarketAvgPriceLastRound;
		this.aMarketAvgQualityLastRound = Constant.CustomerMarket.aMarketAvgQualityLastRound;
		this.cMarketAvgPriceLastRound = Constant.CustomerMarket.cMarketAvgPriceLastRound;
		this.cMarketAvgQualityLastRound = Constant.CustomerMarket.cMarketAvgQualityLastRound;
		this.marketMiddleQualityLastRound = (aMarketAvgQualityLastRound + cMarketAvgQualityLastRound) / 2;
		this.marketMiddlePriceLastRound = (aMarketAvgPriceLastRound + cMarketAvgPriceLastRound) / 2 + (int) 0.5
				* (aMarketAvgPriceLastRound - cMarketAvgPriceLastRound) / 2;

	}

	// TODO
	/**
	 * Registriert eine Verkaufsabteilung beim Markt
	 * 
	 * @param d
	 *            die zuregistrierende Verkaufsabteilung
	 * @return true: Wenn die Abteilung hinzugef�gt wurde
	 *         false: Wenn die Abteilung bereits in der Liste existert
	 */
	public boolean addDistribution(Distribution d) {

		if (d == null) {
			throw new NullPointerException("Distribution darf nicht null sein!");
		}

		// Pruefe, ob die Abteilung bereits in der List steht
		// -1: Abteilung ist neu
		if (listOfDistributions.indexOf(d) == -1) { // TODO: indexOf geht auf
													// equals (muss ggf. noch
													// implementiert werden)

			// Abteilung muss hinzugef�gt werden
			listOfDistributions.add(d);

			return true;

		} else {
			// Abteilung exisitert beretis
			return false;
		}
	}
	public boolean removeDistribution(Distribution d){
		
		return listOfDistributions.remove(d);
		
		
	}
	

	/**
	 * Simuliert den Markt. Kunden werden in den M�rkten durchsimuliert und
	 * nehmen Angebote war. Au�erdem werden alle statistischen Daten gesammelt
	 * und die MarktParameter wie die Peaks etc. f�r die n�chste Runde berechnet
	 */
	public void handleAllOffers() {

		// Alle Angebote aus den Abteilungen in die List laden
		listOfAllOffers = getAllOffers();

		// Die Liste nach Preis/Qualit�t sortieren
		Collections.sort(listOfAllOffers); // TODO

		// Anzahl der bedienten Kunden
		int servedCustomersAMarket = 0;

		// Anzahl der nicht bedienten Kunden
		int notServedCustomersAMarket = 0;

		// Liste mit Arrays von gekaufter Qualit�t und Preis A-Markt
		ArrayList<int[]> boughtOffersAMarket = new ArrayList<int[]>();

		// Anzahl der bedienten Kunden
		int servedCustomersCMarket = 0;

		// Anzahl der nicht bedienten Kunden
		int notServedCustomersCMarket = 0;

		// Liste mit Arrays von gekaufter Qualit�t und Preis C-Markt
		ArrayList<int[]> boughtOffersCMarket = new ArrayList<int[]>();

		// Kunden im A-Markt simulieren
		for (int i = 0; i < aMarketQuantity; i++) {
			// Nachgefragte Qualit�t f�r diesen Kunden nach der Normalverteilung
			// berechnen
			int requestedQuality = getGaussianNumber(50, 100, aMarketPeak, aMarketVariance);

			// Nachgefragte Qualit�t zu Loggingzwecken speichern
			logRequestedQualities.put(requestedQuality, ((logRequestedQualities.get(logRequestedQualities) == null) ? 0
					: logRequestedQualities.get(logRequestedQualities) + 1));

			// Referenz auf das gekaufte Angebot
			Offer boughtOffer = simulateCustomer(listOfAllOffers, requestedQuality);

			if (boughtOffer != null) {
				// Ein treffendes Angebot wurde ausgew�hlt
				servedCustomersAMarket++;

				// Infos zu Preis und Qualit�t
				int[] infoArray = { boughtOffer.getStorageElement().getProduct().getQuality(), boughtOffer.getPrice() };
				boughtOffersAMarket.add(infoArray);

				// CompanyString des Anbieters
				Company c = boughtOffer.getDistribution().getCompany();

				// Umsatz kummulieren f�r die Company
				// Eintrag hinzuf�gen, wenn der noch nicht existiert
				listOfSales.put(c, ((listOfSales.get(c) == null) ? boughtOffer.getPrice() : listOfSales.get(c)
						+ boughtOffer.getPrice()));
			} else {
				// Es wurde kein Angebot gefunden
				notServedCustomersAMarket++;
			}

		}

		// Kunden im C-Markt simulieren
		for (int i = 0; i < cMarketQuantity; i++) {
			// Nachgefragte Qualit�t f�r diesen Kunden nach der Normalverteilung
			// berechnen
			int requestedQuality = getGaussianNumber(0, 50, cMarketPeak, cMarketVariance);

			// Nachgefragte Qualit�t zu Loggingzwecken speichern
			logRequestedQualities.put(requestedQuality, ((logRequestedQualities.get(logRequestedQualities) == null) ? 0
					: logRequestedQualities.get(logRequestedQualities) + 1));

			// Referenz auf das gekaufte Angebot
			Offer boughtOffer = simulateCustomer(listOfAllOffers, requestedQuality);

			if (boughtOffer != null) {
				// Ein treffendes Angebot wurde ausgew�hlt
				servedCustomersCMarket++;

				// Infos zu Preis und Qualit�t
				int[] infoArray = { boughtOffer.getStorageElement().getProduct().getQuality(), boughtOffer.getPrice() };
				boughtOffersCMarket.add(infoArray);

				// CompanyString des Anbieters
				Company c = boughtOffer.getDistribution().getCompany();

				// Umsatz kummulieren f�r die Company
				// Eintrag hinzuf�gen, wenn der noch nicht existiert
				listOfSales.put(c, ((listOfSales.get(c) == null) ? boughtOffer.getPrice() : listOfSales.get(c)
						+ boughtOffer.getPrice()));

			} else {
				// Es wurde kein Angebot gefunden
				notServedCustomersCMarket++;
			}
		}

		// Markt-Parameter f�r die n�chste Runde berechnen
		calculateNewMarketParams(servedCustomersAMarket, notServedCustomersAMarket, boughtOffersAMarket,
				servedCustomersCMarket, notServedCustomersCMarket, boughtOffersCMarket);

	}

	/**
	 * Berechnet die Durchschnittswerte dieser Runde f�r die n�chste Runde aus.
	 * Also Durchschnittspreise und Qualit�ten. Au�erdem wir die Gr��e des neuen
	 * Marktes berechnet. Anschlie�end folgt die Peak-Verschiebung im A und C
	 * Markt
	 * 
	 * @param servedCustomersAMarket
	 *            Anzahl der bedienten Kunden im A-Markt
	 * @param notServedCustomersAMarket
	 *            Anzahl der nicht bedienten Kunden im A-Markt
	 * @param servedCustomersCMarket
	 *            Anzahl der bedienten Kunden im C-Markt
	 * @param boughtOffersAMarket
	 *            Liste der angenommen angebote im A-Markt
	 * @param notServedCustomersCMarket
	 *            Anzahl der nicht bedienten Kunden im C-Markt
	 * @param boughtOffersCMarket
	 *            iste der angenommen angebote im C-Markt
	 */
	// TODO
	private void calculateNewMarketParams(int servedCustomersAMarket, int notServedCustomersAMarket,
			ArrayList<int[]> boughtOffersAMarket, int servedCustomersCMarket, int notServedCustomersCMarket,
			ArrayList<int[]> boughtOffersCMarket) {

		// Berechnung des Wachstums des A-Marktes f�r die n�chste Runde
		aMarketQuantity += servedCustomersAMarket / aMarketIncreaseFactor - notServedCustomersAMarket
				/ aMarketDecreaseFactor;

		aMarketQuantity = (aMarketQuantity < Constant.CustomerMarket.aMarketQuantity) ? Constant.CustomerMarket.aMarketQuantity : aMarketQuantity;
		
		
		// Berechnung des Wachstums des C-Marktes f�r die n�chste Runde
		cMarketQuantity += servedCustomersCMarket / cMarketIncreaseFactor - notServedCustomersCMarket
				/ cMarketDecreaseFactor;
		
		cMarketQuantity = (cMarketQuantity < Constant.CustomerMarket.cMarketQuantity) ? Constant.CustomerMarket.cMarketQuantity : cMarketQuantity;

		// Berechnung des Durchschnittspreises der gekauften Artikel im A-Markt
		int sumPriceA = 0;
		int sumQualityA = 0;
		for (int[] info : boughtOffersAMarket) {			
			sumQualityA += info[0];
			sumPriceA += info[1];
		}

		// Durchschnittspreis berechnen, wenn Angebote gekauft wurden
		if (boughtOffersAMarket.size() > 0) {
			aMarketAvgPriceLastRound = sumPriceA / boughtOffersAMarket.size();

			// Durchschnittsqualit�t berechnen, wenn Angebote gekauft wurden
			aMarketAvgQualityLastRound = sumQualityA / boughtOffersAMarket.size();
		}

		// Berechnung des Durchschnittspreises der gekauften Artikel im C-Markt
		int sumPriceC = 0;
		int sumQualityC = 0;
		for (int[] info : boughtOffersCMarket) {
			// Qualitaet aus dem A-Markt werden ignoriert
			if(info[0] > 49)
				continue;
			sumQualityC += info[0];
			sumPriceC += info[1];
		}

		if (boughtOffersCMarket.size() > 0) {
			// Durchschnittspreis berechnen, wenn Angebote gekauft wurden
			cMarketAvgPriceLastRound = sumPriceC / boughtOffersCMarket.size();

			// Durchschnittsqualit�t berechnen, wenn Angebote gekauft wurden
			cMarketAvgQualityLastRound = sumQualityC / boughtOffersCMarket.size();
		}

		// Den Preismittelpunkt neu berechnen
		marketMiddlePriceLastRound = aMarketAvgPriceLastRound + cMarketAvgPriceLastRound
				+ (int) (0.5 * aMarketAvgPriceLastRound - cMarketAvgPriceLastRound);

		// Den Qualit�tsmittelpunkt neu berechnen
		marketMiddleQualityLastRound = (aMarketAvgQualityLastRound + cMarketAvgQualityLastRound) / 2;

		// Peak f�r den A-Markt neu berechnen (20 % in die Richtung des
		// Durchschnittsangebots verschieben

		// ArrayList mit Qualit�t - Anzahl Paaren
		ArrayList<int[]> offersAMarket = new ArrayList<int[]>();

		// ArrayList mit Qualit�t - Anzahl Paaren
		ArrayList<int[]> offersCMarket = new ArrayList<int[]>();

		// Angebote in die Listen einsortieren
		for (Offer o : listOfAllOffers) {
			if (o.getStorageElement().getProduct().getQuality() > 5.0) {
				// Angebot f�r den A Markt
				int[] offerInfo = { o.getStorageElement().getProduct().getQuality(), o.getQuantityToSell() };
				offersAMarket.add(offerInfo);
			} else {
				// Angebot f�r den C Markt
				int[] offerInfo = { o.getStorageElement().getProduct().getQuality(), o.getQuantityToSell() };
				offersCMarket.add(offerInfo);
			}
		}

		// Summe der Angeboten Qualit�ten im A-Markt
		sumQualityA = 0;

		// Anzahl der Angebote im A-Markt
		int sumCountA = 0;

		for (int[] info : offersAMarket) {
			sumQualityA += info[0] * info[1];
			sumCountA += info[1];
		}
		// Peak nur verschieben, wenn es Verk�ufe gab
		if (sumCountA > 0) {
			// Durchschnittswert der aktuellen Angebote
			int newPeakAMarket = sumQualityA / sumCountA;

			// Peak im A-Markt um 50% in die Richtung der Angebote verschieben
			aMarketPeak += (int) (newPeakAMarket - aMarketPeak) * 0.5;
			
			// Minimal 60
			aMarketPeak = (aMarketPeak < 60) ? 60 : aMarketPeak;

		}
		// Summe der Angeboten Qualit�ten im C-Markt
		sumQualityC = 0;

		// Anzahl der Angebote im C-Markt
		int sumCountC = 0;

		for (int[] info : offersCMarket) {
			sumQualityC += info[0] * info[1];
			sumCountC += info[1];
		}

		// Peak nur verschieben, wenn es Verk�ufe gab
		if (sumCountC > 0) {

			// Durchschnittswert der aktuellen Angebote
			int newPeakCMarket = sumQualityC / sumCountC;

			// Peak im C-Markt um 50% in die Richtung der Angebote verschieben
			cMarketPeak += (int) (newPeakCMarket - cMarketPeak) * 0.5;

			// Maximal 40
			cMarketPeak = (cMarketPeak > 40) ? 40 : cMarketPeak;
		}
	}

	/**
	 * Diese Methode simuliert einen Kunden, der abh�ngig von der Qualit�t sich
	 * den Preis heraussucht und ein Angebot ausw�hlt. Dieses wird dann im Lager
	 * noch ausgebucht.
	 * 
	 * @param listOfAllOffers
	 *            Alle verf�gbaren Angebote auf dem Markt, sortiert nach
	 *            Preis/Leistung
	 * @param requestedQuality
	 *            Die Qualit�t, die der Kunde nachfragen soll
	 * @return Referenz auf das Angebot, dass er am Ende ausgew�hlt hat
	 */
	// TODO
	private Offer simulateCustomer(ArrayList<Offer> listOfAllOffers, int requestedQuality) {

		// Akzeptierten Preis aus der Qualit�t berechnen
		int acceptedPrice = calculateAcceptedPrice(requestedQuality);

		// Preisleistungsverh�ltnis der aktuellen Liste
		int costBenefitRatio = 0;

		ArrayList<Offer> possibleOffers = null;

		// Die Liste der Angebote durchsuchen und das erste ausw�hlen, dass die
		// Anforderungen in Preis und Menge erf�llt
		for (Offer o : listOfAllOffers) {

			// Preis des Angebots auslesen
			// Double um nicht in Integer zu rechnen!
			double offerPrice = o.getPrice();

			// Qualit�t des Angebots auslesen
			// Double um nicht in Integer zu rechnen!
			double offerQuality = o.getStorageElement().getProduct().getQuality();

			// Pruefe ob noch das Angebot noch verf�gbar ist
			if (o.getQuantityToSell() <= o.getQuantitySold()) {
				// Angebot nicht mehr verf�gbar
				continue;
			}

			int offerCostBenefitRatio = (int) Math.round(offerPrice / offerQuality * 100);
			// Pruefe ob der Preis zu hoch ist
			if (offerPrice > acceptedPrice) {
				// Preis ist zu hoch, n�chstes Element aussuchen;
				continue;
			}

			if (offerQuality < requestedQuality) {
				// Qualit�t zu niedrig, n�chstes Element ausw�hlen
				continue;
			}

			// Pruefe ob noch kein Angebot gefunden worden ist, oder die
			// aktuellen schlechter sind
			if (possibleOffers == null || costBenefitRatio > offerCostBenefitRatio) {
				possibleOffers = new ArrayList<Offer>();
				possibleOffers.add(o);
				continue;
			}

			// Eventuell ist das aktuell gelesene Angebot genauso gut
			if (costBenefitRatio == offerCostBenefitRatio) {
				possibleOffers.add(o);
				continue;
			}

			// An diesem Punkt sind alle Angebote, die folgen vom
			// Preis/Leistungsverh�ltnis schlechter, da die Liste sortiert sein
			// muss. Das bedeutet, die Suche kann an dieser Stelle abgebrochen
			// werden und die Auswertung der Angebote beginnen.
			break;
		}

		if (possibleOffers == null) {
			// Kein passendes Angebot wurde gefunden
			return null;
		} else {
			// Eine Angebotsnummer ausw�hlen (durch faire Zufallszahlen mit
			// abschneiden)
			int chosenOfferNum = (int) Math.floor(Math.random() * (possibleOffers.size()));

			// Das ausgew�hlte Angebot
			Offer chosenOffer = possibleOffers.get(chosenOfferNum);

			// Im Lager um eins veringern
			if (!chosenOffer.getDistribution().getCompany().getStorage().unstore(chosenOffer.getStorageElement().getProduct(), 1)) {
				// TODO: Aus irgendeinem Grund ist das Offer im Lager nicht
				// gedeckt. In diesem Fall ist der Kunde entt�uscht, dass nicht
				// geliefert wird und sucht auch kein anderes Angbote mehr.
				// Sollte eigentlich nicht passieren:
				return null;
			}

			// Verkaufte Anzahl um eins erh�hen
			chosenOffer.setQuantitySold(chosenOffer.getQuantitySold() + 1);
			
			// Verkaufspreis auf das Konto buchen
			chosenOffer.getDistribution().getCompany().getBankAccount().increaseBalance(chosenOffer.getPrice());

			return chosenOffer;
		}
	}

	/**
	 * Berechnet eine Normalverteilte Integerzahl
	 * 
	 * @param min
	 *            Niedrigster akzeptierter Wert
	 * @param max
	 *            H�chster Akzeptierter Wert
	 * @param average
	 *            Mittelwert der Normalverteilung
	 * @param variance
	 *            Standardabweichung der Normalverteilung
	 * @return Normalverteilte Integerzahl
	 */
	// TODO
	private int getGaussianNumber(int min, int max, int average, double variance) {
		int number = min - 1;

		// Random object
		Random r = new Random();

		while (number < min || number > max) {
			number = (int) Math.round((average + (r.nextGaussian() * variance)));
		}
		return number;
	}

	/**
	 * Holt alle Angebote von allen registrierten Abteilungen und gibt sie als
	 * gemeinsame Liste zur�ck
	 * 
	 * @return
	 */
	// TODO
	private ArrayList<Offer> getAllOffers() {
		ArrayList<Offer> list = new ArrayList<Offer>();

		// Alle Abteilungen ansprechen
		for (Distribution d : listOfDistributions) {
			// Aus jeder Abteilung die Angebotsliste holen
			for (Offer o : d.getListOfLatestOffers()) {
				// Angebote der Gesamtliste hinzuf�gen
				list.add(o);
			}
		}

		return list;
	}

	/**
	 * Bestimmt aus einer nachgefragten Qualit�t einen Preis, den der Kunde
	 * akzeptiert
	 * 
	 * @param requestedQuality
	 *            : Qualit�t, die der Kunde nachfragt
	 * @return Preis, den der Kunde akzeptiert (maximal)
	 */
	// TODO
	private int calculateAcceptedPrice(int requestedQuality) {
		int price;

		// Pruefe in welchen Bereich der Preisermittlung die Qualit�t f�llt; Um
		// die richtige Steigung zu berechnen
		if (requestedQuality < marketMiddleQualityLastRound) {
			// Preis ohne statitische Varianz berechnen bis zur Mitte
			price = marketMiddlePriceLastRound + (requestedQuality - marketMiddleQualityLastRound)
					* (marketMiddlePriceLastRound - cMarketAvgPriceLastRound)
					/ (marketMiddleQualityLastRound - cMarketAvgQualityLastRound);
		} else {
			// Preis ohne statitische Varianz berechnen ab der Mitte
			price = marketMiddlePriceLastRound + (requestedQuality - marketMiddleQualityLastRound)
					* (aMarketAvgPriceLastRound - marketMiddlePriceLastRound)
					/ (aMarketAvgQualityLastRound - marketMiddleQualityLastRound);
		}

		// Den Preis um +/- 20 Prozent (normalverteilt) ver�ndern
		price = (int) price * 1 + (getGaussianNumber(-20, 20, 0, 10) / 100);

		// Preis zur Qualit�t loggen
		logAcceptedPrices.put(requestedQuality, (String) (((logAcceptedPrices.get(requestedQuality) == null) ? price
				+ "" : logAcceptedPrices.get(requestedQuality) + ";" + price)));

		return price;
	}

	/**
	 * Liefert den Qualit�ts-Peak im A-Markt zur�ck
	 * 
	 * @return A-Markt Peak
	 */
	// TODO
	public int getAMarketPeak() {
		return aMarketPeak;
	}

	/**
	 * Liefert den Qualit�ts-Peak im c-Markt zur�ck
	 * 
	 * @return c-Markt Peak
	 */
	// TODO
	public int getCMarketPeak() {
		return cMarketPeak;
	}

	/**
	 * Liefert eine Liste aller Marktanteile zur�ck
	 * 
	 * @return Liste der Marktanteile
	 */
	public ArrayList<TMarketShare> getMarketShares() {

		// Leere Liste mit Marktanteilen erstellen
		ArrayList<TMarketShare> listOfMarketShares = new ArrayList<TMarketShare>();

		// Array mit allen Company-Referenzen erstellen
		Company[] companyList = listOfSales.keySet().toArray(new Company[0]);

		long sumSales = 0;
		for (Company c : companyList) {
			// Sales der Firma zur Summe kummulieren
			sumSales += listOfSales.get(c);
		}

		
		// F�r jede Firma nun ein MarketShare Object erstellen
		for (Company c : companyList) {
			// Marktanteil der Firma berechnen
			// Zur Liste hinzuf�gen
			long salesCompany = listOfSales.get(c);
			
			listOfMarketShares.add(new TMarketShare(c, (int) (salesCompany * 10000.0 / sumSales), sumSales ));
		}

		// Liste zur�ckgeben
		return listOfMarketShares;
	}
}
