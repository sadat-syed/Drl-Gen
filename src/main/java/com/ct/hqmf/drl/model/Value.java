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

public class Value {

	private String type;
	private RangeValue high;
	private RangeValue low;
	private String code_list_id;
	private String title;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the code_list_id
	 */
	public String getCode_list_id() {
		return code_list_id;
	}

	/**
	 * @param code_list_id the code_list_id to set
	 */
	public void setCode_list_id(String code_list_id) {
		this.code_list_id = code_list_id;
	}

	/**
	 * @return the low
	 */
	public RangeValue getLow() {
		return low;
	}

	/**
	 * @param low the low to set
	 */
	public void setLow(RangeValue low) {
		this.low = low;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public RangeValue getHigh() {
		return high;
	}

	public void setHigh(RangeValue high) {
		this.high = high;
	}

}
