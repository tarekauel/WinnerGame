package message;

import java.io.Serializable;

/**
 * 
 * @author D059270 BeispielMessage
 */
public abstract class GameDataMessage implements IMessage, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String playerName="";

	public GameDataMessage(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public String getType() {
		return "GameDataMessage";

	}
	
	public String getPlayerName(){
		return playerName;
	}

	

}
