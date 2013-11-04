package client.ui;

import java.io.IOException;

import client.connection.UDPClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Diese Klasse erzeugt das Login-UI aus der geladenen FXML-Datei und gibt ab an den vernküpften Controller.
 * @author Lars Trey
 *
 */
public class ClientUIStart extends Application {
	
	private Stage primaryStage;
	private static ClientLoginUIModel loginModel;	
	private static ClientGameUIModel gameModel;

	public static ClientLoginUIModel getLoginModel() {
		return loginModel;
	}

	public static void setLoginModel(ClientLoginUIModel loginModel) {
		ClientUIStart.loginModel = loginModel;
	}

	public static ClientGameUIModel getGameModel() {
		return gameModel;
	}

	public static void setGameModel(ClientGameUIModel gameModel) {
		ClientUIStart.gameModel = gameModel;
	}

	@Override
    public void start(Stage primaryStage) throws Exception {
    	
    	this.primaryStage = primaryStage;
    	this.primaryStage.setTitle("Planspiel - Client - Login");

    	try {
            // Lade das Login-Layout aus der FXML-Datei
    		Font.loadFont(ClientUIStart.class.getResource("segoeui.ttf").toExternalForm(), 10);
    		Font.loadFont(ClientUIStart.class.getResource("segoeuil.ttf").toExternalForm(), 10);
            FXMLLoader loader = new FXMLLoader(ClientUIStart.class.getResource("ClientLoginUI.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
    	
    	if(args!=null && args.length!=0 && !args[0].equals("")){
    		System.out.println("IP gesetzt auf: " + args[0]);
    		//Feste IP wird gesetzt um Broadcast zu umgehen.
    		UDPClient.setIpForDirectConnect(args[0]);
    	}
        launch(args);
    }

}
