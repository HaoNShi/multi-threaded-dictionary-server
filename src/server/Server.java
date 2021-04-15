package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

/**
 * Server: Dictionary Server that is available for clients to use.
 */
public class Server implements Runnable {

	private Dictionary dict;
	private ServerSocket socket;
	private ArrayList<ServerThread> clients;

	public Server(ServerSocket socket, String filepath) throws FileNotFoundException, IOException, ParseException {
		this.dict = new Dictionary(filepath);
		this.socket = socket;
		this.clients = new ArrayList<ServerThread>();
	}

	/**
	 * Run the server.
	 */
	public void run() {
		try {
			// server continues to wait and accept a connection
			while (true) {
				try {
					Socket client = socket.accept();
					ServerThread server = new ServerThread(dict, client);
					clients.add(server);
					new Thread(server).start();
				} catch (Exception e) {
					throw new Exception("Error: client lost.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Terminate the server.
	 */
	public void terminate() throws Exception {
		try {
			for (ServerThread s : clients) {
				s.getSocket().close();
			}
			socket.close();
		} catch (IOException g) {
			throw new Exception("Error: Server cannot be closed.");
		}
	}

}