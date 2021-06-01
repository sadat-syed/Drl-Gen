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
public class DataCriteria {

	private String title;
	private String description;
	private List<String> children_criteria = null;
	private String derivation_operator;
	private String type;
	private String definition;
	private Boolean hard_status;
	private String code_list_id;
	private String status;
	private String property;
	
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

	private String negation_code_list_id;
	/**
	 * @return the negation_code_list_id
	 */
	public String getNegation_code_list_id() {
		return negation_code_list_id;
	}

	/**
	 * @param negation_code_list_id the negation_code_list_id to set
	 */
	public void setNegation_code_list_id(String negation_code_list_id) {
		this.negation_code_list_id = negation_code_list_id;
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

	private Boolean negation;
	private String specific_occurrence;
	private String specific_occurrence_const;
	private String source_data_criteria;
	private Boolean variable;
	private Value value;
	private Map<String, List<String>> inline_code_list;
	private List<Temporal_reference> temporal_references = null;
	private List<Subset_operator> subset_operators = null;
	private Map<String,Val> field_values;

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

	public List<String> getChildren_criteria() {
		return children_criteria;
	}

	public void setChildren_criteria(List<String> children_criteria) {
		this.children_criteria = children_criteria;
	}

	public String getDerivation_operator() {
		return derivation_operator;
	}

	public void setDerivation_operator(String derivation_operator) {
		this.derivation_operator = derivation_operator;
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

	public Boolean getHard_status() {
		return hard_status;
	}

	public void setHard_status(Boolean hard_status) {
		this.hard_status = hard_status;
	}

	public Boolean getNegation() {
		return negation;
	}

	public void setNegation(Boolean negation) {
		this.negation = negation;
	}

	public String getSpecific_occurrence() {
		return specific_occurrence;
	}

	public void setSpecific_occurrence(String specific_occurrence) {
		this.specific_occurrence = specific_occurrence;
	}

	public String getSpecific_occurrence_const() {
		return specific_occurrence_const;
	}

	public void setSpecific_occurrence_const(String specific_occurrence_const) {
		this.specific_occurrence_const = specific_occurrence_const;
	}

	public String getSource_data_criteria() {
		return source_data_criteria;
	}

	public void setSource_data_criteria(String source_data_criteria) {
		this.source_data_criteria = source_data_criteria;
	}

	public Boolean getVariable() {
		return variable;
	}

	public void setVariable(Boolean variable) {
		this.variable = variable;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}



	public List<Temporal_reference> getTemporal_references() {
		return temporal_references;
	}

	public void setTemporal_references(
			List<Temporal_reference> temporal_references) {
		this.temporal_references = temporal_references;
	}

	public List<Subset_operator> getSubset_operators() {
		return subset_operators;
	}

	public void setSubset_operators(List<Subset_operator> subset_operators) {
		this.subset_operators = subset_operators;
	}

	/**
	 * @return the inline_code_list
	 */
	public Map<String, List<String>> getInline_code_list() {
		return inline_code_list;
	}

	/**
	 * @param inline_code_list the inline_code_list to set
	 */
	public void setInline_code_list(Map<String, List<String>> inline_code_list) {
		this.inline_code_list = inline_code_list;
	}

	/**
	 * @return the field_values
	 */
	public Map<String, Val> getField_values() {
		return field_values;
	}

	/**
	 * @param field_values the field_values to set
	 */
	public void setField_values(Map<String, Val> field_values) {
		this.field_values = field_values;
	}

	@Override
	public String toString() {
		return "DataCriteria [title=" + title + ", description=" + description
				+ ", children_criteria=" + children_criteria
				+ ", derivation_operator=" + derivation_operator + ", type="
				+ type + ", definition=" + definition + ", hard_status="
				+ hard_status + ", code_list_id=" + code_list_id + ", status="
				+ status + ", property=" + property
				+ ", negation_code_list_id=" + negation_code_list_id
				+ ", negation=" + negation + ", specific_occurrence="
				+ specific_occurrence + ", specific_occurrence_const="
				+ specific_occurrence_const + ", source_data_criteria="
				+ source_data_criteria + ", variable=" + variable + ", value="
				+ value + ", inline_code_list=" + inline_code_list
				+ ", temporal_references=" + temporal_references
				+ ", subset_operators=" + subset_operators + ", field_values="
				+ field_values + "]";
	}

	
}
