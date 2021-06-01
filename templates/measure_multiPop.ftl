/*
 * @Copyright("2017 General Electric Company")
 *
 * All Rights Reserved.
 * No portions of this source code or the resulting compiled program may be used without
 * express written consent and licensing by GE Healthcare 
 */
 
import org.slf4j.Logger;
import com.ct.haf.mu.entity.measureresult.ProportionResult;
import com.ct.haf.mu.entity.measureresult.ConditionAudit;
import com.ct.haf.mu.entity.measureresult.QdmElementReference;
import com.ct.haf.mu.entity.measureresult.ResultState;
import com.ct.haf.mu.entity.patientdata.QdmElement;
import com.ct.haf.mu.entity.patientdata.QdmElementAttribute;
import com.ct.haf.mu.entity.patientdata.QdmDataType;
import com.ct.haf.mu.entity.patientdata.QdmPeriod;
import com.ct.haf.mu.entity.patientdata.QdmAttributeName;
import com.ct.haf.mu.entity.patientdata.attributetype.QdmCD;
import com.ct.haf.mu.engine.measure.util.QdmFunction;
import com.ct.haf.mu.service.api.ValueSetLookupService;

// Use logging system 
global Logger logger;

// Lookup service for valuesets
global ValueSetLookupService lookupService;
/*

Population Criteria

Initial Population =

Denominator =

Denominator Exclusions =

Numerator =

Numerator Exclusions =

Denominator Exceptions =

Stratification =

Data Criteria (QDM Variables)

Data Criteria (QDM Data Elements)

*/

/*===========================================================================================================
 Initialization
===========================================================================================================*/

// Initialize the settings for each population criteria

<@InitializeRule measure/>

<#-- Initial Patient Population Exception Rule-Group Rule Generation - BEGIN -->
<#if measure.population_criteria["IPP"]??> 
	<#assign ippCriteria = measure.population_criteria["IPP"]>
		<#if ippCriteria?? & ippCriteria.preconditions??>
			<#list ippCriteria.preconditions as precondition>
				<@createCondition precondition measure "Initial Patient Population"/>
			</#list>
		</#if>
</#if>

<#if measure.population_criteria["IPP_1"]??> 
	<#assign ippCriteria = measure.population_criteria["IPP_1"]>
		<#if ippCriteria?? & ippCriteria.preconditions??>
			<#list ippCriteria.preconditions as precondition>
				<@createCondition precondition measure "Initial Patient Population"/>
			</#list>
	</#if>
</#if>

<#if measure.population_criteria["IPP_2"]??>
	<#assign ippCriteria = measure.population_criteria["IPP_2"]>
		<#if ippCriteria?? & ippCriteria.preconditions??>
			<#list ippCriteria.preconditions as precondition>
				<@createCondition precondition measure "Initial Patient Population"/>
			</#list>
	</#if>
</#if>
<#-- Initial Patient Population Exception Rule-Group Rule Generation - END -->

<#-- Denominator Exception Rule-Group Rule Generation - BEGIN -->
<#if measure.population_criteria["DENOM"]??>
	<#assign denomCriteria = measure.population_criteria["DENOM"]>
		<#if denomCriteria?? & denomCriteria.preconditions??>
rule "Denominator - Condition"
			<#list denomCriteria.preconditions as precondition>
				<@createCondition precondition measure "Denominator"/>
			</#list>
	</#if>
</#if>

<#if measure.population_criteria["DENOM_1"]??>
	<#assign denomCriteria = measure.population_criteria["DENOM_1"]>
		<#if denomCriteria?? & denomCriteria.preconditions??>
			<#list denomCriteria.preconditions as precondition>
				<@createCondition precondition measure "Denominator"/>
			</#list>
	</#if>
</#if>

<#if measure.population_criteria["DENOM_2"]??>
	<#assign denomCriteria = measure.population_criteria["DENOM_2"]>
		<#if denomCriteria?? & denomCriteria.preconditions??>
			<#list denomCriteria.preconditions as precondition>
				<@createCondition precondition measure "Denominator"/>
			</#list>
	</#if>
</#if>
<#-- Denominator Rule-Group Rule Generation - END -->

<#-- Denominator Exception Rule-Group Rule Generation - BEGIN -->
<#if measure.population_criteria["DENEXCEP"]??>	
<#assign denomExcpCriteria = measure.population_criteria["DENEXCEP"]>
	<#if denomExcpCriteria?? & denomExcpCriteria.preconditions??>
		<#list denomExcpCriteria.preconditions as precondition>
			<@createCondition precondition measure "Denominator Exceptions"/>
		</#list>
	</#if>
