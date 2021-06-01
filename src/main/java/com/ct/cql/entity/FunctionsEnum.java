package com.ct.cql.entity;

public enum FunctionsEnum {

	CALCULATE_AGE_AT("calculateAgeAt(period)"),
	
	REPORTING_STRATUM("reportingStratum"),
	
	IN_VALUE_SET("InValueSet()");
	
	private String value;
	
	private FunctionsEnum(String value) {
		this.value = value;
	}
	
	/**
	 * Get string value for enum.
	 * 
	 * @return String value
	 */
	public String getValue() {
		return this.value;
	}
}
