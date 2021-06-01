<#assign measureName = measureName>
<#assign measureId = measureId>
<#assign measureVersion = measureVersion>
<#assign tocMap = tocMap>

package com.ct.haf.mu.engine.measure;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import org.slf4j.Logger;
import java.text.SimpleDateFormat;
import java.util.List;
import com.ct.haf.mu.entity.measureresult.ProportionResult;
import com.ct.haf.mu.entity.measureresult.ConditionAudit;
import com.ct.haf.mu.entity.measureresult.QdmElementReference;
import com.ct.haf.mu.entity.measureresult.CalcJob;
import com.ct.haf.mu.entity.measureresult.ResultState;
import com.ct.haf.mu.entity.measureresult.ResultName;
import com.ct.haf.mu.entity.measureresult.ReportingStratum;
import com.ct.haf.mu.entity.patientdata.QdmElement;
import com.ct.haf.mu.entity.patientdata.QdmElementAttribute;
import com.ct.haf.mu.entity.patientdata.QdmCategory;
import com.ct.haf.mu.entity.patientdata.QdmDataType;
import com.ct.haf.mu.entity.patientdata.QdmPeriod;
import com.ct.haf.mu.entity.patientdata.attributetype.QdmCD;
import com.ct.haf.mu.engine.measure.util.QdmFunction;
import com.ct.haf.mu.service.api.ValueSetLookupService;
import com.ct.haf.mu.entity.patientdata.QdmAttributeName;


// Use logging system 
global Logger logger;

// Lookup service for valuesets
global ValueSetLookupService lookupService;

// List of Episode QdmElement's Source Element Ids 
global List<String> episodeSrcElementIdList;

// List of Episode Start Dates
global List<Integer> episodeProviderIdStartDateList;

//salienceNumber to set priority(Highest Ranking is High Priority)
 <#assign salienceNumber = 101>

//===========================================================================================================
// Initialization
//===========================================================================================================
<@generateInitializeEpisodeRule/>
<@generateInitializePopulationRule/>

<#list tocMap?keys as key> 
<#if key == "Initial Population">
<#assign queryVariable = tocMap[key]>
		<#assign childCondGreater = false>
		<#assign periodVariable = "$audit.measurePeriod">
<#if (queryVariable.childConditionNumber?has_content)>
		<#assign childCondGreater = true>
		<#assign periodVariable = "$elementPeriod">
</#if>
<@generateInitializeCalcRule tocMap queryVariable/>

</#if>
</#list> 


 
<#-- define macro for creating rule for query variable --> 
<#macro generateInitializeEpisodeRule>
// Initialize the EpisodeSrcElementIdList
rule "Initialize - EpisodeSrcElementIdList"
ruleflow-group "Initialize"
salience ${salienceNumber-1}
	when
	then
		logger.debug ("Initializing episodeSrcElementIdList :"+salienceNumber);
		episodeSrcElementIdList = new ArrayList<String>();
		drools.getWorkingMemory().setGlobal("episodeSrcElementIdList", episodeSrcElementIdList);
		episodeProviderIdStartDateList = new ArrayList<Integer>();
		drools.getWorkingMemory().setGlobal("episodeProviderIdStartDateList", episodeProviderIdStartDateList);
end
</#macro>

<#macro generateInitializePopulationRule>
// Initialize the settings for default proportion result
rule "Initialize Measure ${measureName} Default Results"
ruleflow-group "Initialize"
	when
		$result : ProportionResult(initialPatientPopulation == ResultState.NOT_DEFINED)
	then
		logger.debug ("Setting up the ProportionResult Result: " + $result.getMeasureId());
		modify ($result) {
			setInitialPatientPopulation(ResultState.NOT_EVALUATED),
		<#if (tocMap["Denominator"]?? && tocMap["Denominator"]?size > 0)> 
			setDenominator(ResultState.NOT_EVALUATED),
		</#if>
		<#if (tocMap["Denominator Exclusions"]?? && tocMap["Denominator Exclusions"]?size > 0)>
			setDenominatorExclusions(ResultState.NOT_EVALUATED),
		</#if>
		<#if (tocMap["Denominator Exceptions"]?? && tocMap["Denominator Exceptions"]?size > 0)>
			setDenominatorExceptions(ResultState.NOT_EVALUATED),
		</#if>
		<#if (tocMap["Numerator"]?? && tocMap["Numerator"]?size > 0)>
			setNumerator(ResultState.NOT_EVALUATED)
		</#if>	
		}
end
</#macro>

<#macro generateInitializeCalcRule tocMap queryVariable>
// Initialize the settings for each proportion result
rule "Initialize ${measureName} MeasureResults"
ruleflow-group "Initialize"
when
		$calcJob : CalcJob(measureId in ("${measureId}"), measureVersion == ${measureVersion})
		PatientMeasureData($patientId : patientId)
