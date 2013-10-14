package Test;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
import Server.Benefit;
import Server.Company;
import Server.GameEngine;
import Server.Location;

public class TestTemplateRound {
	/**************************************
	 * Bitte nur die Variablen verwenden, wenn unbedingt nötig und valide
	 * Zugriffe erfolgen! Bitte die Variablen im Block belassen.
	 */
	// liste der Messages von den Unternehmen:
	ArrayList<GameDataMessageFromClient> messages = new ArrayList<GameDataMessageFromClient>();

	// Liste aller Requests:
	ArrayList<RequestFromClient> requests = new ArrayList<RequestFromClient>();
	// Liste aller angenommenen Angebote vom supplierMarket
	ArrayList<AcceptedSupplierOfferFromClient> accepted = new ArrayList<AcceptedSupplierOfferFromClient>();
	// Production Order Liste
	ArrayList<ProductionOrderFromClient> proOrder = new ArrayList<ProductionOrderFromClient>();
	// Angebote an den CustomerMarket
	ArrayList<OfferFromClient> offerList = new ArrayList<OfferFromClient>();
	// BenefitBooking
	ArrayList<BenefitBookingFromClient> bBook = new ArrayList<BenefitBookingFromClient>();

	/*****************************
	 * 
	 * hierunter können eigene Variablen angelegt werden!
	 */

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// initialisiere Benefits und Standorte
		Location.initLocations();
		Benefit.initBenefits();
	}

	@Before
	public void initializeTests() throws Exception {
		// gib hier an, wieviele Companys du haben willst.
		int noOfCompanys = 2;
		// Companys in Unterschiedlichen Ländern?
		boolean diffCountrys = false;
		// StandardLand ( wenn nicht diffCountrys = true)
		String defaultCountry = "Deutschland";

		// Nominale zu produzierende Menge an Panel (ohne Ausschuss):
		int quantityToProduce = 50;

		// Qualität der angefragten Wafer:
		int qualityWafer = 60;
		// Qualität der angefragten Gehaeuse:
		int qualityCase = 70;

		// Benefit welches gebucht wird
		String benefitName = "Sport";
		// Anzahl der Runden des benefits:
		int benefitRound = 2;

		// StandardLohn:
		int wage = 1000;
		// Marktdaten:
		boolean marketData = true;
		// Maschinenausbau:
		boolean machine = false;
		
		

		/************************************************
		 * Erstellen der Daten von oben!
		 */
		// Erstelle die Unternehmen
		if (diffCountrys) {
			createCompanys(noOfCompanys, diffCountrys);
		} else {
			createCompanys(noOfCompanys, defaultCountry);
		}
		// Frage Ressourcen an
		requestWafer(qualityWafer);
		requestCase(qualityCase);
		// Buche Benefits:
		bookNewBenefit(benefitName, benefitRound);

		for (Company c : GameEngine.getGameEngine().getListOfCompanys()) {
			//setze plausiblen Spielernamen:
			String playerName = c.getName();
			addMessage(playerName, machine, wage, marketData, false);
		}
		
		
		startNewRound();
	}

	@Test
	public void makeTests() {
	}

	@After
	public void resetTests() {

	}

	/*******************************************************************************
	 * 
	 * DO NOT MODIFY BELOW IF NOT NECESSARY!
	 * 
	 ******************************************************************************/

	/**
	 * nimmt eine Benefit buchung auf
	 * 
	 * @param benefitName
	 *            Name des Benefits welches gebucht wird
	 * @param forRounds
	 *            wieviele Runden wird es gebucht?
	 */
	private void bookNewBenefit(String benefitName, int forRounds) {
		bBook.add(new BenefitBookingFromClient(benefitName, forRounds));
	}

	/**
	 * fuegt den Anfragen eine Anfrage an Wafern hinzu
	 * 
	 * @param quality
	 *            Qualitaet in der Angefragt wird
	 */
	private void requestWafer(int quality) {
		requests.add(new RequestFromClient("Wafer", quality));
	}

	/**
	 * fuegt den Anfragen eine Anfrage an Gehaeuse hinzu
	 * 
	 * @param quality
	 *            Qualitaet in der Angefragt wird
	 */
	private void requestCase(int quality) {
		requests.add(new RequestFromClient("Gehäuse", quality));
	}

	/**
	 * ueberladene Funktion
	 * 
	 * @param noOfCompanys
	 *            Anzahl der zu erstellenden Unternehmen
	 * @param defaultCountry
	 *            in welchem Land sollen alle sein?
	 */
	private void createCompanys(int noOfCompanys, String defaultCountry) {
		// erstellt beliebig viele neue Companys (werden im Constructor an die
		// GameEngine gehängt
		for (int i = 0; i < noOfCompanys; i++) {
			try {
				new Company(Location.getLocationByCountry(defaultCountry),
						"Unternehmen-" + i);
			} catch (Exception e) {
				System.out.println("Fehler!");
				e.printStackTrace();
			}

		}
	}

	/**
	 * ueberladene Funktion:
	 * 
	 * @param noOfCompanys
	 *            Anzahl der zu erstellenden Unternehmen
	 * @param differentCountry
	 *            Sollen die unternehmen in unterschiedlichen Ländern sein?
	 */
	private void createCompanys(int noOfCompanys, boolean differentCountry) {
		if (!differentCountry) {
			// es sollen wohl doch keine unterschiedlichen Country genommen
			// werden.
			// also default :
			createCompanys(noOfCompanys, "USA");
			return;
		}

		for (int i = 0; i < noOfCompanys; i++) {
			// holt sich den Standort nach i mod AnzahlStandorte
			// erzeugt ein neues Unternehmen
			try {
				new Company(Location.getListOfLocations().get(
						i % Location.getListOfLocations().size()),
						"Unternehmen-" + i);
			} catch (Exception e) {
				System.out.println("Fehler!");
				e.printStackTrace();
			}
		}

	}

	/**
	 * startet eine neue Runde mit den vorherigen eingaben
	 * 
	 * @return Gibt die Antwort der GameEngine zurück!
	 */
	private ArrayList<GameDataMessageToClient> startNewRound() {
		try {
			return GameEngine.getGameEngine().startNextRound(messages);
		} catch (Exception e) {
			System.out.println("Fehler!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * erstellt eine neue Message (sendet diese aber nicht ab!)
	 * 
	 * @param SpielerName
	 *            Name, der zur Message gehört
	 * @param machine
	 *            soll eine Maschine gekauft werden?
	 * @param wage
	 *            Lohn der Arbeiter?
	 * @param marketData
	 *            Soll Marktforschung gekauft werden?
	 * @param resetData
	 *            sollen die Daten komplett geändert werden, oder alle neu
	 *            gesetzt werden?
	 */
	private void addMessage(String SpielerName, boolean machine, int wage,
			boolean marketData, boolean resetData) {
		messages.add(new GameDataMessageFromClient(SpielerName,
				new PurchaseFromClient(requests, accepted),
				new ProductionFromClient(proOrder), new DistributionFromClient(
						offerList), machine,
				new HumanResourcesFromClient(bBook), wage, marketData));
		// Zuruecksetzen der Daten erwuenscht?
		if (resetData) {
			requests = new ArrayList<RequestFromClient>();
			accepted = new ArrayList<AcceptedSupplierOfferFromClient>();
			proOrder = new ArrayList<ProductionOrderFromClient>();
			offerList = new ArrayList<OfferFromClient>();
			bBook = new ArrayList<BenefitBookingFromClient>();

		}
	}

}
