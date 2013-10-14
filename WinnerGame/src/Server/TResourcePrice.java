package Server;



/**
 * Typklasse für einen Resourcespreis
 * 
 * @author Tarek
 * 
 */
public class TResourcePrice implements Comparable<TResourcePrice> {

	// Qualität der Resource
	private int quality;

	// Preis der Resource in der obigen Qualität
	private int price;

	/**
	 * Erstellt eine TResourcePrice
	 * 
	 * @param quality
	 *            Qualität, die zu dem Preis gehört im Interval ]0;100]
	 * @param price
	 *            Preis, der der Qualität zugeordnet ist ( > 0 )
	 */
	public TResourcePrice(int quality, int price) {
		
		if (!checkQuality(quality)) {
			throw new IllegalArgumentException(
					"Die angegebene Qualität ist ungültig '" + quality + "'");
		}

		if (!checkPrice(price)) {
			throw new IllegalArgumentException(
					"Der angegebene Preis ist ungültig '" + price + "'");
		}

		this.quality = quality;

		this.price = price;
		
		
	}

	/**
	 * Überprüft, ob die Qualität im Interval ]0;100] liegt
	 * 
	 * @param quality
	 *            zu prüfende Qualität
	 * @return Ergebnis der Prüfung
	 */
	private boolean checkQuality(int quality) {
		return (quality > 0 && quality <= 100);
	}

	/**
	 * Überprüft, ob ein Preis gültig ist: > 0
	 * 
	 * @param price
	 *            zu prüfende Preis
	 * @return Ergebnis der Prüfung
	 */
	private boolean checkPrice(int price) {
		return (price > 0);
	}

	/**
	 * Getter
	 * 
	 * @return quality
	 */
	public int getQuality() {
		
		return quality;
	}

	/**
	 * Getter
	 * 
	 * @return price
	 */
	public int getPrice() {
		
		return price;
	}

	@Override
	public int compareTo(TResourcePrice o) {
		if( getQuality() == o.getQuality() )
			return 0;
		if ( getQuality() < o.getQuality()) 
			return -1;
		return 1;
	}

}
