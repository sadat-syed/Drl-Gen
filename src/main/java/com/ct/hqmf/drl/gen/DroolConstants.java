package com.ct.hqmf.drl.gen;

public class DroolConstants {

	final public static String xPathIPP = "/QualityMeasureDocument/component[2]/"
			+ "populationCriteriaSection/text/xml/item/list/item/list//"
			+ "item[1]/list";
	final public static String xPathDenominator = "/QualityMeasureDocument/component[2]/"
			+ "populationCriteriaSection/text/xml/item/list/item/list//"
			+ "item[2]/list";
	final public static String xPathDenominatorExclusions = "/QualityMeasureDocument/component[2]/"
			+ "populationCriteriaSection/text/xml/item/list/item/list//"
			+ "item[3]/list";
	final public static String xPathNumerartor = "/QualityMeasureDocument/component[2]/"
			+ "populationCriteriaSection/text/xml/item/list/item/list//"
			+ "item[4]/list";
	final public static String xPathNumeratorExclusions = "/QualityMeasureDocument/component[2]/"
			+ "populationCriteriaSection/text/xml/item/list/item/list//"
			+ "item[5]/list";
	final public static String xPathDenominatorExceptions = "/QualityMeasureDocument/component[2]/"
			+ "populationCriteriaSection/text/xml/item/list/item/list//"
			+ "item[6]/list";
	final public static String eSpecFolder = "2018-eSpecs";
	final public static String outputFolder = "2018-eSpecs-Output";
	
	//Added on 13th March 2019 as part of CQL POC. - Atish Gurumurthi
	
	final public static String xPathIPP_cql = "/QualityMeasureDocument/subjectOf[28]"
			+ "/measureAttribute[1]/value[@mediaType='text/plain']/@value";
	
	final public static String xPathDenominator_Cql = "/QualityMeasureDocument/subjectOf[29]"
			+ "/measureAttribute[1]/value[@mediaType='text/plain']/@value";
	
	final public static String xPathDenominatorExclusions_Cql = "/QualityMeasureDocument/subjectOf[30]"
			+ "/measureAttribute[1]/value[@mediaType='text/plain']/@value";
	
	final public static String xPathNumerartor_Cql = "/QualityMeasureDocument/subjectOf[31]"
			+ "/measureAttribute[1]/value[@mediaType='text/plain']/@value";
	
	final public static String xPathNumeratorExclusions_Cql = "/QualityMeasureDocument/subjectOf[32]"
			+ "/measureAttribute[1]/value[@mediaType='text/plain']/@value";
	
	final public static String xPathDenominatorExceptions_Cql = "/QualityMeasureDocument/subjectOf[33]"
			+ "/measureAttribute[1]/value[@mediaType='text/plain']/@value";
	
	final public static String eSpecFolder2019 = "2019-CQL-eSpecs";
	final public static String outputFolder2019 = "2019-CQL-eSpecs-Output";
	
	 
	final public static String AND = ":AND";
	final public static String OR = ":OR";
		

}