</#if>

<#if measure.population_criteria["DENEXCEP_1"]??>	
<#assign denomExcpCriteria = measure.population_criteria["DENEXCEP_1"]>
	<#if denomExcpCriteria?? & denomExcpCriteria.preconditions??>
		<#list denomExcpCriteria.preconditions as precondition>
			<@createCondition precondition measure "Denominator Exceptions"/>
		</#list>
	</#if>
</#if>

<#if measure.population_criteria["DENEXCEP_2"]??>	
<#assign denomExcpCriteria = measure.population_criteria["DENEXCEP_2"]>
	<#if denomExcpCriteria?? & denomExcpCriteria.preconditions??>
		<#list denomExcpCriteria.preconditions as precondition>
			<@createCondition precondition measure "Denominator Exceptions"/>
		</#list>
	</#if>
</#if>
<#-- Denominator Exception Rule-Group Rule Generation - END -->

<#-- Denominator Exclusion Rule-Group Rule Generation - BEGIN -->
<#if measure.population_criteria["DENEX"]??>
<#assign denomExCriteria = measure.population_criteria["DENEX"]>
	<#if denomExCriteria?? & denomExCriteria.preconditions??>
		<#list denomExCriteria.preconditions as precondition>
			<@createCondition precondition measure "Denominator Exclusions"/>
		</#list>
	</#if>
</#if>

<#if measure.population_criteria["DENEX_1"]??>
<#assign denomExCriteria = measure.population_criteria["DENEX_1"]>
	<#if denomExCriteria?? & denomExCriteria.preconditions??>
		<#list denomExCriteria.preconditions as precondition>
			<@createCondition precondition measure "Denominator Exclusions"/>
		</#list>
	</#if>
</#if>

<#if measure.population_criteria["DENEX_2"]??>
<#assign denomExCriteria = measure.population_criteria["DENEX_2"]>
	<#if denomExCriteria?? & denomExCriteria.preconditions??>
		<#list denomExCriteria.preconditions as precondition>
			<@createCondition precondition measure "Denominator Exclusions"/>
		</#list>
	</#if>
</#if>		
<#-- Denominator Exclusion Rule-Group Rule Generation - END -->
	
<#-- Numerator Rule-Group Rule Generation - BEGIN -->
<#if measure.population_criteria["NUMER"]??>			
	<#assign numerCriteria = measure.population_criteria["NUMER"]>
		<#if numerCriteria?? & numerCriteria.preconditions??>
			<#list numerCriteria.preconditions as precondition>
				<@createCondition precondition measure "Numerator"/>
			</#list>
	</#if>
</#if>			

<#if measure.population_criteria["NUMER_1"]??>
	<#assign numerCriteria = measure.population_criteria["NUMER_1"]>
		<#if numerCriteria?? & numerCriteria.preconditions??>
			<#list numerCriteria.preconditions as precondition>
				<@createCondition precondition measure "Numerator"/>
			</#list>
	</#if>
</#if>	

<#if measure.population_criteria["NUMER_2"]??>
	<#assign numerCriteria = measure.population_criteria["NUMER_2"]>
		<#if numerCriteria?? & numerCriteria.preconditions??>
			<#list numerCriteria.preconditions as precondition>
				<@createCondition precondition measure "Numerator"/>
			</#list>
	</#if>	
</#if>
<#-- Numerator Rule-Group Rule Generation - END -->

<#-- Stratum Rule-Group Rule Generation - BEGIN -->
<#-- Stratum - 1 -->
<#if measure.population_criteria["STRAT"]??> 
	<#assign stratCriteria = measure.population_criteria["STRAT"]>
		<#if stratCriteria?? & stratCriteria.preconditions??>
			<#list stratCriteria.preconditions as precondition>
				<@createCondition precondition measure "Strat"/>
			</#list>
	</#if>
</#if>

<#-- Stratum - 2 -->
<#if measure.population_criteria["STRAT_1"]??> 
	<#assign stratCriteria = measure.population_criteria["STRAT_1"]>
		<#if stratCriteria?? & stratCriteria.preconditions??>
			<#list stratCriteria.preconditions as precondition>
				<@createCondition precondition measure "Strat"/>
			</#list>
	</#if>
</#if>

