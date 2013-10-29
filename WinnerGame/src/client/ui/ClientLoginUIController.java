package client.ui;

/**
 * Created by:
 * User: Lars Trey
 * Date: 08.10.13
 * Time: 16:38
 */

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import message.GameDataMessageToClient;
import message.LoginConfirmationMessage;
import message.LoginMessage;
import client.connection.Client;
import client.connection.UDPClient;

/**
 * Dies ist die Controller-Klasse der Login-Stage. Hier wird alles implementiert, was das Login-UI manipulieren soll.
 * @author Lars Trey
 */
public class ClientLoginUIController implements Initializable {
	
	private ClientLoginUIModel loginModel;
	
	@FXML private TextField loginNameField;
	@FXML private Button loginButton;
	@FXML private PasswordField passwordField;
	@FXML private ToggleGroup locations;
	@FXML private RadioButton germanyRadioButton;
	@FXML private RadioButton usaRadioButton;
	@FXML private RadioButton chinaRadioButton;
	@FXML private RadioButton indiaRadioButton;
	
	/**
	 * Initialisiert den Controller.
     * Hier werden z.B. alle Felder des UIs initialisiert, die initial beim Aufrufen des UIs gefüllt sein sollen.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    	loginModel = new ClientLoginUIModel();    	

    	loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Boolean isLoggedIn = login();
                if(isLoggedIn){
                	
                	changeScene(actionEvent);
                }
            }

			
        }); 
    	
    	final Tooltip germanyLocationInfo = new Tooltip();
    	final Tooltip usaLocationInfo = new Tooltip();
    	final Tooltip chinaLocationInfo = new Tooltip();
    	final Tooltip indiaLocationInfo = new Tooltip();
    	
    	germanyLocationInfo.setText("Deutschland");
    	usaLocationInfo.setText("USA");
    	chinaLocationInfo.setText("China");
    	indiaLocationInfo.setText("Indien");
    	
    	final ToggleGroup locations = new ToggleGroup();    	
    	germanyRadioButton.setTooltip(germanyLocationInfo);
    	usaRadioButton.setTooltip(usaLocationInfo);
    	chinaRadioButton.setTooltip(chinaLocationInfo);
    	indiaRadioButton.setTooltip(indiaLocationInfo);
    	germanyRadioButton.setUserData("Deutschland");
    	usaRadioButton.setUserData("USA");
    	chinaRadioButton.setUserData("China");
    	indiaRadioButton.setUserData("Indien");
    	
    }
    
    private String getChosenLocation(){
    	
    	if(locations.getSelectedToggle() != null){
    		return locations.getSelectedToggle().getUserData().toString();
    	} else {
    		return null;
    	}
    	
    }
    
    private Boolean login() {
		//Client client = new Client();
		
		//UDPClient udpClient = new UDPClient();
    	loginModel.getUdpClient().start();
		// Search for Server 
		while (loginModel.getUdpClient().getTcpPortOfServer() == 0) {
			//TODO:show Progress
			System.out.println("progress");
		}
		if (loginModel.getUdpClient().getTcpPortOfServer() == -1) {
			//Konnte Server nicht finden!
			return false;
		}
		
		//Connect to Server
		loginModel.getClient().connect(loginModel.getUdpClient().getIPOfServer(), loginModel.getUdpClient().getTcpPortOfServer());
		//Send Login Message
		String name = loginNameField.getText();
		String password = passwordField.getText();
		//TODO: Get Location
		String chosenLocation = getChosenLocation();
		loginModel.getClient().writeMessage(new LoginMessage(name, password, chosenLocation));
		LoginConfirmationMessage message = (LoginConfirmationMessage) loginModel.getClient().readMessage();
		
		ClientUIStart.setLoginModel(loginModel);
		
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

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ClientGameUI.fxml")); 
			Parent root = (Parent)fxmlLoader.load();  
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    	
    }

}