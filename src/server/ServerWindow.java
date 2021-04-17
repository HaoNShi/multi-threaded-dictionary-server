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
		serverFrame.setBounds(100, 100, 492, 346);
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverFrame.setLocationRelativeTo(null);
		serverFrame.getContentPane().setLayout(null);

		String serverInfo = "<html><body>Address : " + ADDRESS + "&emsp &emsp &emsp &emsp &emsp &emsp Port : " + PORT
				+ "<br><br>Dictionary path : " + PATH + "<body></html>";
		infoLabel = new JLabel(serverInfo);
		infoLabel.setVerticalAlignment(SwingConstants.TOP);
		infoLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		infoLabel.setBounds(37, 29, 393, 61);
		serverFrame.getContentPane().add(infoLabel);

		infoTextArea = new JTextArea();
		infoTextArea.setEditable(false);
		infoTextArea.setBounds(37, 100, 393, 132);
		serverFrame.getContentPane().add(infoTextArea);

		runBtn = new JButton("Run");
		runBtn.addActionListener(new RunActionListener());
		runBtn.setBounds(37, 253, 149, 29);
		serverFrame.getContentPane().add(runBtn);

		stopBtn = new JButton("Stop");
		stopBtn.setEnabled(false);
		stopBtn.addActionListener(new StopActionListener());
		stopBtn.setBounds(281, 253, 149, 29);
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
					JOptionPane.showMessageDialog(serverFrame, "Server runs successfully!", "Message", 1);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(serverFrame, "Invalid file path or port!", "Error", 0);
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
						JOptionPane.showMessageDialog(serverFrame, "Server terminated.", "Message", 1);
					} else {
						infoTextArea.setText(infoTextArea.getText() + "\nServer not availiable.");
						JOptionPane.showMessageDialog(serverFrame, "Server not availiable!", "Warning", 2);
					}
				} catch (Exception e2) {
					infoTextArea.setText(infoTextArea.getText() + "\nError occurs when disconnecting the server.");
					JOptionPane.showMessageDialog(serverFrame, "Error occurs when disconnecting the server!", "Error",
							0);
				} finally {
					runBtn.setEnabled(true);
					stopBtn.setEnabled(false);
				}
			}
		}
	}
}