<#-- Stratum - 3 -->
<#if measure.population_criteria["STRAT_2"]??> 
	<#assign stratCriteria = measure.population_criteria["STRAT_2"]>
		<#if stratCriteria?? & stratCriteria.preconditions??>
			<#list stratCriteria.preconditions as precondition>
				<@createCondition precondition measure "Strat"/>
			</#list>
	</#if>
</#if>

<#-- Stratum - 4 -->
<#if measure.population_criteria["STRAT_3"]??> 
	<#assign stratCriteria = measure.population_criteria["STRAT_3"]>
		<#if stratCriteria?? & stratCriteria.preconditions??>
			<#list stratCriteria.preconditions as precondition>
				<@createCondition precondition measure "Strat"/>
			</#list>
	</#if>
</#if>

<#-- Stratum - 5 -->
<#if measure.population_criteria["STRAT_4"]??> 
	<#assign stratCriteria = measure.population_criteria["STRAT_4"]>
		<#if stratCriteria?? & stratCriteria.preconditions??>
			<#list stratCriteria.preconditions as precondition>
				<@createCondition precondition measure "Strat"/>
			</#list>
	</#if>
</#if>

<#-- Stratum - 6 -->
<#if measure.population_criteria["STRAT_5"]??> 
	<#assign stratCriteria = measure.population_criteria["STRAT_5"]>
		<#if stratCriteria?? & stratCriteria.preconditions??>
			<#list stratCriteria.preconditions as precondition>
				<@createCondition precondition measure "Strat"/>
			</#list>
	</#if>
</#if>
<#-- Stratum Rule-Group Rule Generation - END -->

