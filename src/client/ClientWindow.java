package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.simple.JSONObject;

/**
 * Request Page: after the client logged in, the client can do query/add/remove
 * on the page.
 */
public class ClientWindow extends JFrame {

	private JFrame clientFrame;
	private JTextField wordField;
	private JTextField defField;
	private Client client;
	private JButton btnQuery;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnDisconnect;
	private JLabel wordLabel;
	private JLabel lblNewLabel;

	/**
	 * Create the application.
	 */
	public ClientWindow(Client client) {
		initialize(client);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize(Client client) {
		this.client = client;
		clientFrame = new JFrame();
		clientFrame.setTitle("Client");
		clientFrame.setBounds(100, 100, 560, 400);
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientFrame.getContentPane().setLayout(null);

		// input word
		wordField = new JTextField();
		wordField.setBounds(81, 22, 435, 26);
		clientFrame.getContentPane().add(wordField);
		wordField.setColumns(10);

		// input definition
		defField = new JTextField();
		defField.setBounds(81, 68, 435, 178);
		clientFrame.getContentPane().add(defField);
		defField.setColumns(10);

		// When query button pressed
		btnQuery = new JButton("Query");
		btnQuery.addActionListener(new QueryActionListener());
		btnQuery.setBounds(81, 266, 117, 29);
		clientFrame.getContentPane().add(btnQuery);

		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new AddActionListener());
		btnAdd.setBounds(243, 266, 117, 29);
		clientFrame.getContentPane().add(btnAdd);

		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new RemoveActionListener());
		btnRemove.setBounds(399, 266, 117, 29);
		clientFrame.getContentPane().add(btnRemove);

		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new DisconnectActionListener());
		btnDisconnect.setBounds(243, 305, 117, 29);
		clientFrame.getContentPane().add(btnDisconnect);

		wordLabel = new JLabel("Word:");
		wordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		wordLabel.setBounds(10, 26, 58, 15);
		clientFrame.getContentPane().add(wordLabel);

		lblNewLabel = new JLabel("Definition:");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 68, 73, 15);
		clientFrame.getContentPane().add(lblNewLabel);

		clientFrame.setVisible(true);

	}

	/**
	 * QueryActionListener: handles when query button is pushed
	 */
	private class QueryActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnQuery) {
				JSONObject request = new JSONObject();
				request.put("Task", "Query");
				request.put("Key", wordField.getText());
				try {
					JSONObject reply = client.request(request);
					defField.setText(reply.toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * AddActionListener: handles when add button is pushed
	 */
	private class AddActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btnAdd) {
				JSONObject request = new JSONObject();
				request.put("Task", "Add");
				request.put("Key", wordField.getText());
				request.put("Value", defField.getText());
				try {
					JSONObject reply = client.request(request);
					JOptionPane.showMessageDialog(clientFrame, reply.toJSONString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				wordField.setText("");
				defField.setText("");

			}
		}
	}

	/**
	 * RemoveActionListener: handles when remove button is pushed.
	 */
	private class RemoveActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnRemove) {
				JSONObject request = new JSONObject();
				request.put("Task", "Remove");
				request.put("Key", wordField.getText());
				try {
					JSONObject reply = client.request(request);
					JOptionPane.showMessageDialog(clientFrame, reply.toJSONString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				wordField.setText("");

			}
		}
	}

	/**
	 * DisconnectActionListener: handles when disconnect button is pushed.
	 */
	private class DisconnectActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnDisconnect) {

				try {
					client.terminate();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					clientFrame.dispatchEvent(new WindowEvent(clientFrame, WindowEvent.WINDOW_CLOSING));
					clientFrame.setVisible(false);
				}
			}
		}
	}
}
