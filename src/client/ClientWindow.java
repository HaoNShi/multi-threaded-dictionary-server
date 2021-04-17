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
 * Client window
 * 
 * After the client login, the client can operate on the dictionary.
 */
public class ClientWindow extends JFrame {
	private static final long serialVersionUID = 8081172552102489822L;
	private JFrame clientFrame;
	private JLabel wordLabel;
	private JLabel desLabel;
	private JTextField wordField;
	private JTextArea desField;
	private JButton queryBtn;
	private JButton addBtn;
	private JButton removeBtn;
	private JButton updateBtn;
	private JButton disconnectBtn;
	private Client client;
	private String keyword;
	private String description;

	/**
	 * Create the frame.
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
		clientFrame.setBounds(100, 100, 590, 380);
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientFrame.setLocationRelativeTo(null);
		clientFrame.getContentPane().setLayout(null);

		wordLabel = new JLabel("Word:");
		wordLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		wordLabel.setBounds(28, 26, 58, 15);
		clientFrame.getContentPane().add(wordLabel);

		desLabel = new JLabel("Description:");
		desLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		desLabel.setBounds(10, 68, 100, 15);
		clientFrame.getContentPane().add(desLabel);

		wordField = new JTextField();
		wordField.setBounds(96, 22, 335, 26);
		clientFrame.getContentPane().add(wordField);
		wordField.setColumns(10);

		desField = new JTextArea();
		desField.setBounds(96, 68, 335, 241);
		clientFrame.getContentPane().add(desField);
		desField.setColumns(10);

		queryBtn = new JButton("Query");
		queryBtn.addActionListener(new QueryActionListener());
		queryBtn.setBounds(455, 68, 100, 29);
		clientFrame.getContentPane().add(queryBtn);

		addBtn = new JButton("Add");
		addBtn.addActionListener(new AddActionListener());
		addBtn.setBounds(455, 107, 100, 29);
		clientFrame.getContentPane().add(addBtn);

		removeBtn = new JButton("Remove");
		removeBtn.addActionListener(new RemoveActionListener());
		removeBtn.setBounds(455, 146, 100, 29);
		clientFrame.getContentPane().add(removeBtn);

		updateBtn = new JButton("Update");
		updateBtn.addActionListener(new UpdateActionListener());
		updateBtn.setBounds(455, 185, 100, 29);
		clientFrame.getContentPane().add(updateBtn);

		disconnectBtn = new JButton("Disconnect");
		disconnectBtn.addActionListener(new DisconnectActionListener());
		disconnectBtn.setBounds(455, 280, 100, 29);
		clientFrame.getContentPane().add(disconnectBtn);
		clientFrame.setVisible(true);
	}

	/**
	 * Handles when query button is pressed.
	 */
	private class QueryActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == queryBtn) {
				JSONObject request = new JSONObject();
				keyword = wordField.getText();
				if (keyword.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Word cannot be empty!", "Warning", 2);
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
							JOptionPane.showMessageDialog(clientFrame, msg, "Warning", 2);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Handles when add button is pressed.
	 */
	private class AddActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == addBtn) {
				JSONObject request = new JSONObject();
				keyword = wordField.getText();
				description = desField.getText();
				if (keyword.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Word cannot be empty!", "Warning", 2);
				} else if (description.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Description cannot be empty!", "Warning", 2);
				} else {
					request.put("task", "add");
					request.put("key", keyword);
					request.put("value", description);
					try {
						JSONObject reply = client.request(request);
						String state = reply.get("state").toString();
						String msg = reply.get("msg").toString();
						if (state.equals("0")) {
							JOptionPane.showMessageDialog(clientFrame, msg, "Warning", 2);
						} else {
							JOptionPane.showMessageDialog(clientFrame, msg, "Warning", 2);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Handles when remove button is pressed.
	 */
	private class RemoveActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == removeBtn) {
				JSONObject request = new JSONObject();
				keyword = wordField.getText();
				if (keyword.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Word cannot be empty!", "Warning", 2);
				} else {
					request.put("task", "remove");
					request.put("key", keyword);
					try {
						JSONObject reply = client.request(request);
						String state = reply.get("state").toString();
						String msg = reply.get("msg").toString();
						if (state.equals("0")) {
							desField.setText("");
							JOptionPane.showMessageDialog(clientFrame, msg, "Warning", 2);
						} else {
							desField.setText("");
							JOptionPane.showMessageDialog(clientFrame, msg, "Warning", 2);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Handles when update button is pressed.
	 */
	private class UpdateActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == updateBtn) {
				JSONObject request = new JSONObject();
				keyword = wordField.getText();
				description = desField.getText();
				if (keyword.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Word cannot be empty!", "Warning", 2);
				} else if (description.equals("")) {
					JOptionPane.showMessageDialog(clientFrame, "Description cannot be empty!", "Warning", 2);
				} else {
					request.put("task", "update");
					request.put("key", keyword);
					request.put("value", description);
					try {
						JSONObject reply = client.request(request);
						String state = reply.get("state").toString();
						String msg = reply.get("msg").toString();
						if (state.equals("0")) {
							JOptionPane.showMessageDialog(clientFrame, msg, "Warning", 2);
						} else {
							JOptionPane.showMessageDialog(clientFrame, msg, "Warning", 2);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Handles when disconnect button is pressed.
	 */
	private class DisconnectActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == disconnectBtn) {

				try {
					client.terminate();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					//clientFrame.dispatchEvent(new WindowEvent(clientFrame, WindowEvent.WINDOW_CLOSING));
					clientFrame.setVisible(false);
					new ConnectWindow();
				}
			}
		}
	}
}
