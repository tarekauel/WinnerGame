package location;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.Test;

import server.Location;

public class LocationInit {

	@Test
	public void creationTest() {
		// test ob die Locations alle erstellt wurden

		try {

			Location.initLocations();

			BufferedReader r = new BufferedReader(new FileReader(
					"locations.dat"));
			int lineCtr = 0;
			while (r.readLine() != null) {
				lineCtr++;
			}
			r.close();
			assertEquals(lineCtr, Location.getListOfLocations().size());

		} catch (Exception e) {
			// Test failed:
			assertEquals(true, false);
		}

	}
}