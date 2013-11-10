package productionordertest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Company;
import server.Location;
import server.Production;
import server.ProductionOrder;
import server.Resource;

public class ProduceTest {
	
	Company c;
	Production p;
	ProductionOrder prodOrd;
	Resource wafer;
	Resource cases;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
	}

	@Before
	public void initializeTests() throws Exception {
		c = new Company(Location.getLocationByCountry("USA"),"OTTO");
		p = c.getProduction();
		wafer = new Resource(50, "Wafer", 10000);
		cases = new Resource(50, "Gehäuse", 100000);
		p.createProductionOrder(wafer, cases, 100);
	}

	@Test
	public void produceValid() throws Exception{
		prodOrd = p.getListOfAllProductionOrders().get(0);	
	
		prodOrd.produce(1.0, c.getProduction().getMachine());
		assertEquals(true,prodOrd.getPanel()!=null);
		assertEquals(50,prodOrd.getPanel().getQuality()) ;
		assertEquals(640050,prodOrd.getPanel().getCosts() );
		
	}
	
	//ueberprueft ob die Qualitaet um mehr als 20 Punkte gestiegen ist
	@Test
	public void produceNewQualityDifferenceInvalid() throws Exception{
		prodOrd = p.getListOfAllProductionOrders().get(0);
	
		prodOrd.produce(1000000.0, c.getProduction().getMachine());
		assertEquals(70, prodOrd.getPanel().getQuality());
	}
	
	//ueberprueft ob die Qualitaet auf mehr als 100 Punkte gestiegen ist
		@Test
		public void produceNewQuality100DifferenceInvalid() throws Exception{
			wafer = new Resource(90, "Wafer", 10000);
			cases = new Resource(90, "Gehäuse", 100000);
			p.createProductionOrder(wafer, cases, 100);
			prodOrd = p.getListOfAllProductionOrders().get(p.getListOfAllProductionOrders().size()-1);
			
			prodOrd.produce(1000000.0, c.getProduction().getMachine());
			assertEquals(100, prodOrd.getPanel().getQuality());
		}
	@Test
	public void produceNewQualityInvalid() throws Exception{
		prodOrd = p.getListOfAllProductionOrders().get(0);
		
		prodOrd.produce(0.001, c.getProduction().getMachine());
		assertEquals(1, prodOrd.getPanel().getQuality());
	}


}
