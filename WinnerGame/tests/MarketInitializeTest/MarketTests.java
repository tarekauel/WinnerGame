package MarketInitializeTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Company;
import server.CustomerMarket;
import server.Location;
import server.SupplierMarket;

public class MarketTests {
	Company c1;
	Company c2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();

	}

	@Before
	public void initializeTests() throws Exception {
		// init Companys
		c1 = new Company(Location.getLocationByCountry("usa"), "Tester-1");
		c2 = new Company(Location.getLocationByCountry("usa"), "Tester-2");
		// init Märkte
		SupplierMarket.getMarket();
		CustomerMarket.getMarket();
	}

	@Test
	public void supplierMarketAdd() {
		SupplierMarket.getMarket().addPurchase(c1.getPurchase());
		SupplierMarket.getMarket().addPurchase(c2.getPurchase());
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void supplierMarketAddNull() {
		SupplierMarket.getMarket().addPurchase(null);
		SupplierMarket.getMarket().addPurchase(c2.getPurchase());
	}
	@Test
	public void supplierMarketRemove() {
		SupplierMarket.getMarket().addPurchase(c2.getPurchase());
		assertEquals(true,SupplierMarket.getMarket().removePurchase(c2.getPurchase()));
	}
	@Test(expected = java.lang.NullPointerException.class)
	public void supplierMarketRemoveNull() {
		SupplierMarket.getMarket().removePurchase(null);
		
	}
	@Test
	public void supplierMarketRemoveTwoTimes() {
		SupplierMarket.getMarket().addPurchase(c2.getPurchase());
		assertEquals(true,SupplierMarket.getMarket().removePurchase(c2.getPurchase()));
		assertEquals(false,SupplierMarket.getMarket().removePurchase(c2.getPurchase()));
	}
	
	
	
	@Test
	public void customerMarketAdd() {
		CustomerMarket.getMarket().addDistribution(c1.getDistribution());
		CustomerMarket.getMarket().addDistribution(c2.getDistribution());
	}
	
	@Test(expected = java.lang.NullPointerException.class)
	public void customerMarketAddNull() {
		CustomerMarket.getMarket().addDistribution(null);
		CustomerMarket.getMarket().addDistribution(c2.getDistribution());
	}
	@Test
	public void customerMarketRemove() {
		CustomerMarket.getMarket().addDistribution(c2.getDistribution());
		assertEquals(true,CustomerMarket.getMarket().removeDistribution(c2.getDistribution()));
	}
	@Test(expected = java.lang.NullPointerException.class)
	public void customerMarketRemoveNull() {
		CustomerMarket.getMarket().removeDistribution(null);
		
	}
	@Test
	public void customerMarketRemoveTwoTimes() {
		CustomerMarket.getMarket().addDistribution(c2.getDistribution());
		assertEquals(true,CustomerMarket.getMarket().removeDistribution(c2.getDistribution()));
		assertEquals(false,CustomerMarket.getMarket().removeDistribution(c2.getDistribution()));
	}
}
