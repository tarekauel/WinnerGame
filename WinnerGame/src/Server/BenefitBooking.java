package Server;

/**
 * Created by: User: Lars Trey Date: 28.09.13 Time: 19:07
 */

public class BenefitBooking {

	// gebuchter Benefit
	private final Benefit benefit;

	// Dauer an gebuchten Runden
	private final int duration;

	// Runde in der das Benefit beginnt
	private final int startInRound;

	/**
	 * erstellt eine neue Buchung für ein benefit
	 * 
	 * @param benefit
	 *            welches gebucht werden soll
	 * @param duration
	 *            Anzahl der Runden, die das Benefit wirken soll
	 * @exception IllegalArgumentException
	 *                bei falschen Inputparametern
	 */
	public BenefitBooking(Benefit benefit, int duration) throws Exception {
		if (!checkBenefit(benefit)) {
			throw new IllegalArgumentException("Benefit ungültig");
		}
		if (!checkDuration(duration)){
			throw new IllegalArgumentException("Duration ungültig");
		}
		
		this.benefit = benefit;
		this.duration = duration;
		this.startInRound = GameEngine.getGameEngine().getRound() + 1;
	}

	/**
	 * Prueft ob ein Benefit existiert über Suchfunktion der Klasse Benefit
	 * 
	 * @param b
	 *            Benefit welches geprueft wird
	 * @return true, falls das Benefit gefunden wurde
	 */
	private boolean checkBenefit(Benefit b) {
		return Benefit.getBenefitByName(b.getName()) != null;
	}

	/**
	 * ueberprueft eine Dauer auf Sinnhaftigkeit
	 * 
	 * @param d
	 *            Duration, welche geprueft wird
	 * @return true, falls d > 0
	 */
	private boolean checkDuration(int d) {
		return d > 0;
	}

	/**
	 * 
	 * @return Liefert die Gesamtkosten für das Benefit zurueck
	 */
	public int getCostsSum() {
		return duration * benefit.getCostsPerRound();
	}
/**
 * 
 * @return gibt das benefit dieser Buchung an
 */
	public Benefit getBenefit() {

		return benefit;
	}
/**
 * 
 * @return liefert die Dauer des gebuchten Benefits insgesamt
 */
	public int getDuration() {

		return duration;
	}
/**
 * 
 * @return gibt die StartRunde des Benefits an
 */
	public int getStartInRound() {

		return startInRound;
	}
/**
 * 
 * @return gibt die verbleibenden Wirkungsrunden des Benefits an
 */
	public int getRemainingRounds() {

		return (duration
				- (GameEngine.getGameEngine().getRound() - startInRound) < 0) ? 0
				: (duration - (GameEngine.getGameEngine().getRound() - startInRound));

	}

	@Override
	public String toString() {
		return this.benefit.toString() + " Restdauer: "
				+ this.getRemainingRounds() ;
	}

}
