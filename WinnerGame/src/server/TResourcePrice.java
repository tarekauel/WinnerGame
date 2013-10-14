package Server;



/**
 * Typklasse f�r einen Resourcespreis
 * 
 * @author Tarek
 * 
 */
public class TResourcePrice implements Comparable<TResourcePrice> {

	// Qualit�t der Resource
	private int quality;

	// Preis der Resource in der obigen Qualit�t
	private int price;

	/**
	 * Erstellt eine TResourcePrice
	 * 
	 * @param quality
	 *            Qualit�t, die zu dem Preis geh�rt im Interval ]0;100]
	 * @param price
	 *            Preis, der der Qualit�t zugeordnet ist ( > 0 )
	 */
	public TResourcePrice(int quality, int price) {
		
		if (!checkQuality(quality)) {
			throw new IllegalArgumentException(
					"Die angegebene Qualit�t ist ung�ltig '" + quality + "'");
		}

		if (!checkPrice(price)) {
			throw new IllegalArgumentException(
					"Der angegebene Preis ist ung�ltig '" + price + "'");
		}

		this.quality = quality;

		this.price = price;
		
		
	}

	/**
	 * �berpr�ft, ob die Qualit�t im Interval ]0;100] liegt
	 * 
	 * @param quality
	 *            zu pr�fende Qualit�t
	 * @return Ergebnis der Pr�fung
	 */
	private boolean checkQuality(int quality) {
		return (quality > 0 && quality <= 100);
	}

	/**
	 * �berpr�ft, ob ein Preis g�ltig ist: > 0
	 * 
	 * @param price
	 *            zu pr�fende Preis
	 * @return Ergebnis der Pr�fung
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
