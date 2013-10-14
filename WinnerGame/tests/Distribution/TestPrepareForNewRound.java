package Distribution;


import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.Company;
import Server.Distribution;
import Server.FinishedGood;
import Server.Location;
import Server.Storage;

public class TestPrepareForNewRound {
	Location l;
	Company c;
	Distribution d;
	Storage st;
	FinishedGood fg;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();

	}

	@Before
	public void initializeTests() throws Exception {
		l = Location.getLocationByCountry("USA"); 
		c = new Company(l,"OTTO");
		d = c.getDistribution();
		st = c.getStorage();
		fg = FinishedGood.create(80, 20000);
		st.store(fg,1500);
		d.createOffer(80,10,100);
		d.createOffer(80,10,101);
		d.createOffer(80,10,102);
		d.createOffer(80,10,103);
		
	}

	
	@Test 
	public void valid() {
		assertEquals(4,d.getListOfLatestOffers().size());
		d.prepareForNewRound(5);
		assertEquals(0,d.getListOfLatestOffers().size());
		
	}

	@After
	public void resetTests() {

	}

}
