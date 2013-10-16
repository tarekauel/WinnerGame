package aspectlogger;

import java.util.ArrayList;

import org.junit.BeforeClass;

public privileged aspect SingletonReset {
	
	pointcut resetSingleton() :
		execution( @BeforeClass * *.*(..));
	
	before() : resetSingleton() {
		server.SupplierMarket.market = null;
		server.CustomerMarket.market = null;
		server.GameEngine.engine = null;
		server.MarketData.data = null;
		server.Benefit.bookableBenefits  = new ArrayList<server.Benefit>();
		server.Location.listOfLocations = new ArrayList<server.Location>();
	}

}
