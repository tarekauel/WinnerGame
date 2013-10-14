package ProductionOrder;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.*;

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
		wafer = new Resource(80, "Wafer", 10000);
		cases = new Resource(50, "Gehäuse", 100000);
		p.createProductionOrder(wafer, cases, 100);
	}

	@Test
	//TODO in ProductionOrder produce() Zeile  169 muss Faktor 10 weg sonst Qualities von 0-1000
	public void produceValid() throws Exception{
		prodOrd = p.getListOfAllProductionOrders().get(0);	
		int advantage = 100;
		prodOrd.produce(advantage, c.getProduction().getMachine());
		assertEquals(true,prodOrd.getPanel()!=null);
		assertEquals(true,prodOrd.getPanel().getQuality() == 74);
		assertEquals(true,prodOrd.getPanel().getCosts() == 655000);
		fail();
	}
	
	//ueberprueft ob die Qualitaet um mehr als 20 Punkte gestiegen ist
	@Test
	public void produceNewQualityDifferenceInvalid() throws Exception{
		prodOrd = p.getListOfAllProductionOrders().get(0);
		int advantage = 1000;
		prodOrd.produce(advantage, c.getProduction().getMachine());
		assertEquals(true, prodOrd.getPanel().getQuality()== 94);
	}
	
	
	@Test
	public void produceNewQualityInvalid() throws Exception{
		prodOrd = p.getListOfAllProductionOrders().get(0);
		int advantage = 10;
		prodOrd.produce(advantage, c.getProduction().getMachine());
		assertEquals(true, prodOrd.getPanel().getQuality()==7);
	}
	//TODO: Um InputParameter zu pruefen fehlt die passende Exception es wird immer null zurueckgeliefert momentan
	@After
	public void resetTests() {

	}

}
