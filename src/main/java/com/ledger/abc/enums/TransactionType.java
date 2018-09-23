package com.ledger.abc.enums;

/**
 * Enum representing TransactionType values.
 *
 */
public enum TransactionType {
	B {
		@Override
		public Long calculateQuantity(final Long initialQuantity, final Long transactionQuantity,
				final AccountType accountType) {
			if (accountType.equals(AccountType.E)) {
				return initialQuantity + transactionQuantity;
			} else {
				return initialQuantity - transactionQuantity;
			}
		}
	},
	S {
		@Override
		public Long calculateQuantity(final Long initialQuantity, final Long transactionQuantity,
				final AccountType accountType) {
			if (accountType.equals(AccountType.E)) {
				return initialQuantity - transactionQuantity;
			} else {
				return initialQuantity + transactionQuantity;
			}
		}
	};

	/**
	 * 
	 * @param initialQuantity
	 * @param TransactionQuantity
	 * @param accountType
	 * @return updated quantity
	 */
	public abstract Long calculateQuantity(final Long initialQuantity, final Long TransactionQuantity,
			final AccountType accountType);

}
