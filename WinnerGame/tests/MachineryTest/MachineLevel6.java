package MachineryTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import AspectLogger.FakeRandom;
import Server.BankAccount;
import Server.Company;
import Server.Location;
import Server.Machinery;

public class MachineLevel6 {
	Machinery m;
	BankAccount b;
	Company c;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
	}

	@Before
	public void initializeTests() throws Exception {
		// initialisiere Company
		c = new Company(Location.getLocationByCountry("Deutschland"), "OTTO");
		// Hole BankAccount und Maschine
		b = c.getBankAccount();
		m = c.getProduction().getMachine();
		// Setze Bankkonto sehr hoch, damit es keine Probleme macht beim Ausbau
		b.increaseBalance(990000000);
		// erhöhe auf Maschinenlevel 2
		m.increaseLevel(b);
		// erhöhe auf Maschinenlevel 3
		m.increaseLevel(b);
		// erhöhe auf Maschinenlevel 4
		m.increaseLevel(b);
		// erhöhe auf Maschinenlevel 5
		m.increaseLevel(b);
		// erhöhe auf Maschinenlevel 6
		m.increaseLevel(b);
	}

	@Test
	@FakeRandom(randomNextIntNewRandom = { 0 }, randomNextIntMethodName = { "Server.Machinery.isJunk" })
	public void testLow() {
		// Testet Randbereich unten
		assertEquals(false, m.isJunk());
	}

	@Test
	@FakeRandom(randomNextIntNewRandom = { 89 }, randomNextIntMethodName = { "Server.Machinery.isJunk" })
	public void testUnder() {
		// Testet unter der Entscheidungsgrenze
		assertEquals(false, m.isJunk());
	}

	@Test
	@FakeRandom(randomNextIntNewRandom = { 90 }, randomNextIntMethodName = { "Server.Machinery.isJunk" })
	public void testExact() {
		// Testet exakt die Entscheidungsgrenze
		assertEquals(true, m.isJunk());
	}

	@Test
	@FakeRandom(randomNextIntNewRandom = { 91 }, randomNextIntMethodName = { "Server.Machinery.isJunk" })
	public void testAbove() {
		// Testet oberhalb der Entscheidungsgrenze
		assertEquals(true, m.isJunk());
	}

	@Test
	@FakeRandom(randomNextIntNewRandom = { 100 }, randomNextIntMethodName = { "Server.Machinery.isJunk" })
	public void testHigh() {
		// Testet Randbereich oben
		assertEquals(true, m.isJunk());
	}

	@Test
	public void increaseMachineEnoughBank() throws Exception {
		// baue Maschine aus
		assertEquals(true, m.increaseLevel(b));
	}

	@Test
	public void increaseMachineNotEnoughBank() throws Exception {
		// Setze Bank Konto auf 0
		b.decreaseBalance(b.getBankBalance());

		// Reize Dispo aus, bis auf 1 cent
		b.decreaseBalance(Constant.Constant.BankAccount.MAX_CREDIT - 1);
		assertEquals(false, m.increaseLevel(b));
	}

	@Test
	public void decreaseMachine() {
		assertEquals(true, m.decreaseLevel());
	}
}
