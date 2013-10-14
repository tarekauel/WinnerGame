package StorageTest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.Company;
import Server.Location;
import Server.Storage;

public class TestConstructor {
Company c;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		Location.initLocations();
	}

	@Before
	public void initializeTests() throws Exception {
		
		
		c = new Company(Location.getLocationByCountry("USA"),"OTTO");
	}

	@Test
	public void StorageCreateValid() throws Exception{
		assertEquals(true,(new Storage(c)!=null));
	}
	
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void StorageCreateCompanyNull() throws Exception{
		new Storage(null);
	}
	@After
	public void resetTests() {
		c = null;
	}

}
