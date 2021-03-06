package templates;

import static org.junit.Assert.assertEquals;
import message.IMessage;
import message.LoginConfirmationMessage;
import message.LoginMessage;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import server.connection.Server;
import client.connection.Client;
import constant.Constant;

public class ConnectionTest {
	private static Server server = null;
	private static Client client1 = null;
	private static Client client2 = null;
	private static Client client3 = null;
	private static Client client4 = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
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
		
		server.getPlayerList().clear();
	}

	@Test
	public void loginTest1() {
		
		String name="Max";
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
	
	
	
	

}
