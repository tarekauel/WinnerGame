package Server;


/**
 *  Die Klasse stellt ein fertig produziertes Produkt dar (Panel).
 * @author D059270
 *
 */
public class FinishedGood extends Product {
	/**
	 * Fachlicher Konstruktor: Liefert ein neues FinishedGood zurück, falls
	 * die Eingaben valide waren. Sonst null.
	 * 
	 * @param quality
	 * @param costs
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public static FinishedGood create(int quality, int costs) throws Exception {
		
		String name = "Panel";
			FinishedGood finishedGood = new FinishedGood(quality, name, costs);
			
			return finishedGood;
	
		
		

	}

	/**
	 * privater Konstruktor, der den Oberklassenteilkonstruktor von Product
	 * aufruft.
	 * 
	 * @param quality
	 * @param name
	 * @param costs
	 * @throws Exception
	 */
	private FinishedGood(int quality, String name, int costs) throws Exception {
		super(quality, name, costs);
		
	}

}
