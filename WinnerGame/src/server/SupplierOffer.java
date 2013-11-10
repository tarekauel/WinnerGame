package server;

import message.GameDataMessageFromClient.PurchaseFromClient.AcceptedSupplierOfferFromClient;



/**
 * 
 */

public class SupplierOffer {

	private int orderedQuantity;
	private final int round;
	private Resource resource;

	/**
	 * Erstellt ein SupllierOffer (Angebot vom Beschaffungsmarkt an den Spieler)
	 * 
	 * @param resource
	 *            Produkt, dass Angeboten wird
	 * @throws IllegalArgumentException
	 *             Resource darf nicht null sein
	 */
	public SupplierOffer(Resource resource) throws IllegalArgumentException {
		
		if (resource == null)
			throw new IllegalArgumentException("Resource darf nicht null sein!");
		this.resource = resource;
		this.round = GameEngine.getGameEngine().getRound();
		
	}

	public int getOrderedQuantity() {
		
		return orderedQuantity;
	}
	
	/**
	 * Liefert die Runde zurück, in der das Angebot erstellt wurde
	 * @return
	 */
	public int getRound() {
		return round;
	}

	/**
	 * Setzt die bestellte Menge des Spielers. Wird erst in der Runde nach der
	 * Erstellng des SupplierOffers gesetzt.
	 * 
	 * @param orderedQuantity
	 * @return
	 */
	public boolean setOrderedQuantity(int orderedQuantity) {
		
		if (checkOrderedQuantityIsValid(orderedQuantity)) {
			this.orderedQuantity = orderedQuantity;
			
			return true;
		}
		return false;
	}

	public Resource getResource() {
	
		return resource;
	}

	/**
	 * Prüft ob die Quantity positiv ist.
	 * 
	 * @param orderedQuantity
	 * @return
	 */
	private static boolean checkOrderedQuantityIsValid(int orderedQuantity) {
		if (orderedQuantity >= 0) {
		
			return true;

		}
		
		return false;
	}

	/**
	 * Vergleicht ob die Resource identisch sind.
	 * 
	 * @param supplierOffer
	 * @return
	 */
	@Override
	public boolean equals(Object object) {
		SupplierOffer supplierOffer = (SupplierOffer) object;
		if (this.resource.equals(supplierOffer.getResource())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Ueberprueft ob ein AcceptedSupplierOffer zu einem Server.SupplierOffer passt
	 * @param o Vergleichs Angebot vom Client
	 * @return true identisch, false ungleich
	 */
	public boolean equals(AcceptedSupplierOfferFromClient o) {
		return (this.resource.getName().equals(o.name) && this.resource.getQuality() == o.quality); 
	}

}
