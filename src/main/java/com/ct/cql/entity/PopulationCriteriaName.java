package com.ct.cql.entity;

/*
 * Copyright (c) 2013 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
 
public enum PopulationCriteriaName {
	/**
	 * Default Case
	 */
	STANDARD ("Standard", "0", ""),
	/**
	 * Population Criteria 1
	 */
	POPULATION_CRITERIA_1 ("PopulationCriteria1", "1", "_p1"),
	/**
	 * Population Criteria 2
	 */
	POPULATION_CRITERIA_2 ("PopulationCriteria2", "2", "_p2"),
	/**
	 * Population Criteria 3
	 */
	POPULATION_CRITERIA_3 ("PopulationCriteria3", "3", "_p3");
	
	private String stringValue;
	private String intValue;
	private String fileValue;
	
	private PopulationCriteriaName(String stringValue, String intValue, String fileValue) {
		this.stringValue = stringValue;
		this.intValue = intValue;
		this.fileValue = fileValue;
	}

	/**
	 * @return code
	 */
	public String getStringValue() {
		return this.stringValue;
	}
	
	public String getIntValue() {
		return this.intValue;
	}

	public String getFileValue() {
		return this.fileValue;
	}


	/**
	 * Get populationCriteriaName by int value
	 * @param value value
	 * @return populationCriteriaName, exception if not found
	 */
	@SuppressWarnings("nls")
	public static PopulationCriteriaName fromInt(String stringValue) {
		for (PopulationCriteriaName populationCriteriaName : PopulationCriteriaName.values()) {
			if (populationCriteriaName.getStringValue().equals(stringValue)) {
				return populationCriteriaName;
			}
		}
		throw new IllegalArgumentException("bad populationCriteriaName: " + stringValue);
	}
}
