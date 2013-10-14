package Client.Connection;

// Client.java
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;

import Message.IMessage;

/**
 * 
 * @author D059270 Diese Klasse stellt den Client dar und sorgt für eine
 *         Verbindung mit dem Server.
 */
public class Client  {
	private Socket clientSocket;

	

	public static void main(String[] args) {
		new Client();
	}

	
	
	/**
	 * Erstellt einen Socket und verbindet mit dem Server, auf entsprechender IP und entsprechendem Port. Wird kein Server gefunden, wir dies auf der Console dokumentiert. 
	 */
	public void connect(String ip, int port) {
		Socket socket = null;
		try {
			socket = new Socket(ip, port);
			
		} catch (UnknownHostException e) {
			System.out.println("Kein Server gefunden!");

		} catch (IOException e) {
			System.out.println("Kein Server gefunden!");
		}

		this.clientSocket = socket;
	}

	/**
	 * 
	 * @param message
	 *            Schreibt eine Message an den Server, wobei die Message das
	 *            Interface IMessage implementiert haben muss.
	 */
	public void writeMessage(IMessage message) {

		ObjectOutputStream object;
		try {
			object = new ObjectOutputStream(clientSocket.getOutputStream());

			object.writeObject(message);
			object.flush();
			// object.close();
		} catch (IOException e) {
			System.out.println("Server nicht gefunden!");
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @return Wartet auf eine ankommende Message der Servers und gibt diese
	 *         zurück. Die Message muss dann auf den entsprechenden Typ gecastet
	 *         werden. Sollte die Nachricht nicht lesbar sein, wird dies auf der
	 *         Console dokumetiert.
	 */
	public IMessage readMessage() {
		ObjectInputStream objectInputStream;

		IMessage message = null;
		try {
			objectInputStream = new ObjectInputStream(
					clientSocket.getInputStream());

			try {
				message = (IMessage) objectInputStream.readObject();
				// objectInputStream.close();
			} catch (ClassNotFoundException e) {
				System.out.println("Nachricht nicht interpretierbar!");

			}
		} catch (IOException e1) {
			System.out.println("Nachricht kann nicht gelesen werden!");
		}

		return message;
	}

}
