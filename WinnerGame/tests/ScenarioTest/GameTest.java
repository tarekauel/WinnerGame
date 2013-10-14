package ScenarioTest;

import java.util.ArrayList;

import org.junit.Test;

import Message.GameDataMessageFromClient;
import Message.GameDataMessageFromClient.DistributionFromClient;
import Message.GameDataMessageFromClient.DistributionFromClient.OfferFromClient;
import Message.GameDataMessageFromClient.HumanResourcesFromClient;
import Message.GameDataMessageFromClient.HumanResourcesFromClient.BenefitBookingFromClient;
import Message.GameDataMessageFromClient.ProductionFromClient;
import Message.GameDataMessageFromClient.ProductionFromClient.ProductionOrderFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient.AcceptedSupplierOfferFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient.RequestFromClient;
import Message.GameDataMessageToClient;
import Message.GameDataMessageToClient.PurchaseToClient.RequestToClient;
import Message.GameDataMessageToClient.PurchaseToClient.RequestToClient.SupplierOfferToClient;
import Server.Benefit;
import Server.Company;
import Server.GameEngine;
import Server.Location;

public class GameTest {
	
	@Test
	public void gameTest() throws Exception {
		Benefit.initBenefits();
		Location.initLocations();
		
		GameEngine game = GameEngine.getGameEngine();
		
		game.addCompany( new Company(Location.getLocationByCountry("Deutschland"), "SolarWorld"));
		game.addCompany( new Company(Location.getLocationByCountry("Deutschland"), "SolarPlus"));
		
		ArrayList<GameDataMessageFromClient> messages = new  ArrayList<GameDataMessageFromClient>();
		
		ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
		requests.add ( new RequestFromClient("Wafer", 25) );
		requests.add ( new RequestFromClient("Gehäuse", 40) );
		
		ArrayList<AcceptedSupplierOfferFromClient> accepted = new ArrayList<AcceptedSupplierOfferFromClient>();
		
		ArrayList<ProductionOrderFromClient> proOrder = new ArrayList<ProductionOrderFromClient>();
		
		ArrayList<OfferFromClient> offerList = new ArrayList<OfferFromClient>();
		
		ArrayList<BenefitBookingFromClient> bBook = new ArrayList<BenefitBookingFromClient>();
		
		messages.add( new GameDataMessageFromClient("SolarWorld", 
				new PurchaseFromClient( requests, accepted ), 
				new ProductionFromClient(proOrder), 
				new DistributionFromClient( offerList ), 
				false, 
				new HumanResourcesFromClient( bBook), 
				10000, 
				true));
		
		messages.add( new GameDataMessageFromClient("SolarPlus", 
				new PurchaseFromClient( requests, accepted ), 
				new ProductionFromClient(proOrder), 
				new DistributionFromClient( offerList ), 
				false, 
				new HumanResourcesFromClient( bBook), 
				10000, 
				true));
		
		ArrayList<GameDataMessageToClient> answer =  game.startNextRound(messages);
		for( GameDataMessageToClient gm2c : answer) {
			System.out.println("----------------------------------------------");
			System.out.println("Company: " + gm2c.getPlayerName());
			System.out.println("Purchase");
			for(RequestToClient req : gm2c.purchase.requests ) {
				System.out.println("Angebote für folgende Anfrage: " + req.name + "(" + req.quality + ")");
				for(SupplierOfferToClient off : req.supplierOffers) {
					System.out.println("Angebot: " + off.name + " (" + off.quality + ") Stueckpreis ");
				}
			}
				
			System.out.println("----------------------------------------------");
		}
		
		
		
		
	}

}
