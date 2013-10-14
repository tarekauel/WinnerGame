package productiontest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.BankAccount;
import server.Company;
import server.Location;
import server.Resource;
import constant.Constant;

public class TestCreateProductionOrder {
	
	Company c;
	Resource wafer;
	Resource cases;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
	}

	@Before
	public void initializeTests() throws Exception {
		c= new Company(Location.getLocationByCountry("USA"),"OTTO");
		 wafer = new Resource(80, "Wafer", 10000);
		 cases = new Resource(50, "Geh�use", 10000);
		
		
	}

	@Test
	public void createProductionOrderValid() throws Exception {
		assertEquals(true,c.getProduction().createProductionOrder(wafer, cases, 100));
	}
	
	@Test (expected = IllegalArgumentException.class )
	public void createProductionOrderInvalidWafer() throws Exception {
		c.getProduction().createProductionOrder(null, cases, 100);
	}
	
	@Test (expected = IllegalArgumentException.class )
	public void createProductionOrderInvalidCases() throws Exception {
		c.getProduction().createProductionOrder(wafer, null, 100);
	}
	
	@Test (expected = IllegalArgumentException.class )
	public void createProductionOrderQuantityEQZero() throws Exception {
		c.getProduction().createProductionOrder(wafer, cases, 0);
	}
	@Test (expected = IllegalArgumentException.class )
	public void createProductionOrderQuantityLowerZero() throws Exception {
		c.getProduction().createProductionOrder(wafer, cases, -1);
	}
	
	@Test
	public void createProductionOrderBancAccountToLow() throws Exception {
		BankAccount b = c.getBankAccount();
		b.decreaseBalance(b.getBankBalance());
		b.decreaseBalance(Constant.BankAccount.MAX_CREDIT-1);
		assertEquals(false,c.getProduction().createProductionOrder(wafer, cases, 100));
	}


}
