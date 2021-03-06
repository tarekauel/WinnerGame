package client.ui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import client.ui.ClientGameUIModel.Benefit;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kigegner.ClientToServerMessageCreator;
import kigegner.KITarek;
import message.GameDataMessageFromClient.PurchaseFromClient.RequestFromClient;
import message.GameDataMessageFromClient;
import message.GameDataMessageToClient;
import message.GameDataMessageToClient.DistributionToClient;
import message.GameDataMessageToClient.DistributionToClient.OfferToClient;
import message.GameDataMessageToClient.HumanResourcesToClient;
import message.GameDataMessageToClient.HumanResourcesToClient.BenefitBookingToClient;
import message.GameDataMessageToClient.HumanResourcesToClient.PossibleBenefit;
import message.GameDataMessageToClient.MarketingToClient;
import message.GameDataMessageToClient.MarketingToClient.MarketShareToClient;
import message.GameDataMessageToClient.MarketingToClient.RessourcePriceToClient;
import message.GameDataMessageToClient.ProductionToClient;
import message.GameDataMessageToClient.ProductionToClient.ProductionOrderToClient;
import message.GameDataMessageToClient.PurchaseToClient;
import message.GameDataMessageToClient.PurchaseToClient.RequestToClient;
import message.GameDataMessageToClient.PurchaseToClient.RequestToClient.SupplierOfferToClient;
import message.GameDataMessageToClient.StorageToClient;
import message.GameDataMessageToClient.StorageToClient.StorageElementToClient;
import server.TMotivation;

public class ClientGameUIModel {
	
	/**
	 * Problem:
	 * Es kann aufgrund des statischen Attributs "round" das UI nur einmal auf einem System verwendet werden.
	 * 
	 * Warum ist die Problemlösung "schwierig"?:
	 * Bei ändern von "round" in Objektattribut wäre ein Zugriff aus den inneren Klassen nur via "ClientGameUIModel.this.getRound()" möglich,
	 * dies funktioniert allerdings nicht, da die inneren Klassen static sind, was für die Verwendung im Controller wiederum notwendig ist.
	 * 
	 * Wann wird das zu einem Problem?:
	 * Wenn Tielsch mehrere Clients auf einem Rechner starten und testen möchte. (sehr wahrscheinlich)
	 * 
	 * Wie können wir das Problem OHNE großen Arbeitsaufwand lösen?
	 * Keine Ahnung.
	 * 
	 */

	/**
	 * General
	 */

	// private GameDataMessageToClient in = KITarek.reply;
	private GameDataMessageToClient in;
	private GameDataMessageFromClient out;
	private ClientToServerMessageCreator messCreator;
	private ClientGameUIModel clientGameUiModel;

	private static int round;
	private int maxRounds = 20;
	private final ObservableList<Request> purchaseRequestTableData = FXCollections.observableArrayList();
	private final ObservableList<SupplierOffer> purchaseOffersTableData = FXCollections.observableArrayList();
	private final ObservableList<ProductionOrder> productionOrdersTableData = FXCollections.observableArrayList();
	private final ObservableList<StoragePosition> storagePositionsTableData = FXCollections.observableArrayList();
	private final ObservableList<Offer> offerTableData = FXCollections.observableArrayList();
	private final ObservableList<BenefitBooking> benefitBookingTableData = FXCollections.observableArrayList();

	private final ObservableList<Benefit> benfitBoxData = FXCollections.observableArrayList();

	private ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();

	private static NumberFormat nFormatterCurrency = NumberFormat.getCurrencyInstance();

	private static NumberFormat nFormatter = NumberFormat.getInstance();

	private final ArrayList<HashMap<String, Double>> salesChartData = new ArrayList<HashMap<String, Double>>();

	private final ArrayList<HashMap<String, Double>> motivationChartData = new ArrayList<HashMap<String, Double>>();

	private final ArrayList<HashMap<String, Double>> waferPriceListChartData = new ArrayList<HashMap<String, Double>>();

