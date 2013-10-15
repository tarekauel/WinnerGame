package location;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Location;

public class LocationTest {
	@BeforeClass
	public static void prepare() throws Exception {
		Location.initLocations();
	}

	@Test
	public void getLocationValid() throws Exception {

		assertEquals(true, Location.getLocationByCountry("USA") != null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getLocationNull() throws Exception {

		assertEquals(true, Location.getLocationByCountry(null) == null);
	}

	@Test
	public void getLocationInvalidName() throws Exception {

		assertEquals(true, Location.getLocationByCountry("usas") == null);
	}

}
