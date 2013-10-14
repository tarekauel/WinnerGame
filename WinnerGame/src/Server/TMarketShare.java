package Server;



/**
 * Dieser Typ gibt den Marktanteil (in 100% ( d.h. ein Monopol sind 10000 !))
 * einer Firma an
 * 
 * @author Tarek
 * 
 */
// TODO: ist jetz ein Typ :D
public class TMarketShare {

	private Company c;
	private int marketShare;
	private long marketSize;

	/**
	 * Konstruktor für Marketshare
	 * 
	 * @param c
	 *            Die Firma für den dieser Marktanteil gilt
	 * @param marketShare
	 *            der Marktanteil in ganze Prozent
	 * @param marketSize
	 * 			 das GesamtMarktvolumen
	 */
	public TMarketShare(Company c, int marketShare, long marketSize) {
		if (!checkMarketSize(marketSize)){
			throw new IllegalArgumentException("Marktvolumen ist inkorrekt");
		}
		if (!checkMarketShare(marketShare)) {
			throw new IllegalArgumentException("Marktanteil ist ungültig: '"
					+ marketShare + "'");
		}
		if (c == null) {
			throw new IllegalArgumentException(
					"Company-Referenz darf nicht auf NULL zeigen!");
		}
		this.c = c;
		this.marketShare = marketShare;
		this.marketSize = marketSize;
	}

	private boolean checkMarketSize(long marketSize) {
		return (marketSize>=0);
	}

	
	/**
	 * 
	 * @return liefert die Firma des Marktanteils zurück
	 */
	public Company getCompany() {
		
		return c;
	}
	/**
	 * 
	 *  @return gibt das gesamtvolumen des Marktes zurueck
	 */
	public long getMarketSize(){
		return this.marketSize;
	}

	/**
	 * 
	 * @return liefert den Marktanteil der Firma in ganzen Prozent zurück
	 */
	public int getMarketShare() {
	
		return marketShare;
	}

	/**
	 * Überprüft einen Marktanteil auf gültig
	 * 
	 * @param marketShare
	 *            Marktanteil
	 * @return true: gültig false: ungültig
	 */
	private boolean checkMarketShare(int marketShare) {
		// Marktanteil mit dem Interval [0;10000]
		return (marketShare >= 0 && marketShare <= 10000);
	}
}