	private final ArrayList<HashMap<String, Double>> casePriceListChartData = new ArrayList<HashMap<String, Double>>();

	private final HashMap<String, Double> marketShareChartData = new HashMap<String, Double>();

	public ClientGameUIModel() {
		this.clientGameUiModel = this;
	}

	public ClientGameUIModel getClientGameUiModel() {
		return clientGameUiModel;
	}

	public GameDataMessageToClient getIn() {
		return in;
	}

	public void setIn(GameDataMessageToClient in) {
		this.in = in;
	}

	public ClientToServerMessageCreator getMessCreator() {
		return messCreator;
	}

	public GameDataMessageFromClient getOut() {
		return out;
	}

	public void setOut(GameDataMessageFromClient out) {
		this.out = out;
	}

	public NumberFormat getnFormatterCurrency() {
		return nFormatterCurrency;
	}

	public NumberFormat getnFormatter() {
		return nFormatter;
	}

	public static int getRound() {
		return round;
	}

	public static void setRound(int round) {
		ClientGameUIModel.round = round;
	}

	public int getMaxRounds() {
		return maxRounds;
	}

	public void setMaxRounds(int maxRounds) {
		this.maxRounds = maxRounds;
	}

	public ArrayList<RequestFromClient> getRequests() {
		return requests;
	}

	public void setRequests(ArrayList<RequestFromClient> requests) {
		this.requests = requests;
	}

	public ObservableList<Request> getPurchaseRequestTableData() {
		return purchaseRequestTableData;
	}

	public ObservableList<SupplierOffer> getPurchaseOffersTableData() {
		return purchaseOffersTableData;
	}

	public ObservableList<ProductionOrder> getProductionOrdersTableData() {
		return productionOrdersTableData;
	}

	public ObservableList<StoragePosition> getStoragePositionsTableData() {
		return storagePositionsTableData;
	}

	public ObservableList<Offer> getOfferTableData() {
		return offerTableData;
	}

	public ObservableList<BenefitBooking> getBenefitBookingTableData() {
		return benefitBookingTableData;
	}

	public ObservableList<Benefit> getBenefitBoxData() {
		return benfitBoxData;
	}

	public ArrayList<HashMap<String, Double>> getSalesChartData() {
		return salesChartData;
	}

	public ArrayList<HashMap<String, Double>> getMotivationChartData() {
		return motivationChartData;
	}

	public ArrayList<HashMap<String, Double>> getWaferPriceListChartData() {
		return waferPriceListChartData;
	}

	public ArrayList<HashMap<String, Double>> getCasePriceListChartData() {
		return casePriceListChartData;
	}

	public HashMap<String, Double> getMarketShareChartData() {
		return marketShareChartData;
	}

	/**
	 * Parsing der GameDataMessageToClient
	 * 
	 * @param in
	 *            Message, die geparsed werden soll
	 */

	public void setupMessageCreator() {
		// messCreator = new ClientToServerMessageCreator(in.getPlayerName());
		messCreator = new ClientToServerMessageCreator(in.getPlayerName());
	}

	public void parseAnswerFromServer() {

		//ClientGameUIModel.setRound(in.round);
		round = in.round;

		purchaseRequestTableData.clear();
		// purchaseOffersTableData.clear();
		productionOrdersTableData.clear();
		storagePositionsTableData.clear();
		offerTableData.clear();
		benefitBookingTableData.clear();
		benfitBoxData.clear();
		requests.clear();
		salesChartData.clear();
		motivationChartData.clear();
		waferPriceListChartData.clear();
		casePriceListChartData.clear();
		marketShareChartData.clear();

		if (in.storage != null) {
			parseStorage(in.storage);
		}

		if (in.purchase != null) {
			parsePurchase(in.purchase);
		}

		if (in.production != null) {
			parseProduction(in.production);
		}

		if (in.distribution != null) {
			parseDistribution(in.distribution);
		}

		if (in.humanResources != null) {
			parseHumanResources(in.humanResources);
		}

		if (in.marketing != null) {
			parseMarketing(in.marketing);
		}

	}

