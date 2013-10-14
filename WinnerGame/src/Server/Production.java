package Server;

import java.util.ArrayList;

import Constant.Constant;

public class Production extends DepartmentRoundSensitive {
	// Liste aller jemals erstellten Produktions auftr�ge
	ArrayList<ProductionOrder>	listOfAllProductionOrders	= new ArrayList<ProductionOrder>();
	// Liste aller offenen (noch nicht produzierten) Auftr�gen
	ArrayList<ProductionOrder>	listOfOpenProductionOrders	= new ArrayList<ProductionOrder>();
	// Referenz auf die Maschine (auf der wir ja produzieren m�ssen)
	Machinery					machine;



	/**
	 * Regul�rer Konstruktor der Produktion, erzeugt zeitgleich eine neue
	 * Maschine
	 * 
	 * @param c
	 *            Referenz des Unternehmens
	 * @param fix
	 *            Fixkosten
	 * @throws Exception
	 *             falls Abteilung nicht erstellt werden konnte
	 */
	public Production(Company c) throws Exception {
		super(c, "Produktion", Constant.DepartmentFixcost.PRODUCTION);
		machine = new Machinery();
	}

	/**
	 * privater Konstruktor damit niemand eine Falsche Abteilung erzeugen kann
	 * (name falsch)
	 * 
	 * @param c
	 *            Referenz des Unternehmens
	 * @param n
	 *            Name der Abteilung
	 * @param f
	 *            Fixkosten der Abteilung
	 * @throws Exception
	 */
	private Production(Company c, String n, int f) throws Exception {
		super(c, n, f);
		// Hier ist nichts mehr zu tun

	}

	/**
	 * Liefer die Fixkosten
	 * 
	 * @return gibt die Fixkosten zur�ck
	 */
	public int getFixCosts() {
		return super.getFixCosts() + this.machine.getCosts();
	}

	/**
	 * 
	 * @return gibt die Maschine der Produktion zur�ck
	 */
	public Machinery getMachine() {
		return this.machine;
	}

	/**
	 * Erzeuge einen neuen Produktionsauftrag und pflege ihn in die Listen ein.
	 * 
	 * @param wafer
	 *            Der Wafer mit dem die Produktion gestartet wird
	 * @param cases
	 *            Das Geh�use mit dem die Produktion gestartet wird
	 * @param quantity
	 *            Die Anzahl der gew�nschten Produktion
	 * @return true: Produktionsorder konnte angelegt werden
	 *         false: Produktionsorder konnte nicht angelegt werden (nicht
	 *         gen�gen Bargeld)
	 * @throws Exception 
	 */
	public boolean createProductionOrder(Resource wafer, Resource cases, int quantity) throws Exception {
		// Pr�fen, ob genug Geld f�r die Order (Orderkosten) auf dem Konto ist
		if (getCompany().getBankAccount().decreaseBalance( Constant.Production.COST_PER_ORDER )) {

			// erzeuge den Auftrag:
			ProductionOrder po = new ProductionOrder(wafer, cases, quantity);
			// nimm ihn in die Liste ALLER Auftr�ge auf
			listOfAllProductionOrders.add(po);
			// nimm ihn in die Liste der noch nicht bearbeiteten Auftr�ge auf
			listOfOpenProductionOrders.add(po);
			return true;
		}
		return false;
	}

