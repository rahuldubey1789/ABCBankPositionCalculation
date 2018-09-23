package com.ledger.abc;

import com.ledger.abc.utils.ABCConstants;

/**
 * Main class
 */
public class ABCPositionCalculation {

	public static void main(String[] args) {
		// make the objects we will need
		String inputPositionFile = ABCConstants.POSITIONFILE;
		String transactionsFile = ABCConstants.TRANSACTIONFILE;
		String endOfDayFile = ABCConstants.ENDOFDAYPOSITIONFILE;
		for (int i = 0; i < (args.length); i++) {
			if (args[i].equals("-p") && i < (args.length - 1)) { // inputPositionFile
				inputPositionFile = args[i + 1];
			}
			if (args[i].equals("-t") && i < (args.length - 1)) { // transactionsFile
				transactionsFile = args[i + 1];
			}
			if (args[i].equals("-o") && i < (args.length - 1)) { // outputFile
				endOfDayFile = args[i + 1];
			}
		}
		Config config = new Config(inputPositionFile);
		QuantityGenerator quantityGen = new QuantityGenerator(config, transactionsFile,endOfDayFile);
		quantityGen.start();
		quantityGen.queryData();
		quantityGen.writeOutputFile();
	}
}
