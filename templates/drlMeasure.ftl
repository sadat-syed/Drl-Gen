<#assign populationCriteria = populationCriteria>
<#if populationCriteria == "Denominator Exclusion">
	<#assign populationCriteria = "Denominator Exclusions">
</#if>
		<#assign tocMap = tocMap>
		<#assign QueryVariable = class>
		<#assign insertStatement = "none">
		<#assign operType = "unary">

/*===========================================================================================================
${populationCriteria}
===========================================================================================================*/
<#list orderList as queryName>
		<#assign queryVariable = tocMap[queryName.name]>
		<#assign ipop = false>
<#if queryVariable.name?contains("Initial Population")>
		<#assign ipop = true>
</#if>
		<#assign denom = false>
<#if queryVariable.name?contains("Denominator") && !(queryVariable.name?contains("Denominator Exclusion")) && !(queryVariable.name?contains("Denominator Exceptions"))>
		<#assign denom = true>
</#if>
		<#assign denomexcl = false>
<#if queryVariable.name?contains("Denominator Exclusion")>
		<#assign denomexcl = true>
</#if>
		<#assign numer = false>
<#if queryVariable.name?contains("Numerator")>
		<#assign numer = true>
</#if>
		<#assign denomexcp = false>
<#if queryVariable.name?contains("Denominator Exceptions")>
		<#assign denomexcp = true>
</#if>
		<#assign strat = false>
<#if queryVariable.name?lower_case?contains("stratification")>
		<#assign strat = true>
</#if>
		<#assign patientIdVar =  "$audit.patientId">
<#if ipop==true || denom==true || denomexcl==true || numer==true || denomexcp==true || strat==true>
		<#assign patientIdVar =  "$result.patientId">
</#if>
<#if strat = true>
		<#assign stratNum = queryVariable.name?split(" ")[1]>
	<#if (queryVariable.name?split(" ")[1]?split("_")?size > 1)>
		<#assign stratNum = queryVariable.name?split(" ")[1]?split("_")[1]>
	</#if>
</#if>
		<#assign childCondGreater = false>
		<#assign periodVariable = "$audit.measurePeriod">
<#if (queryVariable.childConditionNumber?has_content)>
		<#assign childCondGreater = true>
		<#assign periodVariable = "$elementPeriod">
