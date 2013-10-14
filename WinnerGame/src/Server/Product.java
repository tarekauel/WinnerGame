package Server;

import Constant.Constant;

/**
 * 
 * @author D059270 Die Klasse Product stellt die Superklasse der Produkttypen
 *         Ressorce und FinishedGood dar. Name, Kosten und Qalität werden von
 *         Product geführt.
 */
public abstract class Product {

	private String name; // Wafer/Gehäuse/Panel
	private int costs;
	private int quality;

	/**
	 * Erstellt ein neues Produkt, wenn die Qualität (Interval ]0;100] und die
	 * Kosten (>0) valide sind. Ansonsten wird eine Exception geworfen.
	 * 
	 * @param quality
	 * @param name
	 * @param costs
	 * @throws
	 */
	public Product(int quality, String name, int costs) throws Exception {
		if (checkCostsAreValid(costs)) {
			if (checkQualityIsValid(quality)) {
				this.quality = quality;
				this.name = name;
				this.costs = costs;
				return;
			}
			throw new IllegalArgumentException("Quality not valid!");
		}
		throw new IllegalArgumentException("Costs not valid!");
	}

	public String getName() {
		return name;
	}

	public int getCosts() {
		return costs;
	}

	public int getQuality() {
		return quality;

	}

	public int getStorageCostsPerRound() throws Exception {

		switch (name) {
		case "Wafer":
			return Constant.Product.STORAGECOST_WAFER;
		case "Gehäuse":
			return Constant.Product.STORAGECOST_CASE;
		case "Panel":
			return Constant.Product.STORAGECOST_PANEL;
		default:
			throw new Exception("Name des Products fehlerhaft");

		}
	}

	/**
	 * Wird einmal in jeder Runde aufgerfen, um die Kosten des Produktes um
	 * seine Lagerkosten der Runde zu erhöhen.
	 * 
	 * @throws Exception
	 */
	public void calculateNewCosts() throws Exception {
		switch (name) {
		case "Wafer":
			costs += Constant.Product.STORAGECOST_WAFER;
			break;
		case "Gehäuse":
			costs += Constant.Product.STORAGECOST_CASE;
			break;
		case "Panel":
			costs += Constant.Product.STORAGECOST_PANEL;
			break;
		default:
			throw new Exception("Name des Products fehlerhaft");

		}
	}

	/**
	 * Setzt die Kosten des Produktes, falls diese valide sind.
	 * 
	 * @param costs
	 * @return
	 */
	public Boolean setCosts(int costs) { // Brachen wir diese Methode???
		if (checkCostsAreValid(costs)) {
			this.costs = costs;
			return true;
		}
		return false; // nicht gesetzt

	}

	/**
	 * Prüft ob die Qualität zwischen 0 und einschließlich 100 liegt.
	 * 
	 * @param quality
	 * @return
	 */
	private static Boolean checkQualityIsValid(int quality) {
		if (quality > 0 && quality <= 100) {
			return true;
		}
		return false;
	}

	/**
	 * Prüft ob die Kosten >= 0 sind
	 * 
	 * @param costs
	 * @return
	 */
	private static Boolean checkCostsAreValid(int costs) {
		if (costs >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * Prüft ob Produkte gleich sind. Zm Vergleich werden die Qualität und der
	 * Name herangezogen.
	 * 
	 * @param product
	 * @return
	 */
	public Boolean equals(Product product) {
		if (product == null) {
			return false;
		}
		if (product.name.equals(this.name) && product.quality == this.quality) {
			return true;
		}
		return false;
	}
}
