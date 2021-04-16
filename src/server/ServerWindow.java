package server;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * Server window
 */
public class ServerWindow extends JFrame {
	private static final long serialVersionUID = -6364208820722038520L;
	private JFrame serverFrame;
	private JLabel infoLabel;
	private JTextArea infoTextArea;
	private JButton runBtn;
	private JButton stopBtn;
	private Server server;
	public final int PORT_MAX = 10000;
	public static String PATH = "dictionary.json";
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
						PORT = Integer.parseInt(args[0]);
						PATH = args[1];
					}
					ServerWindow window = new ServerWindow();
					window.serverFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		serverFrame = new JFrame();
		serverFrame.setTitle("Server");
		serverFrame.setBounds(100, 100, 450, 330);
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverFrame.getContentPane().setLayout(null);

		String serverInfo = "<html><body>Address: " + ADDRESS + "<br>Port: " + PORT + "<br>Dictionary Path: " + PATH
				+ "<body></html>";
		infoLabel = new JLabel(serverInfo);
		infoLabel.setVerticalAlignment(SwingConstants.TOP);
		infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		infoLabel.setBounds(32, 23, 366, 67);
		serverFrame.getContentPane().add(infoLabel);

		infoTextArea = new JTextArea();
		infoTextArea.setEditable(false);
		infoTextArea.setBounds(32, 100, 366, 132);
		serverFrame.getContentPane().add(infoTextArea);

		runBtn = new JButton("Run");
		runBtn.addActionListener(new RunActionListener());
		runBtn.setBounds(32, 242, 149, 29);
		serverFrame.getContentPane().add(runBtn);

		stopBtn = new JButton("Stop");
		stopBtn.setEnabled(false);
		stopBtn.addActionListener(new StopActionListener());
		stopBtn.setBounds(249, 242, 149, 29);
		serverFrame.getContentPane().add(stopBtn);
	}

	/**
	 * Handles when connect button is pressed.
	 */
	private class RunActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == runBtn) {
				// assign dictionary path to the dictionary
				try {
					ServerSocket socket = new ServerSocket(PORT);
					server = new Server(socket, PATH);
					new Thread(server).start();
					infoTextArea.setText("Server is running...");
					runBtn.setEnabled(false);
					stopBtn.setEnabled(true);
					JOptionPane.showMessageDialog(serverFrame, "Server runs successfully!");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(serverFrame, "Error: invalid file path.");
				}
			}
		}
	}

	/**
	 * Handles when stop button is pressed.
	 */
	private class StopActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == stopBtn) {
				try {
					if (server != null) {
						server.terminate();
						infoTextArea.setText(infoTextArea.getText() + "\nServer is closed.");
						JOptionPane.showMessageDialog(serverFrame, "Server terminated.");
					} else {
						infoTextArea.setText(infoTextArea.getText() + "\nServer not availiable.");
						JOptionPane.showMessageDialog(serverFrame, "Server not availiable.");
					}
				} catch (Exception e2) {
					infoTextArea.setText(infoTextArea.getText() + "\nError occurs when disconnecting the server.");
					JOptionPane.showMessageDialog(serverFrame, "Error occurs when disconnecting the server.");
				} finally {
					runBtn.setEnabled(true);
					stopBtn.setEnabled(false);
				}
			}
		}
	}
}