</#if>
<@generateRule queryVariable/>
</#list>
<#list orderList as queryName>
<#assign queryVariable = tocMap[queryName.name]>
<@generateMetCondtion queryVariable/>
</#list>
<#-- define macro for creating rule for query variable --> 
<#macro generateRule queryVariable>
<#assign conditionNo = queryVariable.conditionNumber>
/* rule for ${queryVariable.name}*/
<#if strat == true>
rule "Reporting Stratum ${stratNum} Met"
<#else>
rule "${populationCriteria} - Condition ${conditionNo}"
</#if>
<#if strat == true>
ruleflow-group "Reporting Stratification"
<#elseif populationCriteria?contains("Initial Population")>
ruleflow-group "Initial Patient Population"
<#else>
ruleflow-group "${populationCriteria}"
</#if>
	when
	<#if strat == true>
		$result: ProportionResult(reportingStratum == ReportingStratum.NONE)
		$audit : ConditionAudit(sequenceId == ${conditionNo})
	<#elseif queryVariable.furtherDepedent == false>
		exists ConditionAudit(sequenceId == ${conditionNo}, qualifies == ResultState.UNMET)
		$audit : ConditionAudit(sequenceId == ${conditionNo})
		<#else>
		$audit : ConditionAudit(sequenceId == ${conditionNo})
	</#if>
	 	<@generateWhen queryVariable/>
	<#if strat == true>
		not ProportionResult(resultName == $result.resultName, reportingStratum == ReportingStratum.REPORTING_STRATUM_${stratNum})
	</#if>	
	then
	<#if strat == true>
		logger.debug("Qualifies for Reporting Stratum ${stratNum}: " + $result);
		ProportionResult stratumResult  = $result.copy();
		stratumResult.setReportingStratum(ReportingStratum.REPORTING_STRATUM_${stratNum});
		<#if populationCriteriaName??>
		stratumResult.setPopulationCriteriaName(PopulationCriteriaName.${populationCriteriaName});
		</#if>
		insert(stratumResult);
	<#else>
		logger.debug("Condition " + $audit.getSequenceId() + " is met");
		modify ($audit) {
			setQualifies(ResultState.MET)
		}
		<#assign ops = "false">
		<#assign bdate = "false">
		<#list queryVariable.constraintList as constraint>
		<#if constraint.operator == "InValueSet" || constraint.operator == "Greater" || constraint.operator == "Equivalent" || 
			(constraint.operator == "IsNotNull" && constraint.leftOperand?has_content && 
			constraint.leftOperand?contains (".result") && queryVariable.name?contains(leftOperand) ) || constraint.leftOperand?has_content 
			&& constraint.leftOperand?contains (".result") && leftOperand?contains(queryVariable.name) && constraint.operator != "IsNull" && constraint.operator != "IsNotNull"> 
			<#assign ops = "true">
			<#break>
		</#if>
	 	</#list>	
	 	<#list queryVariable.constraintList as constraint>
		<#if constraint?has_content && constraint.leftOperand?has_content && constraint.leftOperand ?contains ("CalendarAgeInYearsAt") && constraint.operator?has_content &&  constraint.operator == "GreaterOrEqual" >
			<#assign bdate = "true">
			<#break>
		</#if>	
		</#list>
			<#if queryVariable.valueSetDataTypeMap?has_content || queryVariable.name?lower_case == "birthdate" || strat == true || bdate == "true" >
				<#if ( insertStatement == "attr") || ops == "true">
					<#if (childCondGreater == true) >
		insert(new QdmElementReference($audit, $qdmElementId,  $qdmAttributeId, $occurrenceQdmElementId));
					<#else>
		insert(new QdmElementReference($audit, $qdmElementId, $qdmAttributeId));
					</#if>
				<#elseif (childCondGreater == true) >
		insert(new QdmElementReference($audit, $qdmElementId,  -1, $occurrenceQdmElementId));	
				<#else>
		insert(new QdmElementReference($audit, $qdmElementId,  -1));
				</#if>
		<#elseif queryVariable.furtherDepedent == true>
		insert(new QdmElementReference($audit, $qdmElementId,  -1));
			</#if>
		</#if>
end

