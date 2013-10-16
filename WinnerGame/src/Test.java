import java.util.HashMap;

import server.CustomerMarket;


public class Test {
	
	public static void main(String[] args) {
		HashMap<Integer, Long> list = CustomerMarket.getMarket().getPriceList();
		for( int i=1; i<= 100; i++) {
			System.out.println(list.get(i));
		}
	}
}
