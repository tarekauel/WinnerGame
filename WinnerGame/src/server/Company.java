package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import constant.Constant;

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
	private boolean hasLost=false;
	
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
	 * berechnet den Gesamtwert des Unternehmens indem vorhandene lagerbestaende zu Wiederbeschaffungs
	 * werten/Verkaufswerten angesetzt werden. darueberhinaus wird der Vorteil durch den Marktanteil
	 * mit eingerechnet sowie der Wert der Location die zu Beginn gekauft wurde
	 * @return TPresentValue mit Runde und dem Wert
	 * @throws Exception 
	 */
	public TPresentValue getPresentValue() throws Exception{
		if(presentValues.size() != 0 && presentValues.get(presentValues.size()-1).round==GameEngine.getGameEngine().getRound()){
			return presentValues.get(presentValues.size()-1);
		}
		
		//Einrechnen des Barvermögens
		long presentValue = this.getBankAccount().getBankBalance();
		
		//Einrechnen der Lagerbestaende
		//fertige Produkte
		HashMap<Integer,Long> priceList = CustomerMarket.getMarket().getPriceList();
		long panelValue = 0;
		for(FinishedGood finishedGood : this.storage.getAllFinishedGoods()){
			long price = priceList.get(finishedGood.getQuality());
			int quantity = storage.getFinishedGoodByQuality(finishedGood.getQuality()).getQuantity();
			panelValue += quantity*price;
		}
		presentValue+= panelValue;
		
		//Rohstoffe
		long resourcesWafer = 0;
		long resourcesCases = 0;
		TreeSet<TResourcePrice> priceListCases = SupplierMarket.getMarket().getCasePricelist();
		TreeSet<TResourcePrice> priceListWafer = SupplierMarket.getMarket().getWaferPricelist();
		java.util.Iterator<TResourcePrice> priceListCasesIterator = priceListCases.iterator();
		java.util.Iterator<TResourcePrice> priceListWaferIterator = priceListWafer.iterator();
		for(StorageElement storageElement : this.getStorage().getAllStorageElements()){
			if(storageElement.getProduct() instanceof Resource){
				int quantity = storageElement.getQuantity();
				Resource resource = (Resource) storageElement.getProduct();
				if(resource.getName().equals("Wafer")){
					while(priceListWaferIterator.hasNext()){
						TResourcePrice resourceprice = priceListWaferIterator.next();
						int quality = resourceprice.getQuality();
						int price = resourceprice.getPrice();
						if(quality==resource.getQuality()){
							resourcesWafer += quantity*price;
							break;
						}//if
					}//while
				}//if
				if(resource.getName().equals("Gehäuse")){
					while(priceListCasesIterator.hasNext()){
						TResourcePrice resourceprice = priceListCasesIterator.next();
						int quality = resourceprice.getQuality();
						int price = resourceprice.getPrice();
						if(quality==resource.getQuality()){
							resourcesCases += quantity*price;
							break;
						}//if
					}//while
				}//if
			}//if
		}
		presentValue += resourcesCases+resourcesWafer;
		
		//Einrechnen des Vorteils durch Marktanteil
		long marketShareAdvantage = 0;
		ArrayList<TMarketShare> arrayListMarektShare = CustomerMarket.getMarket().getMarketShares();
		for(TMarketShare marketShare : arrayListMarektShare){
			if(marketShare.getCompany()==this){
				marketShareAdvantage = (long) ((marketShare.getMarketShare()/10000.0) * marketShare.getMarketSize() * (Constant.PresentValue.PRESENTVALUE_ADVANTAGE_MARKETSHARE / 100.0));
			}
		}
		presentValue+=marketShareAdvantage;
		
		//Einrechenen der Maschinenausbaustufe
		long sumOfInvestion = 0;
		int level = this.getProduction().getMachine().getLevel();
		for(int i = 1; i<level; i++){
			sumOfInvestion+= Constant.Machinery.BUILD_COSTS[i];
		}
		presentValue += sumOfInvestion;
		
		//Einrechnen des Wertes des Grundstuecks
		long estateValue = 0;
		
		//Das Grundstuecks wird pro Runde um 2% abgeschrieben
		estateValue = (long) (this.getLocation().getPurchasePrice()*(1.0-Math.pow(2.0/100, (double) GameEngine.getGameEngine().getRound())));	
		presentValue+=estateValue;
		
		
		//Erzeugen des Typen TPresentValue
		TPresentValue tpresentValue = new TPresentValue(presentValue, GameEngine.getGameEngine().getRound());
		presentValues.add(tpresentValue);
		return tpresentValue;
	}
	
	public ArrayList<TPresentValue> getPresentValues(){
		return presentValues;
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

	public boolean hasLost() {
		return hasLost;
	}

	public void setHasLost(boolean hasLost) {
		this.hasLost = hasLost;
	}
}
