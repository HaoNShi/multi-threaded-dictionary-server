package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ServerThread implements Runnable {
	public Dictionary dict;
	private Socket socket;

	public ServerThread(Dictionary dict, Socket socket) {
		this.dict = dict;
		this.socket = socket;
	}

	/**
	 * Parse the request to result.
	 * 
	 * @param request
	 * @return
	 */
	public JSONObject parseRequest(JSONObject request) {
		JSONObject result = null;
		String task = (String) request.get("Task");
		if (task.equals("Query")) {
			result = dict.query(request.get("Key").toString());
		} else if (task.equals("Add")) {
			result = dict.add(request.get("Key").toString(), request.get("Value").toString());
		} else if (task.equals("Remove")) {
			result = dict.remove(request.get("Key").toString());
		}
		return result;
	}

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