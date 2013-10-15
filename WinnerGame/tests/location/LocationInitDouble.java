package location;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.Test;

import server.Location;

public class LocationInitDouble {

	@Test(expected = IllegalArgumentException.class)
	public void creationTest() throws Exception {
		// test ob die Locations alle erstellt wurden

		

			Location.initLocations();

			Location.initLocations();

	}
}