<@generateIPPQueryVariable tocMap queryVariable/>
not ProportionResult(calcJob == $calcJob, providerId == providerId, patientId == $patientId, episodeId == $episodeId)
	then
		logger.debug("Checking srcElementId: "+$srcElementId+ " of QdmElement: "+$episodeId+ " in the srcElementIdList: "+episodeSrcElementIdList);
		if(!episodeSrcElementIdList.contains($srcElementId)) {
			logger.debug("Checking providerId start date: "+$providerIdStartDate+ " in the episodeProviderIdStartDateList: "+episodeProviderIdStartDateList);
			if(!episodeProviderIdStartDateList.contains($providerIdStartDate)) {
				logger.debug("Creating Episode Result for QdmElement: "+$episodeId+ ", srcQdmElement: "+$srcElementId);
				ProportionResult result = new ProportionResult($calcJob, providerId, $patientId, $episodeId);
				result.setInitialPatientPopulation(ResultState.NOT_EVALUATED);
				result.setDenominator(ResultState.NOT_EVALUATED); 
				result.setNumerator(ResultState.NOT_EVALUATED);
				<#if (tocMap["Denominator Exceptions"]?? && tocMap["Denominator Exceptions"]?size > 0)>
				result.setDenominatorExceptions(ResultState.NOT_EVALUATED);
				<#else>
				result.setDenominatorExceptions(ResultState.NOT_DEFINED);
				</#if>
				<#if (tocMap["Denominator Exclusions"]?? && tocMap["Denominator Exclusions"]?size > 0)>
				result.setDenominatorExclusions(ResultState.NOT_EVALUATED);
				<#else>
				result.setDenominatorExclusions(ResultState.NOT_DEFINED);
				</#if>
				insert(result);
				episodeSrcElementIdList.add($srcElementId);
				logger.debug("Adding Episode with srcElementId "+$srcElementId+ " in the List: "+episodeSrcElementIdList);
				episodeProviderIdStartDateList.add($providerIdStartDate);
				logger.debug("Adding providerId start date "+$providerIdStartDate+" of Episode "+$srcElementId+" in the List: "+episodeProviderIdStartDateList);
				<#if measureId == "177">
				episodeIdList.add($episodeId);//only for CMS177v
				logger.debug("Adding episodeId "+$episodeId +" of Episode "+$episodeId+" in the List: "+episodeIdList);
				</#if>
			}
			else {
				logger.debug("Discarding Episode with srcElementId "+$srcElementId+" as providerId start date "+$providerIdStartDate+" is already in the List: "+episodeProviderIdStartDateList);
			}
		}
end
</#macro>

<#macro generateIPPQueryVariable tocMap query>
	<#assign constraintcounter = 0>
		<#assign listElements = []>
		<@generateElement tocMap query/>
		 <#list query.constraintList as constraint>
	   		<#assign listElements = evaluateConstraint(constraint, listElements, query ,query.constraintList,tocMap,constraintcounter)/>
	   		<#assign constraintcounter = constraintcounter + 1 >
	 	</#list>
	 	<#list listElements as elements>
   		${elements}
	 	</#list>
 	 	<#if query.constraintList?size == 0 && query.valueSetDataTypeMap?has_content>
	 	<@generateElement tocMap query/>
	 	<#elseif query.constraintList?size == 0 && query.dependentObjects?has_content>
	 	<#list query.dependentObjects as singleDependentObjects>
	 	<#if tocMap[singleDependentObjects]?has_content>
	 	<@generateIPPQueryVariable tocMap tocMap[singleDependentObjects]/>
	 	</#if>
	 	</#list>
	 	</#if>
 	<#if query.dependentObjects?has_content && query.constraintList?has_content && query.valueSetDataTypeMap?has_content>
	 	<#list query.dependentObjects as dependentObj>
		 	<#list query.constraintList as constraint>
		 	<#assign leftOperand = (constraint.leftOperand)!"">
			<#assign rightOperand = (constraint.rightOperand)!"">
		 		<#if (dependentObj?contains(leftOperand) || dependentObj?contains(rightOperand))>
		 		 <#assign queryVariable = tocMap[dependentObj]>
		 			<#if query.operator??>
		 			${query.operator}
		 			ConditionAudit(sequenceId == ${queryVariable.conditionNumber}, qualifies == ResultState.MET)
		 			</#if>
		 		</#if>
		 	</#list>
	 	</#list>
 	</#if>
</#macro>

<#macro generateElement tocMap queryVariable>
<#assign valueSetDataTypeMap = false>
	<#if queryVariable.valueSetDataTypeMap?has_content>
	<#assign valueSetDataTypeMap = true>
	</#if>
	<#if valueSetDataTypeMap>
	<#if queryVariable.isLastOperator()>
	     QdmList($qdmElementId : mostRecentId != -1) from collect (
	 </#if>
	 <#if queryVariable.qdmPeriodVariable?has_content && tocMap[queryVariable.qdmPeriodVariable]?has_content>
	 $elementPeriod: QdmPeriod(name == "${queryVariable.qdmPeriodVariable}")
	 </#if>
		QdmElement(
				$episodeId : qdmElementId,
				$srcElementId : srcElementId,
				$episodePeriod : period,
				$providerIdStartDate : providerIdStartDate,
				patientId == $patientId,
				<@genValueSetCodeSetDataType queryVariable/>
	 		)
	 			<#if queryVariable.isLastOperator()>
	    	)
	 </#if>
	</#if> 
</#macro>

