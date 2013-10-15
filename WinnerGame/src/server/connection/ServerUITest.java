package server.connection;

import kigegner.KI;
import kigegner.KITarek;

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
		int round = 10;
		new KITarek(round);
		for( int i=0; i<4; i++)
			new KI(round);
	}
	
}
