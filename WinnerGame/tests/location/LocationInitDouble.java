package location;

import static org.junit.Assert.*;

import org.junit.Test;

import server.Location;

public class LocationInitDouble {

	@Test
	public void creationTest() throws Exception {
		// test ob die Locations alle erstellt wurden

		

			Location.initLocations();
			int i = Location.getListOfLocations().size();
			Location.initLocations();
			assertEquals(i,Location.getListOfLocations().size());
	}
}