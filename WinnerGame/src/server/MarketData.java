package Server;

import java.util.ArrayList;

/**
 * Diese Klasse enthält alle Statistischen Marktdaten
 * @author Tarek
 *
 */
public class MarketData {
	
	// Singleton-Referenz
	private static MarketData data = null;
	
	// Referenz auf den Beschaffungsmarkt
	private SupplierMarket supplierMarket;
	
	// Referenz auf den Absatzmarkt
	private CustomerMarket customerMarket;
	
	// Liste mit Durchschnittslöhen
	private ArrayList<TWage> wages = null;
	
	// Runde, in der die Löhne gelten;
	private int round = 0;
	
	// Liste der Personalabteilungen
	private ArrayList<HumanResources> listOfHr = new ArrayList<HumanResources>();
	
	
	
	/**
	 * Liefert die Instanz auf den Markt zurück. (Singleton)
	 * @return Referenz auf MarketData
	 */
	public static MarketData getMarketData() {
		data = ((data == null) ? new MarketData() : data);
		
		// Gibt das MarketData Objekt zurück und erstellt es gegebenenfalls.
		return data  ;
	}
	
	/**
	 * Private Konstruktor zur Umsetzung des Singleton-Musters
	 */
	private MarketData() {
		
		supplierMarket = SupplierMarket.getMarket();
		customerMarket = CustomerMarket.getMarket();
	}
	
	/**
	 * Liefert den Peak im A-Makrt der nächsten Runde zurück
	 * @return Peak
	 */
	public int requestedQualitAMarketNextRound() {
		return customerMarket.getAMarketPeak();
	}
	
	/**
	 * Liefert den Peak im C-Makrt der nächsten Runde zurück
	 * @return Peak
	 */
	public int requestedQualitCMarketNextRound() {
		return customerMarket.getCMarketPeak();
	}
	
	/**
	 * Fuegt eine Personalabteilun zu den Makrtdaten hinzu, um den Durchschnittslohn zu berechnen
	 * @param hr Personalabteilung, die hinzugefügt werden soll
	 */
	public void addHR( HumanResources hr ) {
		if( hr == null)
			throw new IllegalArgumentException("HR-Referenz darf nicht null sein!");
		this.listOfHr.add(hr);
	}
	
	public boolean removeHR( HumanResources hr ) {
		if( hr == null)
			throw new IllegalArgumentException("HR-Referenz darf nicht null sein!");
		return this.listOfHr.remove(hr);
	}
	
	/**
	 * Liefert den Durchschnittslohn aller Abteilungen auf Niveau 100 umgerechnet zurück
	 * @return Durchschnittslohn auf Niveau 100
	 * @throws Exception
	 */
	public TWage getAvereageWage() throws Exception {
		// Summe aller WageAmounts (auf das Niveau 100 gerechnet)
		long sumWageAmounts = 0;
		
		// Anzahl der Abteilungen, die in der Summe kummuliert worden sind
		int numOfDepts = 0;
		
		// Liste aller HRs durchgehen
		for(HumanResources hr : listOfHr) {
			sumWageAmounts += (long) hr.getWagesPerHour().getAmount() / ( hr.getWagesPerHour().getWageLevel() / 10000.0 );
			++numOfDepts;			
		}
		
		// Den Durchschnittslohn der HRs auf Niveau 100 umgerechnet zurueckgeben
		return new TWage((int) (sumWageAmounts / numOfDepts), GameEngine.getGameEngine().getRound(), 10000);
	}
	
	/**
	 * Liefert den durchschnittlichen Betrag der Benefitinvestitonen zurueck
	 * @return Durchschnittsliche Benefitinvestionen auf Niveau 100
	 */
	public long getAverageBenefit() {
		
		// kummulierte Summe der Benefits aller Spieler
		long sumBenefits = 0;
		
		for(HumanResources hr : listOfHr ) {
			sumBenefits += hr.getSumBenefits();
		}
		
		return (long) sumBenefits / listOfHr.size();
	}
	
	
	

}