<#-- Generation of Final Results Section in drl -->
<@createGridForResults/>
<#if measure.population_criteria["IPP"]??>
rule "Initial Population Met"
ruleflow-group "Initial Patient Population"
	when
		$result: ProportionResult(initialPatientPopulation == ResultState.UNMET)
		<#if popConjunctions[0] == 'allTrue'>
 			<#list ippPC as precondition>
				ConditionAudit(sequenceId == ${precondition.id}, qualifies == ResultState.MET, result == $result)
			</#list>
		</#if>
		<#if popConjunctions[0] == 'atLeastOneTrue'>
			ConditionAudit(sequenceId in (<#list ippPC as precondition> ${precondition.id},</#list>), qualifies == ResultState.MET, result == $result)
		</#if>
	then
		logger.debug("Qualifies for Initial Population: " + $result.getMeasureId());
		$result.setInitialPatientPopulation(ResultState.MET);
		update($result);
end
</#if>

<#if measure.population_criteria["DENOM"]??>  
rule "Denominator Met"
ruleflow-group "Denominator"
	when
		$result: ProportionResult(denominator == ResultState.UNMET)
		<#if denomPC??>
			<#if popConjunctions[0] == 'allTrue'>
	 			<#list denomPC as precondition>
					ConditionAudit(sequenceId == ${precondition.id}, qualifies == ResultState.MET, result == $result)
				</#list>
			</#if>
			<#if popConjunctions[0] == 'atLeastOneTrue'>
				ConditionAudit(sequenceId in (<#list ippPC as precondition> ${precondition.id},</#list>), qualifies == ResultState.MET, result == $result)
			</#if>
		</#if>
	then
		logger.debug("Qualifies for Denominator: " + $result.getMeasureId());
		$result.setDenominator(ResultState.MET);
		update($result);
end
</#if>

<#if measure.population_criteria["DENEX"]??> 
rule "Denominator Exclusions Met"
ruleflow-group "Denominator Exclusions"
	when
		$result: ProportionResult(denominatorExclusions == ResultState.UNMET)
		ConditionAudit(sequenceId == ?, qualifies == ResultState.MET, result == $result)
	then
		logger.debug("Qualifies for Denominator Exclusions: " + $result.getMeasureId());
		$result.setDenominatorExclusions(ResultState.MET);
		update($result);
end
</#if>

<#if measure.population_criteria["NUMER"]??> 
rule "Numerator Met"
ruleflow-group "Numerator"
	when
		$result: ProportionResult(numerator == ResultState.UNMET)
		<#if popConjunctions[1] == 'allTrue'>
 			<#list numerPC as precondition>
				ConditionAudit(sequenceId == ${precondition.id}, qualifies == ResultState.MET, result == $result)
			</#list>
		</#if>
		<#if popConjunctions[1] == 'atLeastOneTrue'>
 			<#list numerPC as precondition>
				ConditionAudit(sequenceId == ${precondition.id}, qualifies == ResultState.MET, result == $result)
			</#list>
		</#if>
	then
		logger.debug("Qualifies for Numerator: " + $result.getMeasureId());
		$result.setNumerator(ResultState.MET);
		update($result);
end
</#if>

<#if measure.population_criteria["DENEXCEP"]??> 
rule "Denominator Exceptions Met"
ruleflow-group "Denominator Exceptions"
	when
		$result: ProportionResult(denominatorExceptions == ResultState.UNMET)
		ConditionAudit(sequenceId in (?, ?), qualifies == ResultState.MET, result == $result)
	then
		logger.debug("Qualifies for Denominator Exceptions: " + $result.getMeasureId());
		$result.setDenominatorExceptions(ResultState.MET);
		update($result);
end
</#if>

<#-- End of Template -->

<#-- Beginning of Marco section -->

<#macro createCondition precondition measure rulegroup>
	<#if (precondition.preconditions!?size > 0)>
  		<#list precondition.preconditions as precondition1>
  			<@createCondition precondition1 measure rulegroup/>
  			<#if precondition1?has_next>
	  			<#if precondition.conjunction_code == 'allTrue'>
	  				<#-- AND -->
	  			</#if>
	  			<#if precondition.conjunction_code == 'atLeastOneTrue'>
	  				<#-- OR -->
	  			</#if>
  			</#if>
  		</#list>
  	<#else>
	  	
	<@createRuleLabel measure precondition rulegroup/>
	when
	exists  ConditionAudit(sequenceId == ${precondition.id-1} ,qualifies==ResultState.UNMET)		
	$audit : ConditionAudit(sequenceId == ${precondition.id-1})
			<@createDerivedCondition measure.data_criteria[precondition.reference] precondition measure rulegroup/>
	then
		logger.debug("Condition " + $audit.getSequenceId() + " is met");
		modify ($audit) {
			setQualifies(ResultState.MET)
		}
		insert(new QdmElementReference($audit, $qdmElementId,  -1));
	end				  			
	  	</#if>	
</#macro>

<#macro createDerivedCondition dataCriteria precondition measure rulegroup>
	<#if dataCriteria.children_criteria??>
		<#list dataCriteria.children_criteria as referenceId>
			<@createDerivedCondition measure.data_criteria[referenceId] precondition measure rulegroup/>
			<#if referenceId?has_next>
				<#if dataCriteria.derivation_operator == 'UNION'>
	then
		logger.debug("Condition " + $audit.getSequenceId() + " is met");
		modify ($audit) {
			setQualifies(ResultState.MET)
		}
		insert(new QdmElementReference($audit, $qdmElementId,  -1));
end	
				<#-- OR -->
				
<@createRuleLabelChild measure precondition rulegroup referenceId?index/>	
when
exists  ConditionAudit(sequenceId == ${precondition.id-1} ,qualifies==ResultState.UNMET)		
$audit : ConditionAudit(sequenceId == ${precondition.id-1})	
				</#if>
			</#if>
		</#list>
	<#else>
		<@createSingleCondition dataCriteria precondition measure/>	
	</#if>
</#macro>

<#-- Macro to create Single Condition in drl -->
<#macro createSingleCondition dataCriteria precondition measure>
QdmElement(
	patientId == $audit.patientId,
		<#if dataCriteria.definition?? & dataCriteria.status??>
	dataType == QdmDataType.${dataCriteria.definition?upper_case}_${dataCriteria.status?upper_case},
		<#else>
			<#if dataCriteria.definition??>
			<#if dataCriteria.definition=="patient_characteristic_birthdate">
			dataType == QdmDataType.PATIENT_CHARACTERISTIC_BIRTH_DATE,
			</#if>
			
			<#if dataCriteria.definition!="patient_characteristic_birthdate">
	dataType == QdmDataType.${dataCriteria.definition?upper_case},
	</#if>
			</#if>
		</#if>
		<#if dataCriteria.definition=="encounter" & dataCriteria.status=="performed">
	isSourceListEntry($audit.providerId),
		</#if>
		
		<#if dataCriteria.temporal_references??><#-- if - 1 start-->
			<#if dataCriteria.temporal_references[0].range??><#-- if - 2 start-->
				<#if dataCriteria.temporal_references[0].range.high?? || dataCriteria.temporal_references[0].range.low??>
			 		<#if dataCriteria.temporal_references[0].type == 'SBS'>
			 			<#-- Starts Before Start -->
	
	
	
	<#if dataCriteria.definition=="patient_characteristic_birthdate">
		<#if dataCriteria.temporal_references[0].range.low??>
			<#if dataCriteria.temporal_references[0].range.low.unit??>
				<#if dataCriteria.temporal_references[0].range.low.unit=="a">
					QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS) > ${dataCriteria.temporal_references[0].range.low.value},
				</#if>
				<#if dataCriteria.temporal_references[0].range.low.unit=="mo">
					QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.MONTHS) > ${dataCriteria.temporal_references[0].range.low.value},
				</#if>
			</#if>
		</#if>
		
		<#if dataCriteria.temporal_references[0].range.high??>
			<#if dataCriteria.temporal_references[0].range.high.unit??>
				<#if dataCriteria.temporal_references[0].range.high.unit=="a">
					QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS) < ${dataCriteria.temporal_references[0].range.high.value},
				</#if>
			
				<#if dataCriteria.temporal_references[0].range.high.unit=="mo">
					QdmFunction.startsBeforeStart(period, $audit.measurePeriod, QdmPeriod.MONTHS) < ${dataCriteria.temporal_references[0].range.high.value},
				</#if>
			</#if>
		</#if>
	</#if>
	
	
	
	<#if dataCriteria.definition!="patient_characteristic_birthdate">
		QdmFunction.startsBeforeStart(period, $audit.measurePeriod) < ${dataCriteria.temporal_references[0].range.high.value},
	</#if>
			 		<#else>
			 			<#if dataCriteria.temporal_references[0].type == 'EBS'>
			 				<#-- Ends Before Start -->
	<#if dataCriteria.temporal_references[0].range.low??>
			<#if dataCriteria.temporal_references[0].range.low.unit??>
				<#if dataCriteria.temporal_references[0].range.low.unit=="a">
					QdmFunction.endsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS) > ${dataCriteria.temporal_references[0].range.low.value},
				</#if>
				<#if dataCriteria.temporal_references[0].range.low.unit=="mo">
					QdmFunction.endsBeforeStart(period, $audit.measurePeriod, QdmPeriod.MONTHS) > ${dataCriteria.temporal_references[0].range.low.value},
				</#if>
			</#if>
		</#if>
		
		<#if dataCriteria.temporal_references[0].range.high??>
			<#if dataCriteria.temporal_references[0].range.high.unit??>
				<#if dataCriteria.temporal_references[0].range.high.unit=="a">
					QdmFunction.endsBeforeStart(period, $audit.measurePeriod, QdmPeriod.YEARS) < ${dataCriteria.temporal_references[0].range.high.value},
				</#if>
			
				<#if dataCriteria.temporal_references[0].range.high.unit=="mo">
					QdmFunction.endsBeforeStart(period, $audit.measurePeriod, QdmPeriod.MONTHS) < ${dataCriteria.temporal_references[0].range.high.value},
				</#if>
			</#if>
		</#if>		 			</#if>
			 			<#if dataCriteria.temporal_references[0].type == 'DURING'>
			 				<#-- During -->
	QdmFunction.during(period, $audit.measurePeriod) < ${dataCriteria.temporal_references[0].range.high.value},
			 			</#if>
			 			<#if dataCriteria.temporal_references[0].type == 'OVERLAP'>
			 				<#-- Overlaps -->
	QdmFunction.overlap(period, $audit.measurePeriod) < ${dataCriteria.temporal_references[0].range.high.value},
			 			</#if>
			 			<#if dataCriteria.temporal_references[0].type == 'SBE'>
			 				<#-- Starts Before End -->
	QdmFunction.startsbeforeEnd(period, ${dataCriteria.temporal_references[0].range.high.unit}) < ${dataCriteria.temporal_references[0].range.high.value},
			 			</#if>
			 			<#if dataCriteria.temporal_references[0].type == 'EAS'>
			 				<#-- Ends After Start -->
	QdmFunction.endsAfterStart(period, $audit.measurePeriod) < ${dataCriteria.temporal_references[0].range.high.value},
			 			</#if>
			 			<#if dataCriteria.temporal_references[0].type == 'EBE'>
			 				<#-- Ends Before Start -->
	QdmFunction.endsBeforeEnd(period, $audit.measurePeriod) < ${dataCriteria.temporal_references[0].range.high.value},
			 			</#if>
			 			<#if dataCriteria.temporal_references[0].type == 'SAE'>
			 				<#-- Starts After End -->
	QdmFunction.startsAfterEnd(period, $audit.measurePeriod) < ${dataCriteria.temporal_references[0].range.high.value},
			 			</#if>
			 			<#if dataCriteria.temporal_references[0].type == 'SCW'>
			 				<#-- Starts Concurrent With -->
	QdmFunction.startsConcurrentWith(period, $audit.measurePeriod) < ${dataCriteria.temporal_references[0].range.high.value},
			 			</#if>
			 			<#if dataCriteria.temporal_references[0].type == 'SACW'>
			 				<#-- Starts After Concurrent With -->
	QdmFunction.startsAfterConcurrentWith(period, $audit.measurePeriod) < ${dataCriteria.temporal_references[0].range.high.value},
			 			</#if>
			 			<#if dataCriteria.temporal_references[0].type == 'SAS'>
			 				<#-- Starts After Start -->
	QdmFunction.startsAfterStart(period, $audit.measurePeriod) < ${dataCriteria.temporal_references[0].range.high.value},
			 			</#if>
			 			<#if dataCriteria.temporal_references[0].type == 'EAE'>
			 				<#-- Ends After End -->
	QdmFunction.endsAfterEnd(period, $audit.measurePeriod) < ${dataCriteria.temporal_references[0].range.high.value},
			 			</#if>
			 		</#if>
				 	</#if>
				</#if><#-- if - 2 end-->
		</#if><#-- if - 1 end-->
		
		
		
		<#if dataCriteria.code_list_id??>	
		
	lookupService.isChild("${dataCriteria.code_list_id}", codeSystemOid, codeValue),
		</#if>	
		$qdmElementId : qdmElementId
		
)
	<#if dataCriteria.value??>