	private void parsePurchase(PurchaseToClient in) {

		if (in.requests != null) {

			for (int i = 0; i < in.requests.size(); i++) {

				RequestToClient req = in.requests.get(i);
				Request request = new Request(req, i + 1);
				purchaseRequestTableData.add(request);

			}

		}

	}

	private void parseProduction(ProductionToClient in) {

		if (in.orders != null) {

			for (int i = 0; i < in.orders.size(); i++) {

				ProductionOrderToClient pOrder = in.orders.get(i);
				ProductionOrder prodOrder = new ProductionOrder(pOrder, i + 1);
				productionOrdersTableData.add(prodOrder);

			}

		}

	}

	private void parseStorage(StorageToClient in) {

		if (in.storageElements != null) {

			for (int i = 0; i < in.storageElements.size(); i++) {

				// System.out.println(in.storageElements.size());
				StorageElementToClient stoElement = in.storageElements.get(i);
				StoragePosition stoPos = new StoragePosition(stoElement, i + 1);
				storagePositionsTableData.add(stoPos);

			}

		}

	}

	private void parseDistribution(DistributionToClient in) {

		if (in.offers != null) {

			for (int i = 0; i < in.offers.size(); i++) {

				// System.out.println(in.storageElements.size());
				OfferToClient o = in.offers.get(i);
				Offer offer = new Offer(o, i + 1);
				offerTableData.add(offer);

			}

			for (int i = 0; i < 5; i++) {
				HashMap<String, Double> map = new HashMap<String, Double>();
				salesChartData.add(map);
				int round = ClientGameUIModel.getRound() - (5 - i);
				if (round <= 0)
					continue;

				for (OfferToClient offer : in.offers) {
					if (offer.round == round) {
						Double oldvalue = (map.get(offer.quality) == null) ? 0L : map.get(offer.quality);
						map.put(offer.quality + "", oldvalue + (offer.price * offer.quantitySold / 100.0));
					}
				}
			}

		}

	}

	private void parseHumanResources(HumanResourcesToClient in) {

		if (in.benefits != null) {
			
			for (int i = 0; i < in.benefits.size(); i++) {
				
				BenefitBookingToClient b = in.benefits.get(i);
				BenefitBooking benefit = new BenefitBooking(i+1, b);
				benefitBookingTableData.add(benefit);

			}

		}

		if (in.possibleBenefits != null) {

			for (PossibleBenefit b : in.possibleBenefits) {
				benfitBoxData.add(new Benefit(b));
			}

		}

		if (in.historyMotivation != null) {

			for (TMotivation m : in.historyMotivation) {
				HashMap<String, Double> map = new HashMap<String, Double>();
				motivationChartData.add(map);
				map.put("Motivation", m.getMotivation() / 10000.0);
			}

		}

	}

	private void parseMarketing(MarketingToClient in) {

		if (in.waferPrice != null) {
			for (RessourcePriceToClient p : in.waferPrice) {
				HashMap<String, Double> map = new HashMap<String, Double>();
				waferPriceListChartData.add(map);
				map.put("Wafer", p.price / 100.0);
			}
		}
		if (in.casePrice != null) {
			for (RessourcePriceToClient p : in.casePrice) {
				HashMap<String, Double> map = new HashMap<String, Double>();
				casePriceListChartData.add(map);
				map.put("Case", p.price / 100.0);
			}
		}
		if (in.marketShares != null) {
			for (MarketShareToClient m : in.marketShares) {
				marketShareChartData.put(m.name, m.share / 10000.0);
			}
		}

	}

	/**
	 * Purchase
	 */

	public static class Request {

		private final SimpleStringProperty name;
		private final SimpleStringProperty quality;
		private final SimpleStringProperty status;
		private final SimpleIntegerProperty id;
		private final ArrayList<SupplierOffer> supplierOfferList;
		private static int lastId = 0;