<#macro genValueSetCodeSetDataType queryVariable>
		<#assign counter = 0>
		<#assign ValidEncounterCounter = 0>
		<#assign mapSize = queryVariable.valueSetDataTypeMap?size>
		<#assign keys = queryVariable.valueSetDataTypeMap?keys>
	<#if mapSize gt 1>
		<#if counter == 0>
			    (
		</#if>
		<#list keys as key>
			    (dataType == QdmDataType.${key} &&
		<#if queryVariable.valueSetDataTypeMap[key]?starts_with("V-")>
				lookupService.isChild("${queryVariable.valueSetDataTypeMap[key]?substring(2, queryVariable.valueSetDataTypeMap[key]?length)}", codeSystemOid, codeValue))
		<#else>
				codeValue == "${queryVariable.valueSetDataTypeMap[key]?substring(2, queryVariable.valueSetDataTypeMap[key]?length)}")
		</#if>
			<#assign counter = counter + 1>
		<#if counter != 0 && counter != (mapSize)>
				||
		</#if>
		</#list> 
		<@generateQdmFunction queryVariable/>
		<#if counter == mapSize>
				)
		</#if>
	<#else>
			<#list keys as key>
				dataType == QdmDataType.${key},
		<#if key == 'ENCOUNTER_PERFORMED'>
				isSourceListEntry($audit.providerId),	
		</#if>
		<#if queryVariable.childQueryVariable?has_content>
		 	<#if queryVariable.childQueryVariable.dependentObjects?size == 0 && queryVariable.childQueryVariable.constraintList?size == 0 && queryVariable.childQueryVariable.valueSetDataTypeMap?size == 0>
		 	<#if queryVariable.childQueryVariable.qdmFunctionVariable?has_content && queryVariable.childQueryVariable.arithmeticRightOperand?has_content && queryVariable.childQueryVariable.qdmPeriodVariable?has_content && queryVariable.childQueryVariable.intervalOperator??>
		 		<#assign intervalLowValue = queryVariable.childQueryVariable.intervalOperator.lowValue>
				<#assign intervalLowClosed= queryVariable.childQueryVariable.intervalOperator.lowClosed>
				<#assign intervalHighValue = queryVariable.childQueryVariable.intervalOperator.highValue>
				<#assign intervalHighClosed= queryVariable.childQueryVariable.intervalOperator.highClosed>
				<#assign childQdmFunction = queryVariable.childQueryVariable.qdmFunctionVariable>
				<#if childQdmFunction == "AfterStartOrConcurrentWithEnd">
				<#assign childQdmFunction = "startsAfterOrConcurrentWithEnd">
				<#elseif childQdmFunction == "startsOrConcurrentWithAfterStart">
				<#assign childQdmFunction = "startsAfterOrConcurrentWithStart">
				<#elseif childQdmFunction == "AfterStartOrConcurrentWithAfterStart">
				<#assign childQdmFunction = "startsAfterOrConcurrentWithStart">
				</#if>
				<#if queryVariable.childQueryVariable.equalsOperatorClosed == 1 && intervalLowClosed==true>
				QdmFunction.${childQdmFunction}(period, $audit.measurePeriod,  QdmPeriod.${queryVariable.childQueryVariable.qdmPeriodVariable}) > 0 && ==${queryVariable.childQueryVariable.arithmeticRightOperand},
						<#elseif intervalLowClosed==true && intervalHighClosed==false>
				QdmFunction.${childQdmFunction}(period, $audit.measurePeriod,  QdmPeriod.${queryVariable.childQueryVariable.qdmPeriodVariable}) >= 0 && <${queryVariable.childQueryVariable.arithmeticRightOperand},
						<#elseif intervalLowClosed==true && intervalHighClosed==true>
				QdmFunction.${childQdmFunction}(period, $audit.measurePeriod,  QdmPeriod.${queryVariable.childQueryVariable.qdmPeriodVariable}) >= 0 && <=${queryVariable.childQueryVariable.arithmeticRightOperand},
						<#elseif intervalLowClosed==true && intervalHighClosed==false>
				QdmFunction.${childQdmFunction}(period, $audit.measurePeriod,  QdmPeriod.${queryVariable.childQueryVariable.qdmPeriodVariable}) > 0 && <=${queryVariable.childQueryVariable.arithmeticRightOperand},
						<#elseif intervalLowClosed==false && intervalHighClosed==false>
				QdmFunction.${childQdmFunction}(period, $audit.measurePeriod,  QdmPeriod.${queryVariable.childQueryVariable.qdmPeriodVariable})) > 0 && <${queryVariable.childQueryVariable.arithmeticRightOperand},
						</#if>
		 	</#if>
		 	</#if>	
		</#if>
			<@generateQdmFunction queryVariable/>
			<#if queryVariable.valueSetDataTypeMap[key]??>
		<#if queryVariable.valueSetDataTypeMap[key]?contains(",")>
			<#list queryVariable.valueSetDataTypeMap[key]?split(",") as x>
			<#if x?is_last>
				<#if x?starts_with("V-")>
				lookupService.isChild("${x?substring(2, x?length)}", codeSystemOid, codeValue)
				<#else>
				codeValue == "${x?substring(2, x?length)}" 
				</#if>
			<#else>
				<#if x?starts_with("V-")>
				lookupService.isChild("${x?substring(2, x?length)}", codeSystemOid, codeValue) ||
				<#else>
				codeValue == "${x?substring(2, x?length)}" || 
				</#if>
			</#if>
			<#assign ValidEncounterCounter = ValidEncounterCounter + 1>
			</#list>
		<#else>
			<#if queryVariable.valueSetDataTypeMap[key]?starts_with("V-")>
				lookupService.isChild("${queryVariable.valueSetDataTypeMap[key]?substring(2, queryVariable.valueSetDataTypeMap[key]?length)}", codeSystemOid, codeValue)
			<#else>
				codeValue == "${queryVariable.valueSetDataTypeMap[key]?substring(2, queryVariable.valueSetDataTypeMap[key]?length)}"
			</#if>
		</#if>
		</#if>
		</#list> 
		<#assign counter = counter + 1>
	</#if>
</#macro>


