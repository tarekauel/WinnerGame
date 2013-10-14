package Server;

import java.io.IOException;
import java.util.ArrayList;

import Constant.Constant;
import Message.GameDataMessageFromClient;
import Message.GameDataMessageFromClient.HumanResourcesFromClient.BenefitBookingFromClient;
import Message.GameDataMessageToClient;
import Message.GameDataMessageToClient.HumanResourcesToClient;
import Message.GameDataMessageToClient.HumanResourcesToClient.PossibleBenefit;
import Message.GameDataMessageToClient.StorageToClient;
import Server.Connection.Server;

public class GameEngine {

	// Singleton referenz
	private static GameEngine engine;

	// Liste der Abteilungen, die zu Beginn jeder Runde für
	// Initialisierungszwecke aufgerufen werden müssen
	private ArrayList<DepartmentRoundSensitive> listSensitiveDepartments = new ArrayList<DepartmentRoundSensitive>();

	// Liste aller Unternehmen, die am Spiel teilnehmen
	private ArrayList<Company> listOfCompanys = new ArrayList<Company>();

	// Liste aller Unternehmen, die verloren haben
	private ArrayList<Company> listOfLosers = new ArrayList<Company>();

	// Rundennummer
	private int round = 1;

	public GameEngine() {

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

	public GameDataMessageToClient getInitialGameDataMessageToClient() {
		StorageToClient storage = new StorageToClient(
				Constant.Product.STORAGECOST_WAFER,
				Constant.Product.STORAGECOST_CASE,
				Constant.Product.STORAGECOST_PANEL, null);

		ArrayList<PossibleBenefit> possibleBenefits = new ArrayList<PossibleBenefit>();
		for (Benefit benefit : Benefit.getBookableBenefits()) {
			possibleBenefits.add(new PossibleBenefit(benefit.getName(), benefit
					.getCostsPerRound()));
		}
		int averageWage = 0;
		HumanResourcesToClient hr = new HumanResourcesToClient(null,
				possibleBenefits, null, averageWage, 0, 40, 0);
		GameDataMessageToClient initialMessage = new GameDataMessageToClient(
				"", null, null, storage, null, hr, null, null,
				Constant.BankAccount.START_CAPITAL,
				Constant.BankAccount.MAX_CREDIT);
		return initialMessage;
	}

	/**
	 * Startet die nächste Runde
	 * 
	 * @param gameDataList
	 *            Übergebene Eingabedaten der Spieler
	 * @throws Exception
	 */
	public ArrayList<GameDataMessageToClient> startNextRound(
			ArrayList<GameDataMessageFromClient> gameDataList) throws Exception {

		prepareAllDepartmentsForNewRound();
		parseClientData(gameDataList);

		for (Company company : listOfCompanys) {
			//Prüfen ob die Company noch beruecksichtigt wird
			if(listOfLosers.contains(company)){
				//scheinbar nicht
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

		round++; // Runde hochzaehlen

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
		for (DepartmentRoundSensitive d : listSensitiveDepartments) {
			d.prepareForNewRound(round);
		}
	}

	/**
	 * Für eine Abteilung hinzu, die zu Beginn der Runde einen
	 * Initialisierungsaufruf braucht
	 * 
	 * @param d
	 *            Abteilung, die der Aufrufliste hinzugefügt werden soll
	 */
	public void addSensitiveDepartment(DepartmentRoundSensitive d) {

		listSensitiveDepartments.add(d);

	}

	/**
	 * Fügt ein Unternehmen dem Spiel hinzu
	 * 
	 * @param c
	 *            Company, die hinzugefügt werden soll
	 */
	public void addCompany(Company c) {

		listOfCompanys.add(c);

	}

	/**
	 * Fuege einen Verlierer hinzu
	 * 
	 * @param c
	 *            die Firma die verloren hat
	 * @throws IOException 
	 */
	public void addCompanyLost(Company c) throws Exception {
		listOfLosers.add(c);
		
		CustomerMarket.getMarket().removeDistribution(c.getDistribution());
		SupplierMarket.getMarket().removePurchase(c.getPurchase());
		MarketData.getMarketData().removeHR(c.getHumanResources());

		if (listOfCompanys.size() < 1) {
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
