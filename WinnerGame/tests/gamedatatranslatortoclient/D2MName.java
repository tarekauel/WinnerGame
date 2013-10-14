package gamedatatranslatortoclient;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import message.GameDataMessageToClient;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Company;
import server.GameDataTranslator;
import server.GameEngine;
import server.Location;

public class D2MName {
	
	Company c;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		GameEngine.getGameEngine();

		
	}

	@Before
	public void initializeTests() throws Exception {
		c = new Company(Location.getLocationByCountry("USA"),"OTTO");
	}

	@Test
	public void getRightName() throws Exception {
		 ArrayList<GameDataMessageToClient> gameDataMessageToClients = GameDataTranslator.getGameDataTranslator().createGameDataMessages();
		 assertEquals("OTTO",gameDataMessageToClients.get(0).getPlayerName());
	}

	@After
	public void resetTests() {
		

	}

}
