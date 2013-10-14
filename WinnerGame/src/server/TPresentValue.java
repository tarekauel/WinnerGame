package Server;



/**
 * Typ f�r PresentValue
 * Speichert ein Key-Value Pair von PresentValue und Runde
 * @author Lars
 *
 */

public class TPresentValue {
	final int round;
	final long presentValue;
	/**
	 * Konstruktor speichert bereits alle Daten.
	 * Ein nachtr�gliches setzen ist nicht m�glich
	 * 
	 * @param round aktuelle Runde
	 * @param PresentValue aktueller Barwert
	 * @throws Exception 
	 */
	public TPresentValue( long presentValue,int round) throws Exception {
	
		if (checkPresentValueValid(presentValue) == false){
			//Motivaions check failed
			throw new IllegalArgumentException("Present value invalid");
		}
		if (checkRoundValid(round)==false){
			//Runden check failed
			throw new IllegalArgumentException("Round invalid");
		}
		this.round = round;
		this.presentValue = presentValue;
		
		
	}
	/**
	 * 
	 * @return gibt den Unternehmenswert an
	 */
	public long getPresentValue(){
		
		return this.presentValue;
	}
	/**
	 * 
	 * @return gibt die Runde der PresentValue an
	 */
	public int getRound(){
		
		return this.round;		
	}
	
	/**
	 * Pr�ft die Runde, ob sie positiv ist. 
	 * @param round gibt die Runde an
	 * @return true, falls die Runde plausibel, also gr��er als 0 ist.
	 */
	private boolean checkRoundValid(int round){
		if (round < 1){
			return false;
		}
		return true;
	}
	
	/**
	 * pr�ft ob die Present Value positiv ist
	 * @param presentValue gibt die tempor�re Motivation an
	 * @return true, falls die presentValue valide ist
	 */
	private boolean checkPresentValueValid(long presentValue){
		if(presentValue > 0){
			return true;
		}
		return false;
	}
}