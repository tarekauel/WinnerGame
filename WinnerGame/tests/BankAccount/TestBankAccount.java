package BankAccount;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Constant.Constant;
import Server.BankAccount;
import Server.Company;
import Server.Location;

public class TestBankAccount {

	BankAccount b;
	Company c;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
	}

	@Before
	public void initializeTests() throws Exception {
		Company c = new Company(Location.getLocationByCountry("Deutschland"),"OTTO");
		b = c.getBankAccount();

	}

	@Test
	public void increase() {

		b.increaseBalance(500);
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void increaseNegativeAmount() {
		b.increaseBalance(-5000000);
	}

	@Test
	public void increaseAmount0() {
		long bankBalance = (long) b.getBankBalance();
		b.increaseBalance(0);
		assertEquals(true,(b.getBankBalance()==bankBalance));
	}

	@Test
	public void decrease() throws Exception {
		assertEquals(true,
				b.decreaseBalance(500));
	}

	@Test
	public void decreaseCompletely() throws Exception {
		assertEquals(true, b.decreaseBalance(b.getBankBalance()));
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void decreaseNegativeAmount() throws Exception {
		b.decreaseBalance(-500);
	}

	@Test
	public void decreaseAmount0() throws Exception {
		assertEquals(true,b.decreaseBalance(0));
	}

	@Test
	public void decreaseTooMuch() throws Exception {
		assertEquals(
				false,
				b.decreaseBalance(b.getBankBalance()
						+ Constant.BankAccount.MAX_CREDIT + 50));
	}

	@After
	public void resetTests() {
		c = null;
		b = null;

	}

}
