package company;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Company;
import server.CustomerMarket;
import server.FinishedGood;
import server.Location;
import server.Resource;
import server.TPresentValue;

public class TestPresentValue {
	
	Company c;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		
	}

	@Before
	public void initializeTests() throws Exception {
		c = new Company(Location.getLocationByCountry("USA"), "OTTO");
		//einlagern
		Resource cases = new Resource(50, "Gehäuse", 10000);
		c.getStorage().store(cases, 100);
		Resource wafer = new Resource(80, "Wafer", 1000);
		c.getStorage().store(wafer, 10000);
		FinishedGood fg = FinishedGood.create(80, 100000);
		c.getStorage().store(fg, 100);
		//veraendern des kontostandes um 1Mio
		c.getBankAccount().decreaseBalance(100000000);		
		//marketShare
		//verkaufe 50 stueck zu 1eur sodass immer gekauft wird
		//um marketshares zu erhalten, da nur eine Company vorhanden ist der Marktanteil im Test 100%
		c.getDistribution().createOffer(80, 50, 100);
		CustomerMarket.getMarket().handleAllOffers();
		
		//erhoehen des Maschinenlevels
		c.getProduction().getMachine().increaseLevel(c.getBankAccount());
		c.getProduction().getMachine().increaseLevel(c.getBankAccount());
		
		//Grundstueck wird da wir uns in Runde 1 befinden in der Berechnung des Presentvalues abgeschrieben.
		//muss demnach hier nicht weiter behandelt werden.
		
		
	}

	@Test
	public void getPresentValueTest() throws Exception {
		TPresentValue presentValue = c.getPresentValue();
		assertEquals(307360500, presentValue.getPresentValue());
		
	}

	@After
	public void resetTests() {

	}

}
