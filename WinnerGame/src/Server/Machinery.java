/**
 * 
 */
package Server;

import java.util.ArrayList;
import java.util.Random;

import Constant.Constant;

/**
 * @author Lars
 * 
 *         Klasse für die Maschine mit Attributen für das level. Hiervon
 *         abgeleitet bietet die Klasse an, wieviel eine Maschine produzieren
 *         kann und wie groß die Auslastung ist
 */
public class Machinery {
	// Maschinen ausbaustufe
	private int level;

	// Produzierte Einheiten
	private int producedQuantity = 0;

	// Liste aller auslastungen:
	private ArrayList<TPercentOfUsage> listOfUsages = new ArrayList<TPercentOfUsage>();

	/**
	 * Erzeugt eine neue Maschine mit Ausbaustufe 1
	 * 
	 * @param p
	 *            enthält Referenz auf die Produktionsabteilung wird benötigt um
	 *            die Belastung zu berechnen
	 */
	public Machinery() {

		level = 1;

	}

	/**
	 * Gibt die momentane Auslastung zurück. Falls das Element dafür noch nicht
	 * erzeugt wurde, wird das hier gemacht
	 * 
	 * @return momentanes Element von TPercentOfUsage
	 * @throws Exception
	 */
	public TPercentOfUsage getCurrentUsage() throws Exception {
		// prüfen ob bereits eine Auslastung angelegt wurde.
		for (TPercentOfUsage o : listOfUsages) {
			if (o.getRound() == GameEngine.getGameEngine().getRound()) {
				return o;
			}
		}
		// Anscheinend nicht:
		TPercentOfUsage tNew = new TPercentOfUsage(
				(int) (producedQuantity / (getMaxCapacity() + 0.0)), GameEngine
						.getGameEngine().getRound());

		listOfUsages.add(tNew);
		return tNew;
	}
	
	/**
	 * Gibt die Auslastung der letzten Runde wieder
	 * 
	 * @return momentanes Element der letzten Runde
	 * @throws Exception
	 */
	public TPercentOfUsage getLastUsage() throws Exception {
		//Sicherstellen, dass die neueste Nutzung eingetrage ist:
		getCurrentUsage();
		//Rueckgabe des vorletzten Wertes
		if (GameEngine.getGameEngine().getRound()>2){
		return listOfUsages.get(listOfUsages.size()-2);
		}else{
			return getCurrentUsage();
		}
	}

	/**
	 * gibt eine Liste aller Auslastungen wieder (falls für die momentane Runde
	 * noch nicht vorhanden, wird es neu erstellt)
	 * 
	 * @return Liste aller Auslastungen der Maschine
	 */
	public ArrayList<TPercentOfUsage> getAllUsages() throws Exception {
		// Der get aufruf, sorgt dafür, dass auf jedenfall die momentane Runde
		// berücksichtigt wird!
		getCurrentUsage();
		return listOfUsages;
	}

	/**
	 * Gibt an, ob eine Produktion erfolgreich war oder nicht. Regelt den
	 * Ausschuss und hat einen internen Counter für die Auslastung (inklusive
	 * Sperrlogik)
	 * 
	 * @return boolean, true, falls produziert wird, false, falls nicht
	 */
	public boolean isJunk() {
		// Erhoehen der Auslastung
		if (producedQuantity >= getMaxCapacity()) {
			return false;
		}
		producedQuantity++;

		// Zufallszahlgenerator initialisieren
		Random r = new Random();
		// Chance auf Produktion: 84% + level.. also mindestens 85%
		boolean ret = (r.nextInt(100) < (Constant.Machinery.JUNK_INIT + level)) ? false
				: true;

		return ret;

	}

	/**
	 * Gibt die Maximal produzierbare Menge an
	 * 
	 * @return Maschinenkapazität als Integer
	 */
	public int getMaxCapacity() {

		return Constant.Machinery.CAPACITY[level-1];
	}

	/**
	 * Gibt die Ausbaustufe der Maschine an
	 * 
	 * @return Maschinenlevel als Integer
	 */
	public int getLevel() {

		return this.level;
	}

	/**
	 * 
	 * @return gibt die Kosten für den nächsten Ausbau an
	 */
	public int getCostsForNextLevel() {
		if (level==10){
			return 0;
		}
		return Constant.Machinery.BUILD_COSTS[level];
	}

	/**
	 * Prüft ob die Maschine weiter ausgebaut werden kann. Hierfür wird auch ein
	 * Betrag fällig (Ausbaukosten)
	 * 
	 * @param b
	 *            BankAccount, von wo die Maschine bezahlt wird
	 * @return true, falls erfolgreiche erhöhung, false, falls maxLevel oder zu
	 *         hohe Kosten
	 * @throws Exception 
	 */
	public boolean increaseLevel(BankAccount b) throws Exception {

		// check ob die Maschine noch nicht auf Max ist
		if (level >= 10) {

			return false;
		}
		// Prüfe ob genug Geld da ist, wenn ja, dann bucht der Befehl es auch
		// direkt ab!
		// drank denken dass der array bei 0 beginnt, maschine aber bei 1
		if (!b.decreaseBalance(Constant.Machinery.BUILD_COSTS[level])) {

			return false;

		}
		level++;

		return true;

	}

	/**
	 * Vermindert die Ausbaustufe der Maschine um 1 keine Rückschreibung der
	 * Kosten
	 * 
	 * @return true, falls erfolgreiche Minderung false, falls Maschinenlvl 1
	 */
	public boolean decreaseLevel() {
		// TODO: vielleicht gibt man dem Spieler einen Teil der Ausbaukosten
		// wieder..

		if (level == 1) {

			return false;
		}
		level--;

		return true;
	}

	/**
	 * Berechnet sich aus Ausbaustufe zum Quadrat und einem Faktor
	 * 
	 * @return gibt die Fixkosten an
	 */
	public int getCosts() {

		return (level * level) * Constant.Machinery.FIX_COST;
	}

	/**
	 * Gibt Kosten für Hilfsstoffe an. je höher das Maschinen level, je
	 * niedriger die Kosten.
	 * 
	 * @return Stückkosten auf der Maschine
	 */
	public int getPieceCosts() {

		return Constant.Machinery.PIECE_COST_BASIC * (11 - level);
	}

}
