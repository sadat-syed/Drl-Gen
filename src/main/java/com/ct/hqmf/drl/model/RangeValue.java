/*
 * @Copyright("2017 General Electric Company")
 *
 * All Rights Reserved.
 * No portions of this source code or the resulting compiled program may be used without
 * express written consent and licensing by GE Healthcare 
 */

package com.ct.hqmf.drl.model;

/**
 * @author 212473687
 *
 */
public class RangeValue {

	private String type;
	private String unit;
	private String value;
	private Boolean inclusive_;
	private Boolean derived_;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getInclusive_() {
		return inclusive_;
	}

	public void setInclusive_(Boolean inclusive_) {
		this.inclusive_ = inclusive_;
	}

	public Boolean getDerived_() {
		return derived_;
	}

	public void setDerived_(Boolean derived_) {
		this.derived_ = derived_;
	}

}
