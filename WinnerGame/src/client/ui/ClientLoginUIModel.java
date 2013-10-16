package client.ui;

import client.connection.Client;
import client.connection.UDPClient;

public class ClientLoginUIModel {

	UDPClient udpClient = new UDPClient();
	Client client = new Client();
	
	public UDPClient getUdpClient() {
		return udpClient;
	}
	public Client getClient() {
		return client;
	}

}