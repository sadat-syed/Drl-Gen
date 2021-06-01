package com.ct.cql.entity;

import java.util.List;
import java.util.Map;

public class SequenceInfo {
	
	boolean ipEvaluted ;
	boolean denomEvaluted ;
	boolean numEvaluted ;
	Map<String, Boolean> checkMap;
	
	Map<Integer, List<String>> childMapDetails;
	
	public SequenceInfo(boolean ipEvaluted, boolean denomEvaluted, boolean numEvaluted) {
		super();
		this.ipEvaluted = ipEvaluted;
		this.denomEvaluted = denomEvaluted;
		this.numEvaluted = numEvaluted;
	}
	public boolean isIpEvaluted() {
		return ipEvaluted;
	}
	public void setIpEvaluted(boolean ipEvaluted) {
		this.ipEvaluted = ipEvaluted;
	}
	public boolean isDenomEvaluted() {
		return denomEvaluted;
	}
	public void setDenomEvaluted(boolean denomEvaluted) {
		this.denomEvaluted = denomEvaluted;
	}
	public boolean isNumEvaluted() {
		return numEvaluted;
	}
	public void setNumEvaluted(boolean numEvaluted) {
		this.numEvaluted = numEvaluted;
	}
	public Map<String, Boolean> getCheckMap() {
		return checkMap;
	}
	public void setCheckMap(Map<String, Boolean> checkMap) {
		this.checkMap = checkMap;
	}
	public Map<Integer, List<String>> getChildMapDetails() {
		return childMapDetails;
	}
	public void setChildMapDetails(Map<Integer, List<String>> childMapDetails) {
		this.childMapDetails = childMapDetails;
	}
	
	

}
