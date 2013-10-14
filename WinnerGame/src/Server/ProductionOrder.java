/**
 * 
 */
package Server;

import Constant.Constant;

/**
 * Klasse f�r den Produktionsauftrag
 * 
 * @author Lars
 * 
 */
public class ProductionOrder {
	// Anzahl der insgesamt erzeugten Produktionen
	private static int counter = 1;
	// ID des auftrags (Spieler unabh�ngige ID!)
	private int id = 0;
	// Speichert die verwendeten Ressourcen
	private Resource wafer = null;
	private Resource cases = null;

	// Speichert hinterher das erzugte Panel:
	private FinishedGood panel = null;
	// Herzustellende Menge laut Auftrag
	private int quantityToProduce = 0;
	// tats�chlich hergestellte Menge:
	private int quantityProduced = 0;
	//Menge des Ausschuss kann nicht abgeleitet werden
	private int waste = 0;

	/**
	 * Erzeugt neue Production order
	 * 
	 * @param wafer
	 *            Resource f�r den Wafer
	 * @param cases
	 *            Resource f�r das case
	 * @param quantity
	 */

	public ProductionOrder(Resource wafer, Resource cases, int quantity) {
		this.id = counter;
		if(!checkResource(wafer)){
			throw new IllegalArgumentException("Wafer ist null.");
		}
		if(!checkResource(cases)){
			throw new IllegalArgumentException("Gehaeuse ist null.");
		}
		if(!checkQuantity(quantity)){
			throw new IllegalArgumentException("Quantity ist nicht gr��er 0");
		}
		this.wafer = wafer;
		this.cases = cases;
		this.quantityToProduce = quantity;
		counter++;
	}

	private boolean checkQuantity(int quantity){
		return quantity > 0;
	}
	
	private boolean checkResource(Resource wafer){
		return wafer!=null;
	}
	/**
	 * abgeleitetes attribut
	 * 
	 * @return int Ausschuss
	 */
	public int getWaste() {
		return this.waste;

	}
	/**
	 * erhoeht den ausschuss um 1
	 */
	public void increaseWaste(){
		waste++;
	}

	/**
	 * 
	 * @return gibt den verwendeten Wafer wieder
	 */
	public Resource getWafer() {
		return this.wafer;
	}

	/**
	 * 
	 * @return gibt das verwendete Geh�use zur�ck
	 */
	public Resource getCase() {
		return this.cases;
	}

	/**
	 * 
	 * @return gibt das Fertige Erzeugnis zur�ck
	 */
	public FinishedGood getPanel() {
		return this.panel;

	}

	/**
	 * Gibt die Produzierte Menge an
	 * 
	 * @return int produzierte Menge
	 */
	public int getProduced() {
		return quantityProduced;
	}

	/**
	 * Gibt die in auftraggegebene Menge an
	 * 
	 * @return int Anzahl aus dem Konstruktor
	 */
	public int getRequested() {

		return quantityToProduce;

	}

	/**
	 * Returns the global ID
	 * 
	 * @return global ID
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Wird jede Runde aufgerufen und fertigt den Produktionsauftrag
	 * @param advantage
	 *            Der Zuschlagssatz aus Motivation, Land und Forschung
	 * 
	 */
	public void produce(int advantageInt, Machinery m)  throws Exception{
		quantityProduced++;


		// Pr�fe ob bereits produziert wurde:
		if (panel != null) {
			// panel bereits gesetzt, also muss das nicht mehr berechnet werden.
			return;
		}

		// Es wird in doubles gerechnet:
		double advantage = advantageInt;
		double additionalFactor = advantage / 100.0;
		// durchschnittsqualit�t der Produkte mit Gewichtung:
		double midQuality = (wafer.getQuality()
				* Constant.Production.IMPACT_WAFER + cases.getQuality()
				* Constant.Production.IMPACT_CASE) / 100.0;
		// neue Qualit�t (nicht mehr als double)
		int newQuality = (int) (midQuality * additionalFactor) ;

		// Pr�fe ob die neue Qualit�t durch den Zuschlag zu sehr ver�ndert wurde
		newQuality = (newQuality - midQuality > Constant.Production.MAX_QUALITY_ADDITION) ? (int) (midQuality + Constant.Production.MAX_QUALITY_ADDITION)
				: newQuality;

		// Qualitaet auf 100 cappen 
		newQuality = (newQuality > 100) ? 100 : newQuality;
		//Qualitaet auf 1 flooren
		newQuality = (newQuality < 1) ? 1 : newQuality;
		
		// Berechne herstellkosten (ohne Ber�cksichtigung vom Ausschuss):
		int costs = wafer.getCosts() * Constant.Production.WAFERS_PER_PANEL
				+ cases.getCosts() + m.getPieceCosts();

		// neues Panel erzeugen
		panel = FinishedGood.create(newQuality, costs);
	}

	public void storeProduction(Storage s) throws Exception {
		// Kosten pro St�ck neu berechnen (Ausschuss ber�cksichtigen)
		this.panel = FinishedGood
				.create(panel.getQuality(),
						(int) (((double) panel.getCosts() * (quantityProduced + waste)
								+ Constant.Production.WORKING_HOURS_PER_PANEL + Constant.Production.WORKING_HOURS_PER_PANEL
								* (quantityProduced + waste)
								* s.getCompany().getHumanResources()
										.getWagesPerHour().getAmount()) / quantityProduced));
		s.store(this.panel, quantityProduced);
	}

}
