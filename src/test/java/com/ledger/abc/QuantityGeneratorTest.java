package com.ledger.abc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import com.ledger.abc.utils.ABCConstants;
import com.ledger.abc.enums.AccountType;
import com.ledger.abc.pojo.Position;

public class QuantityGeneratorTest {
	private Config config;
	private QuantityGenerator quantityGenerator;
	private List<Position> expectedPositonList;

	@Before
	public void setUp() {
		config = new Config(ABCConstants.POSITIONFILE);
		quantityGenerator = new QuantityGenerator(config, ABCConstants.TRANSACTIONFILE,null);
		quantityGenerator.start();
		expectedPositonList = new ArrayList<Position>();
		expectedPositonList.add(new Position(101L, AccountType.valueOf("E"), 101000L, 1000L, "IBM"));
		expectedPositonList.add(new Position(201L, AccountType.valueOf("I"), -101000L, -1000L, "IBM"));
	}

	@Test
	public void testEndOfDayList() {
		List<Position> IBMPositionList = quantityGenerator.getEndOfDayPositionMap().get("IBM");
		Assert.assertEquals(expectedPositonList.get(0).getDelta(), IBMPositionList.get(0).getDelta());
		Assert.assertEquals(expectedPositonList.get(1).getDelta(), IBMPositionList.get(1).getDelta());
	}

}
