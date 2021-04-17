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
 * Connect window
 * 
 * Client entrance, enter the address and port to connect to the server.
 */
public class ConnectWindow extends JFrame {
	private static final long serialVersionUID = -4613468624372407369L;
	private static Client client = new Client();
	private static JFrame connectFrame;
	private JLabel addressLabel;
	private JLabel portLabel;
	private JTextField addressField;
	private JTextField portField;
	private JButton connectBtn;
	public static String ADDRESS = "localhost";
	public static int PORT = 8088;
	private final static int PORT_MAX = 10000;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ConnectWindow();
					if (args.length >= 2) {
						connect(args[0], args[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConnectWindow() {
		connectFrame = new JFrame();
		connectFrame.setTitle("Connect");
		connectFrame.setBounds(100, 100, 450, 210);
		connectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		connectFrame.setLocationRelativeTo(null);
		connectFrame.getContentPane().setLayout(null);

		addressLabel = new JLabel("address:");
		addressLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		addressLabel.setBounds(15, 24, 58, 15);
		connectFrame.getContentPane().add(addressLabel);

		portLabel = new JLabel("Port:");
		portLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		portLabel.setBounds(17, 65, 58, 15);
		connectFrame.getContentPane().add(portLabel);

		addressField = new JTextField();
		addressField.setText(ADDRESS);
		addressField.setBounds(83, 19, 320, 26);
		connectFrame.getContentPane().add(addressField);
		addressField.setColumns(10);

		portField = new JTextField();
		portField.setText(Integer.toString(PORT));
		portField.setColumns(10);
		portField.setBounds(83, 60, 130, 26);
		connectFrame.getContentPane().add(portField);

		connectBtn = new JButton("Connect");
		connectBtn.addActionListener(new ConnectActionListener());
		connectBtn.setBounds(150, 114, 122, 29);
		connectFrame.getContentPane().add(connectBtn);

		connectFrame.setVisible(true);
	}

	/**
	 * Handles when connect button is pressed.
	 */
	private class ConnectActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == connectBtn) {
				String address = addressField.getText();
				String port = portField.getText();
				connect(address, port);
			}
		}
	}

	/**
	 * Connect server.
	 */
	private static void connect(String address, String port) {
		// server accepts "localhost" and IP addresses
		if (!address.equals("localhost") && (!address.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}"))) {
			JOptionPane.showMessageDialog(connectFrame, "Invalid address!", "Error", 0);
		} else if (port.equals("") || !port.matches("^[0-9]+$")) {
			JOptionPane.showMessageDialog(connectFrame, "Port number must be positive integer!", "Error", 0);
		} else {
			ADDRESS = address;
			PORT = Integer.parseInt(port);
			if (PORT > PORT_MAX) {
				JOptionPane.showMessageDialog(connectFrame, "Exceed the maximum number of port!\n(port<10000)",
						"Warning", 2);
			} else {
				try {
					client.start(ADDRESS, PORT);
					connectFrame.setVisible(false);
					new ClientWindow(client);
				} catch (Exception invalidInputs) {
					// when the port is not correct or server is closed
					JOptionPane.showMessageDialog(connectFrame, "Connect Error!\nPlease check the port or server.",
							"Warning", 2);
				}
			}
		}
	}
}
