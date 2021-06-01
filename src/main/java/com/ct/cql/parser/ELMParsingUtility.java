/*package com.ct.cql.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections4.CollectionUtils;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ct.cql.drlgen.DrlGeneratorUtility;
import com.ct.cql.elm.model.Add;
import com.ct.cql.elm.model.After;
import com.ct.cql.elm.model.AliasedQuerySource;
import com.ct.cql.elm.model.And;
import com.ct.cql.elm.model.As;
import com.ct.cql.elm.model.Before;
import com.ct.cql.elm.model.CalculateAgeAt;
import com.ct.cql.elm.model.Code;
import com.ct.cql.elm.model.CodeDef;
import com.ct.cql.elm.model.CodeRef;
import com.ct.cql.elm.model.CodeSystemDef;
import com.ct.cql.elm.model.Count;
import com.ct.cql.elm.model.DurationBetween;
import com.ct.cql.elm.model.End;
import com.ct.cql.elm.model.Equal;
import com.ct.cql.elm.model.Equivalent;
import com.ct.cql.elm.model.Exists;
import com.ct.cql.elm.model.Expression;
import com.ct.cql.elm.model.ExpressionDef;
import com.ct.cql.elm.model.ExpressionRef;
import com.ct.cql.elm.model.Greater;
import com.ct.cql.elm.model.GreaterOrEqual;
import com.ct.cql.elm.model.In;
import com.ct.cql.elm.model.InValueSet;
import com.ct.cql.elm.model.IncludeDef;
import com.ct.cql.elm.model.IncludedIn;
import com.ct.cql.elm.model.Interval;
import com.ct.cql.elm.model.IsNull;
import com.ct.cql.elm.model.Last;
import com.ct.cql.elm.model.Less;
import com.ct.cql.elm.model.LessOrEqual;
import com.ct.cql.elm.model.Library;
import com.ct.cql.elm.model.Library.CodeSystems;
import com.ct.cql.elm.model.Library.Codes;
import com.ct.cql.elm.model.Library.Includes;
import com.ct.cql.elm.model.Library.Statements;
import com.ct.cql.elm.model.Library.ValueSets;
import com.ct.cql.elm.model.Literal;
import com.ct.cql.elm.model.Not;
import com.ct.cql.elm.model.Or;
import com.ct.cql.elm.model.Overlaps;
import com.ct.cql.elm.model.ParameterRef;
import com.ct.cql.elm.model.Property;
import com.ct.cql.elm.model.Quantity;
import com.ct.cql.elm.model.Query;
import com.ct.cql.elm.model.Retrieve;
import com.ct.cql.elm.model.SameOrBefore;
import com.ct.cql.elm.model.SingletonFrom;
import com.ct.cql.elm.model.Start;
import com.ct.cql.elm.model.Subtract;
import com.ct.cql.elm.model.ToList;
import com.ct.cql.elm.model.Union;
import com.ct.cql.elm.model.ValueSetDef;
import com.ct.cql.elm.model.ValueSetRef;
import com.ct.cql.entity.Constraint;
import com.ct.cql.entity.ELMDataType;
import com.ct.cql.entity.FunctionsEnum;
import com.ct.cql.entity.InstanceInfo;
import com.ct.cql.entity.QueryVariable;
import com.ct.hqmf.drl.gen.CreateELMDocument;
import freemarker.template.TemplateException;

public class ELMParsingUtility {

	public static Map<String,Object> tocMap = new ConcurrentHashMap<String,Object>();
	public static Map<String,String> libraries = new HashMap<String,String>();
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException, TemplateException {
		boolean createQueryVariable = true;
		boolean addQdmFunction = true;
		File file = new File("src//main//resources//CMS125v7//BreastCancerScreening-7.2.000.xml");
		Library xmlObj = CreateELMDocument.parseelm(file);
		List<QueryVariable> statementObjects = new ArrayList<QueryVariable>();
		Map<String, String> valueSets = parseValueset(xmlObj);
		Map<String, Expression> statements = parseStatements(xmlObj);
		Map<String, String> codeSets = parseCodeset(xmlObj);
		libraries  = parseIncludes(xmlObj);
		if(!libraries.isEmpty()){
			resolveLibrary(libraries,createQueryVariable,addQdmFunction);
		}
		// ELM to Object Model
		ELMParsingUtility elmParsing = new ELMParsingUtility();
		for(Entry<String, Expression> def : statements.entrySet()){
			QueryVariable statement = new QueryVariable();
			statement.setName(def.getKey());
			Expression exp = def.getValue();
			if(exp instanceof ExpressionRef){
				elmParsing.expressionEvaluatorForExpRef((ExpressionRef)exp, valueSets, statements, statement,codeSets);
			}else{
				String middleqdmFunctionVariable = null;
				StringBuilder qdmFunctionVariable = new StringBuilder();
				elmParsing.expressionEvaluator(exp, valueSets,  statement,createQueryVariable,qdmFunctionVariable,
						addQdmFunction,middleqdmFunctionVariable,codeSets);
			}
			statementObjects.add(statement);
			tocMap.put(def.getKey(), statement);
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		//writer.writeValue(new File("src//main//resources//modelOutput//CMS125//"+statement.getName()+".json"), statement);
		writer.writeValue(new File("src//main//resources//modelOutput//"+statement.getName()+".json"), statement);
		}
		
		
		Map<String,String> queryMap = new LinkedHashMap<String, String>();
		
		 //for sql generation

		for(QueryVariable statementObject : statementObjects){
			if(statementObject.getName().equals("DenominatorExclusions"))
			//if(statementObject.getName().equals("Race"))
			SqlGeneratorUtility.queryFactory(statementObject, tocMap, queryMap,"QdmElements");
			
			
		}
				
		writeToFile(queryMap);	
		
		
		//System.out.println("##############################################################");		
		DrlGeneratorUtility.ruleBaseGenerator();
		//System.out.println("tocMap : "+tocMap);
		DrlGeneratorUtility.createDroolsForGroup((QueryVariable) tocMap.get("Initial Population"), tocMap ,"QdmElements", "Initial Patient Population");
			
		DrlGeneratorUtility.createDroolsForGroup((QueryVariable) tocMap.get("Denominator"), tocMap ,"QdmElements", "Denominator");	
		
		DrlGeneratorUtility.createDroolsForGroup((QueryVariable) tocMap.get("Denominator Exclusions"), tocMap ,"QdmElements", "Denominator Exclusions");	
					
		DrlGeneratorUtility.createDroolsForGroup((QueryVariable) tocMap.get("Numerator"), tocMap ,"QdmElements", "Numerator");
		for (Entry<String, Object> entry : tocMap.entrySet()) {
			if (entry.getKey().contains("|")) {
				 DrlGeneratorUtility.createDroolsForGroup((QueryVariable) tocMap.get(entry.getKey().substring(0, entry.getKey().indexOf("|"))), tocMap ,"QdmElements", entry.getKey().substring(0, entry.getKey().indexOf("|")));
			}
			else{
				
				DrlGeneratorUtility.createDroolsForGroup((QueryVariable) tocMap.get(entry.getKey()), tocMap ,"QdmElements", entry.getKey());
			}
		}
		//
		//DrlGeneratorUtility.conditionDrlGenerator(tocMap);
		System.out.println("tocMap : "+tocMap);
		
	}
	
	public static Map<String, String> parseValueset(Library library){

		Map<String,String> valueSets = new HashMap<String, String>();
		if(library != null && library.getValueSets() != null && CollectionUtils.isNotEmpty(library.getValueSets().getDef())){
			ValueSets entry = library.getValueSets();
			 List<ValueSetDef> valueSetdef = entry.getDef();
			 for(ValueSetDef def : valueSetdef){
				 String leafoid = def.getId();
				 if(leafoid != null){
					 leafoid = leafoid.split(":")[2];
					 //valusets.put(getNameIdentified(def.getName()), leafoid);
					 valueSets.put((def.getName()), leafoid);
				 }
			 }
		}
		return valueSets;
	}
	
	public static Map<String, String> parseCodeset(Library library){

		Map<String,String> codeSets = new HashMap<String, String>();
		Map<String,String> codeSystemsSets = new HashMap<String, String>();
		if(library != null && library.getCodeSystems() != null && CollectionUtils.isNotEmpty(library.getCodeSystems().getDef())){
			CodeSystems entry = library.getCodeSystems();
			List<CodeSystemDef> CodeSetdef = entry.getDef();
			for(CodeSystemDef def : CodeSetdef){
				 String CodeSystemsleafoid = def.getId();
				 if(CodeSystemsleafoid != null){
					 CodeSystemsleafoid = CodeSystemsleafoid.split(":")[2];
					 codeSystemsSets.put((def.getName()), CodeSystemsleafoid);
				 }
			}
		}
		
		if(library != null && library.getCodes() != null && CollectionUtils.isNotEmpty(library.getCodes().getDef())){
			Codes entry = library.getCodes();
			 List<CodeDef> CodeSetdef = entry.getDef();
			 for(CodeDef def : CodeSetdef){
				 String codeleafoid = def.getId();
				 String codeSystemsName = def.getCodeSystem().getName();
				 if(codeleafoid != null){
					 if(codeSystemsSets.containsKey(codeSystemsName))
					 {
						 codeSets.put((def.getName()), codeSystemsSets.get(codeSystemsName)+"."+codeleafoid);
					 }
				 }
			 }
		}
		return codeSets;
	}
	
	public static Map<String, Expression> parseStatements(Library library){

		Map<String,Expression> statements = new HashMap<String, Expression>();
		if(library != null && library.getStatements() != null && CollectionUtils.isNotEmpty(library.getStatements().getDef())){
			Statements entry = library.getStatements();
			 List<ExpressionDef> expression = entry.getDef();
			 for(ExpressionDef def : expression){
				 statements.put((def.getName()), def.getExpression());
			 }
		}
		return statements;
	}
	
	public static Map<String, String> parseIncludes(Library library){

		Map<String,String> statements = new HashMap<String, String>();
		if(library != null && library.getIncludes() != null && CollectionUtils.isNotEmpty(library.getIncludes().getDef())){
			Includes entry = library.getIncludes();
			 List<IncludeDef> expression = entry.getDef();
			 for(IncludeDef def : expression){
				 statements.put(def.getLocalIdentifier(), def.getPath()+"-"+def.getVersion());
			 }
		}
		return statements;
	}
	
	public String expressionEvaluator(Expression exp, Map<String, String> valueSetMap,QueryVariable statement,
			boolean createQueryVariable,StringBuilder qdmFunctionVariable,boolean addQdmFunction,String middleqdmFunctionVariable,Map<String, String> codeSetMap){
	
		String queryVariable = null ;
		qdmFunctionVariable = new StringBuilder();
		queryVariable = evaluateBaseCondition(exp,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
		if(null == queryVariable){
		    if(exp instanceof And){
		    	List <Expression> expList =((And) exp).getOperand();
		    	String operatorName = "And";
		    	queryVariable = evaluateBinaryOperator(expList,valueSetMap,statement,operatorName,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
				
			}
			else if(exp instanceof Or){
				List <Expression> expList =((Or) exp).getOperand();
		    	String operatorName = "Or";
		    	queryVariable = evaluateBinaryOperator(expList,valueSetMap,statement,operatorName,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
			
			else if(exp instanceof Exists){
				Expression expression =((Exists) exp).getOperand();
		    	queryVariable = evaluateExists(expression,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
				
			}
			else if(exp instanceof GreaterOrEqual){
				List <Expression> expList =((GreaterOrEqual) exp).getOperand();
				String operatorName = "GreaterOrEqual";
		    	queryVariable = evaluateBinaryOperator(expList,valueSetMap,statement,operatorName,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
			else if(exp instanceof Less){
				List <Expression> expList =((Less) exp).getOperand();
				String operatorName = "Less";
		    	queryVariable = evaluateBinaryOperator(expList,valueSetMap,statement,operatorName,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
			else if(exp instanceof LessOrEqual){
				List <Expression> expList =((LessOrEqual) exp).getOperand();
				String operatorName = "LessOrEqual";
		    	queryVariable = evaluateBinaryOperator(expList,valueSetMap,statement,operatorName,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
			else if(exp instanceof Equal){
				List <Expression> expList =((Equal) exp).getOperand();
				String operatorName = "Equal";
		    	queryVariable = evaluateBinaryOperator(expList,valueSetMap,statement,operatorName,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
			else if(exp instanceof Union){
				List <Expression> expList =((Union) exp).getOperand();
				String operatorName = "Union";
		    	queryVariable = evaluateBinaryOperator(expList,valueSetMap,statement,operatorName,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
			else if(exp instanceof Query){
		    	queryVariable = evaluateQueryOperator((Query)exp,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
			else if (exp instanceof SingletonFrom)
			{
				Expression expList =((SingletonFrom) exp).getOperand();
				queryVariable = expressionEvaluator(expList,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
			else if(exp instanceof Equivalent)
			{
				List <Expression> expList =((Equivalent) exp).getOperand();
				String operatorName = "Equivalent";
		    	queryVariable = evaluateBinaryOperator(expList,valueSetMap,statement,operatorName,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
			else if (exp instanceof Greater)
			{
				List <Expression> expList =((Greater) exp).getOperand();
				String operatorName = "Greater";
		    	queryVariable = evaluateBinaryOperator(expList,valueSetMap,statement,operatorName,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
			else if (exp instanceof As)
			{
				String operand = evaluateTemporalFunction(((As) exp).getOperand(),"stopDate");
				Expression expList = ((As) exp).getOperand();
				if(("result").equalsIgnoreCase(operand) && expList instanceof Property)
				{
					queryVariable = expressionEvaluator(((Property)expList).getSource(),valueSetMap,statement,createQueryVariable,qdmFunctionVariable,
							addQdmFunction,middleqdmFunctionVariable,codeSetMap);
				}
			}
			else if (exp instanceof DurationBetween)
			{
				List <Expression> expList =((DurationBetween) exp).getOperand();
				String operatorName = "DurationBetween";
		    	queryVariable = evaluateBinaryOperator(expList,valueSetMap,statement,operatorName,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
		}
		return queryVariable;
	}
	
	public String evaluateQdmFunction(String precision)
	{
		String QdmFunctionVariable = "";
		if(precision!=null && !precision.isEmpty())
		{
			if(precision.equalsIgnoreCase("DAY"))
			{
				QdmFunctionVariable = "OrConcurrentWith";
			}
		}
		return QdmFunctionVariable;
	}
	
	public String evaluateBinaryOperator(List <Expression>expList, Map<String, String> valueSetMap,QueryVariable statement,
			String operatorName,boolean createQueryVariable,StringBuilder qdmFunctionVariable,boolean addQdmFunction,String middleqdmFunctionVariable, Map<String, String> codeSetMap){
		String constraintName = null;
		if(CollectionUtils.isNotEmpty(expList)){
			Expression left = expList.get(0);
			Expression right = expList.get(1);
		Constraint constraint = new Constraint();
		constraintName = statement.getName()+"|"+java.util.UUID.randomUUID().toString();
		constraint.setName(constraintName);
		constraint.setOperator(operatorName);
		//left 
		   String leftOperand = evaluateBaseCondition(left,valueSetMap,statement,createQueryVariable,qdmFunctionVariable, addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			if(null == leftOperand){
				leftOperand = expressionEvaluator(left, valueSetMap,  statement,createQueryVariable,qdmFunctionVariable, addQdmFunction,middleqdmFunctionVariable,codeSetMap);
				
			}
			//right
			String rightOperand = evaluateBaseCondition(right,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			if(null == rightOperand){
				rightOperand = expressionEvaluator(right, valueSetMap, statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			}
			
			if(leftOperand!=null && leftOperand.startsWith("Count(")){
				String tempLeftOperand = (leftOperand.split("Count\\("))[1];
				tempLeftOperand = tempLeftOperand.split("\\)")[0];
				constraint.setFromClause(true);
				constraint.setLeftOperand(tempLeftOperand);
				constraint.setRightOperand(leftOperand+","+rightOperand);
				if(!statement.getDependentObjects().contains(tempLeftOperand))
				{
					statement.getDependentObjects().add(tempLeftOperand);
				}
			}
			else
			{
				if(istempTable(leftOperand)){
					constraint.setFromClause(true);
					if(!statement.getDependentObjects().contains(leftOperand))
					{
						statement.getDependentObjects().add(leftOperand);
					}
				}
				constraint.setLeftOperand(leftOperand);
				constraint.setRightOperand(rightOperand);
				
				if(istempTable(rightOperand)){
					constraint.setFromClause(true);
					if(!statement.getDependentObjects().contains(rightOperand))
					{
						statement.getDependentObjects().add(rightOperand);
					}
				}
			}
		//set constraint
		statement.getConstraintList().add(constraint);
		}
		return constraintName;
	}
	
	public String evaluateExists(Expression expression, Map<String, String> valueSetMap, QueryVariable statement
			,boolean createQueryVariable,StringBuilder qdmFunctionVariable,boolean addQdmFunction,String middleqdmFunctionVariable, Map<String, String> codeSetMap){
		
		String constraintName = null;
		Constraint constraint = new Constraint();
		constraintName = statement.getName()+"|"+java.util.UUID.randomUUID().toString();
		//constraintName = statement.getName();
		constraint.setName(constraintName);
		constraint.setOperator("Exists");
		if(null != expression){
		//left 
		 String operand = evaluateBaseCondition(expression,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
		if(null == operand){
			operand = expressionEvaluator(expression, valueSetMap, statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
		}
		constraint.setLeftOperand(operand);
		
		if(istempTable(operand) ){
			constraint.setFromClause(true);
			if(!statement.getDependentObjects().contains(operand))
			{
				statement.getDependentObjects().add(operand);
			}
		}
		//set constraint
			statement.getConstraintList().add(constraint);
		}
		return constraintName;
	}
	
	public String evaluateRetrieveOperator(Retrieve expression, Map<String, String> valueSetMap,
			QueryVariable statement,boolean createQueryVariable, Map<String, String> codeSetMap) {
	
		String queryVariable = "Failure";
		if(null != expression){
		//left 
		Expression code = expression.getCodes();
		if(code instanceof ValueSetRef){
			List<String> valueSetList = new ArrayList<String>();
			String name = (((ValueSetRef) code).getName());
			valueSetList = statement.getValueSetList();
			if(valueSetList == null)
			{
				valueSetList = new ArrayList<String>();
				valueSetList.add(valueSetMap.get(name));
			}
			else{
				if(!valueSetList.contains(valueSetMap.get(name)))
				{
					valueSetList.add(valueSetMap.get(name));
				}
			}
			if(createQueryVariable)
			{
				QueryVariable queryVar = getOrCreateQueryVariable(name);
				queryVar.setDataType(getDataType(expression.getDataType().getLocalPart()));
				queryVar.setValueSetList(valueSetList);
				queryVariable = queryVar.getName();
				if(!statement.getDependentObjects().contains(queryVariable))
				{
					statement.getDependentObjects().add(queryVariable);
				}
				statement.getQueryVariable().add(queryVar);
				tocMap.put(queryVar.getName(), queryVar);
			}
			else
			{
				statement.setDataType(getDataType(expression.getDataType().getLocalPart()));
				statement.setValueSetList(valueSetList);
				statement.getDependentObjects().add(name);
				queryVariable = statement.getName();
			}
		}
		else if (code instanceof ToList)
		{
			Expression operand = ((ToList) code).getOperand();
			if(operand instanceof CodeRef)
			{
				List<String> codeList = new ArrayList<String>();
				String name = ((CodeRef) operand).getName();
				codeList = statement.getValueSetList();
				if(codeList == null)
				{
					codeList = new ArrayList<String>();
					codeList.add(codeSetMap.get(name));
				}
				else{
					if(!codeList.contains(codeSetMap.get(name)))
					{
						codeList.add(codeSetMap.get(name));
					}
				}
				statement.setDataType(expression.getDataType().getLocalPart());
				statement.setValueSetList(codeList);
				statement.getDependentObjects().add(name);
				queryVariable = statement.getName();
			}
			
		}
	}
		return queryVariable;
	}

	public String evaluateQueryOperator(Query expression, Map<String, String> valueSetMap,QueryVariable statement,
			boolean createQueryVariable,StringBuilder qdmFunctionVariable,boolean addQdmFunction,String middleqdmFunctionVariable, Map<String, String> codeSetMap){
		AliasedQuerySource sourceObj = expression.getSource().get(0);
		QueryVariable queryVar = getOrCreateQueryVariable((sourceObj.getAlias()));
		Expression whereExp = expression.getWhere();
		Expression exp = sourceObj.getExpression();
			if(null != exp)
			{
				if(exp.toString().contains("Retrieve"))
				{
					createQueryVariable = false;
					expressionEvaluator(exp, valueSetMap,  queryVar,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
				}
				else
				{
					expressionEvaluator(exp, valueSetMap,  queryVar,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
				}
				expressionEvaluator(whereExp, valueSetMap,  queryVar,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
				statement.getQueryVariable().add(queryVar);
				if(!statement.getDependentObjects().contains(queryVar.getName()))
				statement.getDependentObjects().add(queryVar.getName());
			}
		return queryVar.getName();
	}

	public String evaluateBaseCondition(Expression exp , Map<String, String> valueSetMap,QueryVariable statement,
			boolean createQueryVariable,StringBuilder qdmFunctionVariable,boolean addQdmFunction,String middleqdmFunctionVariable, Map<String, String> codeSetMap){
		String operand= null;
		if(exp instanceof ExpressionRef){
			operand = ((ExpressionRef) exp).getName();
			getOrCreateQueryVariable(operand);
		}
		else if(exp instanceof Literal){
			operand = ((Literal) exp).getValue();
		}
		else if(exp instanceof CalculateAgeAt){
			operand = FunctionsEnum.CALCULATE_AGE_AT.getValue();
		}
		else if(exp instanceof Retrieve){
			operand = evaluateRetrieveOperator((Retrieve)exp,valueSetMap,statement,createQueryVariable,codeSetMap);
		}
		else if(exp instanceof End){
			operand = evaluateTemporalFunction(((End) exp).getOperand(),"stopDate");
			if(qdmFunctionVariable.toString().isEmpty() && addQdmFunction)
			{
				qdmFunctionVariable.append("ends");
			}
			else if (addQdmFunction)
			{
				qdmFunctionVariable.append("End");
			}
		}
		else if(exp instanceof Start){
			operand = evaluateTemporalFunction(((Start) exp).getOperand(),"period");
			if(qdmFunctionVariable.toString().isEmpty() && addQdmFunction)
			{
				qdmFunctionVariable.append("starts");
			}
			else if (addQdmFunction)
			{
				qdmFunctionVariable.append("Start");
			}
		}
		else if(exp instanceof Quantity){
			operand = evaluateQuantity(exp,statement);
		}
		else if(exp instanceof Subtract){
			Expression left = ((Subtract) exp).getOperand().get(0);
			Expression right = ((Subtract) exp).getOperand().get(1);
			addQdmFunction = false;
			String leftOperand = evaluateBaseCondition(left,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			String rightOperand = evaluateBaseCondition(right,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			qdmFunctionVariable.append("Before");
			operand = leftOperand +"-"+rightOperand;
		}
		else if(exp instanceof Add){
			Expression left = ((Add) exp).getOperand().get(0);
			Expression right = ((Add) exp).getOperand().get(1);
			addQdmFunction = false;
			String leftOperand = evaluateBaseCondition(left,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			String rightOperand = evaluateBaseCondition(right,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			qdmFunctionVariable.append("After");
			operand = leftOperand +"+"+rightOperand;
		}
		else if(exp instanceof Interval){
			Interval interval = new Interval();
			Expression left = ((Interval) exp).getLow();
			Expression right = ((Interval) exp).getHigh();
			String leftOperand = evaluateBaseCondition(left,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			if(middleqdmFunctionVariable!=null)
			{
				qdmFunctionVariable.append(middleqdmFunctionVariable);
			}
			String rightOperand = evaluateBaseCondition(right,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			operand = "Interval("+leftOperand +", "+rightOperand +")";
			interval.setLowValue(leftOperand);
			interval.setLowClosed(((Interval) exp).isLowClosed());
			interval.setHighValue(rightOperand);
			interval.setHighClosed(((Interval) exp).isHighClosed());
			statement.setIntervalOperator(interval);
		}
		else if(exp instanceof Count){
			Expression expression  = ((Count) exp).getSource();
			String leftOperand = expressionEvaluator(expression, valueSetMap, statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			operand = "Count("+leftOperand + ")";
		}
		
		if(exp instanceof InValueSet){
			Constraint constraint = getConstraintTemplate(statement);
			constraint.setOperator("InValueSet");	
			String valueSetName =(((InValueSet) exp).getValueset().getName());
			String leftoprand = valueSetMap.get(valueSetName);
			constraint.setLeftOperand(leftoprand);
			Expression codeExp = ((InValueSet) exp).getCode();
			if(codeExp instanceof Property){
				statement = (QueryVariable) tocMap.get(((Property) codeExp).getScope());
				
			}
			if(statement.getConstraintList().isEmpty()){
				statement.setConstraintList(new ArrayList<Constraint>());
				statement.getConstraintList().add(constraint);
				if(tocMap.containsKey(statement.getDependentObjects().get(0)))
				{
					tocMap.remove(statement.getDependentObjects().get(0));
				}
				tocMap.remove(statement.getName());
				statement.setName(statement.getName()+"_"+valueSetName);
				tocMap.put(statement.getName(), statement);
				for(Entry<String, Object> singlequeryVariable:tocMap.entrySet())
				{
					if(singlequeryVariable.getKey().contains(valueSetName))
					{
						if((!((QueryVariable)singlequeryVariable.getValue()).getDependentObjects().isEmpty()) && 
								(((QueryVariable)singlequeryVariable.getValue()).getDependentObjects()).contains(statement.getName()))
						{
							((QueryVariable)singlequeryVariable.getValue()).getDependentObjects().add(statement.getName());
						}
					}
				}
			}
			else
			{
				QueryVariable queryVar = getOrCreateQueryVariable(statement.getName()+"_"+(valueSetName));
				queryVar.setConstraintList(new ArrayList<Constraint>());
				queryVar.getConstraintList().add(constraint);
				queryVar.setValueSetList(statement.getValueSetList());
				queryVar.setDataType(statement.getDataType());
				tocMap.put(queryVar.getName(), queryVar);
				for(Entry<String, Object> singlequeryVariable:tocMap.entrySet())
				{
					if(singlequeryVariable.getKey().contains(valueSetName))
					{
						if((!((QueryVariable)singlequeryVariable.getValue()).getDependentObjects().isEmpty()) && 
								!(((QueryVariable)singlequeryVariable.getValue()).getDependentObjects()).contains(queryVar.getName()))
						{
							((QueryVariable)singlequeryVariable.getValue()).getDependentObjects().add(queryVar.getName());
						}
					}
				}
			}
		}
		
		else if(exp instanceof IncludedIn){
			Constraint constraint = getConstraintTemplate(statement);
			constraint.setOperator("IncludedIn");	
			List<Expression> expList =((IncludedIn) exp).getOperand();
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			String leftOperand = evaluateTemporalFunction(left,"period");
			String rightOperand = evaluateTemporalFunction(right,"period");
			if(null != leftOperand && null != rightOperand){
				if(rightOperand.contains("MP."))
					constraint.setLeftOperand("IncludedInMP("+leftOperand +")");
				else
				constraint.setLeftOperand("IncludedIn("+leftOperand +", "+rightOperand +")");
			}
			if(null == statement.getConstraintList()){
				statement.setConstraintList(new ArrayList<Constraint>());
			}
			statement.getConstraintList().add(constraint);
			operand = constraint.getName();
		}
		
		else if(exp instanceof Overlaps){
			Constraint constraint = getConstraintTemplate(statement);
			constraint.setOperator("Overlaps");	
			List<Expression> expList =((Overlaps) exp).getOperand();
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			String leftOperand = evaluateTemporalFunction(left,"period");
			String rightOperand = evaluateTemporalFunction(right,"period");
			if(null != leftOperand && null != rightOperand){
				if(rightOperand.contains("MP."))
					constraint.setLeftOperand("Overlaps("+leftOperand +")");
				else
				constraint.setLeftOperand("Overlaps("+leftOperand +", "+rightOperand +")");
			}
			if(null == statement.getConstraintList()){
				statement.setConstraintList(new ArrayList<Constraint>());
			}
			statement.getConstraintList().add(constraint);
			operand = constraint.getName();
		}
		
		else if(exp instanceof Before){
			Constraint constraint = getConstraintTemplate(statement);
			List<Expression> expList =((Before) exp).getOperand();
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			constraint.setOperator(left.getClass().getSimpleName().toLowerCase()+"sBefore"+right.getClass().getSimpleName());
			String leftOperand = evaluateBaseCondition(left,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			qdmFunctionVariable.append("Before");
			String rightOperand = evaluateBaseCondition(right,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			if(null != leftOperand && null != rightOperand){
				constraint.setLeftOperand(left.getClass().getSimpleName().toLowerCase()+"sBefore"+right.getClass().getSimpleName()+"("+leftOperand+", "+rightOperand +")");
			}
			if(null == statement.getConstraintList()){
				statement.setConstraintList(new ArrayList<Constraint>());
			}
			statement.getConstraintList().add(constraint);
			operand = constraint.getName();
		}
		else if(exp instanceof After){
			Constraint constraint = getConstraintTemplate(statement);
			List<Expression> expList =((Before) exp).getOperand();
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			constraint.setOperator(left.getClass().getSimpleName().toLowerCase()+"sAfter"+right.getClass().getSimpleName());
			String leftOperand = evaluateBaseCondition(left,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			qdmFunctionVariable.append("After");
			String rightOperand = evaluateBaseCondition(right,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			if(null != leftOperand && null != rightOperand){
				constraint.setLeftOperand(left.getClass().getSimpleName().toLowerCase()+"sAfter"+right.getClass().getSimpleName()+"("+leftOperand+", "+rightOperand +")");
			}
			if(null == statement.getConstraintList()){
				statement.setConstraintList(new ArrayList<Constraint>());
			}
			statement.getConstraintList().add(constraint);
			operand = constraint.getName();
		}
		
		else if(exp instanceof In){
			Constraint constraint = getConstraintTemplate(statement);
			constraint.setOperator("In");
			List<Expression> expList =((In) exp).getOperand();
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			String leftOperand = evaluateBaseCondition(left,valueSetMap,statement,createQueryVariable,qdmFunctionVariable, addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			if(((In) exp).getPrecision()!=null)
			{
				middleqdmFunctionVariable = evaluateQdmFunction(((In) exp).getPrecision().toString());
			}
			String rightOperand = evaluateBaseCondition(right,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			if(null != leftOperand && null != rightOperand){
				constraint.setLeftOperand("In("+leftOperand +", "+rightOperand +")");
			}
			else{
				 leftOperand = evaluateTemporalFunction(left,"period");
				 rightOperand = evaluateTemporalFunction(right,"period");
				 constraint.setLeftOperand("In("+leftOperand +", "+rightOperand +")");
			}
			if(null == statement.getConstraintList()){
				statement.setConstraintList(new ArrayList<Constraint>());
			}
			statement.getConstraintList().add(constraint);
			operand = constraint.getName();
		}
		else if (exp instanceof Last){
			Constraint constraint = getConstraintTemplate(statement);
			constraint.setOperator("Last");
			
			if(((Last) exp).getSource() instanceof Query)
			{
				operand = expressionEvaluator(((Last) exp).getSource(),valueSetMap,statement,createQueryVariable,qdmFunctionVariable,
						addQdmFunction,middleqdmFunctionVariable,codeSetMap);
				constraint.setLeftOperand(operand);
				if(null == statement.getConstraintList()){
					statement.setConstraintList(new ArrayList<Constraint>());
				}
				statement.getConstraintList().add(constraint);
			}
			
		}
		else if (exp instanceof Not)
		{
			Expression not = ((Not) exp).getOperand();
			operand = evaluateBaseCondition(not,valueSetMap,statement,createQueryVariable,qdmFunctionVariable, addQdmFunction,middleqdmFunctionVariable,codeSetMap);
		}
		else if (exp instanceof IsNull)
		{
			operand = evaluateTemporalFunction(((IsNull) exp).getOperand(),"stopDate");
		}
		
		else if (exp instanceof SameOrBefore)
		{
			Constraint constraint = getConstraintTemplate(statement);
			List<Expression> expList =((SameOrBefore) exp).getOperand();
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			constraint.setOperator("SameOrBefore");
			String leftOperand = evaluateBaseCondition(left,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			qdmFunctionVariable.append("BeforeOrConcurrentWith");
			String rightOperand = evaluateBaseCondition(right,valueSetMap,statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSetMap);
			if(null != leftOperand && null != rightOperand){
				constraint.setLeftOperand(leftOperand);
			}
			if(null == statement.getConstraintList()){
				statement.setConstraintList(new ArrayList<Constraint>());
			}
			statement.getConstraintList().add(constraint);
			operand = constraint.getName();
		}
		else if (exp instanceof Property)
		{
			if(((Property) exp).getPath()!=null)
			{
				if(((Property) exp).getPath().equalsIgnoreCase("authorDatetime"))
				{
					qdmFunctionVariable.append("starts");
				}
			}
		}
		statement.setQdmFunctionVariable(qdmFunctionVariable);
		return operand;
}

	public String evaluateTemporalFunction(Expression exp, String operandName){
		    String operand = null;
			if(exp instanceof Property){
				operand =  ((Property)exp).getPath() ;
				operand = operand.replace("relevantPeriod", "period");
			}
			else if (exp instanceof ParameterRef){
				operand = (((ParameterRef) exp).getName()) ;
				operand = operand.replace("Measurement Period", "$audit.measurePeriod");
			}
		return operand;
	}
	
	public String evaluateQuantity(Expression exp,QueryVariable queryVariable){
	    String quanity = null;
		String unit = ((Quantity) exp).getUnit();
		BigDecimal value = ((Quantity) exp).getValue();
		if(null != unit && null != value)
		//quanity = value.toString() + ", "+ unit;
		queryVariable.setQdmPeriodVariable(unit.toUpperCase());
		quanity = value.toString();
		return quanity;
	}
	
	public static Constraint getConstraintTemplate(QueryVariable statement){
		String constraintName = null;
		Constraint constraint = new Constraint();
		constraintName = statement.getName()+"|"+java.util.UUID.randomUUID().toString();
		constraint.setName(constraintName);
		constraint.setFromClause(false);
		return constraint;
		
	}
	
	public static String getDataType(String datatype){
		
		String name = null;
		ELMDataType type =	ELMDataType.fromString(datatype);
		name = type.name();
		return name;
	}
	
	public void expressionEvaluatorForExpRef(ExpressionRef exp, Map<String, String> valueSetMap,
			Map<String, Expression> statementMap, QueryVariable statement,Map<String, String> codeSetMap){
		
		Constraint constraint = new Constraint();
		String constraintName = statement.getName()+"|"+java.util.UUID.randomUUID().toString();
		//String constraintName = statement.getName();
		constraint.setName(constraintName);
		constraint.setOperator("ExpressionRef");
		constraint.setFromClause(true);
		statement.getDependentObjects().add(getNameIdentified(exp.getName()));
		constraint.setLeftOperand(getNameIdentified(exp.getName()));
		statement.getDependentObjects().add((exp.getName()));
		constraint.setLeftOperand((exp.getName()));
		if(null == statement.getConstraintList()){
			 statement.setConstraintList(new ArrayList<Constraint>());
		}
		 statement.getConstraintList().add(constraint);
		//tocMap.put(constraintName, constraint);
	}
	
	public QueryVariable getOrCreateQueryVariable(String name){
		
		QueryVariable variable = (QueryVariable)tocMap.get(name);
		if(variable == null){
			variable = new QueryVariable();
			variable.setName(name);
			tocMap.put(name, variable);
		}
		return variable;
	}
	
	
public static boolean istempTable(String queryName){
		
		boolean isTable = false;
		
		if(queryName == null)
			return isTable;
		
		String test = queryName;
		if(queryName.startsWith("Count(")){
			test = (queryName.split("Count\\("))[1];
			test = test.split("\\)")[0];
		}
		
		InstanceInfo obj = (InstanceInfo) tocMap.get(test);
		if(obj != null && (obj instanceof QueryVariable)){
			isTable = true;
		}
		 return isTable;
	}

   public static void resolveLibrary(String libraryName,boolean createQueryVariable,StringBuilder qdmFunctionVariable,boolean addQdmFunction,String middleqdmFunctionVariable){
	   
	   String libraryFile = libraries.get(libraryName);
	   if(libraryFile != null){
	   File file = new File("src//main//resources//"+libraryFile+".xml");
		Library xmlObj = CreateELMDocument.parseelm(file);
		List<QueryVariable> statementObjects = new ArrayList<QueryVariable>();
		Map<String, String> valueSets = parseValueset(xmlObj);
		Map<String, Expression> statements = parseStatements(xmlObj);
		Map<String, String> codeSets = parseCodeset(xmlObj);
		// ELM to Object Model
		ELMParsingUtility elmParsing = new ELMParsingUtility();
		for(Entry<String, Expression> def : statements.entrySet()){
			
			QueryVariable statement = new QueryVariable();
			statement.setName(def.getKey());
			Expression exp = def.getValue();
			if(def.getValue() instanceof ExpressionRef){
				elmParsing.expressionEvaluatorForExpRef((ExpressionRef)exp, valueSets, statements, statement,codeSets);
			}else{
				elmParsing.expressionEvaluator(exp, valueSets, statement,createQueryVariable,qdmFunctionVariable,
						addQdmFunction,middleqdmFunctionVariable,codeSets);
			}
			statementObjects.add(statement);
			tocMap.put(def.getKey(), statement);
			//System.out.println(statement);
			
		}
		System.out.println("######################library:"+libraryName+" Resolved#####################");
	   }
   }
  
   //overridden method
public static void resolveLibrary(Map<String,String> library,boolean createQueryVariable,boolean addQdmFunction){
	   
	   //System.out.println("Dynamically loading library:"+ libraryName);
	   //String libraryFile = libraries.get(libraryName);
	   //if(libraryFile != null){
		for (Entry<String, String> entry : library.entrySet()) {
	   File file = new File("src//main//resources//CMS123V7//"+entry.getValue()+".xml");
		Library xmlObj = CreateELMDocument.parseelm(file);
		List<QueryVariable> statementObjects = new ArrayList<QueryVariable>();
		Map<String, String> valueSets = parseValueset(xmlObj);
		Map<String, Expression> statements = parseStatements(xmlObj);
		Map<String, String> codeSets = parseCodeset(xmlObj);
		// ELM to Object Model
		ELMParsingUtility elmParsing = new ELMParsingUtility();
		for(Entry<String, Expression> def : statements.entrySet()){
			
			QueryVariable statement = new QueryVariable();
			statement.setName(def.getKey());
			Expression exp = def.getValue();
			if(def.getValue() instanceof ExpressionRef){
				elmParsing.expressionEvaluatorForExpRef((ExpressionRef)exp, valueSets, statements, statement,codeSets);
			}else{
				String middleqdmFunctionVariable = null;
				StringBuilder qdmFunctionVariable = new StringBuilder();
				elmParsing.expressionEvaluator(exp, valueSets, statement,createQueryVariable,qdmFunctionVariable,addQdmFunction,middleqdmFunctionVariable,codeSets);
			}
			statementObjects.add(statement);
			tocMap.put(def.getKey(), statement);
			
		}
	   }
   }
   public static void writeToFile(Map<String,String> queryMap){
		//final String FILENAME = "src//main//resources//sqlOutput//CMS125//CMS125.scala";
	   final String FILENAME = "src//main//resources//sqlOutput//CMS125.scala";
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {

			StringBuilder content = new StringBuilder();

			for(Entry entry :queryMap.entrySet()){
				content.append("val "+entry.getKey() + " = ");
				content.append("spark.sqlContext.sql(s\"");
				content.append(entry.getValue());
				content.append("\")\n");
				content.append(entry.getKey()+".createOrReplaceTempView"+"(\""+entry.getKey()+"\")\n");
				content.append(System.lineSeparator());
			}
			
			 String IPPResult = "val rs125 = sqlContext.sql(s\"select $providerId AS providerId ,a.patientId, case when b.patientId is NULL then 0 else 1 end as ipp from QdmElements a left join InitialPopulation b on a.patientid = b.patientid\" ).distinct();"
					 +"rs125.createOrReplaceTempView(\"rs125\");";

			 String DenominatorResult = "val rs125 = sqlContext.sql(s\"select a.*, case when b.patientId is NULL then 0 else 1 end as denominator from rs125 a left join Denominator b  on a.patientid = b.patientid\").distinct();\n"
			  +"rs125.createOrReplaceTempView(\"rs125\");";
			
			 content.append(IPPResult+"\n");
			 content.append(System.lineSeparator());
			 content.append(DenominatorResult);
			
			
			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			bw.write(content.toString());

			//System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	} 
}

*/