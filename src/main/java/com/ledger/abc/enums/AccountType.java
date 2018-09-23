package com.ledger.abc.enums;

/**
 * Enum representing AccountType values.
 *
 */
public enum AccountType {

	E("EXTERNAL"),I("INTERNAL");
	
	private String variableName;
	
	/**
     * @param variableName
     *            - name of the variable
     */
	AccountType(final String variableName) {
		this.variableName = variableName;
	}
	
	/**
     * @return variable name for the relation
     */
    public String getVariableName() {
        return variableName;
    }
	
}