		public Request(RequestToClient req, int id) {
			this(req.name, req.quality + "", null, id, req.supplierOffers);
		}

		public Request(String name, String quality) {
			this(name, quality, "Neu", lastId + 1, null);
		}

		private String getStatus(ArrayList<SupplierOfferToClient> offerList) {
			String status = "";
			if (offerList.size() == 0) {
				status = "Neu";
			} else {
				if (offerList.get(0).round == ClientGameUIModel.round - 1) {
					status = "Offen";
				} else {
					boolean accepted = false;
					for (SupplierOfferToClient offer : offerList) {
						if (offer.orderedQuantity > 0) {
							accepted = true;
						}
					}
					if (accepted) {
						status = "Angenommen";
					} else {
						status = "Abgelaufen";
					}
				}
			}
			return status;
		}

		private Request(String name, String quality, String status, int id, ArrayList<SupplierOfferToClient> offers) {
			if (status == null) {
				status = getStatus(offers);
			}
			this.name = new SimpleStringProperty(name);
			this.quality = new SimpleStringProperty(quality);
			this.id = new SimpleIntegerProperty(id);
			this.status = new SimpleStringProperty(status);
			supplierOfferList = new ArrayList<SupplierOffer>();
			if (offers != null) {
				int i = 0;
				for (SupplierOfferToClient o : offers) {
					supplierOfferList.add(new SupplierOffer(o, ++i));
				}
			}
			lastId = id;
		}

		public String getName() {
			return name.get();
		}

		public String getQuality() {
			return quality.get();
		}

		public String getStatus() {
			return status.get();
		}

		public Integer getId() {
			return id.get();
		}

		public ArrayList<SupplierOffer> getOffer() {
			return supplierOfferList;
		}
	}

	public void addRequest(RequestFromClient r) {
		requests.add(r);
	}

	public static class SupplierOffer {

		private final SimpleStringProperty name;
		private final SimpleStringProperty quality;
		private SimpleStringProperty quantity;
		private final SimpleStringProperty price;
		private final SimpleIntegerProperty id;
		private final int round;
		private static int lastId = 0;

		public SupplierOffer(SupplierOfferToClient offer, int id) {
			this(offer.name, offer.quality + "", offer.orderedQuantity + "", offer.price + "", id, offer.round);
		}

		private SupplierOffer(String name, String quality, String quantity, String price, int id, int round) {
			this.name = new SimpleStringProperty(name);
			this.quality = new SimpleStringProperty(quality);
			// Falls Quantity 0 ist soll nichts erscheinen!
			quantity = (quantity.equals("0")) ? "" : ClientGameUIModel.nFormatter.format(Integer.parseInt(quantity));
			this.quantity = new SimpleStringProperty(quantity);
			this.id = new SimpleIntegerProperty(id);

			// Währungsformatierung
			long priceTmp = Long.parseLong(price);
			String priceFormatted = nFormatterCurrency.format(priceTmp / 100.0);
			this.price = new SimpleStringProperty(priceFormatted);

			this.round = round;
			lastId = id;
		}

		public String getName() {
			return name.get();
		}

		public String getQuality() {
			return quality.get();
		}

		public String getQuantity() {
			return quantity.get();
		}

		public String getPrice() {
			return price.get();
		}

		public Integer getId() {
			return id.get();
		}

		public int getRound() {
			return round;
		}

		public void setQuantity(String quantity) {
			this.quantity.set(quantity);
		}

	}

	/**
	 * Production
	 */

	public static class ProductionOrder {

		private final SimpleIntegerProperty id;
		private final SimpleStringProperty qualityWafer;
		private final SimpleStringProperty qualityCase;
		private final SimpleStringProperty targetQuantity;
		private final SimpleStringProperty qualityPanel;
		private final SimpleStringProperty actualQuantity;
		private final SimpleStringProperty costsPerUnit;
		private static int lastId = 0;

