package com.ct.cql.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections4.CollectionUtils;

import com.ct.cql.drlgen.DrlGenerator;
import com.ct.cql.elm.model.CodeDef;
import com.ct.cql.elm.model.CodeSystemDef;
import com.ct.cql.elm.model.Expression;
import com.ct.cql.elm.model.ExpressionDef;
import com.ct.cql.elm.model.ExpressionRef;
import com.ct.cql.elm.model.IncludeDef;
import com.ct.cql.elm.model.Library;
import com.ct.cql.elm.model.Library.CodeSystems;
import com.ct.cql.elm.model.Library.Codes;
import com.ct.cql.elm.model.Library.Includes;
import com.ct.cql.elm.model.Library.Statements;
import com.ct.cql.elm.model.Library.ValueSets;
import com.ct.cql.elm.model.ValueSetDef;
import com.ct.cql.entity.CQLModel;
import com.ct.cql.entity.Constants;
import com.ct.cql.entity.Constraint;
import com.ct.cql.entity.DrlFileModel;
import com.ct.cql.entity.PopulationCriteriaName;
import com.ct.cql.entity.QueryVariable;
import com.ct.cql.entity.RuleGroup;
import com.ct.cql.entity.SequenceInfo;
import com.ct.cql.evaluators.ExpressionEvaluators;
import com.ct.hqmf.drl.gen.CreateELMDocument;

public class CQLParser {
	private DrlFileModel drlFileModel;
	
	public CQLModel init(String dirPath) {
		CQLModel model = null;
		try {
			// configure fields needed for DrlGeneration
			drlFileModel = new DrlFileModel(dirPath);

			// parse & get HQMF file. i.e. Diabetes.xml from location
			SourceParser.parseXml(drlFileModel);

			System.out.println(" DRLFileModel :" + drlFileModel.toString());
			if (null != drlFileModel && null != drlFileModel.getCqlXmlFilePath()) {
				model = new CQLModel();
				//SET measure isEpisode OR not.
				model.setEpisodeMeasure(Constants.EPISODE_MEASUREIDS.contains(drlFileModel.getMeasureId()));
				// Source and Library initialization				
				System.out.println("Initialization Started ");
				Library xmlObj = CreateELMDocument.parseelm(new File(drlFileModel.getCqlXmlFilePath()));
				model.setValueSets(parseValueset(xmlObj));
				model.setStatements(parseStatements(xmlObj));
				model.setLibraries(parseIncludes(xmlObj));
				model.setCodeSets(parseCodeset(xmlObj));
				model.setPopulationCriteria(drlFileModel.getPopulationCriteriaList());
				model.setStratCount(drlFileModel.getStratCriteria());
				System.out.println("Initialization Done ");

				if (!model.getLibraries().isEmpty()) {
					resolveLibrary(model);
					System.out.println("Library loaded");
				}
			}
		} catch (Exception e) {
			System.out.println(" Error  : " + e.getStackTrace());
			e.printStackTrace();
		}

		return model;
	}
	
	/*
	 * this method is used to parse value sets
	 */
	
	public static Map<String, String> parseValueset(Library library){
		Map<String,String> valueSets = new LinkedHashMap<>();
		if(library.getValueSets() != null && CollectionUtils.isNotEmpty(library.getValueSets().getDef())){
			ValueSets entry = library.getValueSets();
			List<ValueSetDef> valueSetdef = entry.getDef();
			for (ValueSetDef def : valueSetdef) {
				String leafoid = def.getId();
				if (leafoid != null) {
					leafoid = leafoid.split(":")[2];
					valueSets.put((def.getName()), leafoid);
				}
			}
		}
		return valueSets;
	}
	
	/*
	 * this method is used to parse Statements
	 */
	
	public static LinkedHashMap<String, Expression> parseStatements(Library library){
		LinkedHashMap<String, Expression> statements = new LinkedHashMap<>();
		if (library.getStatements() != null && CollectionUtils.isNotEmpty(library.getStatements().getDef())) {
			Statements entry = library.getStatements();
			List<ExpressionDef> expression = entry.getDef();
			for (ExpressionDef def : expression) {
				statements.put((def.getName()), def.getExpression());
			}
		}
		return statements;
	}

