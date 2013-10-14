package Client.Connection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.MaskFormatter;

public class ClientUI_deprecated {

	private JFrame frame;
	private JPanel panel1;
	private JButton connect_button;
	private JTextField hostTextfield;
	private JTextField portTextfield;
	private JLabel hostLabel;
	private JLabel portLabel;
	private JPanel top;
	private JTextArea io_textarea;
	private JPanel center;
	//private JPanel bottom;
	//private JLabel message_label;
	//private JTextField message_textfield;
	//private JButton send_button;

	
	public void run() {
		frame.setVisible(true);
	}

	
	public void hide() {
		frame.setVisible(false);
	}

	public ClientUI_deprecated() {
		initialize();

		connect_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hostTextfield.getDocument().getLength() > 0
						&& portTextfield.getDocument().getLength() > 0
						&& validIP(hostTextfield.getText()) 
						&& validPort(portTextfield.getText())) {
					//client.connect(hostTextfield.getText(),
					//		Integer.parseInt(portTextfield.getText()));
				} else {
					io_textarea.setText("Bitte die Verbindungsdaten korrekt angeben!");
				}
			}

		});

		hostTextfield.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				if (hostTextfield.getDocument().getLength() > 0
						&& portTextfield.getDocument().getLength() > 0) {
					connect_button.setEnabled(true);
				} else {
					connect_button.setEnabled(false);
				}
			}
		});

		portTextfield.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				if (hostTextfield.getDocument().getLength() > 0
						&& portTextfield.getDocument().getLength() > 0) {
					connect_button.setEnabled(true);
				} else {
					connect_button.setEnabled(false);
				}
			}
		});

	}

	private void initialize() {

		frame = new JFrame("SucheClient");
		frame.setLayout(new GridLayout(1, 1));
		frame.setSize(500, 300);
		panel1 = new JPanel(new BorderLayout());

		top = new JPanel(new FlowLayout());
		hostLabel = new JLabel("Host:");
		hostTextfield = new JTextField("", 10);
		portLabel = new JLabel("Port:");
		portTextfield = new JTextField("", 10);
		connect_button = new JButton("Connect");
		connect_button.setEnabled(false);
		top.add(hostLabel);
		top.add(hostTextfield);
		top.add(portLabel);
		top.add(portTextfield);
		top.add(connect_button);
		panel1.add(top, BorderLayout.NORTH);

		center = new JPanel(new GridLayout(1, 1));
		io_textarea = new JTextArea();
		io_textarea.setEditable(false);
		JScrollPane scroll = new JScrollPane(io_textarea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		center.add(scroll);
		panel1.add(center, BorderLayout.CENTER);

		/*bottom = new JPanel(new FlowLayout());
		message_label = new JLabel("Message:");
		message_textfield = new JTextField("", 20);
		send_button = new JButton("Send");
		bottom.add(message_label);
		bottom.add(message_textfield);
		bottom.add(send_button);
		panel1.add(bottom, BorderLayout.SOUTH);*/

		frame.add(panel1);
		frame.pack();
		frame.setVisible(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	/**
	 * IP-Format überprüfen
	 * 
	 * @author Tarek
	 * @param ip die zu prüfende IP-Adresse
	 * @return Gibt true zurück, wenn die Struktur der IP gültig ist
	 */
	private boolean validIP(String ip) {
		if (ip.equals("localhost"))
			return true;
		String[] ipParts = ip.split(".");
		if (ipParts.length != 4)
			return false;
		try {
			for (String part : ipParts) {
				if (Integer.parseInt(part) < 0 || Integer.parseInt(part) > 127)
					return false;
			}
			return true;
		} catch (NumberFormatException e) {
			// Sollte beim konvertieren der IPParts eine Exception geworfen
			// werden, ist das Format nicht korretk!
			return false;
		}
	}
	
	/**
	 * Port check
	 * 
	 * @author Tarek
	 * @param portString String des zu prüfenden Ports
	 * @return Gibt true zurück, wenn der Port zwischen 0 und 65535 liegt
	 */
	private boolean validPort(String portString) {
		try {
			int port = Integer.parseInt(portString);
			if( port >= 0 && port <= 65535) {
				return true;
			} else {
				return false;
			}
			
		} catch(NumberFormatException e) {
			// Sollte das konvertieren des Ports nicht möglich sein, ist er definitiv ungültig.
			return false;
		}
	}
}
