package ProductTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Constant.Constant;
import Server.FinishedGood;
import Server.Resource;

public class TestConstructor {

	
	@Test
	public void createValidWafer() throws Exception{
		Resource wafer = new Resource(7, "Wafer",20);
		Boolean isNotNull = wafer!=null;
		assertEquals(true, isNotNull);

	}

	@Test
	public void createValidGehause()  throws Exception{
		Resource gehause = new Resource(7, "Gehäuse", 20);
		Boolean isNotNull = gehause!=null;
		assertEquals(true, isNotNull);

	}

	@Test (expected = IllegalArgumentException.class )
	public void createNonValidRessource()  throws Exception{
		new Resource(7, "Panel", 20);

	}

	@Test
	public void createValidFinishedGood() throws Exception {
		FinishedGood fg = FinishedGood.create(7, 10);
		Boolean isNotNull = fg!=null;
		assertEquals(true, isNotNull);

	}

	@Test (expected = IllegalArgumentException.class )
	public void createNonValidProductByCosts() throws Exception{
		FinishedGood fg = FinishedGood.create(7, -1);
		

	}

	@Test (expected = IllegalArgumentException.class )
	public void createNonValidProductByQuality1() throws Exception{
		FinishedGood fg = FinishedGood.create(101, 10);
		


	}
	
	@Test (expected = IllegalArgumentException.class )
	public void createNonValidProductByQuality2() throws Exception{
		
		FinishedGood fg = FinishedGood.create(-1, 10);

	}
	
	@Test
	public void updateStorageCostsPanel()throws Exception{
		FinishedGood good = FinishedGood.create(50, 10);
		int costBefore = good.getCosts();
		good.calculateNewCosts();
		int costAfter = good.getCosts();
		
		assertEquals(true,((costBefore+Constant.Product.STORAGECOST_PANEL) == costAfter));
		
	}

	@Test
	public void updateStorageCostsCase()throws Exception{
		Resource good = new Resource(50, "Gehäuse", 10);
		int costBefore = good.getCosts();
		good.calculateNewCosts();
		int costAfter = good.getCosts();
		
		assertEquals(true,((costBefore+Constant.Product.STORAGECOST_CASE) == costAfter));
		
	}
	@Test
	public void updateStorageCostsWafer()throws Exception{
		Resource good = new Resource(50, "Wafer", 10);
		int costBefore = good.getCosts();
		good.calculateNewCosts();
		int costAfter = good.getCosts();
		
		assertEquals(true,((costBefore+Constant.Product.STORAGECOST_WAFER) == costAfter));
		
	}
}
