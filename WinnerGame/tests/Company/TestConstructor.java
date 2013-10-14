package Company;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.Company;
import Server.Location;

public class TestConstructor {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
	}

	@Before
	public void initializeTests() {
	}

	@Test
	public void createCompany() throws Exception {
		new Company(Location.getLocationByCountry("Deutschland"),"OTTO");
	}

	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createInvalidByWrongCountry() throws Exception {
		new Company(Location.getLocationByCountry("Deutschlsd"),"OTTO");
	}
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createInvalidByNullCountryAndName() throws Exception {
		new Company(null,null);
	}
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createInvalidByWrongName() throws Exception {
		new Company(Location.getLocationByCountry("Deutschlsd"),"");
	}
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createInvalidByNullName() throws Exception {
		new Company(Location.getLocationByCountry("Deutschland"),null);
	}

}
