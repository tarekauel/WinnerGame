package Server;

import java.util.ArrayList;

import Constant.Constant;
import Message.GameDataMessageFromClient;
import Message.GameDataMessageFromClient.DistributionFromClient.OfferFromClient;
import Message.GameDataMessageFromClient.HumanResourcesFromClient.BenefitBookingFromClient;
import Message.GameDataMessageFromClient.ProductionFromClient.ProductionOrderFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient.AcceptedSupplierOfferFromClient;
import Message.GameDataMessageFromClient.PurchaseFromClient.RequestFromClient;
import Message.GameDataMessageToClient;
import Message.GameDataMessageToClient.DistributionToClient;
import Message.GameDataMessageToClient.DistributionToClient.OfferToClient;
import Message.GameDataMessageToClient.HumanResourcesToClient;
import Message.GameDataMessageToClient.HumanResourcesToClient.BenefitBookingToClient;
import Message.GameDataMessageToClient.HumanResourcesToClient.PossibleBenefit;
import Message.GameDataMessageToClient.MarketingToClient;
import Message.GameDataMessageToClient.MarketingToClient.MarketShareToClient;
import Message.GameDataMessageToClient.MarketingToClient.MotivationRoundToClient;
import Message.GameDataMessageToClient.MarketingToClient.RessourcePriceToClient;
import Message.GameDataMessageToClient.ProductionToClient;
import Message.GameDataMessageToClient.ProductionToClient.ProductionOrderToClient;
import Message.GameDataMessageToClient.PurchaseToClient;
import Message.GameDataMessageToClient.PurchaseToClient.RequestToClient;
import Message.GameDataMessageToClient.PurchaseToClient.RequestToClient.SupplierOfferToClient;
import Message.GameDataMessageToClient.ReportingToClient;
import Message.GameDataMessageToClient.ReportingToClient.FixCostToClient;
import Message.GameDataMessageToClient.ReportingToClient.MachineryToClient;
import Message.GameDataMessageToClient.StorageToClient;
import Message.GameDataMessageToClient.StorageToClient.StorageElementToClient;

public class GameDataTranslator {

	private static GameDataTranslator gameDataTranslator = null;

	/**
	 * Singleton Kostruktor
	 * 
	 * @return Singletonobjekt
	 */
	public static GameDataTranslator getGameDataTranslator() {
		if (gameDataTranslator == null) {
			gameDataTranslator = new GameDataTranslator();
		}
		return gameDataTranslator;
	}

	public void convertGameDataMessage2Objects(
			ArrayList<GameDataMessageFromClient> gameDataMessages)
			throws Exception {

		for (GameDataMessageFromClient gameDataMessage : gameDataMessages) {
			Company company = findCompanyOfPlayer(gameDataMessage
					.getPlayerName());
			// Hat die Company verloren?
			if (GameEngine.getGameEngine().getListOfLosers().contains(company)) {
				// scheinbar
				break;
			}
			handlePurchaseRequests(gameDataMessage.purchase.requests, company);
			handleAcceptedSupplierOffers(
					gameDataMessage.purchase.acceptedSupplierOffers, company);
			handleProductionOrders(gameDataMessage.production.orders, company);
			handleDistributionOffers(gameDataMessage.distribution.offers,
					company);
			handleWage(gameDataMessage.wage, company);
			handleMarketResearch(gameDataMessage.buyMarketResearch, company);

		}

	}

	private void handleMarketResearch(boolean buyMarketResearch, Company company)
			throws Exception {
		company.getMarketResearch().setIsBooked(buyMarketResearch);

	}

	/**
	 * Hier wird der neue Lohn für nächste Runde gesetzt.
	 * 
	 * @param wage
	 * @param company
	 * @throws Exception
	 */
	private void handleWage(int wage, Company company) throws Exception {
		HumanResources hr = company.getHumanResources();
		TWage wages = new TWage(wage,
				GameEngine.getGameEngine().getRound() + 1, company
						.getLocation().getWageLevel());
		hr.setWagePerRound(wages);

	}

	/**
	 * Erstellt Offers für die Distribution
	 * 
	 * @param offers
	 * @param company
	 * @throws Exception 
	 */
	private void handleDistributionOffers(ArrayList<OfferFromClient> offers,
			Company company) throws Exception {
		Distribution distribution = company.getDistribution();
		for (OfferFromClient offer : offers) {
			distribution.createOffer(offer.quality, offer.quantityToSell,
					offer.price);
		}
	}

