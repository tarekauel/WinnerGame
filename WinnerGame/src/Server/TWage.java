package Server;

/**
 * Created by: User: Lars Trey Date: 28.09.13 Time: 19:04
 */

public class TWage {

	private final int amount;
	private final int wageLevel;
	private final int round;

	public TWage(int amount, int round, int wageLevel) throws Exception {

		if (!checkAmount(amount)) {
			throw new IllegalArgumentException("Ungültiger Betrag");
		}

		if (!checkRoundValid(round)) {
			// Runden check failed
			throw new IllegalArgumentException("Round invalid");
		}

		if (!checkWageLevel(wageLevel)) {
			throw new IllegalArgumentException("Wage Level ist ungueltig: "
					+ wageLevel);
		}

		this.amount = amount;
		this.round = round;
		this.wageLevel = wageLevel;
	}

	private boolean checkAmount(int amount) {
		return (amount > 0);
	}

	private boolean checkRoundValid(int round) {

		return (round > 0);
	}

	/**
	 * Prueft ob das WageLevel groesser als 0 ist
	 * 
	 * @param wageLevel
	 *            zu pruefende WageLevel
	 * @return Ergebnis der Pruefung
	 */
	private boolean checkWageLevel(int wageLevel) {
		return (wageLevel > 0);
	}

	public int getAmount() {

		return amount;
	}

	public int getRound() {

		return round;
	}

	public int getWageLevel() {

		return wageLevel;
	}

	@Override
	public boolean equals(Object tWage) {
		TWage in = (TWage) tWage;

		// Runden ueberpruefen (gleichheit!)
		if (in.getRound() != this.getRound()) {
			return false;
		}
		// Berechnen der gewichteten betraege
		int weightedIn = (int) (in.getAmount() / (in.getWageLevel() / 10000));
		int weightedThis = (int) (this.getAmount() / (this.getWageLevel() / 10000));

		// vergleich der betraege ist finale bedingung
		return (weightedIn == weightedThis);

	}
}