	/*
	 * this method is used to parse additional files
	 */
	
	public static Map<String, String> parseIncludes(Library library){
		Map<String,String> statements = new LinkedHashMap<>();
		if(library != null && library.getIncludes() != null && CollectionUtils.isNotEmpty(library.getIncludes().getDef())){
			Includes entry = library.getIncludes();
			List<IncludeDef> expression = entry.getDef();
			for (IncludeDef def : expression) {
				statements.put(def.getLocalIdentifier(), def.getPath() + "-" + def.getVersion());
			}
		}
		return statements;
	}

	/*
	 * this method is used to parse code sets
	 */
	
	public static Map<String, String> parseCodeset(Library library) {

		Map<String,String> codeSets = new LinkedHashMap<>();
		Map<String,String> codeSystemsSets = new HashMap<>();
		if(library.getCodeSystems() != null && CollectionUtils.isNotEmpty(library.getCodeSystems().getDef())){
			CodeSystems entry = library.getCodeSystems();
			List<CodeSystemDef> codeSetdef = entry.getDef();
			for (CodeSystemDef def : codeSetdef) {
				String CodeSystemsleafoid = def.getId();
				if (CodeSystemsleafoid != null) {
					CodeSystemsleafoid = CodeSystemsleafoid.split(":")[2];
					codeSystemsSets.put((def.getName()), CodeSystemsleafoid);
				}
			}
		}

		if(library.getCodes() != null && CollectionUtils.isNotEmpty(library.getCodes().getDef())) {
			Codes entry = library.getCodes();
			List<CodeDef> codeSetdef = entry.getDef();
			for (CodeDef def : codeSetdef) {
				String codeleafoid = def.getId();
				String codeSystemsName = def.getCodeSystem().getName();
				if (codeleafoid != null) {
					if (codeSystemsSets.containsKey(codeSystemsName)) {
						codeSets.put((def.getName()), codeleafoid);
					}
				}
			}
		}
		return codeSets;
	}

	/*
	 * this method is merge valuesets , codesets and statements from main xml file with additional files
	 */
	public void resolveLibrary(CQLModel model) {

		for (Entry<String, String> entry : model.getLibraries().entrySet()) {
			File file = new File(drlFileModel.getMeasureDirectoryPath() + File.separatorChar + entry.getValue() + ".xml");
			Library library = CreateELMDocument.parseelm(file);
			Map<String, String> mergedValueSets = new HashMap<>(model.getValueSets());
			LinkedHashMap<String, Expression> mergedStatements = new LinkedHashMap<>(model.getStatements());
			Map<String, String> mergedCodeSets = new HashMap<>(model.getCodeSets());
			if(library != null) {
				Map<String, String> libraryValueSets = parseValueset(library);
				Map<String, Expression> libraryStatements = parseStatements(library);
				Map<String, String> libraryCodeSets = parseCodeset(library);
				mergedValueSets.putAll(libraryValueSets);
				mergedStatements.putAll(libraryStatements);
				mergedCodeSets.putAll(libraryCodeSets);
				model.setValueSets(mergedValueSets);
				model.setStatements(mergedStatements);
				model.setCodeSets(mergedCodeSets);
			}
		}
	}

	/*
	 * this method is used to evaluate expressionRef
	 */
	
	public static void expressionEvaluatorForExpRef(ExpressionRef exp, QueryVariable statement, CQLModel model) {
		int conditionNumber ;
		statement.setConditionNumber(model.getConditionNumber());
		conditionNumber = statement.getConditionNumber();
		model.setConditionNumber(++conditionNumber);
		Constraint constraint = CQLHelper.getConstraintTemplate(statement, exp.getClass().getSimpleName());
		constraint.setFromClause(true);
		CQLHelper.setStatementDependentObject(statement, (exp.getName()));
		constraint.setLeftOperand((exp.getName()));
		if (null == statement.getConstraintList()) {
			statement.setConstraintList(new CopyOnWriteArrayList<Constraint>());
		}
		statement.getConstraintList().add(constraint);
	}

