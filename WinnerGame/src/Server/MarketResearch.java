package Server;

import Constant.Constant;

/**
 * Ist eine Abteilung des Unternehmens. Sie enthaelt die Information ob die MarektData
 * beim Client angezeigt werden soll oder nicht
 * 
 * Created by:
 * User: Lars Trey
 * Date: 28.09.13
 * Time: 18:11
 */

public class MarketResearch extends Department {
	
	
	private boolean isBooked;

    public MarketResearch(Company c) throws Exception {
        super(c, "Marktforschung",Constant.DepartmentFixcost.MARKET_RESEARCH);
        
    }
    /**
     * prueft ob MarketResearch ueberhaupt buchbar ist und setzt dementsprechend isBooked
     * @param isBooked Uebergabeparameter gibt an ob in der naechsten Runde MarketData angezeigt wird oder nicht
     * @throws Exception 
     */
    public void setIsBooked(boolean isBooked) throws Exception{
    	this.isBooked = isBooked;
    	//falls gebucht sollen direkt Kosten abgebucht werden
    	if(this.isBooked){
    		//falls nicht genuegend Geld auf dem Konto vorhanden wird es wieder auf false gesetzt
    		if(!getCompany().getBankAccount().decreaseBalance(Constant.MarketResearch.COSTS_MARKET_RESEARCH)){
    			this.isBooked=false;
    		}
    	}
    }
  
    public boolean getIsBooked(){
    	return isBooked;
    }

}
