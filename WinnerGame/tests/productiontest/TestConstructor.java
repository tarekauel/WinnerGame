package productiontest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Company;
import server.Location;
import server.Production;

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
	public void createProductionValid() throws Exception {
		new Production(c);
	}
	
	@Test (expected = IllegalArgumentException.class )
	public void createProductionInvalidCompanyNull() throws Exception {
		new Production(null);
	}



}