	/**
	 * method to evaluate the expression
	 * @param exp
	 * @param statement
	 * @param model
	 * @param checkMap
	 */
	public void evaluateRuleWise(Expression exp,QueryVariable statement,CQLModel model)
	{
		if (exp instanceof ExpressionRef) {
			expressionEvaluatorForExpRef((ExpressionRef) exp, statement, model);
		} else {
			ExpressionEvaluators.expressionEvaluator(exp, statement, model);
		}
	}
	
	/**
	 * method for evaluating expressions group wise
	 * @param def
	 * @param checkMap
	 * @param model
	 * @param statement
	 *
	 */
	public void checkFunc(Entry<String, Expression> def, SequenceInfo sequenceInfo, CQLModel model,QueryVariable statement, Map<String, Object> expression)
	{
		Expression exp = def.getValue();
		if(def.getKey().contains(RuleGroup.IPP.getValue1()))
		{
			evaluateRuleWise(exp, statement, model);
			sequenceInfo.getCheckMap().put(def.getKey(), true);
			sequenceInfo.setIpEvaluted(true);
			CQLHelper.parseDependentQueryVariable(statement, model, sequenceInfo, expression);
		}
		else if(def.getKey().contains(RuleGroup.DENOM.getValue1()) && !def.getKey().contains(RuleGroup.DENOM_EXCL.getValue1()) 
				&& !def.getKey().contains(RuleGroup.DENOM_EXCEP.getValue1()) && sequenceInfo.isIpEvaluted() )
		{
			evaluateRuleWise(exp, statement, model);
			sequenceInfo.getCheckMap().put(def.getKey(), true);
			sequenceInfo.setDenomEvaluted(true);
			CQLHelper.parseDependentQueryVariable(statement, model, sequenceInfo, expression);
		}
		else if(def.getKey().contains(RuleGroup.DENOM_EXCL.getValue1()) && sequenceInfo.isDenomEvaluted())
		{
			evaluateRuleWise(exp, statement, model);
			sequenceInfo.getCheckMap().put(def.getKey(), true);
			CQLHelper.parseDependentQueryVariable(statement, model, sequenceInfo, expression);
		}
		else if(def.getKey().contains(RuleGroup.NUMER.getValue1()) && sequenceInfo.isDenomEvaluted())
		{
			evaluateRuleWise(exp, statement, model);
			sequenceInfo.getCheckMap().put(def.getKey(), true);
			sequenceInfo.setNumEvaluted(true);
			CQLHelper.parseDependentQueryVariable(statement, model, sequenceInfo, expression);
		}
		else if(def.getKey().contains(RuleGroup.DENOM_EXCEP.getValue1()) && sequenceInfo.isNumEvaluted())
		{
			evaluateRuleWise(exp, statement, model);
			sequenceInfo.getCheckMap().put(def.getKey(), true);
			CQLHelper.parseDependentQueryVariable(statement, model, sequenceInfo, expression);
		}
		else{
			if(sequenceInfo.isIpEvaluted() && !sequenceInfo.getCheckMap().get(def.getKey()))
			{
				evaluateRuleWise(exp, statement, model);
				sequenceInfo.getCheckMap().put(def.getKey(), true);
			}
		}
	}
	
	/*
	 * this method is starting point for parsing of xml file
	 */
	
