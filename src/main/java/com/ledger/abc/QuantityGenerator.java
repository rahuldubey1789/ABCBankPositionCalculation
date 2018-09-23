package com.ledger.abc;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ledger.abc.pojo.Position;
import com.ledger.abc.utils.ABCConstants;
import com.ledger.abc.utils.Utility;
import com.ledger.abc.enums.TransactionType;

/**
 * Generate quantity based on the transactions
 *
 */
public class QuantityGenerator {

	final static Logger LOGGER = LoggerFactory.getLogger(ABCConstants.LOGGER_NAME);
	private Config config;
	private String transactionsFile;
	private Map<String, List<Position>> endOfDayPositionMap;
	private String endOfDayFile;
	private List<Position> endOfDayList;

	/**
	 * 
	 * @param config
	 * @param transactionsFile
	 */
	public QuantityGenerator(Config config, String transactionsFile, String endOfDayFile) {
		endOfDayPositionMap = new HashMap<String, List<Position>>();
		endOfDayList = new ArrayList<Position>();
		this.config = config;
		this.transactionsFile = transactionsFile;
		this.endOfDayFile = endOfDayFile;
	}

	/**
	 * This method updated the start of the position based on transactions
	 * 
	 */
	public void start() {
		// iterate through the transaction file and calculate the new quantity
		if (!new File(transactionsFile).exists()) {
			LOGGER.error("File does not exist: {} ", transactionsFile);
			return;
		}
		JSONArray jsonArray = Utility.getJSONArray(transactionsFile);
		endOfDayPositionMap.putAll(config.getInputPositionMap());
		for (int i = 0; i < jsonArray.length(); i++) {
			final JSONObject jsonObject = jsonArray.getJSONObject(i);
			final String instrument = jsonObject.get(ABCConstants.INSTRUMENT).toString();
			final String transactionType = jsonObject.get(ABCConstants.TRANSACTION_TYPE).toString();
			final Long transactionQuant = Long.valueOf(jsonObject.get(ABCConstants.TRANSACTION_QUANTITY).toString());
			final List<Position> positionList = endOfDayPositionMap.get(instrument);
			if (positionList != null) {
				for (final Position position : positionList) {
					final Long initialQuantity = position.getQuantity();
					final Long updatedQuantity = TransactionType.valueOf(transactionType)
							.calculateQuantity(initialQuantity, transactionQuant, position.getAccountType());
					// update the results
					position.setQuantity(updatedQuantity);
				}
			}
		}
		// update the delta in all
		for (final String instrument : endOfDayPositionMap.keySet()) {
			for (final Position postition : endOfDayPositionMap.get(instrument)) {
				final Long delta = postition.getDelta();
				postition.setDelta(postition.getQuantity() - delta);
				endOfDayList.add(postition);
			}
		}
		LOGGER.info("Updated the positions");
	}

	/**
	 * Print the updated position list
	 */
	public void printOutput() {
		for (final String key : endOfDayPositionMap.keySet()) {
			System.out.println(key + "," + endOfDayPositionMap.get(key).get(0).getAccountNumber() + ","
					+ endOfDayPositionMap.get(key).get(0).getAccountType() + ","
					+ endOfDayPositionMap.get(key).get(0).getQuantity() + ","
					+ endOfDayPositionMap.get(key).get(0).getDelta());
			System.out.println(key + "," + endOfDayPositionMap.get(key).get(1).getAccountNumber() + ","
					+ endOfDayPositionMap.get(key).get(1).getAccountType() + ","
					+ endOfDayPositionMap.get(key).get(1).getQuantity() + ","
					+ endOfDayPositionMap.get(key).get(1).getDelta());
		}

	}

	public void queryData() {
		Collections.sort(endOfDayList, new Comparator<Position>() {
			public int compare(Position p1, Position p2) {
				if (p1.getDelta() > p2.getDelta()) {
					return 1;
				}
				if (p1.getDelta() < p2.getDelta()) {
					return -1;
				}
				return 0;
			}
		});
		LOGGER.info("Lowest net transaction volumes is for " + endOfDayList.get(0).getInstrument());
		LOGGER.info(
				"Largest net transaction volumes is for " + endOfDayList.get(endOfDayList.size() - 1).getInstrument());
	}

	/**
	 * 
	 * @return endOfDayPositionMap
	 */
	public Map<String, List<Position>> getEndOfDayPositionMap() {
		return endOfDayPositionMap;
	}

	/**
	 * This methd writes the end of the day position file
	 * 
	 */
	public void writeOutputFile() {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(endOfDayFile);
			StringBuilder sb = new StringBuilder();
			sb.append(ABCConstants.FILE_HEADER.toString());
			sb.append(ABCConstants.NEW_LINE);
			for (final Position position : endOfDayList) {
				sb.append(position.getInstrument());
				sb.append(",");
				sb.append(position.getAccountNumber().toString());
				sb.append(",");
				sb.append(position.getAccountType());
				sb.append(",");
				sb.append(String.valueOf(position.getQuantity()));
				sb.append(",");
				sb.append(String.valueOf(position.getDelta()));
				sb.append(ABCConstants.NEW_LINE);
				fileWriter.write(sb.toString());
				sb = new StringBuilder();
			}
		} catch (Exception e) {
			LOGGER.error("Error while writing Position file {} ", e.getMessage());
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (Exception e) {
				LOGGER.error("Error while closing end of day Position file {} ", e.getMessage());
			}
		}
	}

}
