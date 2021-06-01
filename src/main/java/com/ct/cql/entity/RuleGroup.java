package com.ct.cql.entity;

public enum RuleGroup {

	IPP("Initial Population", null, null),
	DENOM("Denominator", null, null),
	DENOM_EXCLS("Denominator Exclusions", null, null),
	DENOM_EXCL("Denominator Exclusion", null, null),
	NUMER("Numerator", null, null),
	DENOM_EXCEP("Denominator Exceptions", null, null),
	STRAT_1("Stratification 1", "1", "Stratification 1_1,Stratification 1_2"),
	STRAT_2("Stratification 2", "2", "Stratification 2_1,Stratification 2_2"),
	STRAT_3("Stratification 3", "3", "Stratification 3_1,Stratification 3_2");
	
	private String value1;
	private String value2;
	private String value3;
	
	private RuleGroup(String value1, String value2, String value3) {
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
	}
	
	/**
	 * Get string value for enum.
	 * 
	 * @return String value
	 */
	public String getValue1() {
		return this.value1;
	}
	
	public String getValue2() {
		return this.value2;
	}
	
	public String getValue3() {
		return this.value3;
	}
	
	public static boolean contains(String input) {
	    for (RuleGroup ruleGroup : RuleGroup.values()) {
	        if (ruleGroup.value1.equals(input)) {
	            return true;
	        }
	    }
	    return false;
	}
}
