package Distribution;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.*;

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
	public void createOfferQualityValid() {
		int sizeBefore = d.getListOfOffers().size();
		d.createOffer(80,100,10000);
		int sizeAfter = d.getListOfOffers().size();
		assertEquals(true, sizeBefore+1==sizeAfter);
	}
	
	@Test  (expected = java.lang.IllegalArgumentException.class)
	public void createOfferQualityEQZero() {
		d.createOffer(0,100,10000);
	}
	
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createOfferQualityLowerZero() {
		d.createOffer(-1,100,10000);
	}
	
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createOfferQualityGreaterHunderd() {
		d.createOffer(101,100,10000);
	}
	
	@Test 
	public void createOfferPriceValid() {
		int sizeBefore = d.getListOfOffers().size();
		d.createOffer(80,100,1000);
		int sizeAfter = d.getListOfOffers().size();
		assertEquals(true, sizeBefore+1==sizeAfter);
	}
	
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createOfferPriceLowerZero() {
		d.createOffer(80,100,-1);
	}
	
	@Test (expected = java.lang.IllegalArgumentException.class)
	public void createOfferPriceEqualsZero() {
		d.createOffer(80,100,0);
	}
	
	

	@After
	public void resetTests() {

	}

}