</#macro> 
<#macro generateElement queryVariable>
		<#assign valueSetDataTypeMap = false>
	<#if queryVariable.valueSetDataTypeMap?has_content>
		<#assign valueSetDataTypeMap = true>
	</#if>
	 	<#if (childCondGreater == true) && (strat == false)>
	 	<#list queryVariable.childConditionNumber as singleChildCondnNum>
	 	<#if queryVariable.childConditionNumber?size gt 1>
		$audit${singleChildCondnNum} : ConditionAudit(sequenceId == ${singleChildCondnNum}, result == $audit.result, qualifies == ResultState.MET)
		QdmElementReference(audit == $audit${singleChildCondnNum}, $occurrenceQdmElementId${singleChildCondnNum} : qdmElementId)
		QdmElement(qdmElementId == $occurrenceQdmElementId${singleChildCondnNum}, $elementPeriod${singleChildCondnNum} : period)
		<#else>
		$audit${singleChildCondnNum} : ConditionAudit(sequenceId == ${singleChildCondnNum}, result == $audit.result, qualifies == ResultState.MET)
		QdmElementReference(audit == $audit${singleChildCondnNum}, $occurrenceQdmElementId : qdmElementId)
		QdmElement(qdmElementId == $occurrenceQdmElementId, $elementPeriod${singleChildCondnNum} : period)
		</#if>
		</#list>
		</#if>
		<#if queryVariable.isLastOperator()?has_content && queryVariable.isLastOperator() == true>
		   QdmList($qdmElementId : mostRecentId != -1) from collect (
	 	</#if>
	 	<#if queryVariable.isFirstOperator()?has_content && queryVariable.isFirstOperator() == true>
		   QdmList($qdmElementId : firstId != -1) from collect (
	 	</#if>
		<#if queryVariable.qdmPeriodVariable?has_content && tocMap[queryVariable.qdmPeriodVariable]??>
	 	$elementPeriod: QdmPeriod(name == "${queryVariable.qdmPeriodVariable}")
	 	</#if>
	<#if valueSetDataTypeMap>
	<#assign generateQdmElement = true>
	<#assign mapSize = queryVariable.valueSetDataTypeMap?size>
	<#assign keys = queryVariable.valueSetDataTypeMap?keys>
	<#list keys as key>
	<#if key == "PATIENT_CHARACTERISTIC_BIRTH_DATE">
	<#assign generateQdmElement = false>
	<#break>
	</#if>
	</#list>
	<#if generateQdmElement>
		QdmElement(
		<#if !((queryVariable.isLastOperator()?has_content && queryVariable.isLastOperator() == true) || 
		(queryVariable.isFirstOperator()?has_content && queryVariable.isFirstOperator() == true))>
				$qdmElementId : qdmElementId,
		</#if>
				patientId == ${patientIdVar},
				<@genValueSetCodeSetDataType queryVariable/>
	 			)
	</#if> 
	 </#if>
	<#if queryVariable.isLastOperator() || queryVariable.isFirstOperator()>
    		)
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
<#macro generateWhen query>
	<#if !query.constraintList?? || query.constraintList?size == 0>
	 	<#if query.queryVariable?? &&  query.queryVariable?size == 1>
	 		<#assign variable = (query.queryVariable)[0]>
	 		<#if query.furtherDepedent == false>
 		ConditionAudit(sequenceId == ${variable.conditionNumber}, qualifies == ResultState.MET)
 		<#else>
 		$audit${variable.conditionNumber} : ConditionAudit(sequenceId == ${variable.conditionNumber}, qualifies == ResultState.MET)
		QdmElementReference(audit == $audit${variable.conditionNumber}, $qdmElementId : qdmElementId)
		not QdmElementReference(audit == $audit, qdmElementId == $qdmElementId)
		</#if>
 		</#if>
	<#elseif query.constraintList??>
		<#assign listElements = []>
		<@generateElement query/>
		 <#list query.constraintList as constraint>
	   		<#assign listElements = evaluateConstraint(constraint, listElements, query ,query.constraintList)/>
	 	</#list>
	 	<#list listElements as elements>
   		${elements}
	 	</#list>
	 	<#if query.furtherDepedent == true>
 		not QdmElementReference(audit == $audit, qdmElementId == $qdmElementId)
 		</#if>
 	</#if>
 	 	<#if query.constraintList?size == 0 && query.valueSetDataTypeMap?has_content>
	 	<@generateElement query/>
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
<#function evaluateConstraint constraint listElements queryVariable constraintList>
		<#assign qdmelementAlias = getElementAlias(listElements)>
		<#assign leftOperand = (constraint.leftOperand)!"">
		<#assign rightOperand = (constraint.rightOperand)!"">
		<#assign operator = constraint.operator>
	<#if queryVariable.intervalOperator??>
		<#assign intervalLowValue = queryVariable.intervalOperator.lowValue>
		<#assign intervalLowClosed= queryVariable.intervalOperator.lowClosed>
		<#assign intervalHighValue = queryVariable.intervalOperator.highValue>
		<#assign intervalHighClosed= queryVariable.intervalOperator.highClosed>
	</#if>
	<#assign isLastOperator= queryVariable.isLastOperator>
	<#assign qdmPeriodVariable= queryVariable.qdmPeriodVariable?has_content>
		<#if operator == "Or" || operator == "Union" || operator == "And">
			<#assign operType = "binary">
		</#if>
	<#if (ipop == true || denom == true || denomexcl == true || numer == true || denomexcp == true) && !(operType == "binary")>
	 	<#if tocMap[leftOperand]?has_content>
			<#if (queryVariable.dependentObjects?size == 1) && ((queryVariable.name == "Denominator") && (queryVariable.dependentObjects[0] == "Initial Population") ||
				(queryVariable.name == "Denominator 1") && (queryVariable.dependentObjects[0] == "Initial Population 1") ||
				(queryVariable.name == "Denominator 2") && (queryVariable.dependentObjects[0] == "Initial Population 2") ||
				(queryVariable.name == "Denominator 3") && (queryVariable.dependentObjects[0] == "Initial Population 3")) >
			<#else>
			<#assign leftQuery = tocMap[leftOperand]>
			<#assign element>
