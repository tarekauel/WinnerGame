package gameenginetest;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import server.Benefit;
import server.Company;
import server.GameEngine;
import server.Location;
import constant.Constant;

public class ListOfLosersTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		Benefit.initBenefits();
		// füge Companys hinzu

		new Company(Location.getLocationByCountry("USA"), "Test1");

		new Company(Location.getLocationByCountry("USA"), "Test2");
	}

	@Test
	public void listOfLosersTest() throws Exception {
		// Überlaste die Konten aller Firmen
		for (int i = 0; i < GameEngine.getGameEngine().getListOfCompanys()
				.size(); i++) {
			GameEngine
					.getGameEngine()
					.getListOfCompanys()
					.get(i)
					.getBankAccount()
					.decreaseBalance(
							Constant.BankAccount.START_CAPITAL
									+ Constant.BankAccount.MAX_CREDIT + 10);
		}
		assertEquals("Gameengine Runde: " + 1, GameEngine.getGameEngine()
				.toString());

	}
}
