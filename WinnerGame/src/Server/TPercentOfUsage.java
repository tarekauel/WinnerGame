/**
 * 
 */
package Server;



/**
 * Klasse f�r die Auslastung einer Maschine
 * 
 * @author Lars
 * 
 */
public class TPercentOfUsage {

	private int percentOfUsage;
	private int round;

	/**
	 * Datentyp f�r die Auslastung einer Maschine
	 * 
	 * @param percentOfUsage
	 *            Prozentzahl der Auslastung
	 * @param round
	 *            Runde der Aufzeichnung
	 * @throws Exception
	 *             falls die Eingaben ung�ltig sind
	 */
	public TPercentOfUsage(int percentOfUsage, int round) throws Exception {
	
		if (!checkRound(round)) {
			throw new IllegalArgumentException("Runde ung�ltig");
		}
		if (!checkPercentOfUsage(percentOfUsage)) {
			throw new IllegalArgumentException("Prozentzahl ung�ltig");
		}
		this.percentOfUsage = percentOfUsage;
		this.round = round;
	}

	/**
	 * 
	 * @return Auslastung der Maschine
	 */
	public int getPercentOfUsage() {
		
		return percentOfUsage;
	}

	/**
	 * 
	 * @return Runde der Aufzeichnung
	 */

	public int getRound() {
		
		return round;
	}

	/**
	 * 
	 * @param round
	 *            Zu pr�fende Runde
	 * @return true, falls die Runde gr��er 0 ist.
	 */
	private boolean checkRound(int round) {
		return (round >= 0);
	}

	/**
	 * 
	 * @param p
	 *            PercentOfUsage (angegebener Prozentsatz)
	 * @return gibt an, ob die Zahl gr��er als 0 ist
	 */
	private boolean checkPercentOfUsage(int p) {
		return (p >= 0);
	}
}
