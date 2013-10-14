package HumanResources;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.Benefit;
import Server.Company;
import Server.HumanResources;
import Server.Location;
import Server.MarketData;
import Server.TWage;

public class HRMotivationTest {
	
	private static HumanResources h1 = null;
	private static HumanResources h2 = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		Benefit.initBenefits();		
		h1 = new Company( Location.getLocationByCountry("Test"),"OTTO").getHumanResources();
		h2 = new Company( Location.getLocationByCountry("TestB"),"ANNA").getHumanResources();		
	}

	@Test
	public void testAverageWage() throws Exception{
		h1.setWagePerRound(new TWage(1000, 1, h1.getCompany().getLocation().getWageLevel()));
		h2.setWagePerRound(new TWage(1000, 1, h2.getCompany().getLocation().getWageLevel()));
		TWage expected = new TWage(1500,1, 10000 );
		TWage averageWage = MarketData.getMarketData().getAvereageWage();
		assertEquals(true, expected.equals(averageWage));
	}
	
	@Test
	public void testAverageBenefit() throws Exception {
		/*h1.bookBenefit("Sport", 10);
		h2.bookBenefit("Sport", 10);
		// TODO Runde muss fakebar sein!
		assertEquals( 15000,  MarketData.getMarketData().getAverageBenefit());*/
	}
	
	@Test
	public void testMotivation() throws Exception{
		h1.setWagePerRound(new TWage(1000, 1, h1.getCompany().getLocation().getWageLevel()));
		h2.setWagePerRound(new TWage(1000, 1, h2.getCompany().getLocation().getWageLevel()));
		int motivationH1 = h1.getMotivation();
		int motivationH2 = h2.getMotivation();
		
		assertEquals(7083,motivationH1);
		assertEquals(11533,motivationH2);
	}
	
	
}
