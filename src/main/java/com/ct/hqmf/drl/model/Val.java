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
public class Val {

	private String type;
	private String code_list_id;
	private String title;
	
	/*
	 * Added "low": property to avoid JSONMappinngException.
	 * Ignoring as the value for the property was set as null.
	 */
	@JsonIgnore
	private String low;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode_list_id() {
		return code_list_id;
	}

	public void setCode_list_id(String code_list_id) {
		this.code_list_id = code_list_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

}
