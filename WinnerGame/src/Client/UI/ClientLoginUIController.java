package Client.UI;

/**
 * Created by:
 * User: Lars Trey
 * Date: 08.10.13
 * Time: 16:38
 */

import java.awt.Dialog;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Client.Connection.Client;
import Client.Connection.UDPClient;
import Message.LoginConfirmationMessage;
import Message.LoginMessage;

/**
 * Dies ist die Controller-Klasse der Login-Stage. Hier wird alles implementiert, was das Login-UI manipulieren soll.
 * @author Lars Trey
 */
public class ClientLoginUIController implements Initializable {
	
	private ClientLoginUIModel model;
	
	@FXML private TextField loginNameField;
	@FXML private Button loginButton;
	@FXML private PasswordField passwordField;

	/**
	 * Initialisiert den Controller.
     * Hier werden z.B. alle Felder des UIs initialisiert, die initial beim Aufrufen des UIs gefüllt sein sollen.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    	model = new ClientLoginUIModel();

    	loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Boolean isLoggedIn =login();
                if(isLoggedIn){
            	changeScene(actionEvent);
                }
            }

			
        }); 

    }
    private Boolean login() {
		Client client = new Client();
		
		UDPClient udpClient = new UDPClient();
		udpClient.start();
		// Search for Server 
		while (udpClient.getTcpPortOfServer() == 0) {
			//TODO:show Progress
			
		}
		if (udpClient.getTcpPortOfServer() == -1) {
			//Konnte Server nicht finden!
			//TODO: Manuell Setzen
			return false;
		}
		
		//Connect to Server
		client.connect(udpClient.getIPOfServer(), udpClient.getTcpPortOfServer());
		//Send Login Message
		String name = loginNameField.getText();
		String password = passwordField.getText();
		//TODO: Get Location
		String chosenLocation = "USA";
		client.writeMessage(new LoginMessage(name, password, chosenLocation));
		LoginConfirmationMessage message = (LoginConfirmationMessage) client.readMessage();
		if(message.getSuccess()){
			//Erfolgreich angemeldet
			// TODOAusgabe: message.getInfo();
			return true;
		}
		
		return false;
	}
    private void changeScene(ActionEvent actionEvent){
    	
    	Node source = (Node) actionEvent.getSource();
    	Stage primaryStage = (Stage) source.getScene().getWindow();
    	primaryStage.close();
    	try {
			Parent root = FXMLLoader.load(getClass().getResource("ClientGameUI.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    	
    }

}