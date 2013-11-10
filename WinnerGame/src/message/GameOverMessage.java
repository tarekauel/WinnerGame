package message;

import java.io.Serializable;

/**
 * 
 * @author D059270 Diese Message wird vom Client an den Server als Loginanfrage
 *         gesendet.
 */
public class GameOverMessage implements IMessage, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = "";
	private String info = "";
	

	public GameOverMessage(String name, String info) {
		this.name = name;
		this.info=info;
	}

	@Override
	public String getType() {
		return "GameOverMessage";

	}
	@Override
	public String toString(){
		return name + " meldet: " + info;
	}
	
}
