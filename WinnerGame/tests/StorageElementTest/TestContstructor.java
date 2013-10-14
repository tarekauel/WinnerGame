package StorageElementTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Server.Resource;
import Server.StorageElement;

public class TestContstructor {

	private Resource wafer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void init() throws Exception {
		wafer = new Resource(60, "Wafer", 300);
	}
	

	@Test
	public void StorageElementValid() throws Exception {

		StorageElement se = new StorageElement(5, wafer);

		assertEquals(true, se != null);

	}

	@Test
	public void StorageElementIncrease() throws Exception {
		// create the store element
		StorageElement se = null;

		se = new StorageElement(50, wafer);

		assertEquals(true, se.increaseQuantity(500));

		assertEquals(550, se.getQuantity());

	}

	@Test
	public void StorageElementDecreaseTooMuch() throws Exception {
		// create the store element
		StorageElement se = null;
		se = new StorageElement(50, wafer);

		assertEquals(false, se.reduceQuantity(500));
		// Darf nicht abgebucht haben
		assertEquals(50, se.getQuantity());

	}

	@Test
	public void StorageElementDecreaseValidElementCompletely() throws Exception {
		// create the store element
		StorageElement se = null;

		se = new StorageElement(500, wafer);

		se.reduceQuantity(500);

		assertEquals(0, se.getQuantity());

	}

	@Test
	public void StorageElementDecreaseValidElement() throws Exception {
		// create the store element
		StorageElement se = null;

		se = new StorageElement(500, wafer);

		se.reduceQuantity(50);

		assertEquals(450, se.getQuantity());

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void StorageElementWithInvalidQuantity() throws Exception {
		// StorageElemente erstellen
		new StorageElement(-5, wafer);

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void StorageElementWithQuantity0() throws Exception {
		// StorageElemente erstellen

		new StorageElement(0, wafer);

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void StorageElementWithNullReference() throws Exception {
		// StorageElemente erstellen

		new StorageElement(1, null);

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void StorageElementWithNullReferenceAndQuantity0() throws Exception {
		// StorageElemente erstellen

		new StorageElement(0, null);

	}
}
