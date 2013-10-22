package distributiontest;

import org.junit.Before;
import org.junit.Test;

import server.Company;
import server.Distribution;
import server.Location;

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