	/**
	 * Sucht die passenden Resources im Lager und erstellt mit ihnen die
	 * ProductionOrders
	 * 
	 * @param orders
	 *            vom Client angelegte Orders
	 * @param company
	 * @throws Exception
	 */
	private void handleProductionOrders(
			ArrayList<ProductionOrderFromClient> orders, Company company)
			throws Exception {
		Storage storage = company.getStorage();
		for (ProductionOrderFromClient prodOrder : orders) {
			Resource wafer = null;
			Resource panelCase = null;
			for (Resource resource : storage.getAllResources()) {
				if (resource.getQuality() == prodOrder.qualityCase
						&& resource.getName().equals("Gehäuse")) {
					panelCase = resource;
				}
				if (resource.getQuality() == prodOrder.qualityWafer
						&& resource.getName().equals("Wafer")) {
					wafer = resource;
				}

			}// for
			company.getProduction().createProductionOrder(wafer, panelCase,
					prodOrder.quantity);
		}// for
	}

	/**
	 * nimmt die SupplierOffers an. vergleicht dabei die vom Client
	 * uebermittelten SupplierOffers mit denen auf dem Server. Akkzeptiert die
	 * auf dem Server liegende.
	 * 
	 * @param acceptedSupplierOffers
	 * @param company
	 * @throws Exception
	 */
	private void handleAcceptedSupplierOffers(
			ArrayList<AcceptedSupplierOfferFromClient> acceptedSupplierOffers,
			Company company) throws Exception {
		// geht alle acceptedSupOf des Clients durch
		for (AcceptedSupplierOfferFromClient acceptedSupOf : acceptedSupplierOffers) {
			// Sucht alle aktuellen Requests auf dem Server
			for (Server.Request request : company.getPurchase()
					.getListOfLastRoundRequests()) { // TODO so gehts nicht....
				// Sucht zum jeweiligen Request die 3 SupplierOffers auf dem
				// Server
				for (SupplierOffer supOf : request.getSupplierOffers()) {
					// sucht den Offer der client- und serverseitig
					// übereinstimmt
					if (supOf.equals(acceptedSupOf)) {
						// akzeptiert den serverseitigen
						company.getPurchase().acceptSupplierOffer(supOf,
								acceptedSupOf.quantity);
						break;
					}
				}
			}
		}

	}

	/**
	 * Liefert zu einem Spieler die Company-Referenz zurueck.
	 * 
	 * @param playerName
	 * @return Company
	 */
	private Company findCompanyOfPlayer(String playerName) {

		for (Company company : GameEngine.getGameEngine().getListOfCompanys()) {
			if (company.getName().equals(playerName)) {
				return company;
			}
		}
		throw new IllegalArgumentException("Der Playername ist invalid!");

	}

	/**
	 * Findet zu einer Company den Spielernamen
	 * 
	 * @param company
	 * @return
	 */
	/*
	 * private String findPlayerNameOfCompany(Company company) {
	 * 
	 * for (Player player : Server.Connection.Server.getServer()
	 * .getPlayerList()) { //Pruefung auf Identitaet if(player.getMyCompany() ==
	 * company){ return player.getName(); } } throw new
	 * IllegalArgumentException("Die Company ist ungültig!");
	 * 
	 * }
	 */

	/**
	 * Erstellt Requests für den Einkauf
	 * 
	 * @param requests
	 *            von Client
	 * @param company
	 * @throws Exception
	 */
	private void handlePurchaseRequests(ArrayList<RequestFromClient> requests,
			Company company) throws Exception {
		for (RequestFromClient request : requests) {
			company.getPurchase().createRequest(
					new Resource(request.quality, request.name, 0));
		}

	}

	/**
	 * Erstellt die GameMessages und liefert sie an die Clients
	 * 
	 * @throws Exception
	 */
	public ArrayList<GameDataMessageToClient> createGameDataMessages()
			throws Exception {
		ArrayList<GameDataMessageToClient> messges = new ArrayList<GameDataMessageToClient>();
		for (Company c : GameEngine.getGameEngine().getListOfCompanys()) {
			// Prüfen ob die Company noch beruecksichtigt wird
			if (GameEngine.getGameEngine().getListOfLosers().contains(c)) {
				// scheinbar nicht
				break;

			}
			messges.add(createGameDataMessageToClient(c));

		}
		return messges;
	}

