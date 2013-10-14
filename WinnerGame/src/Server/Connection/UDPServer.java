package Server.Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Der UDP Server lauscht an einem definierten UDP-Port und wartet auf Broadcast
 * von Clients und antwortet mit der IP und den TCP-Port
 * 
 * @author D059270
 * 
 */
public class UDPServer extends Thread {
	DatagramSocket socket = null;
	int tcpGamePort;
	int udpGamePort;

	public UDPServer(int tcpGamePort, int udpGamePort) {
		this.tcpGamePort = tcpGamePort;
		this.udpGamePort = udpGamePort;
	}

	/**
	 * Started den Thread
	 */
	public void run() {
		byte[] buffer = new byte[1024];

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		try {
			socket = new DatagramSocket(udpGamePort);
		} catch (SocketException e) {
			// Kann nicht an Port lauschen
			e.printStackTrace();
		}

		while (true) {
			//System.out.println("Wait for UDP Messages:");
			String message = receive(socket, packet, buffer);
		 //	System.out.println("Received UDP Message:");
			if (message.trim().equals("Ich suche den Spielserver!")) {

				String responseMessage = "Ich bin der Spielserver!Port:"
						+ tcpGamePort;
				send(responseMessage, packet.getAddress().toString(),
						packet.getPort());

			}

		}
	}

	/**
	 * Schlieﬂt den UDP Listener
	 */
	public void close() {
		socket.close();
	}

	/**
	 * Wartet auf eine Nachricht von einem Client
	 * 
	 * @param socket
	 * @param packet
	 * @param buffer
	 * @return
	 */
	private String receive(DatagramSocket socket, DatagramPacket packet,
			byte[] buffer) {

		try {
			socket.receive(packet);
		} catch (IOException e) {
			// Kann nicht an Port lauschen
			e.printStackTrace();
		}
		buffer = packet.getData();
		String message = new String(buffer);
		System.out.println(message);

		return message;
	}

	/**
	 * Sendet dem Client eine UDP-Nachricht
	 * 
	 * @param nachricht
	 * @param ip
	 * @param port
	 */
	private void send(String nachricht, String ip, int port) {
		try {
			System.out.println("Gesendet an " + ip + ":" + port + ": "
					+ nachricht);
			DatagramSocket socket = new DatagramSocket();
			byte[] buffer = nachricht.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
					InetAddress.getByName(ip.replace("/", "")), port);
			socket.send(packet);
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
