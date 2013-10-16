package productionordern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
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
		int advantage = 1000000;
		prodOrd.produce(advantage, c.getProduction().getMachine());
		assertEquals(true,prodOrd.getPanel()!=null);
		assertEquals(70,prodOrd.getPanel().getQuality()) ;
		assertEquals(640050,prodOrd.getPanel().getCosts() );
		
	}
	
	//ueberprueft ob die Qualitaet um mehr als 20 Punkte gestiegen ist
	@Test
	public void produceNewQualityDifferenceInvalid() throws Exception{
		prodOrd = p.getListOfAllProductionOrders().get(0);
		int advantage = 100000;
		prodOrd.produce(advantage, c.getProduction().getMachine());
		assertEquals(70, prodOrd.getPanel().getQuality());
	}
	
	
	@Test
	public void produceNewQualityInvalid() throws Exception{
		prodOrd = p.getListOfAllProductionOrders().get(0);
		int advantage = 1;
		prodOrd.produce(advantage, c.getProduction().getMachine());
		assertEquals(1, prodOrd.getPanel().getQuality());
	}
	//TODO: Um InputParameter zu pruefen fehlt die passende Exception es wird immer null zurueckgeliefert momentan


}
