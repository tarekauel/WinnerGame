package Constant;

import java.io.BufferedReader;
import java.io.FileReader;

public class Constant {
	// Pfad der zu nutzenden INI.
	public static final String PATH = "scale.ini";

	/**
	 * Gibt ein int Array zurück
	 * 
	 * @param s
	 *            Name der Variablen in der Constant Datei
	 * @return String mit dem Inhalt
	 */
	private static int[] getIntArray(String s) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(PATH));
			String tmp = null;
			while ((tmp = r.readLine()) != null) {
				if (tmp.startsWith(s)) {
					r.close();
					tmp = tmp.substring(tmp.indexOf("=") + 2).replace("}", "");

					String[] help = tmp.split(",");
					int[] ret = new int[help.length];
					for (int i = 0; i < help.length; i++) {
						ret[i] = Integer.parseInt(help[i]);
					}
					return ret;
				}
			}
			r.close();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * liest einfache Constanten aus, welche noch gecastet werden müssen
	 * 
	 * @param s
	 *            Name der Variablen in der Constant datei
	 * @return String mit dem Inhalt
	 */
	private static String getConstant(String s) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(PATH));
			String tmp = null;
			while ((tmp = r.readLine()) != null) {
				if (tmp.startsWith(s)) {
					r.close();
					return tmp.substring(tmp.indexOf("=") + 1);
				}
			}
			r.close();

		} catch (Exception e) {

		}
		return null;
	}

	// Section Machinery:
	public static class Machinery {

		public static final int[] CAPACITY = getIntArray("MACHINERY_CAPACITY");
		public static final int[] BUILD_COSTS = getIntArray("MACHINERY_BUILD_COSTS");
		public static final int JUNK_INIT = Integer
				.parseInt(getConstant("MACHINERY_JUNK_INIT"));
		public static final int FIX_COST = Integer
				.parseInt(getConstant("MACHINERY_FIX_COST"));
		public static final int PIECE_COST_BASIC = Integer
				.parseInt(getConstant("MACHINERY_PIECE_COST_BASIC"));

	}

	public static class Production {

		// Section Production
		public static final int WAFERS_PER_PANEL = Integer
				.parseInt(getConstant("PRODUCTION_WAFERS_PER_PANEL"));
		public static final int WORKING_HOURS_PER_PANEL = Integer
				.parseInt(getConstant("PRODUCTION_WORKING_HOURS_PER_PANEL"));
		public static final int COST_PER_ORDER = Integer
				.parseInt(getConstant("PRODUCTION_COST_PER_ORDER"));
		public static final int IMPACT_WAFER = Integer
				.parseInt(getConstant("PRODUCTION_IMPACT_WAFER"));
		public static final int IMPACT_CASE = Integer
				.parseInt(getConstant("PRODUCTION_IMPACT_CASE"));
		public static final int MAX_QUALITY_ADDITION = Integer
				.parseInt(getConstant("PRODUCTION_MAX_QUALITY_ADDITION"));
		public static final int MOTIVATION_IMPACT = Integer
				.parseInt(getConstant("PRODUCTION_MOTIVATION_IMPACT"));
		public static final int LOCATION_IMPACT = Integer
				.parseInt(getConstant("PRODUCTION_LOCATION_IMPACT"));
	
	}
	
	public static class Distribution{
		public static final int DISTRIBUTION_OFFER_COSTS_PER_PANEL = Integer
				.parseInt(getConstant("DISTRIBUTION_OFFER_COSTS_PER_PANEL"));
	}

	public static class Server {

		// Section Technical Constats
		public static final int TCP_PORT = Integer
				.parseInt(getConstant("TCP_PORT"));
		public static final int UDP_PORT = Integer
				.parseInt(getConstant("UDP_PORT"));
		public static final int MAX_PLAYER = Integer
				.parseInt(getConstant("MAX_USERS"));
		public static final int PLAYER_KI_TAREK = Integer
				.parseInt(getConstant("PLAYER_KI_TAREK"));
		public static final int PLAYER_KI_LARS = Integer
				.parseInt(getConstant("PLAYER_KI_LARS"));
	}

	public static class DepartmentFixcost {

		public static final int PURCHASE = Integer
				.parseInt(getConstant("FIXCOST_PURCHASE"));
		public static final int PRODUCTION = Integer
				.parseInt(getConstant("FIXCOST_PRODUCTION"));
		public static final int STORAGE = Integer
				.parseInt(getConstant("FIXCOST_STORAGE"));
		public static final int DISTRIBUTION = Integer
				.parseInt(getConstant("FIXCOST_DISTRIBUTION"));
		public static final int HUMAN_RESOURCES = Integer
				.parseInt(getConstant("FIXCOST_HUMAN_RESOURCE"));
		public static final int MARKET_RESEARCH = Integer
				.parseInt(getConstant("FIXCOST_MARKET_RESEARCH"));

	}

	public static class Company {

	}

	public static class MarketResearch {

		public static final int COSTS_MARKET_RESEARCH = Integer
				.parseInt(getConstant("COSTS_MARKET_RESEARCH"));

	}

	public static class BankAccount {

		// Section BankAccount:
		public static final long MAX_CREDIT = Long
				.parseLong(getConstant("BANK_MAX_CREDIT"));
		// Section Company:
		public static final long START_CAPITAL = Long
				.parseLong(getConstant("COMPANY_START_CAPITAL"));
		public static final long RATES = Integer
				.parseInt(getConstant("BANK_RATES"));

	}

	public static class Product {
		public static final int STORAGECOST_WAFER = Integer
				.parseInt(getConstant("STORAGE_COSTS_WAFER"));
		public static final int STORAGECOST_CASE = Integer
				.parseInt(getConstant("STORAGE_COSTS_CASE"));
		public static final int STORAGECOST_PANEL = Integer
				.parseInt(getConstant("STORAGE_COSTS_PANEL"));
	}

	// Constanten-Klasse für das HR
	public static class HumanResources {
		public static final int IMPACT_DIFF_POS = Integer
				.parseInt(getConstant("HR_IMPACT_DIFF_POS"));
		public static final int IMPACT_DIFF_NEG = Integer
				.parseInt(getConstant("HR_IMPACT_DIFF_NEG"));

		public static final int IMPACT_DIFF_MARKET = Integer
				.parseInt(getConstant("HR_IMPACT_DIFF_MARKET"));
		public static final int IMPACT_DIFF_INTERNAL = Integer
				.parseInt(getConstant("HR_IMPACT_DIFF_INTERNAL"));

		public static final int HR_FACTOR_WAGE = Integer
				.parseInt(getConstant("HR_FACTOR_WAGE"));

		public static final int HR_FACTOR_BENEFIT = Integer
				.parseInt(getConstant("HR_FACTOR_BENEFIT"));

		public static final int HR_EMPLOYEES = Integer
				.parseInt(getConstant("HR_EMPLOYEES"));
		public static final int HR_WORK_WEEK = Integer
				.parseInt(getConstant("HR_WORK_WEEK"));
	}

	public static class CustomerMarket {
		public static final int aMarketPeak = Integer
				.parseInt(getConstant("CUST_APEAK"));
		public static final int aMarketQuantity = Integer
				.parseInt(getConstant("CUST_AQUANT"));
		public static final double aMarketVariance = Integer
				.parseInt(getConstant("CUST_AVARI")) / 100.0;
		public static final double aMarketIncreaseFactor = Integer
				.parseInt(getConstant("CUST_AINC")) / 100.0;
		public static final double aMarketDecreaseFactor = Integer
				.parseInt(getConstant("CUST_ADEC")) / 100.0;
		public static final int cMarketPeak = Integer
				.parseInt(getConstant("CUST_CPEAK"));
		public static final int cMarketQuantity = Integer
				.parseInt(getConstant("CUST_CQUANT"));
		public static final double cMarketVariance = Integer
				.parseInt(getConstant("CUST_CVARI")) / 100.0;
		public static final double cMarketIncreaseFactor = Integer
				.parseInt(getConstant("CUST_CINC")) / 100.0;
		public static final double cMarketDecreaseFactor = Integer
				.parseInt(getConstant("CUST_CDEC")) / 100.0;
		public static final int aMarketAvgPriceLastRound = Integer
				.parseInt(getConstant("CUST_AAVGP"));
		public static final int aMarketAvgQualityLastRound = Integer
				.parseInt(getConstant("CUST_CAVGP"));
		public static final int cMarketAvgPriceLastRound = Integer
				.parseInt(getConstant("CUST_AAVGQ"));
		public static final int cMarketAvgQualityLastRound = Integer
				.parseInt(getConstant("CUST_CAVGQ"));

	}

}
