package aspectlogger;

import java.util.ArrayList;

import org.junit.BeforeClass;

public privileged aspect Reset {
	
	pointcut reset() :
		execution( @BeforeClass * *.*(..));
	
	pointcut setIni() :
		get(private static String constant.Constant.PATH) && !within(Reset);
	
	before() : reset() {
		/** Reset Game **/
		server.SupplierMarket.market = null;
		server.CustomerMarket.market = null;
		server.GameEngine.engine = null;
		server.MarketData.data = null;
		server.Benefit.bookableBenefits  = new ArrayList<server.Benefit>();
		server.Location.listOfLocations = new ArrayList<server.Location>();
		
		/** Reset Server **/
		server.connection.Server.server = null;				
	}
	
	String around() : setIni() {
		for(StackTraceElement s : Thread.currentThread().getStackTrace()) {
			if( s.getClassName().equals("org.eclipse.jdt.internal.junit.runner.RemoteTestRunner")) {
				return "test.ini";
			}
		}
		return constant.Constant.PATH;
	}
}
