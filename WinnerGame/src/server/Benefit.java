package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Benefit {

	// Bezeichnung
	private final String name;

	// Kosten pro Runde
	private final int costsPerRound;

	// Alle buchbaren Benefits
	private static ArrayList<Benefit> bookableBenefits = new ArrayList<Benefit>();

	/**
	 * Erstellt ein neues Benefit
	 * 
	 * @param name
	 *            Name des zu erstellenden Benefits
	 * @param costsPerRound
	 *            Kosten pro Runde des Benefits
	 */
	private Benefit(String name, int costsPerRound) {
		if (!checkName(name))
			throw new IllegalArgumentException(
					"Name darf nicht null sein und nicht leer");
		this.name = name;
		if (!checkCostsPerRound(costsPerRound))
			throw new IllegalArgumentException("Kosten muesen >0 sein");
		this.costsPerRound = costsPerRound;

		if (!existBenefit(name)) {
			bookableBenefits.add(this);
		} else {
			throw new IllegalArgumentException("Benefit existiert bereits: "
					+ name);
		}
	}

	/**
	 * Sucht aus der Liste aller buchbaren Benefits einen bestimmten Eintrag
	 * heraus
	 * 
	 * @param name
	 *            des Benefits, nach dem gesucht wird
	 * @return Benefit, welches gesucht wurde.
	 * 
	 * @exception IllegalArgumentException, wenn das Benefit nicht gefunden worden ist
	 */
	public static Benefit getBenefitByName(String name) {
		for (Benefit benefit : bookableBenefits) {
			if (benefit.name.equals(name)) {
				return benefit;
			}
		}
		throw new IllegalArgumentException("Benefit existiert nicht " + name);
	}
	
	/**
	 * Pr�ft ob ein Benefit exitiert
	 * @param name Name des Benefits
	 * @return Boolean, ob das Benefit existiert
	 */
	public static boolean existBenefit(String name) {
		for (Benefit benefit : bookableBenefits) {
			if (benefit.name.equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Erzeugt ein neues Benefit
	 * 
	 * @param name
	 *            Bezeichner des Benefits
	 * @param costsPerRound
	 *            Die kosten pro Runde
	 * @throws Exception
	 *             tritt auf, falls das Benefit bereits existiert
	 */
	public static void createBenefit(String name, int costsPerRound)
			throws Exception {

		boolean benefitExisting = false;

		for (Benefit b : bookableBenefits) {

			if (b.getName().equals(name)) {
				benefitExisting = true;
			}

		}

		if (benefitExisting == false) {

			Benefit benefit = new Benefit(name, costsPerRound);
			bookableBenefits.add(benefit);

		} else if (benefitExisting == true) {

			throw new Exception("Benefit existiert bereits.");

		}

	}

	/**
	 * 
	 * @return gibt eine Liste aller buchbaren Benefits zur�ck
	 */
	public static ArrayList<Benefit> getBookableBenefits() {

		return bookableBenefits;

	}

	/**
	 * Initialisiert eine Liste mit benefits
	 * 
	 * @throws Exception
	 *             falls ein Fehler in der initialisierung auftritt
	 */

	public static void initBenefits() throws Exception {
		if(Benefit.getBookableBenefits().size()==0){
		BufferedReader r = new BufferedReader(new FileReader("benefit.dat"));
		String line = null;
		while ((line = r.readLine()) != null) {
			// Zeile Teile trennen
			String[] lineParts = line.split(";", 2);

			// Nur wenn zwei Teile gefunden wurden, ist der Eintrag gueltig
			if (lineParts.length != 2)
				continue;

			new Benefit(lineParts[0], Integer.parseInt(lineParts[1]));
		}

		r.close();
		}
	}
/**
 * 
 * @return gibt den Namen eines Benefits an
 */
	public String getName() {

		return name;
	}
/**
 * 
 * @return gibt die Kosten pro Runde eines Benefits an
 */
	public int getCostsPerRound() {

		return costsPerRound;
	}

	@Override
	public String toString() {
		return name + ":" + costsPerRound;
	}

	/**
	 * Ueberprueft ob der Name nicht leer und nicht null ist
	 * 
	 * @param name
	 *            zu pruefender Name
	 * @return Ergebnis der Pruefung
	 */
	private boolean checkName(String name) {
		if (name == null || name.equals(""))
			return false;
		return true;
	}

	/**
	 * Ueberprueft ob die Kosten groesser als 0 sind
	 * 
	 * @param c
	 *            Kosten, die geprueft werden sollen
	 * @return Ergebnis der Pruefung
	 */
	private boolean checkCostsPerRound(int c) {
		return (c > 0);
	}

	@Override
	public boolean equals(Object o) {
		Benefit b = (Benefit) o;
		// Prueft nur auf Namensgleichheit
		if (b.getName().equals(getName()))
			return true;
		return false;
	}

}
