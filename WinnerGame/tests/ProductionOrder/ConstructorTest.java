package ProductionOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.ProductionOrder;
import Server.Resource;

public class ConstructorTest {
	Resource wafer;
	Resource cases;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void initializeTests() throws Exception {
		this.wafer = new Resource(50,"Wafer",500);
		this.cases = new Resource(50,"Gehäuse",500);
	}

	@Test
	public void valid() {
		new ProductionOrder(wafer, cases, 50);
	}

	@Test (expected = java.lang.IllegalArgumentException.class)
	public void invalidQuantity0() {
		new ProductionOrder(wafer, cases, 0);
	}
	
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void invalidQuantityNegative() {
		new ProductionOrder(wafer, cases, -550);
	}
	
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void invalidCaseNull() {
		new ProductionOrder(wafer, null, 50);
	}
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void invalidWaferNull() {
		new ProductionOrder(null, cases, 1000);
	}
	
	@After
	public void resetTests() {

	}

}
