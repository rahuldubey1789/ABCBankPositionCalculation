package com.ledger.abc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ledger.abc.enums.AccountType;
import com.ledger.abc.pojo.Position;
import com.ledger.abc.utils.ABCConstants;

public class ConfigTest {
	private Config config;
	private Map<String, List<Position>> expectedMap;

	@Before
	public void setUp() {
		config = new Config(ABCConstants.POSITIONFILE);
		expectedMap = new HashMap<String, List<Position>>();
		List<Position> postionList = new ArrayList<Position>();
		postionList.add(new Position(101L, AccountType.valueOf("E"), 100000L, 100000L, "IBM"));
		postionList.add(new Position(201L, AccountType.valueOf("I"), -100000L, -100000L, "IBM"));
		expectedMap.put("IBM", postionList);
		postionList.clear();
		postionList.add(new Position(101L, AccountType.valueOf("E"), 10000L, 10000L, "APPL"));
		postionList.add(new Position(201L, AccountType.valueOf("I"), -10000L, -10000L, "APPL"));
		expectedMap.put("APPL", postionList);
	}

	@Test
	public void testAssertMap() {
		Map<String, List<Position>> arrivedMap = config.getInputPositionMap();
		Assert.assertNotNull("arrivedMap Map is null;", arrivedMap);
		Assert.assertEquals("Size mismatch for maps;", expectedMap.size(), arrivedMap.size());
		Assert.assertTrue("Missing keys in arrived map;", arrivedMap.keySet().containsAll(expectedMap.keySet()));
	}
}