		public ProductionOrder(ProductionOrderToClient prodOrder, int id) {
			this(id, prodOrder.qualityWafer + "", prodOrder.qualityCase + "", prodOrder.quantity + "", prodOrder.qualityPanel + "",
					prodOrder.producedQuantity + "", prodOrder.costs + "");
		}

		public ProductionOrder(String qualityWafer, String qualityCase, String targetQuantity) {
			this(lastId + 1, qualityWafer, qualityCase, targetQuantity, "", "", "?");
		}

		private ProductionOrder(int id, String qualityWafer, String qualityCase, String targetQuantity, String qualityPanel, String actualQuantity,
				String costsPerUnit) {
			this.id = new SimpleIntegerProperty(id);
			this.qualityWafer = new SimpleStringProperty(qualityWafer);
			this.qualityCase = new SimpleStringProperty(qualityCase);
			this.targetQuantity = new SimpleStringProperty(ClientGameUIModel.nFormatter.format(Integer.parseInt(targetQuantity)));
			this.qualityPanel = new SimpleStringProperty(qualityPanel);
			this.actualQuantity = new SimpleStringProperty(actualQuantity);
			this.costsPerUnit = new SimpleStringProperty(costsPerUnit);
			lastId = id;
		}

		public Integer getId() {
			return id.get();
		}

		public String getQualityWafer() {
			return qualityWafer.get();
		}

		public String getQualityCase() {
			return qualityCase.get();
		}

		public String getTargetQuantity() {
			return targetQuantity.get();
		}

		public String getQualityPanel() {
			return qualityPanel.get();
		}

		public String getActualQuantity() {
			return actualQuantity.get();
		}

		public String getCostsPerUnit() {
			return costsPerUnit.get();
		}

	}

	public class StoragePosition {

		private final SimpleIntegerProperty id;
		private final SimpleStringProperty ressource;
		private final SimpleStringProperty quality;
		private final SimpleStringProperty quantity;
		private final SimpleStringProperty costs;

		public StoragePosition(int id, String ressource, String quality, String quantity, String costs) {
			this.id = new SimpleIntegerProperty(id);
			this.ressource = new SimpleStringProperty(ressource);
			this.quality = new SimpleStringProperty(quality);
			this.quantity = new SimpleStringProperty(ClientGameUIModel.nFormatter.format(Integer.parseInt(quantity)));
			this.costs = new SimpleStringProperty(ClientGameUIModel.nFormatterCurrency.format(Integer.parseInt(costs) / 100));

		}

		public StoragePosition(StorageElementToClient stoElement, int id) {

			this(id, stoElement.type, stoElement.quality + "", stoElement.quantity + "", stoElement.costs + "");

		}

		public String toString() {
			return "Qualität: " + this.getQuality() + " - Menge: " + this.getQuantity();
		}

		public Integer getId() {
			return id.get();
		}

		public String getRessource() {
			return ressource.get();
		}

		public String getQuality() {
			return quality.get();
		}

		public String getQuantity() {
			return quantity.get();
		}

		public String getCosts() {
			return costs.get();
		}

	}

	/**
	 * Sales
	 */

	public static class Offer {

		private final SimpleIntegerProperty id;
		private final SimpleStringProperty product;
		private final SimpleStringProperty quality;
		private final SimpleStringProperty price;
		private final SimpleStringProperty quantity;
		private final SimpleStringProperty soldQuantity;
		private final SimpleStringProperty profit;
		private final SimpleStringProperty costs;

		private static int lastId = 0;

