package com.ct.cql.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ct.cql.elm.model.Expression;

public class CQLModel {

	private Map<String, String> libraries;
	private LinkedHashMap<String, Expression> statements;
	private Map<String, String> valueSets;
	private Map<String, Object> expressions = new ConcurrentHashMap<>();
	private Map<String, String> codeSets;
	private List<String> populationCriteria;
	private int conditionNumber;
	private boolean isEpisodeMeasure = false;
	private int stratCount;
	private StringBuilder drlOutput;
	private String conditionDrlOutput;
	private SequenceInfo sequenceInfo;
	
	public SequenceInfo getSequenceInfo() {
		return sequenceInfo;
	}
	public void setSequenceInfo(SequenceInfo sequenceInfo) {
		this.sequenceInfo = sequenceInfo;
	}
	public int getConditionNumber() {
		return conditionNumber;
	}
	public void setConditionNumber(int conditionNumber) {
		this.conditionNumber = conditionNumber;
	}
	public Map<String, String> getCodeSets() {
		return codeSets;
	}
	public void setCodeSets(Map<String, String> codeSets) {
		this.codeSets = codeSets;
	}
	public Map<String, String> getLibraries() {
		return libraries;
	}
	public void setLibraries(Map<String, String> libraries) {
		this.libraries = libraries;
	}
	public Map<String, Object> getExpressions() {
		return expressions;
	}
	public void setExpressions(Map<String, Object> tocMap) {
		this.expressions = tocMap;
	}
	public Map<String, String> getValueSets() {
		return valueSets;
	}
	public void setValueSets(Map<String, String> valueSets) {
		this.valueSets = valueSets;
	}
	public Map<String, Expression> getStatements() {
		return statements;
	}
	public void setStatements(LinkedHashMap<String, Expression> statements) {
		this.statements = statements;
	}
	public List<String> getPopulationCriteria() {
		return populationCriteria;
	}
	public void setPopulationCriteria(List<String> populationCriteria) {
		this.populationCriteria = populationCriteria;
	}
	public StringBuilder getDrlOutput() {
		return drlOutput;
	}
	public void setDrlOutput(StringBuilder drlOutput) {
		this.drlOutput = drlOutput;
	}
	public String getConditionDrlOutput() {
		return conditionDrlOutput;
	}
	public void setConditionDrlOutput(String conditionDrlOutput) {
		this.conditionDrlOutput = conditionDrlOutput;
	}
	public boolean isEpisodeMeasure() {
		return isEpisodeMeasure;
	}
	public void setEpisodeMeasure(boolean isEpisodeMeasure) {
		this.isEpisodeMeasure = isEpisodeMeasure;
	}
	public int getStratCount() {
		return stratCount;
	}
	public void setStratCount(int stratCount) {
		this.stratCount = stratCount;
	}
	@Override
	public String toString() {
		return "CQLModel [getCodeSets()=" + getCodeSets() + ", getLibraries()="
				+ getLibraries() + ", getTocMap()=" + getExpressions()
				+ ", getValueSets()=" + getValueSets() + ", getStatements()="
				+ getStatements() +",isEpisodeMeasure()="+isEpisodeMeasure()+"]";
	}
}
