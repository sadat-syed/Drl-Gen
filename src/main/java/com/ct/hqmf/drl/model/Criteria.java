/*
 * @Copyright("2017 General Electric Company")
 *
 * All Rights Reserved.
 * No portions of this source code or the resulting compiled program may be used without
 * express written consent and licensing by GE Healthcare 
 */
package com.ct.hqmf.drl.model;

import java.util.List;

/**
 * @author 212473687
 *
 */
public class Criteria {

	private Boolean conjunction_;
	private String type;
	private String title;
	private String hqmf_id;
	private List<Precondition> preconditions = null;

	public Boolean getConjunction_() {
		return conjunction_;
	}

	public void setConjunction_(Boolean conjunction_) {
		this.conjunction_ = conjunction_;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHqmf_id() {
		return hqmf_id;
	}

	public void setHqmf_id(String hqmf_id) {
		this.hqmf_id = hqmf_id;
	}

	public List<Precondition> getPreconditions() {
		return preconditions;
	}

	public void setPreconditions(List<Precondition> preconditions) {
		this.preconditions = preconditions;
	}

	@Override
	public String toString() {
		return "Criteria [conjunction_=" + conjunction_ + ", type=" + type
				+ ", title=" + title + ", hqmf_id=" + hqmf_id
				+ ", preconditions=" + preconditions + "]";
	}

}