		public Offer(int id, String quality, String quantity, String soldQuantity, String costs, String price) {
			this.id = new SimpleIntegerProperty(id);
			this.product = new SimpleStringProperty("Panel");
			this.price = new SimpleStringProperty(ClientGameUIModel.nFormatter.format(Integer.parseInt(price) / 100.0));
			this.quality = new SimpleStringProperty(quality + "");
			this.quantity = new SimpleStringProperty(ClientGameUIModel.nFormatter.format(Integer.parseInt(quantity)));
			if (soldQuantity != null) {
				this.soldQuantity = new SimpleStringProperty(ClientGameUIModel.nFormatter.format(Integer.parseInt(soldQuantity)));
			} else {
				this.soldQuantity = new SimpleStringProperty("");
			}
			// Währungsformatierung
			if (costs != null) {
				long costsTmp = Long.parseLong(costs);
				String costsFormatted = nFormatterCurrency.format(costsTmp / 100.0);
				this.costs = new SimpleStringProperty(costsFormatted);
				double profit = Integer.parseInt(soldQuantity) * Integer.parseInt(price) / 100.0 - costsTmp / 100.0 * Integer.parseInt(quantity);
				String profitFormatted = nFormatterCurrency.format(profit);
				this.profit = new SimpleStringProperty(profitFormatted);
			} else {
				this.costs = new SimpleStringProperty("");
				this.profit = new SimpleStringProperty("");
			}

			lastId = id;

		}

		public Offer(String quality, String quantity, String price) {
			this(lastId + 1, quality, quantity, null, null, price);
		}

		public Offer(OfferToClient offer, int id) {
			this(id, offer.quality + "", offer.quantityToSell + "", offer.quantitySold + "", offer.costs + "", offer.price + "");
		}

		public Integer getId() {
			return id.get();
		}

		public String getProduct() {
			return product.get();
		}

		public String getQuality() {
			return quality.get();
		}

		public String getQuantity() {
			return quantity.get();
		}

		public String getSoldQuantity() {
			return soldQuantity.get();
		}

		public String getCosts() {
			return costs.get();
		}

		public String getPrice() {
			return price.get();
		}

		public String getProfit() {
			return profit.get();
		}

	}

	/**
	 * Benefit
	 */

	public static class BenefitBooking {

		private final SimpleIntegerProperty id;
		private final SimpleStringProperty benefit;
		private final SimpleStringProperty costs;
		private final SimpleStringProperty remainingRounds;

		private static int lastId = 0;

		public BenefitBooking(int id, String benefit, String costs, String remainingRounds) {
			this.id = new SimpleIntegerProperty(id);
			this.benefit = new SimpleStringProperty(benefit);
			this.remainingRounds = new SimpleStringProperty(remainingRounds);

			// Währungsformatierung
			long costsTmp = Long.parseLong(costs);
			String costsFormatted = nFormatterCurrency.format(costsTmp / 100.0);
			this.costs = new SimpleStringProperty(costsFormatted);
			
			lastId = id;

		}	

		public BenefitBooking(int id, BenefitBookingToClient benefit) {
			this(id, benefit.name, benefit.costsPerRound+"", benefit.remainingRounds+"");
		}

		public BenefitBooking(String benefit, String costs, String duration) {
			this(lastId+1, benefit, costs, duration);
		}

		public Integer getId() {
			return id.get();
		}

		public String getBenefit() {
			return benefit.get();
		}

		public String getCosts() {
			return costs.get();
		}

		public String getRemainingRounds() {
			return remainingRounds.get();
		}
		
		

	}

	public static class Benefit {

		private final SimpleStringProperty benefit;
		private final SimpleStringProperty costs;

		public Benefit(String benefit, String costs) {

			this.benefit = new SimpleStringProperty(benefit);
			// Währungsformatierung
			long costsTmp = Long.parseLong(costs);
			String costsFormatted = nFormatterCurrency.format(costsTmp / 100.0);
			this.costs = new SimpleStringProperty(costsFormatted);

		}

		public Benefit(PossibleBenefit benefit) {
			this(benefit.name, benefit.costsPerRound + "");
		}

		public String getBenefit() {
			return benefit.get();
		}

		public String getCosts() {
			return costs.get();
		}

		public String toString() {
			return benefit.get();
		}

	}

}
