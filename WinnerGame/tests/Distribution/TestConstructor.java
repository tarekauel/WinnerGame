package Distribution;

import org.junit.Before;
import org.junit.Test;

import Server.Company;
import Server.Distribution;
import Server.Location;

public class TestConstructor {
	Company c;

	@Before
	public void initTests() throws Exception {
		Location.initLocations();
		c = new Company(Location.getLocationByCountry("Deutschland"),"OTTO");

	}

	@Test
	public void createDistribution() throws Exception {
		new Distribution(c);
	}
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createInvalidCompanyNull() throws Exception {
		new Distribution(null);
	}
	
	
}
