<#assign expressions = expressions>
/*
 * @Copyright("2018 General Electric Company")
 * All Rights Reserved.
 * No portions of this source code or the resulting compiled program may be used without
 * express written consent and licensing by GE Healthcare 
 * Generated using 'conditionDrlCql.ftl' Apache Freemarker Template
 */
 /*
e-Spec from HTML
*/
package com.ct.haf.mu.engine.measure;

import org.slf4j.Logger;
import org.drools.spi.KnowledgeHelper;
import com.ct.haf.mu.entity.measuredefinition.Condition;
import com.ct.haf.mu.entity.measuredefinition.Variable;

// Use logging system 
global Logger logger;


function void createCondition(KnowledgeHelper k, Variable populationCriteria, int index, String description, int conjunctionId)
{
	Condition condition = new Condition();
	condition.setVariable(populationCriteria);
	condition.setMeasureId("${"${measureId}"?upper_case}");
	condition.setMeasureVersion("${measureVersion}");
	condition.setConditionId(index);
	condition.setDescription(description);
	condition.setConjunctionId(conjunctionId);
	k.insert(condition);
}
rule "Initialize Measure ${measure} Conditions"
ruleflow-group "Initialize"
    then
    	logger.debug("Initializing Conditions for measure: ${measure}");
    	<@createConditions measure "Initial Population"/>
    	<@createConditions measure "Denominator"/>
		<#if (DenominatorExclusions?? && DenominatorExclusions?size > 0)>
			<@createConditions measure "Denominator Exclusions"/>
		</#if>
		<@createConditions measure "Numerator"/>
		<#if (DenominatorExceptions?? && DenominatorExceptions?size > 0)>
			<@createConditions measure "Denominator Exceptions"/>
		</#if>
		<#if (expressions["Stratification 1"]?? && expressions["Stratification 1"]?size > 0)>
			<@createConditions measure "Reporting Stratification 1"/>
		</#if>
		<#if (expressions["Stratification 2"]?? && expressions["Stratification 2"]?size > 0)>
			<@createConditions measure "Reporting Stratification 2"/>
		</#if>
		
<#macro printCondition ruleGroup conditionIPP  var parentCondition operator="">
	<#if operator=="" || operator == "ExpressionRef">
		createCondition(drools, Variable.${ruleGroup}, ${conditionIPP},  "${var}",  ${parentCondition});
	<#else>
		createCondition(drools, Variable.${ruleGroup}, ${conditionIPP},  "${operator}:${var}",  ${parentCondition});
	</#if>
</#macro>

<#-- Macro Section -->
<#-- Macro to create and print Conditions for each Rule group -->
<#macro createConditions measure ruleGroup>
<#if ruleGroup == "Initial Population">
	<#list InitialPopulation as i>
		<#assign queryVariable = expressions[i.name]>
			<#assign conditionIPP = queryVariable.conditionNumber>
			<#assign condition = queryVariable.dependentObjects>
			<#assign constraintList = queryVariable.constraintList>
			<#assign parentCondition = queryVariable.parentDependentConditionNumber>
			<#if parentCondition == 0>
				<#assign parentCondition = -1>
			</#if>
			<#if parentCondition == -1>
					<@printCondition "INITIAL_PATIENT_POPULATION" conditionIPP  i.name parentCondition/>
			<#elseif parentCondition !=-1>
			<#list expressions?values as value>
  				<#if value.conditionNumber == queryVariable.parentDependentConditionNumber>
  				<#if value.constraintList?size != 0>
  				 <#list value.constraintList as element>
  				 	<#if (element.leftOperand?has_content && element.leftOperand?contains(i.name)) ||  
						(element.rightOperand?has_content && element.rightOperand?contains(i.name))>
							<@printCondition "INITIAL_PATIENT_POPULATION" conditionIPP  i.name parentCondition element.operator/>
							<#break>
					</#if>
  				 </#list>
  				 <#elseif value.constraintList?size == 0>
  				 		<@printCondition "INITIAL_PATIENT_POPULATION" conditionIPP  i.name parentCondition/> 
  				 </#if>
  				</#if>
			</#list>
			</#if>
	</#list>
</#if>
<#if ruleGroup == "Denominator">
	<#list Denominator as i>	
		<#assign queryVariable = expressions[i.name]>		
			<#assign conditionDen = queryVariable.conditionNumber>
			<#assign condition = queryVariable.dependentObjects>
			<#assign constraintList = queryVariable.constraintList>
			<#assign check = 0>
			<#assign parentCondition = queryVariable.parentDependentConditionNumber>
			<#if parentCondition == 0>
				<#assign parentCondition = -1>
			</#if>
			<#if (condition)?has_content>
				<#list condition as dependent>
				<#if constraintList?size != 0>
					<#list constraintList as element>
						<#if (element.leftOperand?has_content && element.leftOperand?contains(dependent)) ||  
						(element.rightOperand?has_content && element.rightOperand?contains(dependent))>
							<@printCondition "DENOMINATOR" conditionDen  condition?join(", ") parentCondition element.operator/>
							<#assign check = 1>
							<#break>
						<#else>
							<@printCondition "DENOMINATOR" conditionDen  condition?join(", ") parentCondition/>
							<#assign check = 1>
							<#break>
						</#if>
					</#list>
				<#else>
					<@printCondition "DENOMINATOR" conditionDen  i.name parentCondition />
				</#if>
				<#if check == 1>
						<#break>
					</#if>
				</#list>
			<#else>
				<@printCondition "DENOMINATOR" conditionDen  i.name parentCondition />
			</#if>
	</#list>
