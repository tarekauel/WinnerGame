package Server.Connection;

import java.util.ArrayList;

import Server.Company;
import Server.Location;

/**
 * 
 * @author D059270 Speichert entsprechende Daten des Spielers. Über getter und
 *         setter kann auf die Attribute zugegriffen werden.
 * 
 */

public class Player {

	private String name = "";
	private String password = "";
	private String ip = "";
	private int port;
	private ServerConnection serverConnection;
	private ArrayList<Message.IMessage> messagesFromClient = new ArrayList<Message.IMessage>();
	private ArrayList<Message.IMessage> messagesToClient = new ArrayList<Message.IMessage>();
	

	public ServerConnection getServerConnection() {
		return serverConnection;
	}

	public void setServerConnection(ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}


	

	
	/**
	 * Legt einen neuen Spieler mit den Parametern:
	 * 
	 * @param name
	 * @param password
	 * @param clientSocket
	 *            an.
	 * @throws Exception
	 *             Location ist invalid.
	 */
	public Player(String name, String password,
			ServerConnection serverConnection, String location)
			throws Exception {

		this.name = name;
		this.password = password;
		this.serverConnection = serverConnection;
		this.ip = serverConnection.getClientSocket().getInetAddress()
				.toString();
		this.port = serverConnection.getClientSocket().getPort();

		Location loc = Location.getLocationByCountry(location);

		new Company(loc, name);

	}
	

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void addMessagesToClient(Message.IMessage messagesToClient) {
		this.messagesToClient.add(messagesToClient);
	}

	public void addMessagesFromClient(Message.IMessage messagesFromClient) {
		this.messagesFromClient.add(messagesFromClient);
	}

	public ArrayList<Message.IMessage> getMessagesToClient() {
		return messagesToClient;
	}

	public ArrayList<Message.IMessage> getMessagesFromClient() {
		return messagesFromClient;
	}

}
