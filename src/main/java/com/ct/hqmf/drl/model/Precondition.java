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
public class Precondition {

	private Integer id;
	private List<Precondition> preconditions = null;

	private String reference;

	private Boolean negation;

	/**
	 * @return the negation
	 */
	public Boolean getNegation() {
		return negation;
	}

	/**
	 * @param negation
	 *            the negation to set
	 */
	public void setNegation(Boolean negation) {
		this.negation = negation;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the preconditions
	 */
	public List<Precondition> getPreconditions() {
		return preconditions;
	}

	/**
	 * @param preconditions
	 *            the preconditions to set
	 */
	public void setPreconditions(List<Precondition> preconditions) {
		this.preconditions = preconditions;
	}

	private String conjunction_code;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConjunction_code() {
		return conjunction_code;
	}

	public void setConjunction_code(String conjunction_code) {
		this.conjunction_code = conjunction_code;
	}

	@Override
	public String toString() {
		return "Precondition [id=" + id + ", preconditions=" + preconditions
				+ ", reference=" + reference + ", negation=" + negation
				+ ", conjunction_code=" + conjunction_code + "]";
	}

}
