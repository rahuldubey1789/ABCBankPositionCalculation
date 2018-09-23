package com.ledger.abc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ledger.abc.enums.AccountType;
import com.ledger.abc.pojo.Position;
import com.ledger.abc.utils.ABCConstants;

/**
 * Configuration class
 *
 */
public class Config {
	final static Logger LOGGER = LoggerFactory.getLogger(ABCConstants.LOGGER_NAME);
	private String fileName;
	private Map<String, List<Position>> inputPositionMap;

	/**
	 * @param filename
	 */
	public Config(final String positionFile) {
		this.fileName = positionFile;
		inputPositionMap = new HashMap<String, List<Position>>();
		populatePositionMap();
	}

	/**
	 * This methos populate the input positions map based on the start of the
	 * day position file
	 */
	private void populatePositionMap() {
		File file = null;
		InputStream fis = null;
		BufferedReader br = null;
		try {
			file = new File(fileName);
			if (!file.exists()) {
				System.out
						.println("specified position file {} not found, reverting to default" + file.getAbsoluteFile());
				file = new File(ABCConstants.POSITIONFILE);
			}
			fis = new FileInputStream(file);
			String line; // last line we read
			br = new BufferedReader(new InputStreamReader(fis));
			line = br.readLine(); // the header
			List<Position> list;
			while ((line = br.readLine()) != null) {
				final String[] lineArray = line.split(",");
				final String instrument = lineArray[0];
				final Long accountNum = Long.valueOf(lineArray[1]);
				final AccountType accType = AccountType.valueOf(lineArray[2]);
				final Long quantity = Long.valueOf(lineArray[3]);
				if (inputPositionMap.get(instrument) == null) {
					list = new ArrayList<Position>();
					list.add(new Position(accountNum, accType, quantity, quantity, instrument));
					// initialize delta  with quantity
					inputPositionMap.put(instrument, list);
				} else {
					inputPositionMap.get(instrument)
							.add(new Position(accountNum, accType, quantity, quantity, instrument));
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found {} ", e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Error while populating Position file {} ", e.getMessage());
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e1) {
					; // do nothing
				}
			}

		}
		LOGGER.info("Populated inputPositionMap with size {} ", inputPositionMap.size());
	}

	/**
	 * 
	 * @return inputPositionMap
	 */
	public Map<String, List<Position>> getInputPositionMap() {
		return inputPositionMap;
	}
}