<#macro generateQdmFunction queryVariable>
	<#assign qdmFunction = queryVariable.qdmFunctionVariable>
	<#if (qdmFunction)?has_content>
	<#if qdmFunction?contains(",")>
	<#list qdmFunction?split(",") as singleQdmFunction>
	<#if queryVariable.childConditionNumber?has_content>
		 	<#list queryVariable.childConditionNumber as singleChildCondnNum>
		 	<#if singleQdmFunction?index == singleChildCondnNum?index>
		 	<#if singleQdmFunction == "ends" || singleQdmFunction == "starts">
				QdmFunction.${singleQdmFunction}During(period, ${periodVariable}${singleChildCondnNum}),
			<#elseif singleQdmFunction == "startsOrConcurrentWithAfterStart" || singleQdmFunction == "startsAfterOrConcurrentWithStart">
			<#if queryVariable.qdmPeriodVariable?? && queryVariable.arithmeticOperand??>
			QdmFunction.startsAfterOrConcurrentWithStart(period, ${periodVariable}${singleChildCondnNum},QdmPeriod.${queryVariable.qdmPeriodVariable}) >= 0 && < ${queryVariable.arithmeticRightOperand},
			<#else>
			QdmFunction.startsAfterOrConcurrentWithStart(period, ${periodVariable}${singleChildCondnNum}),
			</#if>
			<#else>
				QdmFunction.${singleQdmFunction}(period, ${periodVariable}${singleChildCondnNum}),
			</#if>
			<#else>
			<#if singleQdmFunction == "startsOrConcurrentWithAfterStart" || singleQdmFunction == "startsAfterOrConcurrentWithStart"
			|| singleQdmFunction == "StartStartOrConcurrentWithAfterStart">
			<#if queryVariable.qdmPeriodVariable?? && queryVariable.arithmeticOperand??>
				QdmFunction.startsAfterOrConcurrentWithStart(period, ${periodVariable}${singleChildCondnNum},QdmPeriod.${queryVariable.qdmPeriodVariable}) >= 0 && < ${queryVariable.arithmeticRightOperand},
			<#else>
				QdmFunction.startsAfterOrConcurrentWithStart(period, ${periodVariable}${singleChildCondnNum}),
			</#if>
			</#if>
			</#if>
		 	</#list>
	<#else>
	<#if singleQdmFunction?is_first>
				(
	</#if>
	<#if singleQdmFunction?is_last>
	<#assign seperator = "),">
	<@getSingleQdmFunction queryVariable singleQdmFunction periodVariable seperator/>
	<#else>
	<#assign seperator = "||">
	<@getSingleQdmFunction queryVariable singleQdmFunction periodVariable seperator/>
	</#if>
	</#if>
	</#list>
	<#else>
	<#assign ChildCondnNum = "">
	<#list queryVariable.childConditionNumber as singleChildCondnNum>
	<#assign ChildCondnNum = singleChildCondnNum>
	</#list>
		<#if qdmFunction == "endsBeforeEnd" || qdmFunction == "startsBeforeEnd" || qdmFunction == "startsBeforeStart" 
				|| qdmFunction == "endsBeforeStart" || qdmFunction == "endsBeforeOrConcurrentWithEnd" 
				|| qdmFunction == "startsBeforeOrConcurrentWithEnd" || qdmFunction =="endsBeforeOrConcurrentWithStart" 
				|| qdmFunction =="startsBeforeOrConcurrentWithStart"  || qdmFunction == "startsAfterOrConcurrentWithStart"
				 || qdmFunction == "startsOrConcurrentWithAfterStart" || qdmFunction == "startsOnConcurrentDayStart"
				 || qdmFunction == "startsStartBeforeStart" || qdmFunction == "startsStartAfterStart">
				 <#if qdmFunction == "startsOrConcurrentWithAfterStart">
				 <#assign qdmFunction = "startsAfterOrConcurrentWithStart">
				 <#elseif qdmFunction == "startsOnConcurrentDayStart">
				  <#assign qdmFunction = "startsOnConcurrentDay">
				  <#elseif qdmFunction == "startsStartBeforeStart">
				  <#assign qdmFunction = "startsBeforeStart">
				  <#elseif qdmFunction == "startsStartAfterStart">
				  <#assign qdmFunction = "startsAfterStart">
				 </#if>
		 	<#if queryVariable.qdmPeriodVariable?? && queryVariable.arithmeticOperand??>
				QdmFunction.${qdmFunction}(period, ${periodVariable}${ChildCondnNum},QdmPeriod.${queryVariable.qdmPeriodVariable}) >= 0 && <= ${queryVariable.arithmeticRightOperand},
			<#else>
				QdmFunction.${qdmFunction}(period, ${periodVariable}${ChildCondnNum}),
			</#if>
		<#elseif qdmFunction == "duringStartAfterStart">
			 	QdmFunction.startsAfterStart(period, ${periodVariable}${ChildCondnNum}),
		<#elseif qdmFunction == "during">
				QdmFunction.during(period, ${periodVariable}${ChildCondnNum}),
		<#elseif qdmFunction == "starts">
				QdmFunction.startsDuring(period, ${periodVariable}${ChildCondnNum}),
		<#elseif qdmFunction == "duringStartBeforeStartduring">
				QdmFunction.during(period, ${periodVariable}),
			<#if (queryVariable.parentConditionNumber > 0)>
				QdmFunction.startsBeforeEnd(period, $audit.elementPeriod),
			<#else>
				QdmFunction.startsBeforeEnd(period, $audit.measurePeriod),
			</#if>
			<#elseif qdmFunction == "ends" || qdmFunction == "starts">
				QdmFunction.${qdmFunction}During(period, ${periodVariable}),
				<#else>
				QdmFunction.${qdmFunction}(period, ${periodVariable}),
		</#if>
	</#if>
	</#if>
</#macro>

<#function evaluateConstraint constraint listElements queryVariable constraintList tocMap constraintcounter>
<#assign qdmelementAlias = getElementAlias(listElements)>
<#assign leftOperand = (constraint.leftOperand)!"">
<#assign rightOperand = (constraint.rightOperand)!"">
<#assign operator = constraint.operator>
<#if queryVariable.intervalOperator??>
<#assign intervalLowValue = queryVariable.intervalOperator.lowValue>
<#assign intervalLowClosed= queryVariable.intervalOperator.lowClosed>
<#assign intervalHighValue = queryVariable.intervalOperator.highValue>
<#assign intervalHighClosed= queryVariable.intervalOperator.highClosed>
<#assign isLastOperator= queryVariable.isLastOperator>
</#if>
<#assign qdmPeriodVariable= queryVariable.qdmPeriodVariable?has_content>
<#if operator == "GreaterOrEqual">
    <#if leftOperand ?contains ("CalendarAgeInYearsAt")>   
 <#assign element>
 QdmElement(
			$qdmElementId : qdmElementId,
			patientId == $audit.patientId,
			dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
				QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS) >= ${rightOperand})
 </#assign>
 <#assign newElements = listElements + [element]>
 	<#return newElements>
 	
 	<#elseif leftOperand ?contains ("CalendarAgeInDaysAt")>
 	<#assign element>
 	QdmElement(
			$qdmElementId : qdmElementId,
			patientId == $audit.patientId,
			dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
		 		QdmFunction.startsAfterStart(period, $elementPeriod, QdmPeriod.DAYS) >= ${rightOperand}
 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	<#elseif leftOperand ?contains ("CalendarAgeInMonthsAt")>
 	<#assign element>
 	QdmElement(
			$qdmElementId : qdmElementId,
			patientId == $audit.patientId,
			dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
		 		QdmFunction.startsAfterStart(period, $elementPeriod, QdmPeriod.MONTHS) >= ${rightOperand})
 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 <#else>
 <#assign element>
			QdmElementAttribute(
                  	qdmElementId == $qdmElementId,
					attributeName == QdmAttributeName.RESULT,
					attributeValue instanceof QdmPQ,
					qdmPq.quantity > ${rightOperand},
                  	$qdmAttributeId : qdmAttributeId)
			</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	</#if>
	
	 <#elseif operator == "In">
     <#if leftOperand ?contains ("CalendarAgeInYearsAt")>    
 	 <#assign element>
	 	QdmElement(
				$${qdmelementAlias} : qdmElementId,
				patientId == $audit.patientId,
				dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
				<#if queryVariable.equalsOperatorClosed == 1 && intervalLowClosed==true>
				QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS) > ${intervalLowValue} && ==${intervalHighValue})
				<#elseif intervalLowClosed==true && intervalHighClosed==false>
				QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS) >= ${intervalLowValue} && <${intervalHighValue})
				<#elseif intervalLowClosed==true && intervalHighClosed==true>
				QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS) >= ${intervalLowValue} && <=${intervalHighValue})
				<#elseif intervalLowClosed==true && intervalHighClosed==false>
				QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS) > ${intervalLowValue} && <=${intervalHighValue})
				<#elseif intervalLowClosed==false && intervalHighClosed==false>
				QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS) > ${intervalLowValue} && <${intervalHighValue})
				</#if>
	 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	</#if>
	<#if leftOperand ?contains ("CalendarAgeInDaysAt")>    
 	 <#assign element>
				<#if queryVariable.equalsOperatorClosed == 1 && intervalLowClosed==true>
			QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.DAYS) > ${intervalLowValue} && ==${intervalHighValue})
				<#elseif intervalLowClosed==true && intervalHighClosed==false>
			QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.DAYS) >= ${intervalLowValue} && <${intervalHighValue})
				<#elseif intervalLowClosed==true && intervalHighClosed==true>
			QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.DAYS) >= ${intervalLowValue} && <=${intervalHighValue})
				<#elseif intervalLowClosed==true && intervalHighClosed==false>
			QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.DAYS) > ${intervalLowValue} && <=${intervalHighValue})
				<#elseif intervalLowClosed==false && intervalHighClosed==false>
			QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.DAYS) > ${intervalLowValue} && <${intervalHighValue})
				</#if>
	 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	</#if>
 	
	 <#elseif operator == "InValueSet">
     <#assign element>
	 	QdmElementAttribute(
                  	qdmElementId == $qdmElementId,
                  	attributeValue instanceof QdmCD,
                  	lookupService.isChild("${leftOperand}", qdmCd.codingSystem, qdmCd.codeValue),
                  	$qdmAttributeId : qdmAttributeId)
 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	
 	<#elseif operator == "Equivalent">
 	<#if constraint.codeSetList??>
     	<#assign element>
	 	QdmElementAttribute(
                  	qdmElementId == $qdmElementId,
                  	<#list constraint.codeSetList as i>
                  	codeValue == "${i}",
					</#list>
                  	$qdmAttributeId : qdmAttributeId)
 	</#assign>
 	</#if>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	
 	<#elseif operator == "Greater">
     <#assign element>
	 	QdmElementAttribute(
                  	qdmElementId == $qdmElementId,
					attributeName == QdmAttributeName.RESULT,
					qdmPq.quantity > ${rightOperand},
                  	$qdmAttributeId : qdmAttributeId)
 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	
 	<#elseif operator == "IsNull">
 	<#assign element>
 	not QdmElementAttribute(
			qdmElementId == $qdmElementId,
			attributeName == QdmAttributeName.RESULT)
 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	
 	<#elseif operator == "IsNotNull">	
	  <#if leftOperand?has_content && leftOperand?contains (".result") && queryVariable.name?contains(leftOperand)>
	  <#assign element>
  		QdmElementAttribute(
			qdmElementId == $qdmElementId, 
			attributeName == QdmAttributeName.RESULT, 
			$qdmAttributeId : qdmAttributeId)
			</#assign>
		<#assign newElements = listElements + [element]>
 		<#return newElements>
  	</#if>
 	
 <#elseif operator == "Less">
	<#if leftOperand ?contains ("CalendarAgeInYearsAt")>
 	<#assign element>
 	QdmElement(
			$qdmElementId : qdmElementId,
			patientId == $audit.patientId,
			dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
					QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS)< ${rightOperand} && >= 0)
 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	<#else>
 	<#if qdmPeriodVariable?has_content>
 	<#assign element>
 	QdmElementAttribute(
			qdmElementId == $qdmElementId, 
			attributeValue instanceof QdmPQ,
			qdmPq.quantity < ${rightOperand},
			$qdmAttributeId : qdmAttributeId)
 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	</#if>
 	</#if>
 	
 	<#elseif operator == "LessOrEqual">
	<#if leftOperand ?contains ("CalendarAgeInYearsAt")>
 	<#assign element>
 	QdmElement(
			$qdmElementId : qdmElementId,
			patientId == $audit.patientId,
			dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
					QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS)<= ${rightOperand} && >= 0)
 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	<#elseif leftOperand ?contains ("CalendarAgeInDaysAt")>
 	<#assign element>
 	QdmElement(
			$qdmElementId : qdmElementId,
			patientId == $audit.patientId,
			dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
		 		QdmFunction.startsAfterStart(period, $elementPeriod, QdmPeriod.DAYS) >= 0 && <= ${rightOperand},
 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	</#if>
 	
 	<#elseif operator == "Equal">
	<#if leftOperand ?contains ("CalendarAgeInYearsAt")>
 	<#assign element>
 	QdmElement(
			$qdmElementId : qdmElementId,
			patientId == $audit.patientId,
			dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
					QdmFunction.startsBeforeEnd(period, $audit.measurePeriod, QdmPeriod.YEARS) == ${rightOperand})
 	</#assign>
 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 	</#if>
 	
 	
 	
  <#elseif operator == "during">	
	 <#assign operands = getOperands(leftOperand, "during")>
	 <#assign element = ""/>
	 <#assign leftoperand = (operands[0])?split(".")[0]>
	 <#if operands[1]??>
	 <#assign rightoperand = (operands[1])?split(".")[0]>
	 <#else>
	 <#assign rightoperand = "MP">
	 </#if>
	 <#assign rightperiod = "">
	 <#if rightoperand?trim == "MP">
	 	<#assign rightperiod = "$audit.measurePeriod">
	 <#elseif tocMap[rightoperand]??>
		 <#assign rightperiod = "$elementPeriod">
		 <#assign rightQuery = tocMap[rightOperand]>
	 <#assign element>
	 $${rightQuery.name} :ConditionAudit(sequenceId == ${rightQuery.conditionNumber}, qualifies == ResultState.MET)
	 QdmElementReference(audit == $${rightQuery.name}, $occurrenceQdmElementId : qdmElementId)
	 QdmElement(qdmElementId == $occurrenceQdmElementId, $elementPeriod : period)
	 </#assign>
	 </#if>
	<#assign newElements = listElements + [element]>
 	<#return newElements>
	 
  <#elseif operator == "Exists">	
  <#if tocMap[leftOperand]?has_content>
	 <#assign variable = tocMap[leftOperand]>
	<#assign element><@generateIPPQueryVariable tocMap variable/></#assign>
	<#assign newElements = listElements + [element]>
 	<#return newElements>
	</#if>	
	<#elseif operator == "NotExists">	
  <#if tocMap[leftOperand]?has_content>
	 <#assign variable = tocMap[leftOperand]>
	<#assign element> not ConditionAudit(sequenceId == ${variable.conditionNumber}, qualifies == ResultState.MET)</#assign>
	<#assign newElements = listElements + [element]>
 	<#return newElements>
	</#if>	
	
 <#elseif operator == "ExpressionRef">	
	 <#assign variable = tocMap[leftOperand]>
	 <#if variable?has_content>
	<#assign element>ConditionAudit(sequenceId == ${variable.conditionNumber}, qualifies == ResultState.MET)</#assign>
	<#assign newElements = listElements + [element]>
 	<#return newElements>
	</#if>	
	
 <#elseif operator == "QueryAnd">	
 <#assign count = listElements?size>	
	<#assign leftQuery = (tocMap[leftOperand])!"">
	<#assign rightQuery = (tocMap[rightOperand])!"">
	<#assign queryName = queryVariable.name>
	<#if leftQuery?has_content && rightQuery?has_content>
        <#assign element><@generateIPPQueryVariable tocMap leftQuery/>
        <@generateIPPQueryVariable tocMap rightQuery/>
	    </#assign>
	    <#elseif leftQuery?has_content && listElements?has_content>
	    <#assign element><@generateIPPQueryVariable tocMap leftQuery/>
	     </#assign>
	     <#elseif rightQuery?has_content && listElements?has_content>
	    <#assign element><@generateIPPQueryVariable tocMap rightQuery/>
	     </#assign>
	     </#if>
	  <#if element?has_content && list?has_content>
     <#assign newElements = list + [element]>
     <#elseif element?has_content>
     <#assign newElements = [element]>
	 <#return newElements>
	 </#if>
	     
 <#elseif operator == "And">
    <#assign count = listElements?size>	
	<#assign leftQuery = (tocMap[leftOperand])!"">
	<#assign rightQuery = (tocMap[rightOperand])!"">
	<#assign queryName = queryVariable.name>
	<#if leftQuery?has_content && rightQuery?has_content>
        <#assign element><@generateIPPQueryVariable tocMap leftQuery/>
        <@generateIPPQueryVariable tocMap rightQuery/>
	    </#assign>
    	<#assign list = listElements>
    <#elseif leftQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element><@generateIPPQueryVariable tocMap leftQuery/>
	    ${element1}<#rt>)
	    </#assign>
    
    <#elseif rightQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element><@generateIPPQueryVariable tocMap rightQuery/>
	    ${element1}<#rt>)
	    </#assign>
	 <#else>
		 <#assign count = listElements?size>	
	    <#if count?? && count &gt;= 2>
		<#assign element1 = listElements[count -1]>
		<#assign element2 = listElements[count - 2]>
		<#assign list = removeLast(listElements, 2)>
		<#if element1?has_content && element2?has_content>
	<#assign element>${element1}
	${element2}</#assign>
    	</#if>
    </#if> 
	</#if>
	<#if element?has_content && list?has_content>
     <#assign newElements = list + [element]>
     <#elseif element?has_content>
     <#assign newElements = [element]>
	 <#return newElements>
	 </#if>
	 
	 <#elseif operator == "or">
    <#assign count = listElements?size>	
	<#assign leftQuery = (tocMap[leftOperand])!"">
	<#assign rightQuery = (tocMap[rightOperand])!"">
	<#assign queryName = queryVariable.name>
	<#if leftQuery?has_content && rightQuery?has_content>
        <#assign element>(ConditionAudit(sequenceId == ${leftQuery.conditionNumber}, qualifies == ResultState.MET)
	    
	    ConditionAudit(sequenceId == ${rightQuery.conditionNumber}, qualifies == ResultState.MET))
	    </#assign>
    	<#assign list = listElements>
    <#elseif leftQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element>(ConditionAudit(sequenceId == ${leftQuery.conditionNumber}, qualifies == ResultState.MET)
	    
	    ${element1}<#rt>)
	    </#assign>
    
    <#elseif rightQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element>(ConditionAudit(sequenceId == ${rightQuery.conditionNumber}, qualifies == ResultState.MET)
	    
	    ${element1}<#rt>)
	    </#assign>
	 <#else>
		 <#assign count = listElements?size>	
	    <#if count?? && count &gt;= 2>
		<#assign element1 = listElements[count -1]>
		<#assign element2 = listElements[count - 2]>
		<#assign list = removeLast(listElements, 2)>
	<#assign element>${element1}
	
	${element2}</#assign>
    	</#if> 
	</#if>
		<#if element?has_content && list?has_content>
     <#assign newElements = list + [element]>
     <#elseif element?has_content>
     <#assign newElements = [element]>
	 <#return newElements>
	 </#if>

	
  <#elseif operator == "Union">	
	<#assign count = listElements?size>	
	<#assign leftQuery = (tocMap[leftOperand])!"">
	<#assign rightQuery = (tocMap[rightOperand])!"">
	<#assign queryName = queryVariable.name>
    <#if leftQuery?has_content && rightQuery?has_content>
	    <#assign element>($${queryName}: ConditionAudit(sequenceId == ${leftQuery.conditionNumber}, qualifies == ResultState.MET)
	    
	    $${queryName}: ConditionAudit(sequenceId == ${rightQuery.conditionNumber}, qualifies == ResultState.MET))
	    </#assign>
    	<#assign list = listElements>
    <#elseif leftQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element>($${queryName}: ConditionAudit(sequenceId == ${leftQuery.conditionNumber}, qualifies == ResultState.MET)
	    
	    ${element1}<#rt>)
	    </#assign>
    
    <#elseif rightQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element>($${queryName}: ConditionAudit(sequenceId == ${rightQuery.conditionNumber}, qualifies == ResultState.MET)
	    
	    ${element1}<#rt>)
	    </#assign>
	 <#else>
		 <#assign count = listElements?size>	
	    <#if count?? && count &gt;= 2>
		<#assign element1 = listElements[count -1]>
		<#assign element2 = listElements[count - 2]>
		<#assign list = removeLast(listElements, 2)>
	<#assign element>${element1}
	
	${element2}</#assign>
    	</#if> 
	</#if>
		<#if element?has_content && list?has_content>
     <#assign newElements = list + [element]>
     <#elseif element?has_content>
     <#assign newElements = [element]>
	 <#return newElements>
	 </#if>
	 
	  <#elseif operator == "QueryExpressionRef">	
  	<#if queryVariable.dependentObjects?has_content>
		<#list queryVariable.dependentObjects as dependentObj>
			<#assign dependentObjQueryVariable = tocMap[dependentObj]>
				<#if dependentObjQueryVariable.valueSetDataTypeMap?has_content>
				<#assign element>
				<#list dependentObjQueryVariable.constraintList as dependentConstraint>
						<#if dependentConstraint.operator == "QueryExpressionRef" && tocMap[dependentConstraint.leftOperand]?has_content>
		QdmElement(
			$encounterQdmElementId : qdmElementId,
			patientId == $patientId,
			$encounterPeriod : period,
					<@genValueSetCodeSetDataType tocMap[dependentConstraint.leftOperand]/>
					)
						</#if>
						</#list>
		QdmElement(
			$episodeId : qdmElementId,
			$srcElementId : srcElementId,
			$providerIdStartDate : providerIdStartDate,
			patientId == $patientId,
			<@genValueSetCodeSetDataType dependentObjQueryVariable/>
					)
				</#assign>
				<#elseif dependentObjQueryVariable.dependentObjects?has_content>
					<#list dependentObjQueryVariable.dependentObjects as dependentVariable>
					<#assign dependentQueryVariable = tocMap[dependentVariable]>
						<#if dependentQueryVariable.valueSetDataTypeMap?has_content>
							<#assign element>
		QdmElement(
					$episodeId : qdmElementId,
					$srcElementId : srcElementId,
					$episodePeriod : period,
					$providerIdStartDate : providerIdStartDate,			
					patientId == $patientId,
					<@genValueSetCodeSetDataType dependentQueryVariable/>
					)
							</#assign>
						</#if>
					</#list>
					<#elseif dependentObjQueryVariable.constraintList?has_content>
						<#list dependentObjQueryVariable.constraintList as dependentConstraint>
						<#assign dependentConstraintleftQuery = (tocMap[dependentConstraint.leftOperand])!"">
						<#if dependentConstraintleftQuery?has_content>
						<#assign element>
							<@generateIPPQueryVariable tocMap dependentConstraintleftQuery/>
						</#assign>
						</#if>
						</#list>
				</#if>
				<#assign newElements = listElements + [element]>
		</#list>
		<#return newElements>
	</#if>
	
	<#elseif operator == "childExpressionRef">
	<#if constraint.occuranceQueryVariable != 0 && tocMap[leftOperand]?has_content>
	<#assign variable = tocMap[leftOperand]>
	<#assign element>
	<@generateIPPQueryVariable tocMap variable/>
	</#assign>
	<#assign newElements = listElements + [element]>
	<#return newElements>
 	</#if>
	
	<#elseif operator == "SameOrAfter">
	<#if constraint.occuranceQueryVariableCounter == 0 >
			<#list queryVariable.constraintList as singleConstraint>
				<#list constraint.rightOperand?split(".") as rOperand>
				<#if rOperand == singleConstraint.rightOperand>
					<#assign occuranceQueryVariable = singleConstraint.leftOperand>
						<#if tocMap[occuranceQueryVariable]?has_content>
			 				<#assign tocMapOccuranceQueryVariable = tocMap[occuranceQueryVariable]>
			 				<#assign occuranceQueryVariableConditionNumber = tocMapOccuranceQueryVariable.conditionNumber>
			 			</#if>		
				</#if>
				</#list>
		</#list>
	<#assign element>
	QdmElementReference(audit == $occurrenceAudit${occuranceQueryVariableConditionNumber} , $qdmElementId${constraint.occuranceQueryVariableCounter} : qdmElementId)
				QdmElement(
				qdmElementId == $qdmElementId${constraint.occuranceQueryVariableCounter},
				$period${constraint.occuranceQueryVariableCounter} : period
			  	)
			  	<#assign parentoccuranceQueryVariableCounter = constraint.occuranceQueryVariableCounter>
	</#assign>
	
	<#elseif (queryVariable.occuranceQueryVariableCounter)-1 == constraint.occuranceQueryVariableCounter>
				
			<#list queryVariable.constraintList as singleConstraint>
				<#list constraint.rightOperand?split(".") as rOperand>
				<#if rOperand == singleConstraint.rightOperand>
					<#assign rOccuranceQueryVariable = singleConstraint.leftOperand>
						<#if tocMap[rOccuranceQueryVariable]?has_content>
			 				<#assign tocMapROccuranceQueryVariable = tocMap[rOccuranceQueryVariable]>
			 				<#assign occuranceRQueryVariableConditionNumber = tocMapROccuranceQueryVariable.conditionNumber>
			 			</#if>		
				</#if>
				</#list>
				<#list constraint.leftOperand?split(".") as lOperand>
				<#if lOperand == singleConstraint.rightOperand>
					<#assign lOccuranceQueryVariable = singleConstraint.leftOperand>
						<#if tocMap[lOccuranceQueryVariable]?has_content>
			 				<#assign tocMapLOccuranceQueryVariable = tocMap[lOccuranceQueryVariable]>
			 				<#assign occuranceLQueryVariableConditionNumber = tocMapLOccuranceQueryVariable.conditionNumber>
			 			</#if>		
				</#if>
				</#list>
		</#list>
	<#assign element>
			QdmElementReference(audit == $occurrenceAudit${occuranceLQueryVariableConditionNumber} , $qdmElementId${constraint.occuranceQueryVariableCounter} : qdmElementId)
			QdmElement(
			qdmElementId == $qdmElementId${constraint.occuranceQueryVariableCounter},
			$period${constraint.occuranceQueryVariableCounter} : period,
			QdmFunction.startsAfterEnd(period, $period${constraint.occuranceQueryVariableCounter-1}, QdmPeriod.DAYS) >= ${constraint.rightOperand?substring((constraint.rightOperand?length)-1,constraint.rightOperand?length)})
					
			QdmElementReference(audit == $occurrenceAudit${occuranceRQueryVariableConditionNumber} , $qdmElementId${constraint.occuranceQueryVariableCounter+1} : qdmElementId)
			QdmElement(
			qdmElementId == $qdmElementId${constraint.occuranceQueryVariableCounter+1},
			$period${constraint.occuranceQueryVariableCounter+1} : period,
			QdmFunction.startsAfterEnd(period, $period${constraint.occuranceQueryVariableCounter}, QdmPeriod.DAYS) >= ${constraint.rightOperand?substring((constraint.rightOperand?length)-1,constraint.rightOperand?length)})
	</#assign>
	
	<#else>
			<#list queryVariable.constraintList as singleConstraint>
				<#list constraint.rightOperand?split(".") as othrOperand>
				<#if othrOperand == singleConstraint.rightOperand>
					<#assign occuranceQueryVariable = singleConstraint.leftOperand>
						<#if tocMap[occuranceQueryVariable]?has_content>
			 				<#assign tocMapOccuranceQueryVariable = tocMap[occuranceQueryVariable]>
			 				<#assign occuranceOQueryVariableConditionNumber = tocMapOccuranceQueryVariable.conditionNumber>
			 			</#if>		
				</#if>
				</#list>
		</#list>
		<#assign element>
			QdmElementReference(audit == $occurrenceAudit${occuranceOQueryVariableConditionNumber} , $qdmElementId${constraint.occuranceQueryVariableCounter} : qdmElementId)
			QdmElement(
			qdmElementId == $qdmElementId${constraint.occuranceQueryVariableCounter},
			$period${constraint.occuranceQueryVariableCounter} : period,
			QdmFunction.startsAfterEnd(period, $period${parentoccuranceQueryVariableCounter}, QdmPeriod.DAYS) >= ${constraint.rightOperand?substring((constraint.rightOperand?length)-1,constraint.rightOperand?length)})
		</#assign>
	</#if>
	<#assign newElements = listElements + [element]>
	<#return newElements>
	
 </#if>
 		 
		 <#if (operator == "GreaterOrEqual" || operator == "Less") &&(constraintcounter == constraintList?size)>
	   		<#assign element>
	   		)
	   		</#assign>
	   		<#assign newElements = listElements + [element]>
	<#return newElements>
	   		</#if>

  <#return listElements>
</#function>

<#function getElementAlias listElements>
<#assign alias = "qdmElementId">
<#if listElements??>
<#list 1..100000000 as x>
<#assign found = false>
<#list listElements as element>
<#if element?contains(alias)>
<#assign alias = ("qdmElementId"+ x?c)>
<#assign found = true>
<#break>
</#if>
</#list>
<#if !found>
<#break>
</#if>
</#list>
</#if>
<#return alias>	
</#function>

<#function removeLast list numberOfItems>
<#assign length = list?size>
<#if length  &gt; numberOfItems>
 	<#assign newElements = (list?chunk(length - numberOfItems))[0]>
	<#return newElements>
</#if>
<#return []>
</#function>

<#function getOperands string operator>
<#if string??>
<#assign csv = string?keep_after((operator + "("))>
<#assign csv = csv?keep_before_last(")")>
<#assign csv = csv?split(",")>
<#return csv>	
</#if>
<#return []>	
</#function>