package Server;
//Interface fuer Rundensensitive Sachen (Company und BankAccount)
public interface IRoundSensitive {
	/**
	 * Bereitet auf neue Runde vor
	 * @param round aktuelle Spielrunde
	 */
	public void prepareForNewRound(int round)throws Exception;
}
