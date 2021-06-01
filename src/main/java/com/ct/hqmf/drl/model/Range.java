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
public class Range {

	private String type;
	private RangeValue low;
	private RangeValue high;

	/**
	 * @return the high
	 */
	public RangeValue getHigh() {
		return high;
	}

	/**
	 * @param high
	 *            the high to set
	 */
	public void setHigh(RangeValue high) {
		this.high = high;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public RangeValue getLow() {
		return low;
	}

	public void setLow(RangeValue low) {
		this.low = low;
	}

}
