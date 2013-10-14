package Test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import Client.Connection.Client;
import Client.Connection.UDPClient;
import Constant.Constant;
import Message.IMessage;
import Message.LoginConfirmationMessage;
import Message.LoginMessage;
import Server.Location;
import Server.Connection.Server;
import Server.Connection.UDPServer;

public class ConnectionTest {
	private static Server server = null;
	private static Client client1 = null;
	private static Client client2 = null;
	private static Client client3 = null;
	private static Client client4 = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Location.initLocations();
		String ip = "127.0.0.1";
		int port = Constant.Server.TCP_PORT;
		server = Server.getServer();
		client1 = new Client();
		client1.connect(ip, port);
		client2 = new Client();
		client2.connect(ip, port);
		client3 = new Client();
		client3.connect(ip, port);
		client4 = new Client();
		client4.connect(ip, port);
		
	}

	@After
	public void afterLoginTest()  {
		//server.close();
		server.getPlayerList().clear();
	}

	@Test
	public void loginTest1() {
		
		String name="Michael";
		String password="123456";
		String chosenLocation="Deutschland";
		LoginMessage loginMessage = new LoginMessage(name, password, chosenLocation);
		client1.writeMessage(loginMessage);
		IMessage message = client1.readMessage();
		
		assertEquals("LoginConfirmationMessage", message.getType());
		
		LoginConfirmationMessage loginConfirmationMessage = (LoginConfirmationMessage) message;
		assertEquals(true, loginConfirmationMessage.getSuccess());
		
	}
	
	
	@Test
	public void loginTestSameName() {
		
		String name="Michael";
		String password="123456";
		String chosenLocation="Deutschland";
		LoginMessage loginMessage = new LoginMessage(name, password, chosenLocation);
		client3.writeMessage(loginMessage);
		IMessage message3 = client3.readMessage();
		
		assertEquals("LoginConfirmationMessage", message3.getType());
		
		LoginConfirmationMessage loginConfirmationMessage3 = (LoginConfirmationMessage) message3;
		assertEquals(true, loginConfirmationMessage3.getSuccess());
		
		client4.writeMessage(loginMessage);
		IMessage message4 = client4.readMessage();
		LoginConfirmationMessage loginConfirmationMessage4 = (LoginConfirmationMessage) message4;
		assertEquals(false, loginConfirmationMessage4.getSuccess());
		
	}
	
	@Test
	public void udpTest() { // Kann auf SAP-Rechnern, da Broadcast, möglicher Wiese nicht funktionieren.
		
		UDPClient udpClient = new UDPClient();
		udpClient.start();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(Constant.Server.TCP_PORT,udpClient.getTcpPortOfServer());
		
		
	}
	
	

}
