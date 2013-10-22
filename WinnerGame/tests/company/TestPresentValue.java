package company;

import java.util.HashMap;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Company;
import server.CustomerMarket;
import server.FinishedGood;
import server.Location;
import server.Resource;
import server.StorageElement;
import server.SupplierMarket;
import server.TPresentValue;
import server.TResourcePrice;

public class TestPresentValue {
	
	Company c;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		
	}

	@Before
	public void initializeTests() throws Exception {
		c = new Company(Location.getLocationByCountry("USA"), "OTTO");
		//einlagern
		Resource cases = new Resource(50, "Gehäuse", 10000);
		c.getStorage().store(cases, 100);
		Resource wafer = new Resource(80, "Wafer", 1000);
		c.getStorage().store(wafer, 10000);
		FinishedGood fg = FinishedGood.create(80, 100000);
		c.getStorage().store(fg, 100);
		//veraendern des kontostandes um 1Mio
		c.getBankAccount().decreaseBalance(100000000);		
		//marketShare
		//verkaufe 50 stueck zu 1eur sodass immer gekauft wird
		//um marketshares zu erhalten, da nur eine Company vorhanden ist der Marktanteil im Test 100%
		c.getDistribution().createOffer(80, 50, 100);
		CustomerMarket.getMarket().handleAllOffers();
		
		//erhoehen des Maschinenlevels
		c.getProduction().getMachine().increaseLevel(c.getBankAccount());
		c.getProduction().getMachine().increaseLevel(c.getBankAccount());
		
		//Grundstueck wird da wir uns in Runde 1 befinden in der Berechnung des Presentvalues abgeschrieben.
		//muss demnach hier nicht weiter behandelt werden.
		
		
	}

	@Test
	public void makeTests() throws Exception {
		
		HashMap<Integer,Long> priceList = CustomerMarket.getMarket().getPriceList();	

		for(int i = 1;i <=100; i++){
			System.out.println("Qualitaet: "+i+" Preis: "+ priceList.get(i));
		}
		
		long resourcesWafer = 0;
		long resourcesCases = 0;
		TreeSet<TResourcePrice> priceListCases = SupplierMarket.getMarket().getCasePricelist();
		TreeSet<TResourcePrice> priceListWafer = SupplierMarket.getMarket().getWaferPricelist();
		java.util.Iterator<TResourcePrice> priceListCasesIterator = priceListCases.iterator();
		java.util.Iterator<TResourcePrice> priceListWaferIterator = priceListWafer.iterator();
		for(StorageElement storageElement : c.getStorage().getAllStorageElements()){
			if(storageElement.getProduct() instanceof Resource){
				int quantity = storageElement.getQuantity();
				Resource resource = (Resource) storageElement.getProduct();
				if(resource.getName().equals("Wafer")){
					while(priceListWaferIterator.hasNext()){
						TResourcePrice resourceprice = priceListWaferIterator.next();
						int quality = resourceprice.getQuality();
						int price = resourceprice.getPrice();
						if(quality==resource.getQuality()){
							System.out.println("Wafer Q:"+quality+" Preis: "+price);
							System.out.println("gesamt: "+ (quantity*price));
							break;
						}//if
					}//while
				}//if
				if(resource.getName().equals("Gehäuse")){
					while(priceListCasesIterator.hasNext()){
						TResourcePrice resourceprice = priceListCasesIterator.next();
						int quality = resourceprice.getQuality();
						int price = resourceprice.getPrice();
						if(quality==resource.getQuality()){
							resourcesCases = resourcesCases +  price;
							System.out.println("Case Q: "+quality+" Preis: "+price);
							System.out.println("gesamt: "+ (quantity*price));
							break;
						}//if
					}//while
				}//if
			}//if
		}

		System.out.println("FinishedGoods: "+ c.getStorage().getFinishedGoodByQuality(80).getQuantity());
		TPresentValue value = c.getPresentValue();
		System.out.println(value.getRound() +"\n"+
						   value.getPresentValue());
		
	}

	@After
	public void resetTests() {

	}

}
