package com.ct.cql.entity;

import java.io.File;
import java.util.List;

public class DrlFileModel {
	private String measureDirectoryPath;
	private String measureName;
	private String drlDirectoryPath;
	private String measureId;
	private String measureVersion;
	private String fileData;
	private String measureXmlName;
	private String measureXmlFilePath;
	private File measureXmlFile;
	private String cqlXmlFilePath;
	private String drlFilePath;
	private String conditionDrlFilePath;
	private List<String> populationCriteriaList;
	boolean isEpisodeMeasure = false;
	private int stratCriteria;
	
	/*
	 * setting parameters for READING & PARSING measureFile.xml i.e.
	 * CMS122v7.xml setting parameters for WRITING
	 * ../measure/measure/measureFile.drl i.e. CMS122v7/CMS122v7/CMS122v7.drl
	 * setting parameters for WRITING .. measure/measure/measure_Conditions.drl
	 * i.e. CMS122v7/CMS122v7/CMS122v7_Conditions.drl
	 */

	public DrlFileModel(String measureDirectoryPath) {
		this.measureDirectoryPath = measureDirectoryPath;

		// traverse at folder& get parent folder's named xml i.e. CMS122V7.xml
		File folderLoc = new File(measureDirectoryPath);
		if (folderLoc.exists() & folderLoc.isDirectory()) {
			// SET FIELDS FOR READING & PARSING CMS123V7.xml
			measureXmlName = measureDirectoryPath + File.separatorChar + folderLoc.getName() + ".xml";
			measureXmlFile = new File(measureXmlName);
			if (!(measureXmlFile.exists() && measureXmlFile.isFile())) {
				measureXmlFile = null;
			}

			// SET FIELDS FOR WRITING DRL FILES
			// create folder of measure name inside measureName folder i.e .
			// ../CMS122v7/CMS122v7/CMS122V7.drl &&
			// ../CMS122v7/CMS122v7/CMS122V7_condition.drl
			//measureName = folderLoc.getName();
			setMeasureName(folderLoc.getName());
			String[] measure = getMeasureName().split("v");
			measureId = measure[0];
			measureVersion = measure[1];
			setDrlDirectoryPath(measureDirectoryPath + File.separatorChar + getMeasureName()+"_Output");

			File drlDirectory = new File(getDrlDirectoryPath());
			if (!drlDirectory.exists()){
				drlDirectory.mkdir();
				System.out.println("Directory created");
			}
			
			//SET measure isEpisode OR not.
			this.isEpisodeMeasure = Constants.EPISODE_MEASUREIDS.contains(measureId);
			
		} else {
			throw new IllegalArgumentException("Invalid FolderPath :" + measureDirectoryPath);
		}

	}

	public String getMeasureDirectoryPath() {
		return measureDirectoryPath;
	}

	public void setMeasureDirectoryPath(String measureDirectoryPath) {
		this.measureDirectoryPath = measureDirectoryPath;
	}

	public String getMeasureName() {
		return measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public String getDrlDirectoryPath() {
		return drlDirectoryPath;
	}

	public void setDrlDirectoryPath(String drlDirectoryPath) {
		this.drlDirectoryPath = drlDirectoryPath;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	public String getMeasureVersion() {
		return measureVersion;
	}

	public void setMeasureVersion(String measureVersion) {
		this.measureVersion = measureVersion;
	}

	public String getFileData() {
		return fileData;
	}

	public void setFileData(String fileData) {
		this.fileData = fileData;
	}

	public String getDrlFilePath() {
		return drlFilePath;
	}

	public void setDrlFilePath(String drlFilePath) {
		this.drlFilePath = drlFilePath;
	}

	public String getConditionDrlFilePath() {
		return conditionDrlFilePath;
	}

	public void setConditionDrlFilePath(String conditionDrlFilePath) {
		this.conditionDrlFilePath = conditionDrlFilePath;
	}

	@Override
	public String toString() {
		return "DrlFileModel [measureDirectoryPath=" + measureDirectoryPath + ", measureName=" + measureName + ", drlDirectoryPath=" + drlDirectoryPath + ", measureId=" + measureId + ", measureVersion=" + measureVersion + ", fileData=" + fileData + ", measureXmlName=" + measureXmlName + ", measureXmlFilePath="
				+ measureXmlFilePath + ", measureXmlFile=" + measureXmlFile + ", cqlXmlFilePath=" + cqlXmlFilePath + ", drlFilePath=" + drlFilePath + ", conditionDrlFilePath=" + conditionDrlFilePath + ", populationCriteriaList=" + populationCriteriaList + ", isEpisodeMeasure=" + isEpisodeMeasure + "]";
	}

	public String getMeasureXmlName() {
		return measureXmlName;
	}

	public void setMeasureXmlName(String measureXmlName) {
		this.measureXmlName = measureXmlName;
	}

	public String getMeasureXmlFilePath() {
		return measureXmlFilePath;
	}

	public void setMeasureXmlFilePath(String measureXmlFilePath) {
		this.measureXmlFilePath = measureXmlFilePath;
	}

	public File getMeasureXmlFile() {
		return measureXmlFile;
	}

	public void setMeasureXmlFile(File measureXmlFile) {
		this.measureXmlFile = measureXmlFile;
	}

	public String getCqlXmlFilePath() {
		return cqlXmlFilePath;
	}

	public void setCqlXmlFilePath(String cqlXmlFilePath) {
		this.cqlXmlFilePath = cqlXmlFilePath;
	}

	public List<String> getPopulationCriteriaList() {
		return populationCriteriaList;
	}

	public void setPopulationCriteriaList(List<String> populationCriteriaList) {
		this.populationCriteriaList = populationCriteriaList;
	}

	public boolean isEpisodeMeasure() {
		return isEpisodeMeasure;
	}

	public void setEpisodeMeasure(boolean isEpisode) {
		this.isEpisodeMeasure = isEpisode;
	}

	public int getStratCriteria() {
		return stratCriteria;
	}

	public void setStratCriteria(int stratCriteria) {
		this.stratCriteria = stratCriteria;
	}
}
