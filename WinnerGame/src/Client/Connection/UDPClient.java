package Client.Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import Constant.Constant;

/**
 * Der UDP Client sendet einen Broadcast und empfängt die Antwortnachricht des
 * Servers. Nach 3 Versuchen der Verbindungsherstellung gibt der UDPClient auf.
 * 
 * @author D059270
 * 
 */
public class UDPClient extends Thread {
	DatagramSocket socket = null;
	int tcpServerPort = 0;
	String serverIP = "";
	int sentMessages=0;
	Timer timerWaitForAnswer = new Timer();

	/**
	 * Liefert den Port des Servers zurueck. Falls 0 wurde der Server noch nicht
	 * gefunden.
	 * Falls -1: Der Server wurde nach mehreren Versuchen nicht gefunden 
	 * 
	 * @return
	 */
	public int getTcpPortOfServer() {
		return tcpServerPort;
	}

	/**
	 * Liefert die IP des Servers zurueck.
	 * Falls "" wurde der Server noch nicht
	 * gefunden.
	 * Falls "Can't find Server", konnte der Server nach mehreren Veruschen nicht gefunden werden.
	 * @return
	 */
	public String getIPOfServer() {
		return serverIP;
	}

	/**
	 * Started den Thread und das Suchen
	 */
	public void run() {
		byte[] buffer = new byte[1024];

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// Kann nicht an Port lauschen
			e.printStackTrace();
		}

		while (true) {

			send(socket, "Ich suche den Spielserver!", "255.255.255.255",
					Constant.Server.UDP_PORT);

			// Nach einer Minute wird erneut eine Nachricht gesendet, da UDP
			// "unzuverlässig" ist.
			timerWaitForAnswer.schedule(timerTask(), 60000);
			//System.out.println("Wait for UDP Message from Server");
			
			String message = receive(socket, packet, buffer);
			//	System.out.println("Received UDP Message from Server");
			if (message.trim().startsWith("Ich bin der Spielserver!")) {

				tcpServerPort = Integer.parseInt(message.split(":")[1].trim());
				serverIP = packet.getAddress().toString().replace("/","");
				socket.close();
				timerWaitForAnswer.cancel();
				break;
			}

		}
	}

	private TimerTask timerTask() {
		return new TimerTask() {

			@Override
			public void run() {
				send(socket,"Ich suche den Spielserver!", "255.255.255.255",
						Constant.Server.UDP_PORT);
				if (sentMessages>3) {
					tcpServerPort=-1;
					serverIP="Can't find Server";
				}
				timerWaitForAnswer.schedule(timerTask(), 60000);

			}
		};
	}

	/**
	 * Wartet auf eine Nachricht liest diese.
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
	 * Sendet eine Nachricht an eine IP
	 * 
	 * @param nachricht
	 * @param ip
	 * @param port
	 */
	private void send(DatagramSocket socket,String nachricht, String ip, int port) {
		sentMessages++;
		try {
			System.out.println("Gesendet an " + ip + ":" + port + ": "
					+ nachricht);
			
			byte[] buffer = nachricht.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
					InetAddress.getByName(ip.replace("/", "")), port);
			socket.send(packet);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
