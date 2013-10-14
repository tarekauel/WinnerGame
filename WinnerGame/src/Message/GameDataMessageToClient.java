package Message;

import java.io.Serializable;
import java.util.ArrayList;

import Server.Company;
import Server.GameEngine;
import Server.TMotivation;

public class GameDataMessageToClient extends GameDataMessage implements
		Serializable {

	public GameDataMessageToClient(String playerName,
			PurchaseToClient purchase, ProductionToClient production,
			StorageToClient storage, DistributionToClient distribution,
			HumanResourcesToClient humanResources, MarketingToClient marketing,
			ReportingToClient reporting, long cash, long maxCredit) {
		super(playerName);
		this.purchase = purchase;
		this.production = production;
		this.distribution = distribution;
		this.marketing = marketing;
		this.humanResources = humanResources;
		this.reporting = reporting;
		this.cash = cash;
		this.maxCredit = maxCredit;
		this.storage = storage;
		this.round = GameEngine.getGameEngine().getRound();
			
		//Baue die Loser Liste:
		for(Company c:GameEngine.getGameEngine().getListOfLosers()){
			listOfLosers.add(new Loser(c.getName(),this.round));
		}
		
		
	}

	public final PurchaseToClient purchase;
	public final ProductionToClient production;
	public final StorageToClient storage;
	public final DistributionToClient distribution;
	public final MarketingToClient marketing;
	public final ReportingToClient reporting;
	public final HumanResourcesToClient humanResources;
	public final long cash;
	public final long maxCredit;
	public final int round;
	public final ArrayList<Loser> listOfLosers =  new ArrayList<Loser>();

	public static class Loser implements Serializable{
		public final String name;
		public final int round;
		public Loser(String name, int round) {
			this.name = name;
			this.round = round;
			
		}
	}
	
	public static class PurchaseToClient implements Serializable {
		public PurchaseToClient(ArrayList<RequestToClient> requests) {
			this.requests = requests;
		}

		public final ArrayList<RequestToClient> requests; // ewige Liste

		public static class RequestToClient implements Serializable {
			public RequestToClient(String name, int quality,
					ArrayList<SupplierOfferToClient> supplierOffers) {
				this.name = name;
				this.quality = quality;
				this.supplierOffers = supplierOffers;

			}

			public final ArrayList<SupplierOfferToClient> supplierOffers;
			public final String name;
			public final int quality;

			public static class SupplierOfferToClient implements Serializable {
				public SupplierOfferToClient(String name, int quality,
						int orderedQuantity, int price, int round) {
					this.name = name;
					this.quality = quality;
					this.price = price;
					this.orderedQuantity = orderedQuantity;
					this.round = round;
				}

				public final String name;
				public final int quality;
				public final int orderedQuantity;
				public final int price;
				public final int round;
			}
		}

	}

	public static class ProductionToClient implements Serializable {

		public ProductionToClient(ArrayList<ProductionOrderToClient> orders) {
			this.orders = orders;
		}

		public static class ProductionOrderToClient implements Serializable {
			public ProductionOrderToClient(int qualityWafer, int qualityCase,
					int qualityPanel, int quantity, int producedQuantity,
					int costs) {

				this.qualityWafer = qualityWafer;
				this.qualityCase = qualityCase;
				this.qualityPanel = qualityPanel;
				this.quantity = quantity;
				this.producedQuantity = producedQuantity;
				this.costs = costs;

			}

			public final int qualityWafer;
			public final int qualityCase;
			public final int quantity;
			public final int qualityPanel;
			public final int producedQuantity;
			public final int costs;

		}

		public final ArrayList<ProductionOrderToClient> orders; // Ewige Liste

	}

	public static class StorageToClient implements Serializable {

		public StorageToClient(int storageCostsWafer, int storageCostsCase,
				int storageCostsPanel,
				ArrayList<StorageElementToClient> storageElements) {

			this.storageCostsWafer = storageCostsWafer;
			this.storageCostsCase = storageCostsCase;
			this.storageCostsPanel = storageCostsPanel;
			this.storageElements = storageElements;
		}

		public static class StorageElementToClient implements Serializable {

			public StorageElementToClient(String type, int quality, int costs,
					int quantity) {
				this.type = type;
				this.quality = quality;
				this.quantity = quantity;
				this.costs = costs;
			}

			public final String type;
			public final int quality;
			public final int quantity;
			public final int costs;
			public String toString(){
				return "Qualität: "+quality+" - Menge: "+quantity;
			}
		}

		public final int storageCostsWafer;
		public final int storageCostsCase;
		public final int storageCostsPanel;

		public final ArrayList<StorageElementToClient> storageElements; // Liste
																		// StoageElements

	}

	public static class DistributionToClient implements Serializable {

		public DistributionToClient(ArrayList<OfferToClient> offers, int costsPerOffer) {
			this.offers = offers;
			this.costsPerOffer = costsPerOffer;
		}

		public static class OfferToClient implements Serializable {

			public OfferToClient(int quality, int quantityToSell,
					int quantitySold, int price, int round, int costs) {
				this.quality = quality;
				this.quantityToSell = quantityToSell;
				this.quantitySold = quantitySold;
				this.price = price;
				this.round = round;
				this.costs = costs;
			}

			

			public final int quality;
			public final int quantityToSell;
			public final int quantitySold;
			public final int price;
			public final int round;
			public final int costs;
		}

		public final ArrayList<OfferToClient> offers;
		public final int costsPerOffer;
	}

	public static class HumanResourcesToClient implements Serializable {

		

		public HumanResourcesToClient(
				ArrayList<BenefitBookingToClient> benefits,
				ArrayList<PossibleBenefit> possibleBenefits,
				ArrayList<TMotivation> historyMotivation, int averageWage,
				int myWage, int countEmployees, int wageCosts) {
			super();
			this.benefits = benefits;
			this.possibleBenefits = possibleBenefits;
			this.historyMotivation = historyMotivation;
			this.averageWage = averageWage;
			this.myWage = myWage;
			this.countEmployees = countEmployees;
			this.wageCosts = wageCosts;
		}

		public final ArrayList<BenefitBookingToClient> benefits;
		public final ArrayList<PossibleBenefit> possibleBenefits;
		public final ArrayList<TMotivation> historyMotivation;

		public final int averageWage;
		public final int myWage;
		public final int countEmployees;
		public final int wageCosts;
		

		public static class BenefitBookingToClient implements Serializable {

			
			public BenefitBookingToClient(String name, int remainingRounds,
					int costsPerRound) {
				
				this.name = name;
				this.remainingRounds = remainingRounds;
				this.costsPerRound = costsPerRound;
			}
			public final String name;
			public final int remainingRounds;
			public final int costsPerRound;
		}
		
		public static class PossibleBenefit implements Serializable {
			
			public PossibleBenefit(String name, int costsPerRound) {
				super();
				this.name = name;
				this.costsPerRound = costsPerRound;
			}
			public final String name;
			public final int costsPerRound;
		}

	}

	public static class MarketingToClient implements Serializable {

		public MarketingToClient(boolean isBooked,int peakAMarket, int peakCMarket,
				ArrayList<MarketShareToClient> marketShares,
				ArrayList<RessourcePriceToClient> waferPrice,
				ArrayList<RessourcePriceToClient> casePrice) {
			
			this.isBooked = isBooked;
			this.peakAMarket = peakAMarket;
			this.peakCMarket = peakCMarket;
			this.marketShares = marketShares;
			this.waferPrice = waferPrice;
			this.casePrice = casePrice;
		}

		public final boolean isBooked;
		public final int peakAMarket;
		public final int peakCMarket;
		public final ArrayList<MarketShareToClient> marketShares;
		public final ArrayList<RessourcePriceToClient> waferPrice;
		public final ArrayList<RessourcePriceToClient> casePrice;
		// For HR

		public static class MotivationRoundToClient implements Serializable {
			int round;
			int motivation;
		}

		public static class RessourcePriceToClient implements Serializable {

			public RessourcePriceToClient(int quality, int price) {

				this.quality = quality;
				this.price = price;
			}

			public final int quality;
			public final int price;

		}

		public static class MarketShareToClient implements Serializable {

			public MarketShareToClient(int share, String name) {
				super();
				this.share = share;
				this.name = name;
			}

			public final int share;
			public final String name;

		}

	}

	public static class ReportingToClient implements Serializable {

		public ReportingToClient(ArrayList<FixCostToClient> fixCosts,
				MachineryToClient machinery,
				ArrayList<SellsToClient> sellsOfRounds,
				ArrayList<CashValueOfRoundToClient> cashValues) {

			this.fixCosts = fixCosts;
			this.machinery = machinery;
			this.sellsOfRounds = sellsOfRounds;
			this.cashValues = cashValues;
		}

		public final ArrayList<FixCostToClient> fixCosts;
		public final MachineryToClient machinery;
		public final ArrayList<SellsToClient> sellsOfRounds;
		public final ArrayList<CashValueOfRoundToClient> cashValues;

		public static class SellsToClient implements Serializable {

			public SellsToClient(int round, ArrayList<Integer> qualities) {

				this.round = round;
				this.qualities = qualities;
			}

			public final int round;
			public final ArrayList<Integer> qualities;

		}

		public static class FixCostToClient implements Serializable {

			public FixCostToClient(String nameOfDepartment, int costs) {

				this.nameOfDepartment = nameOfDepartment;
				this.costs = costs;
			}

			public final String nameOfDepartment;
			public final int costs;
		}

		public static class MachineryToClient implements Serializable {

			public MachineryToClient(int level, int maxCapacity,
					int averageUsage, int usageLastRound) {

				this.level = level;
				this.maxCapacity = maxCapacity;
				this.averageUsage = averageUsage;
				this.usageLastRound = usageLastRound;
			}

			public final int level;
			public final int maxCapacity;
			public final int averageUsage;
			public final int usageLastRound;
		}

		public static class CashValueOfRoundToClient implements Serializable {

			public CashValueOfRoundToClient(int round, int costs) {

				this.round = round;
				this.costs = costs;
			}

			public final int round;
			public final int costs;
		}

	}
}
