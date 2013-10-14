package Server;

import java.util.ArrayList;

/**
 * 
 * @author D059270 Stellt eine Nachfrage nach einem Rohstoff in einer bestimmten
 *         Qualit�t dar. Angebote hierzu k�nnen �ber getSpplierOffers abgerufen
 *         werden.
 */
public class Request {

	private Resource resourceReqested;
	private ArrayList<SupplierOffer> supplierOffers = new ArrayList<SupplierOffer>();

	/**
	 * Konstruktor zum erstellen einer Anfrage an den Beschaffungsmarkt
	 * 
	 * @param resource die Resource, die nachgefagt werden soll
	 */
	public Request(Resource resource) {
		this.resourceReqested = resource;
		if( resource == null ) {
			throw new IllegalArgumentException( "Resource darf nicht null sein!");
		}
	}

	/**
	 * Liefert alle Angebote (max. 3) zur gestellten Anfrage zur�ck. Der Spieler
	 * kann diese annehmen.
	 * 
	 * @return
	 */
	public SupplierOffer[] getSupplierOffers() {
		return supplierOffers.toArray(new SupplierOffer[supplierOffers.size()]);
	}

	/**
	 * F�gt ein Angebot zur gestellten Anfrage hinzu. Es k�nnen max. 3 Angebote
	 * hinzgef�gt werden. Das Angebot mss vom gleichen Rohstofftyp sein.
	 * 
	 * @param supplierOffer
	 * @return
	 */
	public boolean addSupplierOffer(SupplierOffer supplierOffer) {
		if (supplierOffer == null) {
			throw new IllegalArgumentException("SupplierOffer darf nicht null sein!");
		}
		String nameOfSupplierProdct = supplierOffer.getResource().getName();
		String nameOfResourceProdct = resourceReqested.getName();
		if (supplierOffers.size() < 3
				&& nameOfResourceProdct.equals(nameOfSupplierProdct)) {
			supplierOffers.add(supplierOffer);
			return true;
		} else {
			throw new IllegalArgumentException("Einem Request k�nnen nur 3 SupplierOffer zugewiesen werden!");
		}
		
	}

	/**
	 * Liefert die angefrage Resource zur�ck.
	 * 
	 * @return
	 */
	public Resource getRequestedResource() {
		return resourceReqested;
	}

}
