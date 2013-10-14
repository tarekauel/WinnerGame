package StorageElementTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.Resource;
import Server.StorageElement;

public class TestStoreAndUnstore {
	StorageElement se;
	StorageElement se2;
	StorageElement se3;
	Resource wafer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void initializeTests() throws Exception {
		wafer = new Resource(60, "Wafer", 300);
		se = new StorageElement(5, wafer);
		

	}

	@Test
	public void incQuantityValid() {
		assertEquals(true, se.increaseQuantity(5));
	}

	@Test
	public void incQuantity0() {
		assertEquals(false, se.increaseQuantity(0));
	}

	@Test
	public void incQuantityInvalid() {
		assertEquals(false, se.increaseQuantity(-5));
	}
	
	@Test
	public void redQuantityInvalid() {
		assertEquals(false, se.reduceQuantity(-5));
	}

	@Test
	public void redQuantityValid() {
		assertEquals(true, se.reduceQuantity(5));
	}
	@Test
	public void redQuantity0() {
		assertEquals(false, se.reduceQuantity(0));
	}
}