	public Map<String, Object> startParsing(CQLModel model, SequenceInfo sequenceInfo) {
		System.out.println("Parsing starts");
		Map<String, Object> expression = new ConcurrentHashMap<>();
		List<QueryVariable> statementList = new ArrayList<>();

		Map<String, Boolean> checkMap = new HashMap<>();
		Map<Integer, List<String>> childMapDetails = new HashMap<>();
		for (Entry<String, Expression> def : model.getStatements().entrySet()) {
			if (!def.getKey().contains(Constants.IGNORE_SDE)) {
				boolean defaultValue = false;
				checkMap.put(def.getKey(), defaultValue);
			}
		}
		sequenceInfo.setCheckMap(checkMap);
		model.setSequenceInfo(sequenceInfo);
		while (sequenceInfo.getCheckMap().containsValue(false)) {
			for (Entry<String, Expression> def : model.getStatements().entrySet()) {
				QueryVariable statement = new QueryVariable();
				if (!def.getKey().contains(Constants.IGNORE_SDE) && !sequenceInfo.getCheckMap().get(def.getKey()))
				{
					statement.setName(def.getKey());
					statement.setMiddleqdmFunctionVariable("");
					statement.setQdmFunctionVariable(new StringBuilder(""));
					statement.setCreateQueryVariable(true);
					statement.setAddQdmFunction(true);
					//statement.setDataType(new ArrayList<String>());
					//statement.setValueSetList(new ArrayList<String>());

					checkFunc(def, sequenceInfo, model, statement,expression);
					statementList.add(statement);			
					expression.put(def.getKey(), statement);
					model.setExpressions(expression);
				}
			}
		}
		for(Object quer : model.getExpressions().values()){
			QueryVariable query = (QueryVariable)quer;
			CQLHelper.mergeConstraints(query); 
			//To set the a map to hold all the queryVariables with their dependentOpjects 
			if(query.getDependentObjects().size()>0){
				childMapDetails.put(query.getConditionNumber(), query.getDependentObjects());
				sequenceInfo.setChildMapDetails(childMapDetails);
			}
		}
		for(Object quer : model.getExpressions().values()){
			//To remove the birthdate dependency from stratification
			if(((QueryVariable)quer).getName().contains(Constants.STRATIFICATION)){
				modifyStratification((QueryVariable)quer, expression);
			}
		}
		//To set the parent dependent Condition number for the defs whose parent has not been set
		childMapDetails = sequenceInfo.getChildMapDetails();
		CQLHelper.setParentDependentCondition(childMapDetails, model); 
		
		System.out.println("Parsing Ends");
		return expression;
	}

	/**
	 *  To make the stratification independent of Birthdate
	 * */
	
	public static void modifyStratification(QueryVariable statement, Map<String, Object> expression){
		if(statement.getDependentObjects().isEmpty()){
			return;
		} 
		else {
			QueryVariable stat = (QueryVariable) expression.get(statement.getDependentObjects().get(0));
			String key = statement.getDependentObjects().get(0);
			if(stat.getName().equalsIgnoreCase(Constants.BIRTHDATE)) {
				statement.setDependentObjects(new CopyOnWriteArrayList<String>());
				CopyOnWriteArrayList<Constraint> constraintList = new CopyOnWriteArrayList<>();
				constraintList = stat.getConstraintList();
				for (Constraint constraint : constraintList) {
					constraint.setName(statement.getName()+"|"+constraint.getName().split("|")[1]);
				}
				statement.setConstraintList(constraintList);
				statement.setIntervalOperator(stat.getIntervalOperator());
				//remove the query variable for Birthdate
				expression.remove(key);
			}
		}
	}
	
