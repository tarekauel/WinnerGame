package aspectlogger;

import java.util.ArrayList;

import annotation.Reset;

public privileged aspect SingletonReset {
	
	pointcut resetSingleton() :
		execution( @Reset * *.*(..));
	
	before() : resetSingleton() {
		server.SupplierMarket.market = null;
		server.CustomerMarket.market = null;
		server.GameEngine.engine = null;
		server.MarketData.data = null;
		server.Benefit.bookableBenefits  = new ArrayList<server.Benefit>();
		server.Location.listOfLocations = new ArrayList<server.Location>();
	}

}
