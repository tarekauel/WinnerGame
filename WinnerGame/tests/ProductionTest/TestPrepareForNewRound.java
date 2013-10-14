package ProductionTest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.*;

public class TestPrepareForNewRound {
	
	Company c;
	Production p;
	Resource wafer;
	Resource cases;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
	}

	@Before
	public void initializeTests() throws Exception{
		c = new Company(Location.getLocationByCountry("USA"),"OTTO");
		p = c.getProduction();
		wafer = new Resource(80, "Wafer", 10000);
		cases = new Resource(50, "Gehäuse", 10000);
		p.createProductionOrder(wafer, cases, 100);
		p.createProductionOrder(wafer, cases, 101);
		p.createProductionOrder(wafer, cases, 102);
		
	}

	@Test
	public void prepareForNewRoundValid() {
		int sizeBefore = p.getListOfOpenProductionOrders().size();
		p.prepareForNewRound(2);
		int sizeAfter = p.getListOfOpenProductionOrders().size();
		assertEquals(true,sizeBefore==sizeAfter+3);
	}
	
	@Test
	public void prepareForNewRoundInvalid() {
		p.prepareForNewRound(2);
		int sizeAfter = p.getListOfOpenProductionOrders().size();
		assertEquals(false,sizeAfter!=0);
		
	}

	@After
	public void resetTests() {

	}

}