	/*
	 * this method is to set qdmfunction && qdmperiod for child if its null. 
	 */
	public Map<String, Object> prerequisitesDrlGeneration(CQLModel model) {
		for (Map.Entry<String, Object> singleQueryVar : model.getExpressions().entrySet()) 
		{
			QueryVariable singleQueryVariable = (QueryVariable)singleQueryVar.getValue();
			if(singleQueryVariable.getDependentObjects()!=null 
					&& !singleQueryVariable.getDependentObjects().isEmpty())
				{
					List<String> dependentQueryVar = singleQueryVariable.getDependentObjects();
					if(dependentQueryVar!=null && !dependentQueryVar.isEmpty()){
					for(String singleDependentQueryVar : dependentQueryVar)
					{
						if(model.getExpressions().containsKey(singleDependentQueryVar))
						{
							QueryVariable queryVariable = (QueryVariable) model.getExpressions().get(singleDependentQueryVar);
							if(queryVariable.getQdmFunctionVariable().toString().isEmpty() 
									&& !singleQueryVariable.getQdmFunctionVariable().toString().isEmpty())
							{  
								queryVariable.setQdmFunctionVariable
								(singleQueryVariable.getQdmFunctionVariable());
							}
							if(queryVariable.getQdmPeriodVariable().toString().isEmpty() 
									&& !singleQueryVariable.getQdmPeriodVariable().toString().isEmpty())
							{
								queryVariable.setQdmPeriodVariable
								(singleQueryVariable.getQdmPeriodVariable());
							}
							if(singleQueryVariable.isLastOperator())
							{
								queryVariable.setLastOperator(true);
								singleQueryVariable.setLastOperator(false);
							}
						}
					}
				}
			}
		}
		return model.getExpressions();
	}
	
	/*
	 * this method is used to generate drl file from cqlmodel and populationCriteria
	 */
	public void startDrlGeneration(CQLModel model, String populationCriteria) {
		try {
			System.out.println("TOC MAP : "+model.getExpressions());
			System.out.println("Drl Generation starts");
			DrlGenerator drlGenerator = new DrlGenerator();
			drlGenerator.init(drlFileModel);
			String rule;
				
			StringBuilder mainDrl = new StringBuilder();
			model.setDrlOutput(mainDrl);
			drlGenerator.ruleBaseGenerator(model, populationCriteria);
			PopulationCriteriaName popCriteria = PopulationCriteriaName.fromInt(populationCriteria);
			for(RuleGroup ruleGroup : RuleGroup.values()) {		
				if(model.getExpressions().containsKey(ruleGroup.getValue1() + " " +popCriteria.getIntValue())) {
					rule = ruleGroup.getValue1() + " "  + popCriteria.getIntValue();
					drlGenerator.createDroolsForGroup(rule, model, ruleGroup.getValue1(), populationCriteria);
				} 
				else if (model.getExpressions().containsKey(ruleGroup.getValue1() +popCriteria.getIntValue())) {
					rule = ruleGroup.getValue1() + popCriteria.getIntValue();
					drlGenerator.createDroolsForGroup(rule, model, ruleGroup.getValue1(), populationCriteria);
				} 
				else if(model.getExpressions().containsKey(ruleGroup.getValue1())) {
					rule = ruleGroup.getValue1();
					drlGenerator.createDroolsForGroup(rule, model, ruleGroup.getValue1(), populationCriteria);
				} 
				else {
					if(model.getStratCount() > 0 && (ruleGroup.getValue2() != null && ruleGroup.getValue2().equals(popCriteria.getIntValue()))) {
						String[] stratArray = ruleGroup.getValue3().split(",");
						for(String strat : stratArray) {
							drlGenerator.createDroolsForGroup(strat, model, strat, populationCriteria);
						}
 					}
				}
			}
			drlGenerator.conditionDrlGenerator(model);
			drlGenerator.generateDrlFile(popCriteria.getFileValue(), model);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * java CQLParser src/main/resources/CMS124v9
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		CQLParser cqlParser = new CQLParser();
		if (args.length == 0) {
			throw new IllegalArgumentException("bad args");
		}
		CQLModel model = cqlParser.init(args[0]);// folderPath
		if (null != model) {
			for(String populationCriteria : model.getPopulationCriteria()) {
				SequenceInfo sequenceInfo = new SequenceInfo(false, false, false);
				cqlParser.startParsing(model, sequenceInfo);
				if(model.isEpisodeMeasure())
				{
					CQLHelper.modifyEpisodeMeasures(model);
				}
				cqlParser.prerequisitesDrlGeneration(model);
				CQLHelper.isfurtherDepedent(model);
				cqlParser.startDrlGeneration(model, populationCriteria);	
			}
		}
		System.out.println("CQL Parser Processed Successfully.");
	}
}
