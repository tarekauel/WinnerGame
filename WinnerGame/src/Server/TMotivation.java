package Server;

import java.io.Serializable;



/**
 * Typ für die Motivation Speichert ein Key-Value Pair von Motivation und Runde
 * 
 * @author Lars
 * 
 */

public class TMotivation implements Serializable {
	final int round;
	final int motivation;

	/**
	 * Konstruktor speichert bereits alle Daten. Ein nachträgliches setzen ist
	 * nicht möglich
	 * 
	 * @param round
	 *            aktuelle Runde
	 * @param motivation
	 *            aktuelle Motivation
	 * @throws Exception
	 */
	public TMotivation(int motivation, int round) throws Exception {
		
		if (checkMotivationValid(motivation) == false) {
			// Motivaions check failed
			throw new IllegalArgumentException("Motivation invalid");
		}
		if (checkRoundValid(round) == false) {
			// Runden check failed
			throw new IllegalArgumentException("Round invalid");
		}
		this.round = round;
		this.motivation = motivation;
		

	}

	public int getMotivation() {
		
		return this.motivation;
	}

	public int getRound() {
		
		return this.round;
	}

	/**
	 * Prüft die Runde, ob sie positiv ist.
	 * 
	 * @param round
	 *            gibt die Runde an
	 * @return true, falls die Runde plausibel, also größer als 0 ist.
	 */
	private boolean checkRoundValid(int round) {
		if (round < 1) {
			return false;
		}
		return true;
	}

	/**
	 * prüft ob die Motivation >= 0
	 * 
	 * @param motivation
	 *            gibt die temporäre Motivation an
	 * @return true, falls die Motivation valide
	 */
	private boolean checkMotivationValid(int motivation) {
		if (motivation >= 0) {
			return true;
		}
		return false;
	}
}