	/**
	 * Sorgt für die Erstellung einer GamDataMessageToClient
	 * 
	 * @param company
	 * @return
	 * @throws Exception
	 */
	private GameDataMessageToClient createGameDataMessageToClient(
			Company company) throws Exception {
		String playerName = company.getName();

		// Abteilungen erstellen
		PurchaseToClient purchase = createPurchase(company);
		ProductionToClient production = createProduction(company);
		DistributionToClient distribution = createDistribution(company);
		HumanResourcesToClient humanResources = createHumanResources(company);
		MarketingToClient marketing = createMarketing(company);
		ReportingToClient reporting = createReporting(company);
		StorageToClient storage = createStorage(company);
		// Hauptdaten erstellen
		long cash = company.getBankAccount().getBankBalance();
		long maxCredit = Constant.BankAccount.MAX_CREDIT;

		// Message erstellen
		GameDataMessageToClient message = new GameDataMessageToClient(
				playerName, purchase, production, storage, distribution,
				humanResources, marketing, reporting, cash, maxCredit);
		return message;
	}

	private StorageToClient createStorage(Company company) {
		Storage serverStorage = company.getStorage();
		ArrayList<StorageElementToClient> storageElements = new ArrayList<StorageElementToClient>();
		for (StorageElement storageElement : serverStorage
				.getAllStorageElements()) {
			storageElements.add(new StorageElementToClient(storageElement
					.getProduct().getName(), storageElement.getProduct()
					.getQuality(), storageElement.getProduct().getCosts(),
					storageElement.getQuantity()));
		}

		StorageToClient storage = new StorageToClient(
				Constant.Product.STORAGECOST_WAFER,
				Constant.Product.STORAGECOST_CASE,
				Constant.Product.STORAGECOST_PANEL, storageElements);
		return storage;
	}

	private ReportingToClient createReporting(Company company) throws Exception {
		ArrayList<FixCostToClient> fixCosts = new ArrayList<FixCostToClient>();

		// getFixCostsOfAllDepartments
		fixCosts.add(new FixCostToClient("Verkauf", company.getDistribution()
				.getFixCosts()));
		fixCosts.add(new FixCostToClient("Personal", company
				.getHumanResources().getFixCosts()));
		fixCosts.add(new FixCostToClient("Marktforschung", company
				.getMarketResearch().getFixCosts()));
		fixCosts.add(new FixCostToClient("Produktion", company.getProduction()
				.getFixCosts()));
		fixCosts.add(new FixCostToClient("Einkauf", company.getPurchase()
				.getFixCosts()));
		fixCosts.add(new FixCostToClient("Lager", company.getStorage()
				.getFixCosts()));

		MachineryToClient machinery = new MachineryToClient(company
				.getProduction().getMachine().getLevel(), company
				.getProduction().getMachine().getMaxCapacity(), company
				.getProduction().getMachine().getCurrentUsage()
				.getPercentOfUsage(), company.getProduction().getMachine()
				.getLastUsage().getPercentOfUsage());
		// TODO: gefaked, cashValue und sellings sind 0
		ReportingToClient reporting = new ReportingToClient(fixCosts,
				machinery, null, null);

		return reporting;
	}

	/**
	 * Erstellt die Marketingdaten für den Client
	 * 
	 * @param company
	 * @return
	 */
	private MarketingToClient createMarketing(Company company) {
		// ist es ueberhaupt gebucht/gekauft
		boolean isBooked = company.getMarketResearch().getIsBooked();

		// getPeaks
		int peakAMarket = CustomerMarket.getMarket().getAMarketPeak();
		int peakCMarket = CustomerMarket.getMarket().getCMarketPeak();

		// getMarkewtShares
		ArrayList<MarketShareToClient> marketShares = new ArrayList<MarketShareToClient>();

		for (TMarketShare marketShare : CustomerMarket.getMarket()
				.getMarketShares()) {
			marketShares.add(new MarketShareToClient(marketShare
					.getMarketShare(), company.getName()));
		}

		// getWaferPrices
		ArrayList<RessourcePriceToClient> waferPrices = new ArrayList<RessourcePriceToClient>();
		java.util.Iterator<TResourcePrice> waferPriceIterator = SupplierMarket
				.getMarket().getWaferPricelist().iterator();
		while (waferPriceIterator.hasNext()) {
			TResourcePrice resourcePrice = waferPriceIterator.next();
			waferPrices.add(new RessourcePriceToClient(resourcePrice
					.getQuality(), resourcePrice.getPrice()));
		}

		// getCasePrices
		ArrayList<RessourcePriceToClient> casePrices = new ArrayList<RessourcePriceToClient>();
		java.util.Iterator<TResourcePrice> casePriceIterator = SupplierMarket
				.getMarket().getCasePricelist().iterator();
		while (casePriceIterator.hasNext()) {
			TResourcePrice resourcePrice = casePriceIterator.next();
			casePrices.add(new RessourcePriceToClient(resourcePrice
					.getQuality(), resourcePrice.getPrice()));
		}

		MarketingToClient marketing = new MarketingToClient(isBooked,
				peakAMarket, peakCMarket, marketShares, waferPrices, casePrices);
		return marketing;
	}

