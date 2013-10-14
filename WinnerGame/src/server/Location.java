package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by: User: Lars Trey Date: 28.09.13 Time: 17:16
 */

public class Location {

	private int advantage;
	private String country;
	private int purchasePrice;
	private int wageLevel;
	private int wageInit;
	private static ArrayList<Location> listOfLocations = new ArrayList<Location>();

	/**
	 * muss vor Spielstart aufgerufen werden. Erzeugt die verschiedenen
	 * Locations
	 * 
	 * @throws IOException
	 */

	public static void initLocations() throws IOException {
		FileReader reader = new FileReader("locations.dat");
		BufferedReader buffReader = new BufferedReader(reader);
		String line = buffReader.readLine();
		while (line != null) {
			String[] attributes = line.split(":");
			new Location(Integer.parseInt(attributes[1]), attributes[0],
					Integer.parseInt(attributes[2]),
					Integer.parseInt(attributes[3]),
					Integer.parseInt(attributes[4]));
			line = buffReader.readLine();
		}
		buffReader.close();

	}

	/**
	 * Erzeugt eine neue Location
	 * 
	 * @param a
	 *            Advantage/ Standortvorteil
	 * @param c
	 *            Country / Produktionsstandort
	 * @param p
	 *            Price /Anschaffungskosten des Standorts
	 * @param w
	 *            WageLevel / Lohn niveau
	 */
	public Location(int a, String c, int p, int w, int initW) {

		this.advantage = a;
		this.country = c;
		this.purchasePrice = p;
		this.wageLevel = w;
		this.wageInit = initW;
		listOfLocations.add(this);

	}

	public static ArrayList<Location> getListOfLocations() {

		return listOfLocations;
	}

	public int getInitWage() {
		return wageInit;
	}

	/**
	 * Sucht nach einer Location über das Land
	 * 
	 * @param c
	 *            Country: das zu suchende Land
	 * @return null, falls der Name nicht gefunden wurde
	 */
	public static Location getLocationByCountry(String c) {

		// Suche in der internen Liste:

		for (Location o : listOfLocations) {
			// Vergleiche (ignoriere Groß/Kleinschreibung)
			if (o.getCountry().toLowerCase().equals(c.toLowerCase())) {
				// Strings stimmen überein, also zurückgeben

				return o;
			}
		}
		return null;
	}

	public int getAdvantage() {

		return this.advantage;
	}

	public String getCountry() {

		return this.country;
	}

	public int getPurchasePrice() {

		return this.purchasePrice;
	}

	public int getWageLevel() {

		return this.wageLevel;
	}

	@Override
	public String toString() {

		return this.country;
	}

}
