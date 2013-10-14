package Server;


/**
 * 
 * @author D059270 Resorce erbt von Prodct und kann nur ein Wafer oder ein
 *         Geh�use sein.
 */
public class Resource extends Product {
	
	/**
	 * Erstellt eine Resource (also Wafer oder Geh�use). 
	 * @param quality Qualit�t der Resource
	 * @param name Name (Typ): Wafer || GEh�use
	 * @param costs Kosten der Resource pro St�ck in Cent
	 * @throws Exception wird geworfen, wenn ein Parameter ung�ltig ist.
	 */
	public Resource(int quality, String name, int costs) throws Exception {		
		super(quality, name, costs);
		if(!checkNameIsValid(name)) {
			throw new IllegalArgumentException("Name der Resource ist ung�ltig. Muss Wafer oder Geh�use sein!");
		}
		
	}

	/**
	 * Pr�ft ob der Name den validen Produktnamen (Wafer oder Geh�se)
	 * entspricht.
	 * 
	 * @param name
	 * @return
	 */
	private static Boolean checkNameIsValid(String name) {
		if (name.equals("Wafer") || name.equals("Geh�use")) {
			return true;
		}
		
		return false;
	}

}
