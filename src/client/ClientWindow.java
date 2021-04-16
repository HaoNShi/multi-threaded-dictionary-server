package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.simple.JSONObject;

/**
 * After the client logged in, the client can do query/add/remove on the page.
 */
public class ClientWindow extends JFrame {

	private JFrame clientFrame;
	private JTextField wordField;
	private JTextArea desField;
	private Client client;
	private JButton btnQuery;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnDisconnect;
	private JButton btnUpdate;
	private JLabel wordLabel;
	private JLabel desLabel;
	private String keyword;
	private String description;

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
		clientFrame.setBounds(100, 100, 590, 370);
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientFrame.getContentPane().setLayout(null);

		// input word
		wordField = new JTextField();
		wordField.setBounds(96, 22, 335, 26);
		clientFrame.getContentPane().add(wordField);
		wordField.setColumns(10);

		// input definition
		desField = new JTextArea();
		desField.setBounds(96, 68, 335, 241);
		clientFrame.getContentPane().add(desField);
		desField.setColumns(10);

		// When query button pressed
		btnQuery = new JButton("Query");
		btnQuery.addActionListener(new QueryActionListener());
		btnQuery.setBounds(455, 68, 100, 29);
		clientFrame.getContentPane().add(btnQuery);

		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new AddActionListener());
		btnAdd.setBounds(455, 107, 100, 29);
		clientFrame.getContentPane().add(btnAdd);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new UpdateActionListener());
		btnUpdate.setBounds(455, 185, 100, 29);
		clientFrame.getContentPane().add(btnUpdate);

		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new RemoveActionListener());
		btnRemove.setBounds(455, 146, 100, 29);
		clientFrame.getContentPane().add(btnRemove);

		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new DisconnectActionListener());
		btnDisconnect.setBounds(455, 280, 100, 29);
		clientFrame.getContentPane().add(btnDisconnect);

		wordLabel = new JLabel("Word:");
		wordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		wordLabel.setBounds(28, 26, 58, 15);
		clientFrame.getContentPane().add(wordLabel);

		desLabel = new JLabel("Description:");
		desLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		desLabel.setBounds(10, 68, 100, 15);
		clientFrame.getContentPane().add(desLabel);

		clientFrame.setVisible(true);

	}

	/**
	 * Handles when query button is pushed.
	 */
	private class QueryActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnQuery) {
				JSONObject request = new JSONObject();
				keyword = wordField.getText();
				if (keyword.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Word cannot be empty!");
				} else {
					request.put("task", "query");
					request.put("key", keyword);
					try {
						JSONObject reply = client.request(request);
						String state = reply.get("state").toString();
						String msg = reply.get("msg").toString();
						if (state.equals("0")) {
							desField.setText(msg);
						} else {
							desField.setText("");
							JOptionPane.showMessageDialog(clientFrame, msg);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Handles when add button is pushed.
	 */
	private class AddActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAdd) {
				JSONObject request = new JSONObject();
				keyword = wordField.getText();
				description = desField.getText();
				if (keyword.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Word cannot be empty!");
				} else if (description.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Description cannot be empty!");
				} else {
					request.put("task", "add");
					request.put("key", keyword);
					request.put("value", description);
					try {
						JSONObject reply = client.request(request);
						String state = reply.get("state").toString();
						String msg = reply.get("msg").toString();
						if (state.equals("0")) {
							JOptionPane.showMessageDialog(clientFrame, msg);
						} else {
							JOptionPane.showMessageDialog(clientFrame, msg);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Handles when remove button is pushed.
	 */
	private class RemoveActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnRemove) {
				JSONObject request = new JSONObject();
				keyword = wordField.getText();
				if (keyword.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Word cannot be empty!");
				} else {
					request.put("task", "remove");
					request.put("key", keyword);
					try {
						JSONObject reply = client.request(request);
						String state = reply.get("state").toString();
						String msg = reply.get("msg").toString();
						if (state.equals("0")) {
							desField.setText("");
							JOptionPane.showMessageDialog(clientFrame, msg);
						} else {
							desField.setText("");
							JOptionPane.showMessageDialog(clientFrame, msg);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Handles when update button is pushed.
	 */
	private class UpdateActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnUpdate) {
				JSONObject request = new JSONObject();
				keyword = wordField.getText();
				description = desField.getText();
				if (keyword.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Word cannot be empty!");
				} else if (description.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Description cannot be empty!");
				} else {
					request.put("task", "update");
					request.put("key", keyword);
					request.put("value", description);
					try {
						JSONObject reply = client.request(request);
						String state = reply.get("state").toString();
						String msg = reply.get("msg").toString();
						if (state.equals("0")) {
							JOptionPane.showMessageDialog(clientFrame, msg);
						} else {
							JOptionPane.showMessageDialog(clientFrame, msg);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Handles when disconnect button is pushed.
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
