package scenarien;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import message.GameDataMessageFromClient;
import message.GameDataMessageFromClient.DistributionFromClient;
import message.GameDataMessageFromClient.DistributionFromClient.OfferFromClient;
import message.GameDataMessageFromClient.HumanResourcesFromClient;
import message.GameDataMessageFromClient.HumanResourcesFromClient.BenefitBookingFromClient;
import message.GameDataMessageFromClient.ProductionFromClient;
import message.GameDataMessageFromClient.ProductionFromClient.ProductionOrderFromClient;
import message.GameDataMessageFromClient.PurchaseFromClient;
import message.GameDataMessageFromClient.PurchaseFromClient.AcceptedSupplierOfferFromClient;
import message.GameDataMessageFromClient.PurchaseFromClient.RequestFromClient;
import message.LoginConfirmationMessage;
import message.LoginMessage;

import org.junit.BeforeClass;
import org.junit.Test;

import server.connection.Server;
import client.connection.Client;
import constant.Constant;

public class GameEngineRoundTest {

	@BeforeClass 
	public static void beforeTests(){
		//SingletonObjecte = null
	}

	@Test
	public void roundTestWithConnection() throws Exception {
		if(Constant.Server.MAX_PLAYER==3){ //Test erwartet 3 Spieler
		// Server starten
		Server.main(null);

		// Client 1 connect
		Client c1 = new Client();
		String ip="localhost";
		c1.connect(ip, Constant.Server.TCP_PORT);
		

		// Client 2 connect
		Client c2 = new Client();
		c2.connect(ip, Constant.Server.TCP_PORT);
		

		// Client 3 connect
		Client c3 = new Client();
		c3.connect(ip, Constant.Server.TCP_PORT);
		
		c1.writeMessage(new LoginMessage("SolarWorld", "passwort1", "Deutschland"));
		c2.writeMessage(new LoginMessage("SolarPlus", "passwort1", "USA"));
		c3.writeMessage(new LoginMessage("SolarMax", "passwort1", "China"));
		
		// Read LoginConfirmationMessages
		LoginConfirmationMessage message1 = (LoginConfirmationMessage) c1
				.readMessage();
		assertEquals(true, message1.getSuccess());
		LoginConfirmationMessage message2 = (LoginConfirmationMessage) c2
				.readMessage();
		assertEquals(true, message2.getSuccess());
		LoginConfirmationMessage message3 = (LoginConfirmationMessage) c3
				.readMessage();
		assertEquals(true, message3.getSuccess());

		
		
		// Set GameData
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		requests.add ( new RequestFromClient("Wafer", 25) );
		requests.add ( new RequestFromClient("Geh�use", 40) );
		
		ArrayList<AcceptedSupplierOfferFromClient> accepted = new ArrayList<AcceptedSupplierOfferFromClient>();
		
		ArrayList<ProductionOrderFromClient> proOrder = new ArrayList<ProductionOrderFromClient>();
		
		ArrayList<OfferFromClient> offerList = new ArrayList<OfferFromClient>();
		
		ArrayList<BenefitBookingFromClient> bBook = new ArrayList<BenefitBookingFromClient>();
		
		GameDataMessageFromClient gameMessagefromClient1 = new GameDataMessageFromClient("SolarWorld", 
				new PurchaseFromClient( requests, accepted ), 
				new ProductionFromClient(proOrder), 
				new DistributionFromClient( offerList ), 
				false, 
				new HumanResourcesFromClient( bBook), 
				10000, 
				true);
		GameDataMessageFromClient gameMessagefromClient2 =  new GameDataMessageFromClient("SolarPlus", 
				new PurchaseFromClient( requests, accepted ), 
				new ProductionFromClient(proOrder), 
				new DistributionFromClient( offerList ), 
				false, 
				new HumanResourcesFromClient( bBook), 
				10000, 
				true);
		
		GameDataMessageFromClient gameMessagefromClient3 =  new GameDataMessageFromClient("SolarMax", 
				new PurchaseFromClient( requests, accepted ), 
				new ProductionFromClient(proOrder), 
				new DistributionFromClient( offerList ), 
				false, 
				new HumanResourcesFromClient( bBook), 
				10000, 
				true);
	
		
		//Send GameData		
		
		c1.writeMessage(gameMessagefromClient1);
		c2.writeMessage(gameMessagefromClient2);
		c3.writeMessage(gameMessagefromClient3);
		
		//Get GameData
		
		c1.readMessage();
		
		c2.readMessage();
	
		c3.readMessage();
	
		//Neuer Client versucht sich einzuloggen, was nicht funktionieren darf
		Client c4 = new Client();
		c4.connect(ip, Constant.Server.TCP_PORT);
		c4.writeMessage(new LoginMessage("Neue Firma", "passwort", "China"));
		LoginConfirmationMessage messageBack4 = (LoginConfirmationMessage) c4.readMessage();
		assertEquals(false, messageBack4.getSuccess());
		
		Server.getServer().close();
		}else{
			System.out.println("F�r den Test ist es erforderlich, dass in der Ini MaxUsers=3 steht!");
			fail("MaxUsers in Ini !=3");
			
		}

	}

}
