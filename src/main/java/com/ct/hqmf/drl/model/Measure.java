/*
 * @Copyright("2017 General Electric Company")
 *
 * All Rights Reserved.
 * No portions of this source code or the resulting compiled program may be used without
 * express written consent and licensing by GE Healthcare 
 */

package com.ct.hqmf.drl.model;

import java.util.Map;

/**
 * @author 212473687
 *
 */
public class Measure {

	private String id;
	private String hqmf_id;
	private String hqmf_set_id;
	private Integer hqmf_version_number;
	private String title;
	private String description;
	private String cms_id;
	
	private Map<String,Criteria> population_criteria;
	private Map<String,DataCriteria> data_criteria;
	private Map<String,SourceDataCriteria> source_data_criteria;
	private Object attributes;
	private Object populations;
	private Object measure_period;

	/**
	 * @return the attributes
	 */
	public Object getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the populations
	 */
	public Object getPopulations() {
		return populations;
	}

	/**
	 * @param populations the populations to set
	 */
	public void setPopulations(Object populations) {
		this.populations = populations;
	}

	/**
	 * @return the measure_period
	 */
	public Object getMeasure_period() {
		return measure_period;
	}

	/**
	 * @param measure_period the measure_period to set
	 */
	public void setMeasure_period(Object measure_period) {
		this.measure_period = measure_period;
	}

	/**
	 * @return the population_criteria
	 */
	public Map<String, Criteria> getPopulation_criteria() {
		return population_criteria;
	}

	/**
	 * @param population_criteria the population_criteria to set
	 */
	public void setPopulation_criteria(Map<String, Criteria> population_criteria) {
		this.population_criteria = population_criteria;
	}

	/**
	 * @return the data_criteria
	 */
	public Map<String, DataCriteria> getData_criteria() {
		return data_criteria;
	}

	/**
	 * @param data_criteria the data_criteria to set
	 */
	public void setData_criteria(Map<String, DataCriteria> data_criteria) {
		this.data_criteria = data_criteria;
	}

	/**
	 * @return the source_data_criteria
	 */
	public Map<String, SourceDataCriteria> getSource_data_criteria() {
		return source_data_criteria;
	}

	/**
	 * @param source_data_criteria the source_data_criteria to set
	 */
	public void setSource_data_criteria(
			Map<String, SourceDataCriteria> source_data_criteria) {
		this.source_data_criteria = source_data_criteria;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHqmf_id() {
		return hqmf_id;
	}

	public void setHqmf_id(String hqmf_id) {
		this.hqmf_id = hqmf_id;
	}

	public String getHqmf_set_id() {
		return hqmf_set_id;
	}

	public void setHqmf_set_id(String hqmf_set_id) {
		this.hqmf_set_id = hqmf_set_id;
	}

	public Integer getHqmf_version_number() {
		return hqmf_version_number;
	}

	public void setHqmf_version_number(Integer hqmf_version_number) {
		this.hqmf_version_number = hqmf_version_number;
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

	public String getCms_id() {
		return cms_id;
	}

	public void setCms_id(String cms_id) {
		this.cms_id = cms_id;
	}

	@Override
	public String toString() {
		return "Measure [id=" + id + ", hqmf_id=" + hqmf_id + ", hqmf_set_id="
				+ hqmf_set_id + ", hqmf_version_number=" + hqmf_version_number
				+ ", title=" + title + ", description=" + description
				+ ", cms_id=" + cms_id + ", population_criteria="
				+ population_criteria + ", data_criteria=" + data_criteria
				+ ", source_data_criteria=" + source_data_criteria
				+ ", attributes=" + attributes + ", populations=" + populations
				+ ", measure_period=" + measure_period + "]";
	}
	
	
}