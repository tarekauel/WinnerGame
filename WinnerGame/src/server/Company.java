package Server;

import java.util.ArrayList;

/**
 * Created by: User: Lars Trey Date: 28.09.13 Time: 17:22
 */

public class Company {

	private String name;

	private Location location;
	private BankAccount bankAccount;

	private Purchase purchase;
	private Production production;
	private Storage storage;
	private Distribution distribution;
	private HumanResources humanResources;
	private MarketResearch marketResearch;
	
	private ArrayList<TPresentValue> presentValues = new ArrayList<TPresentValue>();

	/**
	 * Legt ein neues Unternehmen an
	 * 
	 * @param l
	 *            Location, Unternehmensstandort
	 * @throws Exception
	 *             falls falsche eingabe werte
	 * @exception IllegalArgumentException
	 */
	/*
	 * public Company(Location l) throws Exception {
	 * 
	 * if (!checkLocation(l)) { throw new
	 * IllegalArgumentException("Ungültiger Standort"); }
	 * 
	 * // erzeuge Bankkonto mit 1 Mio Kapital bankAccount = new
	 * BankAccount(this); // setze Location
	 * 
	 * this.location = l; // 'Kaufe die Location'
	 * bankAccount.decreaseBalance(l.getPurchasePrice());
	 * 
	 * // Erzuege alle Abteilungen this.purchase = new Purchase(this);
	 * this.production = new Production(this); this.storage = new Storage(this);
	 * this.distribution = new Distribution(this); this.humanResources = new
	 * HumanResources(this); this.marketResearch = new MarketResearch(this);
	 * 
	 * // Anmelden an der Gamengine GameEngine.getGameEngine().addCompany(this);
	 * 
	 * }
	 */

	public Company(Location l, String name) throws Exception {

		if (!checkLocation(l)) {
			throw new IllegalArgumentException("Ungültiger Standort");
		}

		
		 if (!checkName(name)){
		 throw new IllegalArgumentException("Name darf nicht leer sein");
		 }
		this.name = name;
		// erzeuge Bankkonto mit 1 Mio Kapital
		bankAccount = new BankAccount(this);
		// setze Location

		this.location = l;
		// 'Kaufe die Location'
		bankAccount.decreaseBalance(l.getPurchasePrice());

		// Erzuege alle Abteilungen
		this.purchase = new Purchase(this);
		this.production = new Production(this);
		this.storage = new Storage(this);
		this.distribution = new Distribution(this);
		this.humanResources = new HumanResources(this);
		this.marketResearch = new MarketResearch(this);

		// Anmelden an der Gamengine
		GameEngine.getGameEngine().addCompany(this);

	}

	/**
	 * ueberprueft eine location
	 * 
	 * @param l
	 *            Location welche geprueft wird
	 * @return true, falls der Standort gefunden wurde
	 */
	private boolean checkLocation(Location l) {
		if (l != null) {
			return Location.getLocationByCountry(l.getCountry()) != null;
		}
		return false;
	}

	/**
	 * Prueft den Namen , darf nicht null oder "" sein
	 * 
	 * @param name
	 *            der zu pruefen ist
	 * @return true, falls name valide
	 */
	private boolean checkName(String name) {
		return !(name == null || name == "");
	}

	/**
	 * initialisiert eine neue Runde
	 * 
	 * @param round
	 *            Runde auf die sich vorbereitet wird
	 */
	public void initRound(int round) {

	}

	/**
	 * 
	 * @return gibt den Standort des Unternehmens zurueck
	 */
	public Location getLocation() {

		return this.location;
	}

	/**
	 * 
	 * @return liefert das Bank Konto
	 */
	public BankAccount getBankAccount() {

		return this.bankAccount;
	}

	/**
	 * 
	 * @return liefert den Einkauf
	 */
	public Purchase getPurchase() {

		return this.purchase;
	}

	/**
	 * 
	 * @return liefert die Produktion
	 */
	public Production getProduction() {

		return this.production;
	}

	/**
	 * 
	 * @return liefert das Lager
	 */
	public Storage getStorage() {

		return this.storage;
	}

	/**
	 * 
	 * @return Liefert den Verkauf
	 */
	public Distribution getDistribution() {

		return this.distribution;
	}

	/**
	 * 
	 * @return Liefert das Personal Management
	 */
	public HumanResources getHumanResources() {
		return this.humanResources;
	}

	/**
	 * 
	 * @return Liefert die Abteilung Marktforschung
	 */
	public MarketResearch getMarketResearch() {

		return this.marketResearch;
	}
	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	public TPresentValue getPresentValue() throws Exception{
		if(presentValues.get(presentValues.size()-1).round==GameEngine.getGameEngine().getRound()){
			return presentValues.get(presentValues.size()-1);
		}
		//Einrechnen des Barvermögens
		long presentValue = this.getBankAccount().getBankBalance();
		
		//Berechnen des prozentualen Wertes der PresentValue
		int percentLocation = 100-GameEngine.getGameEngine().getRound();
		percentLocation = (percentLocation > 0) ? percentLocation:1;
		//Einrechnen des Grundstücks
		presentValue += (long) (percentLocation * this.getLocation().getPurchasePrice()/100.0);
		TPresentValue tpresentValue = new TPresentValue(presentValue, GameEngine.getGameEngine().getRound());
		presentValues.add(tpresentValue);
		return tpresentValue;
	}

	/**
	 * 
	 * @return liefert namen der Company/Spielers zurueck
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "Unternehmen in:" + this.location;

	}
}
