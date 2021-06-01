 */
/*
 * @Copyright("2017 General Electric Company")
 *
 * All Rights Reserved.
 * No portions of this source code or the resulting compiled program may be used without
 * express written consent and licensing by GE Healthcare 
 */
 
<#assign ippCriteria = measure.population_criteria["IPP"]>
	<#if ippCriteria?? & ippCriteria.preconditions??>
		ruleflow-group "${measure.cms_id} Initial Patient Population"
		<#list ippCriteria.preconditions as precondition>
			<@createCondition precondition measure/>
		</#list>
	</#if>
	
<#assign denomCriteria = measure.population_criteria["DENOM"]>
	<#if denomCriteria?? & denomCriteria.preconditions??>
		ruleflow-group "${measure.cms_id} Denominator"
		<#list denomCriteria.preconditions as precondition>
			<@createCondition precondition measure/>
		</#list>
	</#if>

<#if measure.population_criteria["DENEXCEP"]??>	
<#assign denomExcpCriteria = measure.population_criteria["DENEXCEP"]>
	<#if denomExcpCriteria?? & denomExcpCriteria.preconditions??>
	ruleflow-group "${measure.cms_id} Denominator Exclusions"
		<#list denomExcpCriteria.preconditions as precondition>
			<@createCondition precondition measure/>
		</#list>
	</#if>
</#if>

<#if measure.population_criteria["DENEX"]??>
<#assign denomExCriteria = measure.population_criteria["DENEX"]>
	<#if denomExCriteria?? & denomExCriteria.preconditions??>
	ruleflow-group "${measure.cms_id} Denominator Exceptions"
		<#list denomExCriteria.preconditions as precondition>
			<@createCondition precondition measure/>
		</#list>
	</#if>
</#if>
			
<#assign numerCriteria = measure.population_criteria["NUMER"]>
	<#if numerCriteria?? & numerCriteria.preconditions??>
		ruleflow-group "${measure.cms_id} Numerator"
		<#list numerCriteria.preconditions as precondition>
			<@createCondition precondition measure/>
		</#list>
	</#if>			

<#macro createCondition precondition measure>
	<#if (precondition.preconditions!?size > 0)>
  		<#list precondition.preconditions as precondition1>
  			<@createCondition precondition1 measure/>
  			<#if precondition1?has_next>
	  			<#if precondition.conjunction_code == 'allTrue'>
	  				AND
	  			</#if>
	  			<#if precondition.conjunction_code == 'atLeastOneTrue'>
	  				OR
	  			</#if>
  			</#if>
  		</#list>
  	<#else>	
		when
		exists  ConditionAudit(sequenceId == ${precondition.id} ,qualifies==ResultState.UNMET)		
			$audit : ConditionAudit(sequenceId == ${precondition.id})
		<@createDerivedCondition measure.data_criteria[precondition.reference] measure/>
		then
			logger.debug("Condition " + $audit.getSequenceId() + " is met");
			modify ($audit) {
				setQualifies(ResultState.MET)
			}
			insert(new QdmElementReference($audit, $qdmElementId,  -1));
		end				  			
  	</#if>	
</#macro>

<#macro createDerivedCondition dataCriteria measure>
	<#if dataCriteria.children_criteria??>
		<#list dataCriteria.children_criteria as referenceId>
			<@createDerivedCondition measure.data_criteria[referenceId] measure/>
			<#if referenceId?has_next>
				<#if dataCriteria.derivation_operator == 'UNION'>
					OR
				</#if>
			</#if>
		</#list>
	<#else>
		<@createSingleCondition dataCriteria measure/>	
	</#if>
</#macro>


<#macro createSingleCondition dataCriteria measure>
	QdmElement(
		$qdmElementId : qdmElementId,
		patientId == $audit.patientId,
		<#if dataCriteria.definition?? & dataCriteria.status??>
			dataType == ${dataCriteria.definition},${dataCriteria.status}
		<#else>
			<#if dataCriteria.definition??>
			dataType == ${dataCriteria.definition}
			</#if>
		</#if>
		
		<#if dataCriteria.definition=="encounter" & dataCriteria.status=="performed">
			isSourceListEntry($audit.providerId),
		</#if>
		
		<#if dataCriteria.temporal_references??>
			<#if dataCriteria.temporal_references[0].range??>
				<#if dataCriteria.temporal_references[0].range.high??>
			 		<#if dataCriteria.temporal_references[0].type == 'SBS'>
			 		QdmFunction.startsBeforeStart(period, ${dataCriteria.temporal_references[0].range.high.unit}) < ${dataCriteria.temporal_references[0].range.high.value}
			 		<#else>
			 			<#if dataCriteria.temporal_references[0].type == 'EBS'>
			 			QdmFunction.startsBeforeStart(period, ${dataCriteria.temporal_references[0].range.high.unit}) < ${dataCriteria.temporal_references[0].range.high.value}
			 			</#if>
			 		</#if>
				 <#else>
				 	<#if dataCriteria.temporal_references[0].range.low??>
				 		<#if dataCriteria.temporal_references[0].type == 'SBS'>
				 		QdmFunction.startsBeforeStart(period, ${dataCriteria.temporal_references[0].range.low.unit}) >= ${dataCriteria.temporal_references[0].range.low.value} 
				 		</#if>
				 	</#if>
				 </#if>
			</#if>
		</#if>
		<#if dataCriteria.code_list_id??>	
			lookupService.isChild(${dataCriteria.code_list_id}, codeSystemOid, codeValue)
		</#if>	
		)
	<#if dataCriteria.value??>
		QdmElementAttribute(
			qdmElementId == $qdmElementId, 
			attributeName == QdmAttributeName.RESULT, 
			attributeValue instanceof QdmCD,
			lookupService.isChild(${dataCriteria.value.code_list_id}, qdmCd.codingSystem, qdmCd.codeValue),
			$qdmAttributeId : qdmAttributeId)	
	</#if>	
	
</#macro>