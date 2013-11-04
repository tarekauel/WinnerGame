package server;

import java.io.IOException;
import java.util.ArrayList;

import kigegner.ClientToServerMessageCreator;
import message.GameDataMessageFromClient;
import message.GameDataMessageFromClient.HumanResourcesFromClient.BenefitBookingFromClient;
import message.GameDataMessageToClient;
import message.GameDataMessageToClient.DistributionToClient;
import message.GameDataMessageToClient.HumanResourcesToClient;
import message.GameDataMessageToClient.PurchaseToClient;
import message.GameDataMessageToClient.PurchaseToClient.RequestToClient;
import message.GameDataMessageToClient.ReportingToClient;
import message.GameDataMessageToClient.DistributionToClient.OfferToClient;
import message.GameDataMessageToClient.HumanResourcesToClient.BenefitBookingToClient;
import message.GameDataMessageToClient.HumanResourcesToClient.PossibleBenefit;
import message.GameDataMessageToClient.MarketingToClient.MarketShareToClient;
import message.GameDataMessageToClient.MarketingToClient.RessourcePriceToClient;
import message.GameDataMessageToClient.MarketingToClient;
import message.GameDataMessageToClient.ProductionToClient;
import message.GameDataMessageToClient.ProductionToClient.ProductionOrderToClient;
import message.GameDataMessageToClient.ReportingToClient.CashValueOfRoundToClient;
import message.GameDataMessageToClient.ReportingToClient.FixCostToClient;
import message.GameDataMessageToClient.ReportingToClient.MachineryToClient;
import message.GameDataMessageToClient.StorageToClient;
import message.GameDataMessageToClient.StorageToClient.StorageElementToClient;
import server.connection.Player;
import server.connection.Server;
import server.connection.ServerConnection;
import constant.Constant;

public class GameEngine {

	// Singleton referenz
	private static GameEngine engine;

	// Liste der Abteilungen, die zu Beginn jeder Runde für
	// Initialisierungszwecke aufgerufen werden müssen
	private ArrayList<IRoundSensitive> listSensitiveDepartments = new ArrayList<IRoundSensitive>();

	// Liste aller Unternehmen, die am Spiel teilnehmen
	private ArrayList<Company> listOfCompanys = new ArrayList<Company>();

	// Liste aller Unternehmen, die verloren haben
	private ArrayList<Company> listOfLosers = new ArrayList<Company>();

	// Rundennummer
	private int round = 1;

	private GameEngine() {

		engine = this;

	}

	/**
	 * Liefert die Instanz auf die Gameengine zurück, erstellt sie ggf
	 * 
	 * @return GameEngine Instanz auf die Gameengine
	 * 
	 */
	public static GameEngine getGameEngine() {
		if (engine == null) {
			engine = new GameEngine();
		}

		return engine;
	}

	/**
	 * @return aktuelle Runde
	 */
	public int getRound() {

		return round;
	}

	/**
	 * 
	 * @return gibt alles Companys der Spieler zurueck
	 */
	public ArrayList<Company> getListOfCompanys() {
		return this.listOfCompanys;
	}
	
