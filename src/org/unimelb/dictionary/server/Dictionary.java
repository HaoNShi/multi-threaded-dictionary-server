package org.unimelb.dictionary.server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Dictionary
 * <p>
 * Read a JSON dictionary that supports query/add/remove functions.
 */
public class Dictionary {
    private JSONObject dictionary;

    public Dictionary() {
    }

    public Dictionary(String filePath) throws IOException, ParseException {
        this.dictionary = new JSONObject();
        this.dictionary = readFile(filePath);
    }

    public synchronized JSONObject getDictionary() {
        return dictionary;
    }

    public synchronized void setDictionary(JSONObject dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * Check if a word is in the dictionary.
     */
    public synchronized boolean isInDict(String word) {
        return dictionary.get(word) != null;
    }

    /**
     * Read dictionary file.
     */
    public synchronized JSONObject readFile(String filePath) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        // read file and return the JSON object
        try {
            File f = new File(filePath);
            FileReader fr = new FileReader(f);
            return (JSONObject) jsonParser.parse(fr);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found!");
        } catch (IOException e) {
            throw new IOException("File type error!");
        } catch (ParseException e) {
            throw new ParseException(0, "JSON format error!");
        }
    }

    /**
     * Query for a given word.
     */
    public synchronized JSONObject query(String word) {
        JSONObject message = new JSONObject();
        if (isInDict(word)) {
            message.put("operation", "query");
            message.put("msg", dictionary.get(word));
            message.put("state", "0");
            return message;
        } else {
            // clearly indicate if the word was not found
            message.put("operation", "query");
            message.put("msg", "Word does not exist in the dictionary!");
            message.put("state", "1");
            return message;
        }
    }

    /**
     * Add a given word to the dictionary.
     */
    public synchronized JSONObject add(String word, String meaning) {
        JSONObject message = new JSONObject();
        if (isInDict(word)) {
            message.put("operation", "add");
            message.put("msg", "Word already exists in the dictionary!");
            message.put("state", "1");
            return message;
        } else {
            dictionary.put(word, meaning);
            message.put("operation", "add");
            message.put("msg", "Word has been added successfully.");
            message.put("state", "0");
            return message;
        }
    }

    /**
     * Remove a given word from the dictionary.
     */
    public synchronized JSONObject remove(String word) {
        JSONObject message = new JSONObject();
        if (isInDict(word)) {
            dictionary.remove(word);
            message.put("operation", "remove");
            message.put("msg", "Word has been removed successfully.");
            message.put("state", "0");
            return message;
        } else {
            message.put("operation", "remove");
            message.put("msg", "Word does not exist!");
            message.put("state", "1");
            return message;
        }
    }

    /**
     * Update a given word to the dictionary.
     */
    public synchronized JSONObject update(String word, String meaning) {
        JSONObject message = new JSONObject();
        if (!isInDict(word)) {
            message.put("operation", "update");
            message.put("msg", "Word does not exist!");
            message.put("state", "1");
            return message;
        } else {
            dictionary.put(word, meaning);
            message.put("operation", "update");
            message.put("msg", "Word has been updated successfully.");
            message.put("state", "0");
            return message;
        }
    }
}
