package Server.Connection;

import KIGegner.KITarek;

/**
 * 
 * @author D059270 Der Server stellt die Kommunikation zwischen Client und
 *         Server her.
 */
public class ServerUITest  {
	/**
	 * startet den Server
	 * 
	 * @param args
	 */

	public static void main(String[] args) throws Exception {
		Server.getServer();
		new KITarek(15);
	}
}
