package scenarien;

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
import message.GameDataMessageToClient;
import message.GameDataMessageToClient.PurchaseToClient.RequestToClient;
import message.GameDataMessageToClient.PurchaseToClient.RequestToClient.SupplierOfferToClient;

import org.junit.Test;

import server.Benefit;
import server.Company;
import server.GameEngine;
import server.Location;

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
