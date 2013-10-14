package GameDataTranslatorToClient;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Message.GameDataMessageToClient;
import Server.Company;
import Server.GameDataTranslator;
import Server.GameEngine;
import Server.Location;

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
