package machinerytest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.BankAccount;
import server.Company;
import server.Location;
import server.Machinery;
import annotation.FakeRandom;

public class MachineLevel8 {
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
		// erh�he auf Maschinenlevel 2
		m.increaseLevel(b);
		// erh�he auf Maschinenlevel 3
		m.increaseLevel(b);
		// erh�he auf Maschinenlevel 4
		m.increaseLevel(b);
		// erh�he auf Maschinenlevel 5
		m.increaseLevel(b);
		// erh�he auf Maschinenlevel 6
		m.increaseLevel(b);
		// erh�he auf Maschinenlevel 7
		m.increaseLevel(b);
		// erh�he auf Maschinenlevel 8
		m.increaseLevel(b);
	}

	@Test
	@FakeRandom(randomNextIntNewRandom = { 0 }, randomNextIntMethodName = { "server.Machinery.isJunk" })
	public void testLow() {
		// Testet Randbereich unten
		assertEquals(false, m.isJunk());
	}

	@Test
	@FakeRandom(randomNextIntNewRandom = { 91 }, randomNextIntMethodName = { "server.Machinery.isJunk" })
	public void testUnder() {
		// Testet unter der Entscheidungsgrenze
		assertEquals(false, m.isJunk());
	}

	@Test
	@FakeRandom(randomNextIntNewRandom = { 92 }, randomNextIntMethodName = { "server.Machinery.isJunk" })
	public void testExact() {
		// Testet exakt die Entscheidungsgrenze
		assertEquals(true, m.isJunk());
	}

	@Test
	@FakeRandom(randomNextIntNewRandom = { 93 }, randomNextIntMethodName = { "server.Machinery.isJunk" })
	public void testAbove() {
		// Testet oberhalb der Entscheidungsgrenze
		assertEquals(true, m.isJunk());
	}

	@Test
	@FakeRandom(randomNextIntNewRandom = { 100 }, randomNextIntMethodName = { "server.Machinery.isJunk" })
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
		b.decreaseBalance(constant.Constant.BankAccount.MAX_CREDIT - 1);
		assertEquals(false, m.increaseLevel(b));
	}

	@Test
	public void decreaseMachine() {
		assertEquals(true, m.decreaseLevel());
	}
}