	private Company findCompanyOfPlayer(String playerName) {

		for (Company company : GameEngine.getGameEngine().getListOfCompanys()) {
			if (company.getName().equals(playerName)) {
				return company;
			}
		}
		throw new IllegalArgumentException("Der Playername ist invalid!");

	}
	public synchronized GameDataMessageToClient getInitialGameDataMessageToClient(String player) throws Exception {
		
		Company company =findCompanyOfPlayer(player);
		ArrayList<MarketShareToClient> marketShares = new ArrayList<MarketShareToClient>();		
		ArrayList<RessourcePriceToClient> waferPrice = new ArrayList<RessourcePriceToClient>();		
		ArrayList<RessourcePriceToClient> casePrice = new ArrayList<RessourcePriceToClient>();		
		message.GameDataMessageToClient.MarketingToClient marketing = new MarketingToClient(false, 0, 0, marketShares, waferPrice, casePrice);
		
		ArrayList<ProductionOrderToClient> productionOrders = new ArrayList<ProductionOrderToClient>();			
		message.GameDataMessageToClient.ProductionToClient production = new ProductionToClient(productionOrders);
		
		ArrayList<FixCostToClient> fixCosts = new ArrayList<FixCostToClient>();
		fixCosts.add(new FixCostToClient("Verkauf", Constant.DepartmentFixcost.DISTRIBUTION));
		fixCosts.add(new FixCostToClient("Personal", company.getHumanResources().getFixCosts()));
		fixCosts.add(new FixCostToClient("Marktforschung", Constant.DepartmentFixcost.MARKET_RESEARCH));
		fixCosts.add(new FixCostToClient("Produktion", Constant.DepartmentFixcost.PRODUCTION));
		fixCosts.add(new FixCostToClient("Einkauf", Constant.DepartmentFixcost.PURCHASE));
		fixCosts.add(new FixCostToClient("Lager", Constant.DepartmentFixcost.STORAGE));
	
		ArrayList<CashValueOfRoundToClient> cashValues = new ArrayList<CashValueOfRoundToClient>();	
		MachineryToClient machinery = new MachineryToClient(1, Constant.Machinery.CAPACITY[0], 0, 0);
		message.GameDataMessageToClient.ReportingToClient reporting = new ReportingToClient(fixCosts, machinery, cashValues);
		
		ArrayList<RequestToClient> requests = new ArrayList<RequestToClient>();
		message.GameDataMessageToClient.PurchaseToClient purchase = new PurchaseToClient(requests);		
		
		ArrayList<StorageElementToClient> storageElements = new ArrayList<StorageElementToClient>();
		message.GameDataMessageToClient.StorageToClient storage = new StorageToClient(Constant.Product.STORAGECOST_WAFER, Constant.Product.STORAGECOST_CASE, Constant.Product.STORAGECOST_PANEL, storageElements);
		
		ArrayList<OfferToClient> offers = new ArrayList<OfferToClient>();		
		message.GameDataMessageToClient.DistributionToClient distribution = new DistributionToClient(offers, Constant.Distribution.DISTRIBUTION_OFFER_COSTS_PER_PANEL);
		
		ArrayList<BenefitBookingToClient> benefits = new ArrayList<BenefitBookingToClient>();
		ArrayList<PossibleBenefit> possibleBenefits = new ArrayList<PossibleBenefit>();
		for (Benefit benefit : Benefit.getBookableBenefits()) {
			possibleBenefits.add(new PossibleBenefit(benefit.getName(), benefit.getCostsPerRound()));
		}
		ArrayList<TMotivation> motivation = new ArrayList<TMotivation>();
		message.GameDataMessageToClient.HumanResourcesToClient humanResources = new HumanResourcesToClient(benefits, possibleBenefits, motivation, MarketData.getMarketData().getAvereageWage().getAmount(), company.getHumanResources().getWagesPerHour().getAmount(), company.getHumanResources().getCountEmployees(),company.getHumanResources().getWagesSum());
				
		GameDataMessageToClient initialMessage = new GameDataMessageToClient(player, purchase, production, storage, distribution, humanResources, marketing, reporting, company.getBankAccount().getBankBalance(), Constant.BankAccount.MAX_CREDIT);
		
		return initialMessage;
		
	}