	/**
	 * Erstellt die HR-Daten für den Client
	 * 
	 * @param company
	 * @return
	 * @throws Exception
	 */
	private HumanResourcesToClient createHumanResources(Company company)
			throws Exception {
		HumanResources serverHR = company.getHumanResources();
		// Create Benefits
		ArrayList<BenefitBookingToClient> benefits = new ArrayList<BenefitBookingToClient>();
		for (BenefitBooking benefit : serverHR.getBenefitBooking()) {

			benefits.add(new BenefitBookingToClient(benefit.getBenefit()
					.getName(), benefit.getRemainingRounds(), benefit
					.getBenefit().getCostsPerRound()));
		}
		// TODO: UMRECHUNG pruefen
		ArrayList<PossibleBenefit> possibleBenefits = new ArrayList<PossibleBenefit>();
		for (Benefit benefit : Benefit.getBookableBenefits()) {
			possibleBenefits.add(new PossibleBenefit(benefit.getName(), benefit
					.getCostsPerRound()));
		}
		HumanResourcesToClient hr = new HumanResourcesToClient(benefits,
				possibleBenefits, serverHR.getHistoryOfMotivation(), MarketData
						.getMarketData().getAvereageWage().getAmount()
						* (company.getLocation().getWageLevel() / 10000),
				serverHR.getWagesPerHour().getAmount(),
				serverHR.getCountEmployees(), serverHR.getWagesSum());

		return hr;
	}

	/**
	 * Erstellt die Verkaufsdaten für den Cliet.
	 * 
	 * @param company
	 * @return
	 */
	private DistributionToClient createDistribution(Company company) {
		// Create Offers
		ArrayList<OfferToClient> offers = new ArrayList<OfferToClient>();
		for (Offer offer : company.getDistribution().getListOfOffers()) {
			offers.add(new OfferToClient(offer.getStorageElement().getProduct()
					.getQuality(), offer.getQuantityToSell(), offer
					.getQuantitySold(), offer.getPrice(), offer.getRound(),
					offer.getStorageElement().getProduct().getCosts()));
		}
		DistributionToClient distribution = new DistributionToClient(offers, Constant.Distribution.DISTRIBUTION_OFFER_COSTS_PER_PANEL);
		return distribution;
	}

	/**
	 * Erstellt die Produktionsdaten für den Client
	 * 
	 * @param company
	 * @return
	 */
	private ProductionToClient createProduction(Company company) {
		// Create Productionorders
		ArrayList<ProductionOrderToClient> orders = new ArrayList<ProductionOrderToClient>();
		for (ProductionOrder productionOrder : company.getProduction()
				.getListOfAllProductionOrders()) {
			orders.add(new ProductionOrderToClient(productionOrder.getWafer()
					.getQuality(), productionOrder.getCase().getQuality(),
					productionOrder.getPanel().getQuality(), productionOrder
							.getRequested(), productionOrder.getProduced(),
					productionOrder.getPanel().getCosts()));
		}
		ProductionToClient production = new ProductionToClient(orders);
		return production;
	}

	/**
	 * Erstellt die Einkaufsdaten für den Client
	 * 
	 * @param company
	 * @return
	 */
	private PurchaseToClient createPurchase(Company company) {
		// Create Requests
		ArrayList<RequestToClient> requests = new ArrayList<PurchaseToClient.RequestToClient>();
		for (Request request : company.getPurchase().getListOfRequest()) {
			// Create SupplierOffers
			ArrayList<SupplierOfferToClient> supplierOffers = new ArrayList<SupplierOfferToClient>();
			for (SupplierOffer supplierOffer : request.getSupplierOffers()) {

				supplierOffers
						.add(new GameDataMessageToClient.PurchaseToClient.RequestToClient.SupplierOfferToClient(
								supplierOffer.getResource().getName(),
								supplierOffer.getResource().getQuality(),
								supplierOffer.getOrderedQuantity(),
								supplierOffer.getResource().getCosts(),
								supplierOffer.getRound()));
			}
			requests.add(new RequestToClient(request.getRequestedResource()
					.getName(), request.getRequestedResource().getQuality(),
					supplierOffers));
		}
		return new PurchaseToClient(requests);
	}

}
