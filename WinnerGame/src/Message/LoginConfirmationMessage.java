package Message;

import java.io.Serializable;

/**
 * 
 * @author D059270 Diese Message wird vom Server an den Client als Antwort auf
 *         eine Loginanfrage gesendet.
 */
public class LoginConfirmationMessage implements IMessage, Serializable {
	private Boolean success = false;
	private String info = "";

	public LoginConfirmationMessage(Boolean success, String info) {
		this.success = success;
		this.info = info;
	}

	@Override
	public String getType() {
		return "LoginConfirmationMessage";

	}

	public Boolean getSuccess() {
		return success;
	}

	public String getInfo() {
		return info;
	}

}
