package aspecttest;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import server.Company;
import server.Location;
import server.Purchase;
import server.Resource;
import server.SupplierMarket;
import annotation.FakeSupplierMarketOfferQualities;

public class FakeMarketTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
	}
	
	@Test
	@FakeSupplierMarketOfferQualities( differences = { -10, 0, 7 } )
	public void test() throws Exception {
		Company c = new Company(Location.getLocationByCountry("Deutschland"),"OTTO");
		Purchase p = c.getPurchase();
		SupplierMarket m = SupplierMarket.getMarket();
		m.addPurchase(p);
		p.createRequest( new Resource(10, "Wafer", 0));
		m.handleRequest();
		assertEquals(1, p.getListOfRequest().get(0).getSupplierOffers()[0].getResource().getQuality());
		assertEquals(10, p.getListOfRequest().get(0).getSupplierOffers()[1].getResource().getQuality());
		assertEquals(17, p.getListOfRequest().get(0).getSupplierOffers()[2].getResource().getQuality());
		
	}

}