</#if>
<#if ruleGroup == "Denominator Exclusions">
	<#list DenominatorExclusions as i>	
		<#assign queryVariable = expressions[i.name]>	
			<#assign conditionDenExcl = queryVariable.conditionNumber>
			<#assign condition = queryVariable.dependentObjects>
			<#assign constraintList = queryVariable.constraintList>
			<#assign parentCondition = queryVariable.parentDependentConditionNumber>
			<#assign check = 0>
			<#if parentCondition == 0>
				<#assign parentCondition = -1>
			</#if>
			<#if (condition)?has_content>
				<#list condition as dependent>
				<#if constraintList?size != 0>
					<#list constraintList as element>
						<#if (element.leftOperand?has_content && element.leftOperand?contains(dependent)) ||  
						(element.rightOperand?has_content && element.rightOperand?contains(dependent))>
							<@printCondition "DENOMINATOR_EXCLUSIONS" conditionDenExcl  condition?join(", ") parentCondition element.operator/>
							<#assign check = 1>
							<#break>
						<#else>
							<@printCondition "DENOMINATOR_EXCLUSIONS" conditionDenExcl  condition?join(", ") parentCondition/>
							<#assign check = 1>
							<#break>
						</#if>
					</#list>
				<#else>
					<@printCondition "DENOMINATOR_EXCLUSIONS" conditionDenExcl  i.name parentCondition />
				</#if>
				<#if check == 1>
						<#break>
					</#if>
				</#list>
			<#else>
				<@printCondition "DENOMINATOR_EXCLUSIONS" conditionDenExcl  i.name parentCondition />
			</#if>
	</#list>
</#if>
<#if ruleGroup == "Numerator">
	<#list Numerator as i>		
		<#assign queryVariable = expressions[i.name]>	
			<#assign conditionNum = queryVariable.conditionNumber>
			<#assign condition = queryVariable.dependentObjects>
			<#assign constraintList = queryVariable.constraintList>
			<#assign check = 0>
			<#assign parentCondition = queryVariable.parentDependentConditionNumber>
			<#if parentCondition == 0>
				<#assign parentCondition = -1>
			</#if>
			<#if (condition)?has_content>
				<#list condition as dependent>
					<#if constraintList?size != 0>
						<#list constraintList as element>
							<#if (element.leftOperand?has_content && element.leftOperand?contains(dependent)) ||  
							(element.rightOperand?has_content && element.rightOperand?contains(dependent))>
								<@printCondition "NUMERATOR" conditionNum  condition?join(", ") parentCondition element.operator/>
								<#assign check = 1>
								<#break>
							<#else>
								<@printCondition "NUMERATOR" conditionNum  condition?join(", ") parentCondition/>
								<#assign check = 1>
								<#break>
							</#if>
						</#list>
					<#else>
						<@printCondition "NUMERATOR" conditionNum  i.name parentCondition />
					</#if>
					<#if check == 1>
						<#break>
					</#if>
				</#list>
			<#else>
				<@printCondition "NUMERATOR" conditionNum  i.name parentCondition />
			</#if>
	</#list>
</#if>
<#if ruleGroup == "Denominator Exceptions">
	<#list DenominatorExceptions as i>		
		<#assign queryVariable = expressions[i.name]>	
			<#assign conditionNum = queryVariable.conditionNumber>
			<#assign condition = queryVariable.dependentObjects>
			<#assign constraintList = queryVariable.constraintList>
			<#assign check = 0>
			<#assign parentCondition = queryVariable.parentDependentConditionNumber>
			<#if parentCondition == 0>
				<#assign parentCondition = -1>
			</#if>
			<#if (condition)?has_content>
				<#list condition as dependent>
					<#if constraintList?size != 0>
						<#list constraintList as element>
							<#if (element.leftOperand?has_content && element.leftOperand?contains(dependent)) ||  
							(element.rightOperand?has_content && element.rightOperand?contains(dependent))>
								<@printCondition "DENOMINATOR_EXCEPTIONS" conditionNum  condition?join(", ") parentCondition element.operator/>
								<#assign check = 1>
								<#break>
							<#else>
								<@printCondition "DENOMINATOR_EXCEPTIONS" conditionNum  condition?join(", ") parentCondition/>
								<#assign check = 1>
								<#break>
							</#if>
						</#list>
					<#else>
						<@printCondition "DENOMINATOR_EXCEPTIONS" conditionNum  i.name parentCondition />
					</#if>
					<#if check == 1>
						<#break>
					</#if>
				</#list>
			<#else>
				<@printCondition "DENOMINATOR_EXCEPTIONS" conditionNum  i.name parentCondition />
			</#if>
	</#list>
</#if>
<#if ruleGroup == "Reporting Stratification 1">
<#list Stratification1 as i>		
		<#assign queryVariable = expressions[i.name]>	
			<#assign conditionNum = queryVariable.conditionNumber>
			<#assign check = 0>
			<#assign parentCondition = queryVariable.parentDependentConditionNumber>
			<#if parentCondition == 0>
				<#assign parentCondition = -1>
			</#if>
				<@printCondition "REPORTING_STRATUM" conditionNum  i.name parentCondition />
	</#list>
</#if>
<#if ruleGroup == "Reporting Stratification 2">
<#list Stratification2 as i>		
		<#assign queryVariable = expressions[i.name]>	
			<#assign conditionNum = queryVariable.conditionNumber>
			<#assign check = 0>
			<#assign parentCondition = queryVariable.parentDependentConditionNumber>
			<#if parentCondition == 0>
				<#assign parentCondition = -1>
			</#if>
				<@printCondition "REPORTING_STRATUM" conditionNum  i.name parentCondition />
	</#list>
</#if>
</#macro>
end