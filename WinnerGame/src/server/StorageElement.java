package Server;


/**
 * Ein StorageElement ist ein Artikel/ eine Lagerposition im Storage. 
 * Jedes StorageElement besitzt ein Produkt, was ein Fertigprodukt oder ein Rohstoff sein kann.
 * Zur Erzeugung muss eine Menge angegeben werden, mit der der Artikel eingelagert wird.
 * 
 * @author felix
 *
 */

public class StorageElement {
	
	private int quantity;
	private Product product;
	/**
	 * Erzeugt ein neues StorageElement
	 * @param quantity Anzahl der Produkte die das Element enthaelt
	 * @param product Zu verstauendes
	 * @throws IllegalArgumentException
	 */
	public StorageElement(int quantity, Product product) throws Exception{
		
		if(product == null || !(checkQuantityHigherZero(quantity))){
			throw new IllegalArgumentException("product is null or quantity is lower zero. Class StorageElement Method Constructor");
		}
		this.product = product;
		this.quantity = quantity;
		
	}
	
	/**
	 * falls uebergebene Quantity<0 wird false zurueckgegeben, sonst true
	 * @param quantity
	 * @return true falls uebergabewert > 0
	 *         false sonst
	 */
	private boolean checkQuantityHigherZero(int quantity){
		if(quantity>0){
			return true;
		}
		else return false;
	} 
	
	/**
	 *ueberprueft ob geforderte Menge noch im Lager vorhanden ist, wenn ja dann true sonst false
	 * @param quantity
	 * @return
	 */
	private boolean checkEnoughInStorage(int quantity){
		if(this.quantity >= quantity) return true;
		else return false;
		
	}
	
	/**
	 * liefert die noch vorhandene Menge des Produkts zurueck
	 * @return
	 */
	
	public int getQuantity(){
	
		return quantity;
	}
	
	/**
	 * reduziert die Menge des Artikels im Lager um die angegebene Menge, ueberprueft vorher ob um diese Menge reduziert werden kann
	 * bei erfolgreichem Reduzieren ist die Rueckgabe true sonst false.
	 * @param quantity
	 * @return true: bei erfolgreichem Reduzieren
	 * 		   false sonst
	 */
	public boolean reduceQuantity(int quantity){
	
		boolean enoughInStore = checkEnoughInStorage(quantity);
		if(enoughInStore&&quantity>0){
			this.quantity = this.quantity - quantity;
			
			return true;
		}
	
		return false;
	}
	
	/**
	 * erhoeht die Menge/Quantity des Artikels im Lager um angegebene Quantity, ueberprueft zuvor ob Quantity groesser null
	 * wenn erfolgreich dann true sonst false
	 * @param quantity
	 * @return true: bei erfolg 
	 * 		   false sonst
	 */
	public boolean increaseQuantity(int quantity){
		//TODO: durchschnittspreis neu berechnen!
		
		if(quantity>0){
			this.quantity = this.quantity + quantity;
			
			return true;
		}
		
		return false;
	}
	/**
	 * liefert referenz auf das product zurueck
	 * @return
	 */
	public Product getProduct(){
		
		return product;
	}

	
}
