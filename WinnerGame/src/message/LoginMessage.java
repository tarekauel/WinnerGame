package message;

import java.io.Serializable;

/**
 * 
 * @author D059270 Diese Message wird vom Client an den Server als Loginanfrage
 *         gesendet.
 */
public class LoginMessage implements IMessage, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = "";
	private String password = "";
	private String chosenLocation;

	public LoginMessage(String name, String password, String chosenLocation) {
		this.name = name;
		this.password = password;
		this.chosenLocation = chosenLocation;
	}

	@Override
	public String getType() {
		return "LoginMessage";

	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getChosenLocation() {
		return chosenLocation;
	}

}