	/**
	 * Startet die nächste Runde
	 * 
	 * @param gameDataList
	 *            Übergebene Eingabedaten der Spieler
	 * @throws Exception
	 */
	public synchronized ArrayList<GameDataMessageToClient> startNextRound(
			ArrayList<GameDataMessageFromClient> gameDataList) throws Exception {
		round++; // Runde hochzaehlen
		prepareAllDepartmentsForNewRound();
		parseClientData(gameDataList);

		for (Company company : listOfCompanys) {
			// Prüfen ob die Company noch beruecksichtigt wird
			if (listOfLosers.contains(company)) {
				// scheinbar nicht
				break;
			}
			// ---------Storage---------------------
			company.getStorage().debitStorageCost();
			// ---------Production---------------------
			company.getProduction().produce();
			// ---------Fixkosten berechnen+abbuchen---------------------
			long costs = company.getMarketResearch().getFixCosts();
			costs += company.getDistribution().getFixCosts();
			costs += company.getHumanResources().getFixCosts();
			costs += company.getProduction().getFixCosts();
			costs += company.getPurchase().getFixCosts();
			costs += company.getStorage().getFixCosts();
			company.getBankAccount().decreaseBalance(costs);
			company.getHumanResources().refreshMotivationHistory();
			
			if(this.round>= Constant.Server.MAX_ROUNDS){ 
				//Maximale Rundezahl erreicht!
				Server.getServer().sendGameOver();
			}

		}

		// Der CustomerMarkt darf erst ab Runde 4 beginnnen
		// sonst ist er bereits gestorben, wenn angebote kommen
		if (round >= 4) {
			CustomerMarket.getMarket().handleAllOffers();
		}

		SupplierMarket.getMarket().handleRequest();

		// Maschinenpark nach Produktion ausbauen
		// Benefits nach der Produktion aktivieren
		for (GameDataMessageFromClient message : gameDataList) {
			for (Company company : listOfCompanys) {
				if (company.getName().equals(message.getPlayerName())) {
					handleBenefitBooking(message.humanResources.benefits,
							company);
					if (message.increaseMachineLevel) {
						company.getProduction().increaseMachineryLevel();
					}
				}
			}
		}

		

		return createDataForClient();

	}

	private void handleBenefitBooking(
			ArrayList<BenefitBookingFromClient> benefits, Company company)
			throws Exception {
		HumanResources hr = company.getHumanResources();

		for (BenefitBookingFromClient benefit : benefits) {
			hr.bookBenefit(benefit.name, benefit.duration);
		}
	}

	private void parseClientData(
			ArrayList<GameDataMessageFromClient> gameDataList) throws Exception {
		GameDataTranslator.getGameDataTranslator()
				.convertGameDataMessage2Objects(gameDataList);

	}

	private ArrayList<GameDataMessageToClient> createDataForClient()
			throws Exception {
		return GameDataTranslator.getGameDataTranslator()
				.createGameDataMessages();

	}

	private void prepareAllDepartmentsForNewRound() throws Exception {
		for (IRoundSensitive d : listSensitiveDepartments) {
			if(d!=null){
			d.prepareForNewRound(round);
			}
		}
	}

	/**
	 * Für eine Abteilung hinzu, die zu Beginn der Runde einen
	 * Initialisierungsaufruf braucht
	 * 
	 * @param d
	 *            Abteilung, die der Aufrufliste hinzugefügt werden soll
	 */
	public synchronized void addRoundSensitive(IRoundSensitive d) {

		listSensitiveDepartments.add(d);

	}

	/**
	 * Fügt ein Unternehmen dem Spiel hinzu
	 * 
	 * @param c
	 *            Company, die hinzugefügt werden soll
	 */
	public synchronized void addCompany(Company c) {

		listOfCompanys.add(c);

	}

	/**
	 * Fuege einen Verlierer hinzu
	 * 
	 * @param c
	 *            die Firma die verloren hat
	 * @throws IOException
	 */
	public synchronized void addCompanyLost(Company c) throws Exception {
		c.setHasLost(true);
		listOfLosers.add(c);
		

		CustomerMarket.getMarket().removeDistribution(c.getDistribution());
		SupplierMarket.getMarket().removePurchase(c.getPurchase());
		MarketData.getMarketData().removeHR(c.getHumanResources());
		
		if (listOfCompanys.size() == listOfLosers.size()-1) {
			//Ein Spieler bleibt uebrig!
			Server.getServer().sendGameOver();
		}

	}

	/**
	 * 
	 * @return alle Verlierer
	 */
	public ArrayList<Company> getListOfLosers() {
		return listOfLosers;
	}

	@Override
	public String toString() {
		return "Gameengine Runde: " + this.round;
	}

}