QdmElementAttribute(
	qdmElementId == $qdmElementId, 
	attributeName == QdmAttributeName.RESULT, 
	attributeValue instanceof QdmCD,
			<#if dataCriteria.value.code_list_id??>
	lookupService.isChild("${dataCriteria.value.code_list_id}", qdmCd.codingSystem, qdmCd.codeValue),
			</#if>
	$qdmAttributeId : qdmAttributeId)	
	</#if>	
</#macro>

<#-- Macro to print the Initialization section in drl -->
<#macro InitializeRule measure>
rule "Initialize Measure ${measure.cms_id} Results"
ruleflow-group "Initialize"
when
<#if measure.population_criteria["IPP"]??>
	$result : ProportionResult(initialPatientPopulation == ResultState.NOT_DEFINED)
</#if>
then
	logger.debug ("Setting up the ProportionResult Result: " + $result.getMeasureId());
	modify ($result) {
		setInitialPatientPopulation(ResultState.NOT_EVALUATED),
	<#if measure.population_criteria["DENOM"]??>
		setDenominator(ResultState.NOT_EVALUATED),
	</#if>
	<#if measure.population_criteria["DENEXCEP"]??>
		setDenominatorException(ResultState.NOT_EVALUATED),
	</#if>
	<#if measure.population_criteria["DENEX"]??>
		setDenominatorExclusion(ResultState.NOT_EVALUATED),
	</#if>
	<#if measure.population_criteria["NUMER"]??>
		setNumerator(ResultState.NOT_EVALUATED)
	</#if>
	}
end
</#macro>

<#-- Macro to print the Condition # and correct Rule Group before each rule in Drl -->
<#macro createRuleLabel measure precondition rulegroup>
<@createGrid/>
<#assign ruleGroup = rulegroup>
rule "${ruleGroup} - Condition ${precondition.id-1}"
ruleflow-group "${ruleGroup}"
</#macro>

<#-- Macro to print the Condition # and correct Rule Group before each rule in Drl Child-->
<#macro createRuleLabelChild measure precondition rulegroup number>
<@createGrid/>
<#assign ruleGroup = rulegroup>
rule "${ruleGroup} - Condition ${precondition.id-1}_${number}"
ruleflow-group "${ruleGroup}"
</#macro>



<#-- Macro to print place holder to add comments from e-Spec before each rule -->
<#macro createGrid>
/*=============================================================================
QdmElement -> eSpec
Using:
	Value Set --> eSpec
================================================================================*/
</#macro>
<#-- Macro to print place holder to add comments from e-Spec before each rule -->
<#macro createGridForResults>
/*==============================================================================
Results
==============================================================================*/
</#macro>