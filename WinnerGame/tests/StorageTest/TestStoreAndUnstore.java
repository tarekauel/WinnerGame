package StorageTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.Company;
import Server.Location;
import Server.Resource;
import Server.Storage;
import Server.StorageElement;

public class TestStoreAndUnstore {
	Company c;
	Storage s;
	StorageElement se;
	Resource w;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Location.initLocations();
	}

	@Before
	public void initializeTests() throws Exception {

		w = new Resource(50, "Wafer", 5000);
		se = new StorageElement(500, w);

		c = new Company(Location.getLocationByCountry("USA"),"OTTO");
		s = new Storage(c);

	}

	@Test
	public void storeValid() throws Exception {
		s.store(w, 500);
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void storeProductNull() throws Exception{
		s.store(null,500);
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void storeProductNullQuantity0() throws Exception{
		s.store(null,0);
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void storeProductNullQuantityNegative() throws Exception{
		s.store(null, -10);
	}
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void storeQuantityNegative() throws Exception{
		s.store(w, -10);
	}
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void storeQuantity0() throws Exception{
		s.store(w, 0);
	}
	
	@Test
	public void unstore() throws Exception{
		s.store(w, 100);
		assertEquals(true,s.unstore(w, 50));
	}
	
	@Test
	public void unstoreToMuch() throws Exception{
		s.store(w, 100);
		assertEquals(false,s.unstore(w, 500));
	}
	@Test
	public void unstoreExact() throws Exception{
		s.store(w, 100);
		assertEquals(true,s.unstore(w, 100));
	}
	@Test
	public void unstoreNegative() throws Exception{
		s.store(w, 100);
		assertEquals(false,s.unstore(w, -50));
	}
	@Test
	public void unstoreProductNull() throws Exception{
		s.store(w, 100);
		assertEquals(false,s.unstore(null, 50));
	}
	@Test
	public void unstore0() throws Exception{
		s.store(w, 100);
		assertEquals(false,s.unstore(w, 0));
	}
	
}
