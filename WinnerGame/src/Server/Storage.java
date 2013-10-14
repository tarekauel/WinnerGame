package Server;

import java.util.ArrayList;

import Constant.Constant;

/**
 * Das Storage/Lager verwaltet alle StorageElemente und lagert diese ein und
 * wieder aus. Es ist eine Abteilung des Unternehmens.
 * 
 * 
 * @author Felix
 * 
 */

public class Storage extends DepartmentRoundSensitive {

	private ArrayList<StorageElement> listOfStorageElements = new ArrayList<StorageElement>();

	/**
	 * Erzeugt ein Storage Objekt
	 * 
	 * @param c
	 * @throws Exception
	 *             falls Objekterzeugung fehlerhaft / Uebergabeparameter
	 *             fehlerhaft
	 */
	public Storage(Company c) throws Exception {
		super(c, "Lager", Constant.DepartmentFixcost.STORAGE);

	}

	/**
	 * lagert ein Product in das Storage ein. Falls das Produkt als
	 * StorageElement schon existiert wird die Quantity erhoeht sonst wird ein
	 * neues StorageElement erzeugt.
	 * 
	 * @param product
	 *            Produkt, das eingelagert werden soll
	 * @param quantity
	 *            Menge des einzulagernden Produkts
	 * @throws Exception
	 */

	public void store(Product product, int quantity) throws Exception {
		int size = listOfStorageElements.size();
		StorageElement storageElement = null;
		boolean found = false;
		for (int i = 0; i < size; i++) {
			storageElement = listOfStorageElements.get(i);
			if (storageElement.getProduct().equals(product)) {
				// falls Element gefunden wird Anzahl erhoeht und Kosten neu
				// berechnet
				Product prod = storageElement.getProduct();
				int oldCosts = prod.getCosts();
				int oldQuantity = storageElement.getQuantity();
				// Berechnung an sich erfolgt in double, danach wird direkt auf
				// int gecasted
				int newCosts = (int) ((double) ((oldCosts * oldQuantity + product
						.getCosts() * quantity) / (oldQuantity + quantity)));
				prod.setCosts(newCosts);
				storageElement.increaseQuantity(quantity);


				found = true;
				break;
			}
		}

		if (!found) {
			// Exceptions lassen Programm stoppen
			storageElement = new StorageElement(quantity, product);

			listOfStorageElements.add(storageElement);

		}
	}// store

	/**
	 * bucht die Kosten fuer die Lagerung der Produkte pro Runde vom Bankkonto
	 * ab
	 * 
	 * @return true bei Erfolg false sonst.
	 * @throws Exception
	 */

	public boolean debitStorageCost() throws Exception {

		int costs = this.getStorageCostsSum();
		return this.getCompany().getBankAccount().decreaseBalance(costs);

	}

	/**
	 * erhoeht die Kosten der Produkte im Lager, da sie pro Runde Lagerkosten
	 * verursachen.
	 * 
	 * @throws Exception
	 */
	public void updateStorageElements() throws Exception {
		StorageElement storageElement = null;
		Product product = null;
		int size = listOfStorageElements.size();
		for (int i = 0; i < size; i++) {
			storageElement = listOfStorageElements.get(i);
			product = storageElement.getProduct();
			product.calculateNewCosts();
		}

	}

	/**
	 * Berechnet die Summe der Kosten pro Runde, die durch die StorageElemente
	 * verursacht werden.
	 * 
	 * @return
	 * @throws Exception
	 */

	public int getStorageCostsSum() throws Exception {

		StorageElement storageElement = null;
		Product product = null;
		int sum = 0;
		int size = listOfStorageElements.size();
		for (int i = 0; i < size; i++) {
			storageElement = listOfStorageElements.get(i);
			product = storageElement.getProduct();
			sum = sum + storageElement.getQuantity()*product.getStorageCostsPerRound();
		}
		return sum;
	}// getStorageCostsSum

