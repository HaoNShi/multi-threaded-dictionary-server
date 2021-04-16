package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Dictionary: read a JSON dictionary that supports query/add/remove functions.
 */

public class Dictionary {
	private JSONObject dictionary;

	public Dictionary() {
	}

	public Dictionary(String filePath) throws FileNotFoundException, IOException, ParseException {
		this.dictionary = new JSONObject();
		this.dictionary = readFile(filePath);
	}

	/**
	 * Read dictionary file.
	 * 
	 * @param filePath
	 * @return dictionary - JSON object
	 * @throws ParseException
	 */
	public synchronized JSONObject readFile(String filePath) throws FileNotFoundException, IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		// read file and return the JSON object
		try {
			File f = new File(filePath);
			FileReader fr = new FileReader(f);
			JSONObject jasonObject = (JSONObject) jsonParser.parse(fr);
			return jasonObject;

			// handle exceptions
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Please give valid dictionary path");
		} catch (IOException e) {
			throw new IOException("File type error. Please make sure it's JSON file.");
		} catch (ParseException e) {
			throw new ParseException(0, "File type error. Please make sure it's JSON file.");
		}
	}

	/**
	 * Query for a given word.
	 * 
	 * @param word
	 * @return response message
	 */
	public synchronized JSONObject query(String word, String meaning) {
		JSONObject message = new JSONObject();
		if (isInDict(word) && meaning == null) {
			message.put("operation", "query");
			message.put("msg", dictionary.get(word));
			message.put("state", "0");
			return message;
		} else if (isInDict(word) && meaning != null) {
			message.put("operation", "query");
			message.put("msg", "Word in dictionary. But please clear the definition field.");
			message.put("state", "1");
			return message;
		} else if (!isInDict(word) && meaning != null) {
			message.put("operation", "query");
			message.put("msg", "Word not in dictionary. Please clear the definition field. ");
			message.put("state", "2");
			return message;
		} else {
			// clearly indicate if the word was not found
			message.put("operation", "query");
			message.put("msg", "Word does not exist in the dictionary");
			message.put("state", "3");
			return message;
		}
	}

	/**
	 * Add a given word to the dictionary.
	 * 
	 * @param word
	 * @param meaning
	 * @return response message
	 */
	public synchronized JSONObject add(String word, String meaning) {
		JSONObject message = new JSONObject();
		if (isInDict(word)) {
			message.put("operation", "add");
			message.put("msg", "Word already exists in the dictionary");
			message.put("state", "1");
			return message;
		} else if (!isInDict(word) && meaning != null) {
			dictionary.put(word, meaning);
			message.put("operation", "add");
			message.put("msg", "Word has been added.");
			message.put("state", "0");
			return message;
		} else {
			message.put("operation", "add");
			message.put("msg", "Input cannot be null.");
			message.put("state", "2");
			return message;
		}
	}

	/**
	 * Remove a given word from the dictionary.
	 * 
	 * @param word
	 * @return response message
	 */
	public synchronized JSONObject remove(String word, String meaning) {
		JSONObject message = new JSONObject();
		if (isInDict(word) && meaning == null) {
			dictionary.remove(word);
			message.put("operation", "remove");
			message.put("msg", "\"" + word + "\" has been removed.");
			message.put("state", "0");
			return message;
		} else if (isInDict(word) && meaning != null) {
			message.put("operation", "remove");
			message.put("msg", "Word is in the dictionary. But please clear the definition field.");
			message.put("state", "1");
			return message;
		} else {
			message.put("operation", "remove");
			message.put("msg", "Word does not exist");
			message.put("state", "2");
			return message;
		}
	}

	public synchronized JSONObject getDictionary() {
		return dictionary;
	}

	public synchronized void setDictionary(JSONObject dictionary) {
		this.dictionary = dictionary;
	}

	/**
	 * Check if a word is in the dictionary.
	 * 
	 * @param word
	 * @return boolean
	 */
	public synchronized boolean isInDict(String word) {
		return dictionary.get(word) != null;
	}

}
