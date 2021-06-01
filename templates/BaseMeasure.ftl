<#assign measureName = measureName>
<#assign tocMap = tocMap>

package com.ct.haf.mu.engine.measure;

import java.util.List;
import org.slf4j.Logger;
import com.ct.haf.mu.entity.measureresult.ProportionResult;
import com.ct.haf.mu.entity.measureresult.ConditionAudit;
import com.ct.haf.mu.entity.measureresult.QdmElementReference;
import com.ct.haf.mu.entity.measureresult.ResultState;
import com.ct.haf.mu.entity.patientdata.QdmElement;
import com.ct.haf.mu.entity.patientdata.QdmElementAttribute;
import com.ct.haf.mu.entity.patientdata.QdmDataType;
import com.ct.haf.mu.entity.patientdata.QdmPeriod;
import com.ct.haf.mu.engine.measure.util.QdmFunction;
import com.ct.haf.mu.engine.measure.util.QdmList;
import com.ct.haf.mu.entity.patientdata.attributetype.QdmPQ;
import com.ct.haf.mu.entity.patientdata.attributetype.QdmCD;
import com.ct.haf.mu.service.api.ValueSetLookupService;
import com.ct.haf.mu.entity.patientdata.QdmAttributeName;
import com.ct.haf.mu.entity.measureresult.ReportingStratum;
import com.ct.haf.mu.entity.measureresult.PopulationCriteriaName;

// Use logging system 
global Logger logger;

// Lookup service for valuesets
global ValueSetLookupService lookupService;


//===========================================================================================================
// Initialization
//===========================================================================================================

// Initialize the settings for each population critieria
rule "Initialize Measure ${measureName} Results"
ruleflow-group "Initialize"
	when
		$result : ProportionResult(initialPatientPopulation == ResultState.NOT_DEFINED)
	then
		logger.info ("Setting up the ProportionResult Result: " + $result.getMeasureId());
		modify ($result) {
		<#if (populationCriteriaName??)> 
			setPopulationCriteriaName(PopulationCriteriaName.${populationCriteriaName}),	
		</#if>
		<#if (tocMap["Initial Population"]?? && tocMap["Initial Population"]?size > 0)> 
			setInitialPatientPopulation(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Initial Population 1"]?? && tocMap["Initial Population 1"]?size > 0)>
			setInitialPatientPopulation(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Initial Population 2"]?? && tocMap["Initial Population 2"]?size > 0)>
			setInitialPatientPopulation(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Initial Population 3"]?? && tocMap["Initial Population 3"]?size > 0)>
			setInitialPatientPopulation(ResultState.NOT_EVALUATED),
		</#if>
		<#if (tocMap["Denominator"]?? && tocMap["Denominator"]?size > 0)> 
			setDenominator(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Denominator 1"]?? && tocMap["Denominator 1"]?size > 0)> 
			setDenominator(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Denominator 2"]?? && tocMap["Denominator 2"]?size > 0)> 
			setDenominator(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Denominator 3"]?? && tocMap["Denominator 3"]?size > 0)> 
			setDenominator(ResultState.NOT_EVALUATED),
		</#if>
		<#if (tocMap["Denominator Exclusions"]?? && tocMap["Denominator Exclusions"]?size > 0)>
			setDenominatorExclusions(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Denominator Exclusion"]?? && tocMap["Denominator Exclusion"]?size > 0)>
			setDenominatorExclusions(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Denominator Exclusions 1"]?? && tocMap["Denominator Exclusions 1"]?size > 0) || 
		(tocMap["Denominator Exclusion 1"]?? && tocMap["Denominator Exclusion 1"]?size > 0)>
			setDenominatorExclusions(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Denominator Exclusions 2"]?? && tocMap["Denominator Exclusions 2"]?size > 0)> || 
		(tocMap["Denominator Exclusion 2"]?? && tocMap["Denominator Exclusion 2"]?size > 0)>
			setDenominatorExclusions(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Denominator Exclusions 3"]?? && tocMap["Denominator Exclusions 3"]?size > 0)> || 
		(tocMap["Denominator Exclusion 3"]?? && tocMap["Denominator Exclusion 3"]?size > 0)>
			setDenominatorExclusions(ResultState.NOT_EVALUATED),
		</#if>
		<#if (tocMap["Denominator Exceptions"]?? && tocMap["Denominator Exceptions"]?size > 0)>
			setDenominatorExceptions(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Denominator Exceptions 1"]?? && tocMap["Denominator Exceptions 1"]?size > 0)>
			setDenominatorExceptions(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Denominator Exceptions 2"]?? && tocMap["Denominator Exceptions 2"]?size > 0)>
			setDenominatorExceptions(ResultState.NOT_EVALUATED),
		<#elseif (tocMap["Denominator Exceptions 3"]?? && tocMap["Denominator Exceptions 3"]?size > 0)>
			setDenominatorExceptions(ResultState.NOT_EVALUATED),
		</#if>
		<#if (tocMap["Numerator"]?? && tocMap["Numerator"]?size > 0)>
			setNumerator(ResultState.NOT_EVALUATED)
		<#elseif (tocMap["Numerator 1"]?? && tocMap["Numerator 1"]?size > 0)>
			setNumerator(ResultState.NOT_EVALUATED)
		<#elseif (tocMap["Numerator 2"]?? && tocMap["Numerator 2"]?size > 0)>
			setNumerator(ResultState.NOT_EVALUATED)
		<#elseif (tocMap["Numerator 3"]?? && tocMap["Numerator 3"]?size > 0)>
			setNumerator(ResultState.NOT_EVALUATED)
		</#if>
			
		}
end