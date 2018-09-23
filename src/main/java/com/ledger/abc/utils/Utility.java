package com.ledger.abc.utils;

import java.io.BufferedReader;
import java.io.FileReader;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class
 */
public final class Utility {
	final static Logger LOGGER = LoggerFactory.getLogger(ABCConstants.LOGGER_NAME);

	private Utility() {
		// Shouldn't instantiate the class
	}

	public static String jsonAsString(String filename) {
		String result = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();
		} catch (Exception e) {
			LOGGER.error("Error while reading File {} ", e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e1) {
					; // do nothing
				}
			}
		}
		return result;
	}

	public static JSONArray getJSONArray(String filename) {
		return new JSONArray(jsonAsString(filename));
	}

}
