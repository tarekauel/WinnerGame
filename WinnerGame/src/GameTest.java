import java.util.ArrayList;

import server.Benefit;
import server.Company;
import server.CustomerMarket;
import server.Distribution;
import server.GameEngine;
import server.Location;
import server.Production;
import server.Purchase;
import server.Request;
import server.Resource;
import server.Storage;
import server.SupplierMarket;
import server.TMarketShare;

public class GameTest {

	public static void main(String[] args) throws Exception {
		
		GameEngine g = GameEngine.getGameEngine();
		
		Benefit.initBenefits();

		Location.initLocations();

		Company c = new Company(Location.getLocationByCountry("Deutschland"),"OTTO");

		Purchase p = c.getPurchase();
		Production pro = c.getProduction();
		Storage s = c.getStorage();
		Distribution d = c.getDistribution();

		p.createRequest(new Resource(20, "Wafer", 0));
		p.createRequest(new Resource(20, "Gehäuse", 0));

		Company c2 = new Company(Location.getLocationByCountry("Deutschland"),"ANNA");

		Purchase p2 = c2.getPurchase();
		Production pro2 = c2.getProduction();
		Storage s2 = c2.getStorage();
		Distribution d2 = c2.getDistribution();

		p2.createRequest(new Resource(26, "Wafer", 0));
		p2.createRequest(new Resource(26, "Gehäuse", 0));

		SupplierMarket.getMarket().handleRequest();

		ArrayList<Request> listReq = p.getListOfLatestRequest();
		p.acceptSupplierOffer(listReq.get(0).getSupplierOffers()[0], 54 * 101); // 1000
																				// Wafer
																				// kaufen
		p.acceptSupplierOffer(listReq.get(1).getSupplierOffers()[0], 101); // 1000
																			// Gehäuse
																			// kaufen

		
		ArrayList<Resource> store = s.getAllResources();
		pro.createProductionOrder(store.get(0), store.get(1), 100);

		pro.produce();

		d.createOffer(s.getAllFinishedGoods().get(0).getQuality(), s
				.getAllStorageElements().get(2).getQuantity(), 100);

		ArrayList<Request> listReq2 = p2.getListOfLatestRequest();
		p2.acceptSupplierOffer(listReq2.get(0).getSupplierOffers()[0], 54 * 100); // 1000
																					// Wafer
																					// kaufen
		p2.acceptSupplierOffer(listReq2.get(1).getSupplierOffers()[0], 100); // 1000
																				// Gehäuse
																				// kaufen
		//SupplierMarket.getMarket().recalculatePrices();
		ArrayList<Resource> store2 = s2.getAllResources();
		pro2.createProductionOrder(store2.get(0), store2.get(1), 100);

		pro2.produce();

		d2.createOffer(s2.getAllFinishedGoods().get(0).getQuality(), s2
				.getAllStorageElements().get(0).getQuantity(), 100);

		CustomerMarket.getMarket().handleAllOffers();

		ArrayList<TMarketShare> shares = CustomerMarket.getMarket()
				.getMarketShares();

		int i = 1;

	}
}
