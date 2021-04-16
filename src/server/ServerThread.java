package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * ServerThread
 */
public class ServerThread implements Runnable {
	public Dictionary dict;
	private Socket socket;

	public ServerThread(Dictionary dict, Socket socket) {
		this.dict = dict;
		this.socket = socket;
	}

	/**
	 * Parse the request to result.
	 */
	public JSONObject parseRequest(JSONObject request) {
		JSONObject result = null;
		String task = request.get("task").toString();
		if (task.equals("query")) {
			result = dict.query(request.get("key").toString());
		} else if (task.equals("add")) {
			result = dict.add(request.get("key").toString(), request.get("value").toString());
		} else if (task.equals("remove")) {
			result = dict.remove(request.get("key").toString());
		} else if (task.equals("update")) {
			result = dict.update(request.get("key").toString(), request.get("value").toString());
		}
		return result;
	}

	/**
	 * Run the server.
	 */
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			OutputStreamWriter wr = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
			String content;
			JSONParser jsonParser = new JSONParser();
			while ((content = br.readLine()) != null) {
				JSONObject request = (JSONObject) jsonParser.parse(content);
				JSONObject result = parseRequest(request);
				wr.write(result.toString() + '\n');
				wr.flush();
			}
			br.close();
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
