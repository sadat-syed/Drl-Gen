/*
 * @Copyright("2017 General Electric Company")
 *
 * All Rights Reserved.
 * No portions of this source code or the resulting compiled program may be used without
 * express written consent and licensing by GE Healthcare 
 */

package com.ct.hqmf.drl.model;

import java.util.List;
import java.util.Map;

/**
 * @author 212473687
 *
 */

public class SourceDataCriteria {

	private String title;
	private String description;
	private String codeListId;
	private String type;
	private String definition;
	private Boolean hardStatus;
	private Boolean negation;
	private String sourceDataCriteria;
	private Boolean variable;
	private String code_list_id;
	private String status;
	private String hard_status;
	private String source_data_criteria;
	private String property;
	private Map<String,List<String>> inline_code_list;
	private String specific_occurrence;
	private String specific_occurrence_const;
	private List<String> children_criteria;
	private String derivation_operator;
	
	/**
	 * @return the children_criteria
	 */
	public List<String> getChildren_criteria() {
		return children_criteria;
	}

	/**
	 * @param children_criteria the children_criteria to set
	 */
	public void setChildren_criteria(List<String> children_criteria) {
		this.children_criteria = children_criteria;
	}

	/**
	 * @return the derivation_operator
	 */
	public String getDerivation_operator() {
		return derivation_operator;
	}

	/**
	 * @param derivation_operator the derivation_operator to set
	 */
	public void setDerivation_operator(String derivation_operator) {
		this.derivation_operator = derivation_operator;
	}

	/**
	 * @return the specific_occurrence
	 */
	public String getSpecific_occurrence() {
		return specific_occurrence;
	}

	/**
	 * @param specific_occurrence the specific_occurrence to set
	 */
	public void setSpecific_occurrence(String specific_occurrence) {
		this.specific_occurrence = specific_occurrence;
	}

	/**
	 * @return the specific_occurrence_const
	 */
	public String getSpecific_occurrence_const() {
		return specific_occurrence_const;
	}

	/**
	 * @param specific_occurrence_const the specific_occurrence_const to set
	 */
	public void setSpecific_occurrence_const(String specific_occurrence_const) {
		this.specific_occurrence_const = specific_occurrence_const;
	}

	/**
	 * @return the inline_code_list
	 */
	public Map<String,List<String>> getInline_code_list() {
		return inline_code_list;
	}

	/**
	 * @param inline_code_list the inline_code_list to set
	 */
	public void setInline_code_list(Map<String,List<String>> inline_code_list) {
		this.inline_code_list = inline_code_list;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return the source_data_criteria
	 */
	public String getSource_data_criteria() {
		return source_data_criteria;
	}

	/**
	 * @param source_data_criteria the source_data_criteria to set
	 */
	public void setSource_data_criteria(String source_data_criteria) {
		this.source_data_criteria = source_data_criteria;
	}

	/**
	 * @return the hard_status
	 */
	public String getHard_status() {
		return hard_status;
	}

	/**
	 * @param hard_status the hard_status to set
	 */
	public void setHard_status(String hard_status) {
		this.hard_status = hard_status;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCodeListId() {
		return codeListId;
	}

	public void setCodeListId(String codeListId) {
		this.codeListId = codeListId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public Boolean getHardStatus() {
		return hardStatus;
	}

	public void setHardStatus(Boolean hardStatus) {
		this.hardStatus = hardStatus;
	}

	public Boolean getNegation() {
		return negation;
	}

	public void setNegation(Boolean negation) {
		this.negation = negation;
	}

	public String getSourceDataCriteria() {
		return sourceDataCriteria;
	}

	public void setSourceDataCriteria(String sourceDataCriteria) {
		this.sourceDataCriteria = sourceDataCriteria;
	}

	public Boolean getVariable() {
		return variable;
	}

	public void setVariable(Boolean variable) {
		this.variable = variable;
	}

}