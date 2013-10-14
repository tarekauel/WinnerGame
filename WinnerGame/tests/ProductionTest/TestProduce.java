package ProductionTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.*;

public class TestProduce {

	Company c;
	Storage st;
	Resource wafer80;
	Resource wafer70;
	Resource cases;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
	}

	@Before
	public void initializeTests() throws Exception {
		c = new Company(Location.getLocationByCountry("USA"),"OTTO");
		st = c.getStorage();
		wafer80 = new Resource(80, "Wafer", 100000);
		wafer70 = new Resource(70, "Wafer", 100000);
		cases = new Resource(80, "Gehäuse", 100000);

	}

	@Test
	public void produceValid() throws Exception {
		c.getStorage().store(wafer80, 2160);
		c.getStorage().store(cases, 50);
		c.getProduction().createProductionOrder(wafer80, cases, 40);
		c.getProduction().produce();
		assertEquals(1, st.getAllFinishedGoods().size());
	}

	@Test
	// produzieren ohne Produktionsauftraege
	public void produceWithoutProdOrder() throws Exception {
		c.getProduction().produce();
		assertEquals(0, st.getAllFinishedGoods().size());
	}

	@Test
	//produzieren obwohl maximale Kapazitaet erreicht
	public void produceMaxCapacityReached() throws Exception {
		c.getStorage().store(wafer80, 600);
		c.getStorage().store(wafer70, 600);
		c.getStorage().store(cases, 50);
		// erstelle ProductionOrder die Kapazitaet der Maschine vollausreizt
		// damit zweite ProductionOrder nicht produziert wird
		c.getProduction().createProductionOrder(wafer80, cases, 501);
		c.getProduction().createProductionOrder(wafer70, cases, 50);
		c.getProduction().produce();
		// pruefe ob zweite ProductionOrder nicht mehr produziert wurde
		assertEquals(0, c.getProduction().getListOfAllProductionOrders().get(1)
				.getProduced()
				+ c.getProduction().getListOfAllProductionOrders().get(1)
						.getWaste());

	}

	@Test
	//es soll solange produziert werden bis Wafer auf 0
	public void produceNotEnougWaferResources() throws Exception {
		c.getStorage().store(wafer80, 50);
		c.getStorage().store(cases, 50);
		c.getProduction().createProductionOrder(wafer80, cases, 400);
		c.getProduction().produce();
		assertEquals(0, c.getProduction().getListOfAllProductionOrders().get(0)
			.getProduced()
				+ c.getProduction().getListOfAllProductionOrders().get(0)
						.getWaste());

	}

	
	@Test
	//es soll solange produziert werden bis Cases auf 0
	//dabei werden schon abgebuchte Wafer wieder zurueck gebucht
	public void produceNotEnougCaseResources() throws Exception {
		c.getStorage().store(wafer80, 500);
		c.getStorage().store(cases, 1);
		c.getProduction().createProductionOrder(wafer80, cases, 100);
		c.getProduction().produce();
		assertEquals(446,c.getStorage().getAllStorageElements().get(0).getQuantity());
		assertEquals(1, c.getProduction().getListOfAllProductionOrders().get(0).getWaste()+c.getProduction().getListOfAllProductionOrders().get(0)
						.getProduced());
	}
	

	@After
	public void resetTests() {

	}

}