	/**
	 * lagert das uebergebene Product aus dem Storage mit angegebener Menge aus
	 * ueberprueft zuvor ob das angegebene Product im Storage in ausreichender
	 * Menge vorhanden ist.
	 * 
	 * @param product
	 *            welches product soll ausgelagert werden
	 * @param quantity
	 *            wie viel davon soll ausgelagert werden
	 * @return true bei Erfolg, false sonst
	 */

	public boolean unstore(Product product, int quantity) {
		// muss hier die angegebene Quantity//Product wieder geprueft werden??
		StorageElement storageElement = null;
		Product productTmp = null;
		int size = listOfStorageElements.size();
		boolean success = false;
		for (int i = 0; i < size; i++) {
			storageElement = listOfStorageElements.get(i);
			productTmp = storageElement.getProduct();
			if (productTmp == product) {
				success = storageElement.reduceQuantity(quantity);
				// falls das storageelement jetzt leer ist, lösche die Referenz
				if (storageElement.getQuantity() == 0) {
					listOfStorageElements.remove(storageElement);

				}
				// beenden der schleife
				break;
			}
		}// for sucht passendes StrEl anhand von Prod aendert dann die Anzahl
		return success; // success macht keine angabe ob
						// reduceQuantity()fehlschlug oder
						// kein StorageElement/product in der ArrayList gefunden
						// wurde
	}// unstore

	/**
	 * sucht ein Fertigprodukt anhand seiner eindeutigen Qualitaet
	 * 
	 * @param quality
	 *            gesuchte Qualitaet
	 * @return gibt Referenz auf das Objekt zurueck
	 */

	public StorageElement getFinishedGoodByQuality(int quality) {
		StorageElement storageElement = null;
		Product product = null;
		int size = listOfStorageElements.size();
		for (int i = 0; i < size; i++) {
			storageElement = listOfStorageElements.get(i);
			product = storageElement.getProduct();
			if (product.getQuality() == quality) {
				return storageElement;
			}// if
		}// for
		return null;
	}// getFinishedGoodByQuality

	/**
	 * sucht alle Fertigprodukte im Lager und gibt diese zurueck
	 * 
	 * @return
	 */
	public ArrayList<FinishedGood> getAllFinishedGoods() {
		ArrayList<FinishedGood> finishedGoods = new ArrayList<FinishedGood>();
		StorageElement storageElement = null;
		Product product = null;
		FinishedGood finishedGood = null;
		int size = listOfStorageElements.size();
		for (int i = 0; i < size; i++) {
			storageElement = listOfStorageElements.get(i);
			product = storageElement.getProduct();
			if (product instanceof FinishedGood) {
				finishedGood = (FinishedGood) product;
				finishedGoods.add(finishedGood);
			}
		}// for
		return finishedGoods;
	}// getAllFinishedGoods

	/**
	 * sucht alle im Lager vorhandenen Rohstoffe und gibt diese zurueck
	 * 
	 * @return Liste aller Rohstoffe
	 */
	public ArrayList<Resource> getAllResources() {
		ArrayList<Resource> resources = new ArrayList<Resource>();
		StorageElement storageElement = null;
		Product product = null;
		Resource resource = null;
		int size = listOfStorageElements.size();
		for (int i = 0; i < size; i++) {
			storageElement = listOfStorageElements.get(i);
			product = storageElement.getProduct();
			if (product instanceof Resource) {
				resource = (Resource) product;
				resources.add(resource);
			}
		}// for
		return resources;

	}// getAllResources

	/**
	 * Liefert eine Liste aller Storage-Elemente zurück
	 * 
	 * @return Liste aller Storage Elemente
	 */
	public ArrayList<StorageElement> getAllStorageElements() {
		return listOfStorageElements;
	}

	public void prepareForNewRound(int round) throws Exception {
		updateStorageElements();
	}
}
