package org.unimelb.dictionary.client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Client
 */
public class Client {
    public final int TIME_LIMIT = 10000000;
    private Socket socket;
    private OutputStreamWriter wr;
    private BufferedReader br;
    private double time;

    /**
     * Start the client server.
     */
    public void start(String address, int port) throws Exception {
        try {
            Socket socket = new Socket(address, port);
            this.socket = socket;
            this.time = System.currentTimeMillis();
        } catch (Exception e) {
            throw new Exception("Invalid address!");
        }

        try {
            this.wr = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e1) {
            throw new Exception("Connection error!");
        }
    }

    /**
     * Terminate the client.
     */
    public void terminate() throws Exception {
        try {
            wr.close();
            br.close();
            socket.close();
        } catch (Exception e) {
            throw new Exception("Socket cannot be closed!");
        }
    }

    /**
     * Client process request.
     */
    public JSONObject request(JSONObject request) throws Exception {
        try {
            if ((System.currentTimeMillis() - this.time) <= TIME_LIMIT) {
                this.time = System.currentTimeMillis();
                wr.write(request.toString() + '\n');
                wr.flush();
                String content = br.readLine();
                JSONParser jsonParser = new JSONParser();
                JSONObject result = (JSONObject) jsonParser.parse(content);
                return result;
            } else {
                this.terminate();
                throw new Exception("Time out!");
            }
        } catch (Exception e) {
            this.terminate();
            throw new Exception("Connection error!");
        }
    }
}
