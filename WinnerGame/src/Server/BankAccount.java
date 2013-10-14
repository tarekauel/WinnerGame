package Server;

import Constant.Constant;

public class BankAccount implements IRoundSensitive {

	private long bankBalance;
	private Company c;

	/**
	 * erstellt ein neues Bankkonto
	 * 
	 * @param c
	 *            Das Unternehmen, dem das Konto zugeordnet ist
	 * @exception tritt
	 *                auf, wenn Company null
	 */
	public BankAccount(Company c) throws Exception {
		if (!checkCompany(c)) {
			throw new IllegalArgumentException("Company invalid");
		}

		// CheckAmount wirft Exception falls negative Zahl
		checkAmount(Constant.BankAccount.START_CAPITAL);
		this.c = c;
		this.bankBalance = Constant.BankAccount.START_CAPITAL;
	}

	/**
	 * Prüft die Validität des Unternehmens
	 * 
	 * @param c
	 *            Company = Unternehmen, welches geprüft wird
	 * @return true, falls die Company != null
	 */
	private boolean checkCompany(Company c) {
		return c != null;
	}

	/**
	 * 
	 * @return gibt den momentanen Kontostand aus
	 */
	public long getBankBalance() {

		return bankBalance;
	}

	/**
	 * setzt einen neuen Kontostand
	 * 
	 * @param bankBalance
	 *            neuer Kontostand
	 */
	private void setBankBalance(long bankBalance) {

		this.bankBalance = bankBalance;
	}

	/**
	 * Bucht einen Geldbetrag auf ein Konto
	 * 
	 * @param amount
	 *            der zugebucht werden soll
	 * @exception wirft
	 *                exception falls amount <= 0
	 */
	public void increaseBalance(long amount) {

		checkAmount(amount);
		long newBankBalance = getBankBalance() + amount;
		setBankBalance(newBankBalance);

	}

	/**
	 * Bucht den Betrag vom Konto des Spielers nach vorheriger Überprüfung ab
	 * 
	 * @param amount
	 *            Betrag der abgebucht werden soll ( > 0 )
	 * @return true: Betrag wurde abgebucht false: Betrag konnte nicht abgebucht
	 *         werden
	 * @throws Exception 
	 * @exception falls
	 *                amount negativ
	 */
	public boolean decreaseBalance(long amount) throws Exception {
		checkAmount(amount);

		// Kann ich das noch bezahlen? bankBalance kann auch negativ sein
		if (amount <= (bankBalance + Constant.BankAccount.MAX_CREDIT)) {
			long newBankBalance = getBankBalance() - amount;
			setBankBalance(newBankBalance);
			return true;
		}
		// nicht mehr genug geld, also verloren
		GameEngine.getGameEngine().addCompanyLost(c);
		return false;
	}

	@Override
	public String toString() {
		return "Guthaben:" + this.bankBalance;
	}

	/**
	 * prüft einen Geldbetrag auf validität (groesser 0)
	 * 
	 * @param amount
	 *            Geldbetrag der Geprüft werden soll
	 * @throws IllegalArgumentException
	 */
	private void checkAmount(long amount) throws IllegalArgumentException {
		if (amount < 0) { // TODO: geaendert, da zum Beispiel keine Lagerkosten anfallen und dann mit 0 abgebucht wird
			throw new IllegalArgumentException("Amount darf nicht < 0 sein");
		}
	}

	@Override
	public void prepareForNewRound(int round) throws Exception {
		// Berechne die Zinsen falls noetig.
		if (bankBalance < 0) {
			bankBalance += bankBalance * Constant.BankAccount.RATES;
		}
		// Pruefe ob wir jetzt endgültig pleite sind
		if (bankBalance < Constant.BankAccount.MAX_CREDIT) {
			GameEngine.getGameEngine().addCompanyLost(c);
		}

	}
}
