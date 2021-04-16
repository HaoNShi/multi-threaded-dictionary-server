package client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Client main (contains login GUI for client)
 */
public class ConnectWindow extends JFrame {
	private Client client = new Client();
	private JFrame connectFrame;
	private JTextField addressField;
	private JTextField portField;
	private JButton btnConnect;
	private String address;
	private int port;
	private final int PORT_MAX = 10000;
	private JLabel addressLabel;
	private JLabel portLabel;
	public static String ADDRESS = "localhost";
	public static int PORT = 8088;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (args.length >= 2) {
						ADDRESS = args[0];
						PORT = Integer.parseInt(args[1]);
					}
					new ConnectWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public ConnectWindow() {
		connectFrame = new JFrame();
		connectFrame.setTitle("Connect");
		connectFrame.setBounds(100, 100, 450, 210);
		connectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		connectFrame.getContentPane().setLayout(null);

		// input address
		addressField = new JTextField();
		addressField.setText(ADDRESS);
		addressField.setBounds(83, 19, 320, 26);
		connectFrame.getContentPane().add(addressField);
		addressField.setColumns(10);

		// input port number
		portField = new JTextField();
		portField.setText(Integer.toString(PORT));
		portField.setColumns(10);
		portField.setBounds(83, 60, 130, 26);
		connectFrame.getContentPane().add(portField);

		// when connect button is pressed,
		// connect the client to the server
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ConnectActionListener());
		btnConnect.setBounds(150, 114, 122, 29);
		connectFrame.getContentPane().add(btnConnect);

		addressLabel = new JLabel("Address:");
		addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		addressLabel.setBounds(15, 24, 58, 15);
		connectFrame.getContentPane().add(addressLabel);

		portLabel = new JLabel("Port:");
		portLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		portLabel.setBounds(17, 65, 58, 15);
		connectFrame.getContentPane().add(portLabel);

		connectFrame.setVisible(true);
	}

	/**
	 * React when connect button is pushed.
	 */
	private class ConnectActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnConnect) {
				// server connection becomes available
				// if the address and port number are valid
				address = addressField.getText();
				if (!portField.getText().equals("")) {
					port = Integer.parseInt(portField.getText());
				}
				// server accepts "localhost" and IP addresses
				if (!address.equals("localhost")
						&& (!address.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}"))) {
					JOptionPane.showMessageDialog(connectFrame, "Error: Invalid address.");
					// port number must be digits in [0-10000]
				} else if (!portField.getText().matches("^[0-9]+$")) {
					JOptionPane.showMessageDialog(connectFrame, "Error: port number must be digits.");
				} else if (port < 0 || port > PORT_MAX) {
					JOptionPane.showMessageDialog(connectFrame, "Error: port number must be [0-10000].");
				} else {
					try {
						client.start(address, port);
						connectFrame.setVisible(false);
						new ClientWindow(client);
					} catch (Exception invalidInputs) {
						// Invalid inputs, show errors.
						JOptionPane.showMessageDialog(connectFrame, "Error: Please check the address and port.");
					}
				}
			}
		}
	}
}
