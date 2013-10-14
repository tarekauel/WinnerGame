package Server;

import java.io.Serializable;



/**
 * Typ f�r die Motivation Speichert ein Key-Value Pair von Motivation und Runde
 * 
 * @author Lars
 * 
 */

public class TMotivation implements Serializable {
	final int round;
	final int motivation;

	/**
	 * Konstruktor speichert bereits alle Daten. Ein nachtr�gliches setzen ist
	 * nicht m�glich
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
	 * Pr�ft die Runde, ob sie positiv ist.
	 * 
	 * @param round
	 *            gibt die Runde an
	 * @return true, falls die Runde plausibel, also gr��er als 0 ist.
	 */
	private boolean checkRoundValid(int round) {
		if (round < 1) {
			return false;
		}
		return true;
	}

	/**
	 * pr�ft ob die Motivation >= 0
	 * 
	 * @param motivation
	 *            gibt die tempor�re Motivation an
	 * @return true, falls die Motivation valide
	 */
	private boolean checkMotivationValid(int motivation) {
		if (motivation >= 0) {
			return true;
		}
		return false;
	}
}
