package server.connection;

import kigegner.KI;
import kigegner.KITarek;

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
		for( int i=0; i<0; i++)
			new KI(round);
	}
	
}
