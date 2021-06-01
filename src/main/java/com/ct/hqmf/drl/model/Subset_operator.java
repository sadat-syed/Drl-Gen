/*
 * @Copyright("2017 General Electric Company")
 *
 * All Rights Reserved.
 * No portions of this source code or the resulting compiled program may be used without
 * express written consent and licensing by GE Healthcare 
 */

package com.ct.hqmf.drl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author 212473687
 *
 */
public class Subset_operator {
	private String type;
	
	/*
	 * Added "value": property to avoid JSONMappinngException.
	 * Ignoring as the value for the property was set as null.
	 */
	@JsonIgnore
	private String value;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