	/**
	 * produziert alle offenen Produktionsauftr�ge und leert die Liste der
	 * offenen Auftr�ge am Ende. Das Abbuchen der Rohstoffe erfolgt in der
	 * Produktion, das gutschreiben der Panels im Auftrag
	 * 
	 */
	public void produce() throws Exception {
		// Gibt die Maximale Anzahl der Werkst�cke an
		int max = this.machine.getMaxCapacity();
		// Z�hlt mit, wieviele Werkst�cke auf der Maschine lagen
		int triedToProduce = 0;
		
		// Den Zuschlag auf die Qualit�t berechnen
		int wageMotivation= getCompany().getHumanResources().getMotivation();
		int locationAdvantage = getCompany().getLocation().getAdvantage();
		int advantage= wageMotivation*Constant.Production.MOTIVATION_IMPACT/100+
				locationAdvantage*Constant.Production.LOCATION_IMPACT/100;

		// Es muss sicher gestellt werden, dass nicht mehr Werkstuecke auf der
		// Maschine lagen, als diese kann.

		// Schleife ueber alle Production Orders

		for (ProductionOrder p : listOfOpenProductionOrders) {
			boolean innerBreak = false;
			// Darf ueberhaupt noch jemand produzieren?
			if (triedToProduce >= max) {
				// scheinbar nicht
				break;
			}

			// Schleife ueber jede einzelne Position des Auftrages
			// (Werkstueck)
			for (int j = 0; j < p.getRequested(); j++) {
				// Darf �berhaupt noch jemand produzieren?
				if (triedToProduce >= max) {
					// scheinbar nicht
					break;
				}

				// Zieht den Lohn f�r die Produktion vom Konto ab
				if (!getCompany().getBankAccount().decreaseBalance(
						Constant.Production.WORKING_HOURS_PER_PANEL * getCompany().getHumanResources().getWagesPerHour().getAmount())) {
					innerBreak = true;
					break;					
				}
				

				// Abbuchen der Ressourcen:
				// Zieh die Storage elemente aus dem Storage ab:
				// nutze daf�r das lager des spielers:
				// Wafer abbuchen (direkt in der Anzahl waferPerPanel)
				if (!this.getCompany().getStorage().unstore(p.getWafer(), Constant.Production.WAFERS_PER_PANEL)){
					innerBreak = true;
					break;	
				};
				// Geh�use abbuchen
				if(!this.getCompany().getStorage().unstore(p.getCase(), 1)){
					innerBreak = true;
					//falls nicht genuegend cases vorhanden muessen Wafer wieder zurueckgebucht werden
					this.getCompany().getStorage().store(p.getWafer(),Constant.Production.WAFERS_PER_PANEL);
					break;	
				};
				//Bucht die Stunden im HR ein:
				this.getCompany().getHumanResources().increaseWorkingHour(Constant.Production.WORKING_HOURS_PER_PANEL);

				// "Werkst�ck auf Maschine legen":
				triedToProduce++;

				// Gucken, ob wir tats�chlich produzieren:
				if (machine.isJunk()) {
					// Das Teil ist also Ausschuss
					p.increaseWaste();
					continue;
				} else {
					// Produziere das fertige Panel
					// Berechne Zuschlag aus Motivation und Location fuer Produktion

					p.produce(advantage, this.machine);
				}
			}

			// Lager die Produktion ein und berechnet die Kosten neu
			if(p.getProduced()!=0){
			p.storeProduction(this.getCompany().getStorage());
			}

			// Wird true, wenn auf dem Konto kein Geld mehr f�r Produktion ist
			//oder nicht mehr ausreichend Rohstoffe vorhanden sind
			if (innerBreak)
				break;

		}

		

	
	}

	/**
	 * 
	 * @return gibt die Liste aller bisher erstellten Produktionsauftr�ge seit
	 *         Spielbeginn zur�ck
	 */
	public ArrayList<ProductionOrder> getListOfAllProductionOrders() {
		return listOfAllProductionOrders;
	}

	/**
	 * 
	 * @return gibt die Liste aller offenen Auftr�ge zur�ck, also die, die
	 *         diese
	 *         Runde angelegt wurden
	 */
	public ArrayList<ProductionOrder> getListOfOpenProductionOrders() {
		return listOfOpenProductionOrders;
	}


	/**
	 * erh�ht das Maschinen level (falls m�glich)
	 * @return true, falls erh�ht, false falls nicht erh�ht
	 * @throws Exception 
	 */
	public boolean increaseMachineryLevel() throws Exception{
		return machine.increaseLevel(this.getCompany().getBankAccount());
	}

	@Override
	public void prepareForNewRound(int round) {
		listOfOpenProductionOrders = new ArrayList<ProductionOrder>();
	}
}
