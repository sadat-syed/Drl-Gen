 /*
 * Template for creating Condition Drl.
 * @Copyright("2018 General Electric Company")
 *
 * All Rights Reserved.
 * No portions of this source code or the resulting compiled program may be used without
 * express written consent and licensing by GE Healthcare 
 * Generated using 'conditionDrl.ftl' Apache Freemarker Template
 */
 
package com.ct.haf.mu.engine.measure;

import org.slf4j.Logger;
import org.drools.spi.KnowledgeHelper;
import com.ct.haf.mu.entity.measuredefinition.Condition;
import com.ct.haf.mu.entity.measuredefinition.Variable;

// Use logging system 
global Logger logger;

/*
e-Spec from HTML
*/

function void createCondition(KnowledgeHelper k, Variable populationCriteria, int index, String description, int conjunctionId)
{
	Condition condition = new Condition();
	condition.setVariable(populationCriteria);
	condition.setMeasureId("${measureId}");
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
    	<@createConditions measure  ipp "Initial Patient Population"/>
    	<@createConditions measure  denominator "Denominator"/>
    	<@createConditions measure  denomExclusion "Denominator Exclusion"/>
    	<@createConditions measure  numerator "Numerator"/>
    	<@createConditions measure  numeratorExcusions "Numerator Exclusions"/>
    	<@createConditions measure  denomException "Denominator Exception"/>

<#-- Macro Section -->
<#-- Macro to create and print Conditions for each Rule group -->
<#macro createConditions measure conditions ruleGroup>

<#if ruleGroup=="Initial Patient Population">
	<#list ipp as i>			
 		createCondition(drools, Variable.INITIAL_PATIENT_POPULATION, ${i?index} ,  ${i},  -1);
	</#list>
</#if>

<#if ruleGroup=="Denominator">
	<#list denominator as i>			
 		createCondition(drools, Variable.DENOMINATOR, ${i?index} ,  ${i},  -1);
	</#list>
</#if>

<#if ruleGroup=="Denominator Exclusion">
<#list denomExclusion as i>	
 		createCondition(drools, Variable.DENOMINATOR_EXCLUSIONS, ${i?index} ,  ${i},  -1);
	</#list>
</#if>

<#if ruleGroup=="Numerator">
	<#list numerator as i>			
 		createCondition(drools, Variable.NUMERATOR, ${i?index} ,  ${i},  -1);
	</#list>
</#if>

<#if ruleGroup=="Numerator Exclusions">
<#list numeratorExcusions as i>			
 		createCondition(drools, Variable.NUMERATOR_EXCLUSIONS, ${i?index} ,  ${i},  -1);
	</#list>
</#if>

<#if ruleGroup=="Denominator Exception" || ruleGroup=="Denominator Exceptions">
	<#list denomException as i>			
 		createCondition(drools, Variable.DENOMINATOR_EXCEPTIONS, ${i?index} ,  ${i},  -1);
	</#list>
</#if>

</#macro>
end