ConditionAudit(sequenceId == ${leftQuery.conditionNumber}, qualifies == ResultState.MET)
	 	</#assign>
	 	<#assign newElements = listElements + [element]>
 		<#return newElements>
	 </#if>
 	<#else>
 	<#assign element>
 	</#assign>
	 	<#assign newElements = listElements + [element]>
 	<#return newElements>
 		</#if>
 	</#if>
	<#if operator == "GreaterOrEqual">
    	<#if leftOperand ?contains("CalendarAgeInYearsAt")>   
 		<#assign element>
	 	QdmElement(
				$${qdmelementAlias} : qdmElementId,
				patientId == ${patientIdVar},
				dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
				QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS) >= ${rightOperand})
	
 		</#assign>
 		<#assign newElements = listElements + [element]>
 		<#return newElements>
 		<#elseif leftOperand ?contains("CalendarAgeInMonthsAt")>   
 		<#assign element>
	 	QdmElement(
				$${qdmelementAlias} : qdmElementId,
				patientId == ${patientIdVar},
				dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
				QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.MONTHS) >= ${rightOperand})
 		</#assign>
 		<#assign newElements = listElements + [element]>
 		<#return newElements>
 		<#elseif leftOperand ?contains ("CalendarAgeInDaysAt")>
	 	<#assign element>
		 		QdmFunction.startsAfterStart(period, $elementPeriod, QdmPeriod.DAYS) >= ${rightOperand},
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
	     <#if leftOperand ?contains("CalendarAgeInYearsAt")>    
 		 <#assign element>
	 	QdmElement(
				$${qdmelementAlias} : qdmElementId,
				patientId == ${patientIdVar},
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
		<#elseif leftOperand ?contains ("CalendarAgeInDaysAt")>    
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
                qdmCd.codeValue == "${i}",
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
		<#if leftOperand?has_content && leftOperand?contains (".result") && leftOperand?contains(queryVariable.name)>
	  	<#assign insertStatement = "attr">
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
				$${qdmelementAlias} : qdmElementId,
				patientId == ${patientIdVar},
				dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
				QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS)< ${rightOperand} && >= 0)
 		</#assign>
 		<#assign newElements = listElements + [element]>
 		<#return newElements>
 		<#else>
 			<#if qdmPeriodVariable?has_content>
		<#assign insertStatement = "attr">
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
				$${qdmelementAlias} : qdmElementId,
				patientId == ${patientIdVar},
				dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
				QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS)<= ${rightOperand} && >= 0)
 		</#assign>
 		<#assign newElements = listElements + [element]>
 		<#return newElements>
 		<#elseif leftOperand ?contains ("CalendarAgeInDaysAt")>
 		<#assign element>
		QdmFunction.startsAfterStart(period, $elementPeriod, QdmPeriod.DAYS) >= 0 && <= ${rightOperand},
 		</#assign>
 		<#assign newElements = listElements + [element]>
 		<#return newElements>
 		</#if>
 	<#elseif operator == "Equal">
		<#if leftOperand ?contains ("CalendarAgeInYearsAt")>
 		<#assign element>
		QdmElement(
				$${qdmelementAlias} : qdmElementId,
				patientId == ${patientIdVar},
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
		<#assign element>
		ConditionAudit(sequenceId == ${variable.conditionNumber}, qualifies == ResultState.MET)
		</#assign>
		<#assign newElements = listElements + [element]>
 		<#return newElements>
		</#if>	
	<#elseif operator == "NotExists">	
  		<#if tocMap[leftOperand]?has_content>
	 	<#assign variable = tocMap[leftOperand]>
		<#assign element>
		not ConditionAudit(sequenceId == ${variable.conditionNumber}, qualifies == ResultState.MET)
		</#assign>
		<#assign newElements = listElements + [element]>
 		<#return newElements>
		</#if>	
 	<#elseif operator == "ExpressionRef" && !(queryVariable.name == "Denominator")>	
	 	<#assign variable = tocMap[leftOperand]>
	 	<#if variable?has_content>
		<#assign element>
		ConditionAudit(sequenceId == ${variable.conditionNumber}, qualifies == ResultState.MET)
		</#assign>
		<#assign newElements = listElements + [element]>
 		<#return newElements>
		</#if>	
 	<#elseif operator == "And">
    	<#assign count = listElements?size>	
		<#assign leftQuery = (tocMap[leftOperand])!"">
		<#assign rightQuery = (tocMap[rightOperand])!"">
		<#assign queryName = queryVariable.name>
		<#if leftQuery?has_content && rightQuery?has_content>
        <#assign element>
		ConditionAudit(sequenceId == ${leftQuery.conditionNumber}, qualifies == ResultState.MET)
	    ConditionAudit(sequenceId == ${rightQuery.conditionNumber}, qualifies == ResultState.MET)
	    </#assign>
    	<#assign list = listElements>
    	<#elseif leftQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element>
ConditionAudit(sequenceId == ${leftQuery.conditionNumber}, qualifies == ResultState.MET)
	    ${element1}<#rt>)
	    </#assign>
    	<#elseif rightQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element>
ConditionAudit(sequenceId == ${rightQuery.conditionNumber}, qualifies == ResultState.MET)
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
	<#elseif operator == "Or">
    	<#assign count = listElements?size>	
		<#assign leftQuery = (tocMap[leftOperand])!"">
		<#assign rightQuery = (tocMap[rightOperand])!"">
		<#assign queryName = queryVariable.name>
		<#if leftQuery?has_content && rightQuery?has_content>
        <#assign element>
		(ConditionAudit(sequenceId == ${leftQuery.conditionNumber}, qualifies == ResultState.MET)
	    or
	    ConditionAudit(sequenceId == ${rightQuery.conditionNumber}, qualifies == ResultState.MET))
	    </#assign>
    	<#assign list = listElements>
    	<#elseif leftQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element>
		(ConditionAudit(sequenceId == ${leftQuery.conditionNumber}, qualifies == ResultState.MET)
	    or
	    ${element1}<#rt>)
	    </#assign>
	    <#elseif rightQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element>
		(ConditionAudit(sequenceId == ${rightQuery.conditionNumber}, qualifies == ResultState.MET)
	    or
	    ${element1}<#rt>)
	    </#assign>
	 	<#else>
		<#assign count = listElements?size>	
	    	<#if count?? && count &gt;= 2>
		<#assign element1 = listElements[count -1]>
		<#assign element2 = listElements[count - 2]>
		<#assign list = removeLast(listElements, 2)>
		<#assign element>${element1}
		or
	${element2}</#assign>
    		</#if> 
		</#if>
		<#if element?has_content && list?has_content>
     	<#assign newElements = list + [element]>
     	<#return newElements>
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
	    <#assign element>
ConditionAudit(sequenceId == ${leftQuery.conditionNumber} || == ${rightQuery.conditionNumber}, qualifies == ResultState.MET)
	    </#assign>
    	<#assign list = listElements>
    	<#elseif leftQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element>
		($${queryName}: ConditionAudit(sequenceId == ${leftQuery.conditionNumber}, qualifies == ResultState.MET)
	    or
	    ${element1}<#rt>)
	    </#assign>
	    <#elseif rightQuery?has_content && listElements?has_content>
	    <#assign element1 = listElements[count -1]>
	    <#assign list = removeLast(listElements, 1)>
	    <#assign element>
		($${queryName}: ConditionAudit(sequenceId == ${rightQuery.conditionNumber}, qualifies == ResultState.MET)
	    or
	    ${element1}<#rt>)
	    </#assign>
	 	<#else>
		<#assign count = listElements?size>	
	    	<#if count?? && count &gt;= 2>
		<#assign element1 = listElements[count -1]>
		<#assign element2 = listElements[count - 2]>
		<#assign list = removeLast(listElements, 2)>
		<#assign element>${element1}
		or
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
  		<#if tocMap[leftOperand]?has_content>
	 	<#assign variable = tocMap[leftOperand]>
		<#assign element>
		ConditionAudit(sequenceId == ${variable.conditionNumber}, qualifies == ResultState.MET)</#assign>
		<#assign newElements = listElements + [element]>
 		<#return newElements>
		</#if>	
	<#elseif operator == "childExpressionRef">
		<#if constraint.occuranceQueryVariable != 0>
		<#assign element> 
		$occurrenceAudit${constraint.occuranceQueryVariable} : ConditionAudit(sequenceId == ${constraint.occuranceQueryVariable}, result == $audit.result, qualifies == ResultState.MET)</#assign>
		<#assign newElements = listElements + [element]>
		<#return newElements>
 		</#if>
		<#if tocMap[leftOperand]?has_content>
		<#assign variable = tocMap[leftOperand]>
		<@generateWhen variable/>
		</#if>
	<#elseif operator == "SameOrAfter">
		<#if constraint.occuranceQueryVariableCounter == 0 >
			<#if queryVariable.constraintList?has_content>
			<#list queryVariable.constraintList as singleConstraint>
				<#list constraint.rightOperand?split(".") as rOperand>
				<#if singleConstraint.rightOperand?has_content>
				<#if rOperand == singleConstraint.rightOperand>
					<#assign occuranceQueryVariable = singleConstraint.leftOperand>
						<#if tocMap[occuranceQueryVariable]?has_content>
			 				<#assign tocMapOccuranceQueryVariable = tocMap[occuranceQueryVariable]>
			 				<#assign occuranceQueryVariableConditionNumber = tocMapOccuranceQueryVariable.conditionNumber>
			 						<#assign element>
									QdmElementReference(audit == $occurrenceAudit${occuranceQueryVariableConditionNumber} , $qdmElementId${constraint.occuranceQueryVariableCounter} : qdmElementId)
											QdmElement(
											qdmElementId == $qdmElementId${constraint.occuranceQueryVariableCounter},
											$period${constraint.occuranceQueryVariableCounter} : period
										  	)
										  	<#assign parentoccuranceQueryVariableCounter = constraint.occuranceQueryVariableCounter>
									</#assign>
			 			</#if>
			 			<#else>
			 			<#assign element>
			 			</#assign>		
				</#if>
				</#if>
				</#list>
		</#list>
			</#if>
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
  <#return listElements>
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
<#macro getSingleQdmFunction queryVariable singleQdmFunction periodVariable seperator>
				<#assign ChildCondnNum = "">
	<#list queryVariable.childConditionNumber as singleChildCondnNum>
	<#assign ChildCondnNum = singleChildCondnNum>
	</#list>
	<#if singleQdmFunction?has_content>
		<#if singleQdmFunction == "endsBeforeEnd" || singleQdmFunction == "startsBeforeEnd" || singleQdmFunction == "startsBeforeStart" 
				|| singleQdmFunction == "endsBeforeStart" || singleQdmFunction == "endsBeforeOrConcurrentWithEnd" 
				|| singleQdmFunction == "startsBeforeOrConcurrentWithEnd" || singleQdmFunction =="endsBeforeOrConcurrentWithStart" 
				|| singleQdmFunction =="startsBeforeOrConcurrentWithStart"  || singleQdmFunction == "startsAfterOrConcurrentWithStart"
				 || singleQdmFunction == "startsOrConcurrentWithAfterStart" || singleQdmFunction == "startsOnConcurrentDayStart"
				 || singleQdmFunction == "startsAfterStart" || singleQdmFunction == "StartBeforeStart">
				 <#if singleQdmFunction == "startsOrConcurrentWithAfterStart">
				 <#assign singleQdmFunction = "startsAfterOrConcurrentWithStart">
				 <#elseif singleQdmFunction == "startsOnConcurrentDayStart">
				  <#assign singleQdmFunction = "startsOnConcurrentDay">
				 </#if>
		 	<#if queryVariable.qdmPeriodVariable?? && queryVariable.arithmeticOperand??>
				QdmFunction.${singleQdmFunction}(period, ${periodVariable}${ChildCondnNum},QdmPeriod.${queryVariable.qdmPeriodVariable}) >= 0 && < ${queryVariable.arithmeticRightOperand}${seperator}
			<#else>
				QdmFunction.${singleQdmFunction}(period, ${periodVariable}${ChildCondnNum})${seperator}
			</#if>
		<#elseif singleQdmFunction == "duringStartAfterStart">
			 	QdmFunction.startsAfterStart(period, ${periodVariable}${ChildCondnNum})${seperator}
		<#elseif singleQdmFunction == "during">
				QdmFunction.during(period, ${periodVariable}${ChildCondnNum})${seperator}
		<#elseif singleQdmFunction == "starts">
				QdmFunction.startsDuring(period, ${periodVariable}${ChildCondnNum})${seperator}
		<#elseif singleQdmFunction == "duringStartBeforeStartduring">
				QdmFunction.during(period, ${periodVariable})${seperator}
			<#if (queryVariable.parentConditionNumber > 0)>
				QdmFunction.startsBeforeEnd(period, $audit.elementPeriod)${seperator}
			<#else>
				QdmFunction.startsBeforeEnd(period, $audit.measurePeriod)${seperator}
			</#if>
			<#elseif singleQdmFunction == "ends" || singleQdmFunction == "starts">
				QdmFunction.${singleQdmFunction}During(period, ${periodVariable})${seperator}
		<#else>
				QdmFunction.${singleQdmFunction}(period, ${periodVariable})${seperator}
		</#if>
		</#if>
</#macro>
<#-- define macro for Met condition for IPP,Numerator,Denominator,Denominator Exclusions,Numerator and Denominator Exceptions --> 
<#macro generateMetCondtion queryVariable>
<#if queryVariable.name?starts_with("Initial Population")>
rule "Initial Population Met"
ruleflow-group "Initial Patient Population"
	when
		$result: ProportionResult(initialPatientPopulation == ResultState.UNMET)
		ConditionAudit(sequenceId == ${queryVariable.conditionNumber}, qualifies == ResultState.MET, result == $result)
	then
		logger.debug("Qualifies for Initial Population: " + $result.getMeasureId());
		$result.setInitialPatientPopulation(ResultState.MET);
		update($result);
end
<#elseif queryVariable.name?contains("Denominator") && !(queryVariable.name?contains("Denominator Exclusion")) && !(queryVariable.name?contains("Denominator Exceptions"))>
rule "Denominator Met"
ruleflow-group "Denominator"
	when
		$result: ProportionResult(denominator == ResultState.UNMET)
		ConditionAudit(sequenceId == ${queryVariable.conditionNumber}, qualifies == ResultState.MET, result == $result)
	then
		logger.debug("Qualifies for Denominator: " + $result.getMeasureId());
		$result.setDenominator(ResultState.MET);
		update($result);
end
<#elseif queryVariable.name?contains("Denominator Exclusion")>
rule "Denominator Exclusions Met"
ruleflow-group "Denominator Exclusions"
	when
		$result: ProportionResult(denominatorExclusions == ResultState.UNMET)
		ConditionAudit(sequenceId == ${queryVariable.conditionNumber}, qualifies == ResultState.MET, result == $result)
	then
		logger.debug("Qualifies for Denominator Exclusions: " + $result.getMeasureId());
		$result.setDenominatorExclusions(ResultState.MET);
		update($result);
end
<#elseif queryVariable.name?contains("Numerator")>
rule "Numerator Met"
ruleflow-group "Numerator"
	when
		$result: ProportionResult(numerator == ResultState.UNMET)
		ConditionAudit(sequenceId == ${queryVariable.conditionNumber}, qualifies == ResultState.MET, result == $result)
	then
		logger.debug("Qualifies for Numerator: " + $result.getMeasureId());
		$result.setNumerator(ResultState.MET);
		update($result);
end
<#elseif queryVariable.name?contains("Denominator Exceptions")>
rule "Denominator Exceptions Met"
ruleflow-group "Denominator Exceptions"
	when
		$result: ProportionResult(denominatorExceptions == ResultState.UNMET)
		ConditionAudit(sequenceId == ${queryVariable.conditionNumber}, qualifies == ResultState.MET, result == $result)
	then
		logger.debug("Qualifies for Denominator Exceptions " + $result.getMeasureId());
		$result.setDenominatorExceptions(ResultState.MET);
		update($result);
end
</#if>
</#macro>