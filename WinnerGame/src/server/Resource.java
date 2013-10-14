package Server;


/**
 * 
 * @author D059270 Resorce erbt von Prodct und kann nur ein Wafer oder ein
 *         Gehäuse sein.
 */
public class Resource extends Product {
	
	/**
	 * Erstellt eine Resource (also Wafer oder Gehäuse). 
	 * @param quality Qualität der Resource
	 * @param name Name (Typ): Wafer || GEhäuse
	 * @param costs Kosten der Resource pro Stück in Cent
	 * @throws Exception wird geworfen, wenn ein Parameter ungültig ist.
	 */
	public Resource(int quality, String name, int costs) throws Exception {		
		super(quality, name, costs);
		if(!checkNameIsValid(name)) {
			throw new IllegalArgumentException("Name der Resource ist ungültig. Muss Wafer oder Gehäuse sein!");
		}
		
	}

	/**
	 * Prüft ob der Name den validen Produktnamen (Wafer oder Gehäse)
	 * entspricht.
	 * 
	 * @param name
	 * @return
	 */
	private static Boolean checkNameIsValid(String name) {
		if (name.equals("Wafer") || name.equals("Gehäuse")) {
			return true;
		}
		
		return false;
	}

}
