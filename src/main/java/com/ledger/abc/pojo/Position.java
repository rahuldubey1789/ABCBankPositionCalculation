package com.ledger.abc.pojo;

import com.ledger.abc.enums.AccountType;

/**
 * POJO to hold input positions
 *
 */
public class Position {
	private Long accountNumber;
	private AccountType accountType;
	private Long quantity;
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	private Long delta;
	private String instrument;

	public Position(Long accountNumber, AccountType accountType, Long quantity, Long delta, String instrument) {
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.quantity = quantity;
		this.delta = delta;
		this.instrument=instrument;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public Long getQuantity() {
		return quantity;
	}

	public Long getDelta() {
		return delta;
	}

	public void setDelta(Long delta) {
		this.delta = delta;
	}

	public String getInstrument() {
		return instrument;
	}

}
