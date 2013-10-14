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

public class TestCreateOffer {
	Company c;
	Storage st;
	Distribution d;
	FinishedGood fg;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
	}

	@Before
	public void initializeTests() throws Exception {
		c = new Company(Location.getLocationByCountry("USA"),"OTTO");
		st = c.getStorage();
		d = c.getDistribution();
		fg = FinishedGood.create(80, 1000);
		st.store(fg, 1500);
		
	}

	@Test
	public void createOfferQualityValid() throws Exception {
		int sizeBefore = d.getListOfOffers().size();
		d.createOffer(80,100,10000);
		int sizeAfter = d.getListOfOffers().size();
		assertEquals(true, sizeBefore+1==sizeAfter);
	}
	
	@Test  (expected = java.lang.IllegalArgumentException.class)
	public void createOfferQualityEQZero() throws Exception {
		d.createOffer(0,100,10000);
	}
	
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createOfferQualityLowerZero() throws Exception {
		d.createOffer(-1,100,10000);
	}
	
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createOfferQualityGreaterHunderd() throws Exception {
		d.createOffer(101,100,10000);
	}
	
	@Test 
	public void createOfferPriceValid() throws Exception {
		int sizeBefore = d.getListOfOffers().size();
		d.createOffer(80,100,1000);
		int sizeAfter = d.getListOfOffers().size();
		assertEquals(true, sizeBefore+1==sizeAfter);
	}
	
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createOfferPriceLowerZero() throws Exception {
		d.createOffer(80,100,-1);
	}
	
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createOfferPriceEqualsZero() throws Exception {
		d.createOffer(80,100,0);
	}
	
	

	@After
	public void resetTests() {

	}

}
