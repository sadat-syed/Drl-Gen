package com.ct.cql.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import com.ct.cql.elm.model.Expression;
import com.ct.cql.elm.model.Interval;
import com.ct.cql.entity.CQLModel;
import com.ct.cql.entity.Constraint;
import com.ct.cql.entity.ELMDataType;
import com.ct.cql.entity.InstanceInfo;
import com.ct.cql.entity.QueryVariable;
import com.ct.cql.entity.RuleGroup;
import com.ct.cql.entity.SequenceInfo;
import com.ct.cql.evaluators.ExpressionEvaluators;

public class CQLHelper {

	/*
	 * This method is used to get or create new queryvariable
	 * input are 
	 * 1. Query variable to set ParentDependentConditionNumber for new queryvariable
	 * 2. Name to check whether same is already created
	 * 3. CqlModel
	 */
	
	public static QueryVariable getOrCreateQueryVariable(QueryVariable statement, String name, CQLModel model){
		QueryVariable variable = (QueryVariable)model.getExpressions().get(name);
		if(variable == null){
			variable = new QueryVariable();
			int conditionNumber ;
			variable.setConditionNumber(model.getConditionNumber());
			conditionNumber = model.getConditionNumber();
			model.setConditionNumber(++conditionNumber);
			variable.setName(name);
			variable.setParentDependentConditionNumber(statement.getConditionNumber());
			variable.setMiddleqdmFunctionVariable("");
			variable.setQdmFunctionVariable(new StringBuilder(""));
			variable.setCreateQueryVariable(true);
			variable.setAddQdmFunction(true);
			model.getExpressions().put(name, variable);
		}
		return variable;
	}
	
	/*
	 * This method is used to get datatypes
	 * input are 
	 * 1. datatype to get from ELMDataType enum
	 */
	
	public static String getDataType(String datatype){
		ELMDataType type =	ELMDataType.fromString(datatype);
		return type.name();
	}
	
	/*
	 * This method is used to create Constraint Template
	 * input are 
	 * 1. Query variable to create constraint name
	 */
	
	public static Constraint getConstraintTemplate(QueryVariable statement){
		String constraintName = null;
		Constraint constraint = new Constraint();
		constraintName = statement.getName()+"|"+java.util.UUID.randomUUID().toString();
		constraint.setName(constraintName);
		constraint.setFromClause(false);
		return constraint;
		
	}
	
	/*
	 * This method is used to create Constraint Template
	 * input are 
	 * 1. Query variable to create constraint name
	 * 2. constraint OperatorName to set operator in Constraint
	 */
	
	public static Constraint getConstraintTemplate(QueryVariable statement,String constraintOperatorName){
		String constraintName = null;
		Constraint constraint = new Constraint();
		constraintName = statement.getName()+"|"+java.util.UUID.randomUUID().toString();
		constraint.setName(constraintName);
		constraint.setOperator(constraintOperatorName);
		constraint.setFromClause(false);
		return constraint;
		
	}
	
	/*
	 * This method is used to create Constraint Template
	 * input are 
	 * 1. queryName check whether its temp table or not
	 * 2. CqlModel
	 */
	
	public static boolean isTempTable(String queryName, CQLModel model){
		
		boolean isTable = false;
		if(queryName == null)
			return isTable;
		
		String test = queryName;
		if(queryName.startsWith("Count(")){
			test = (queryName.split("Count\\("))[1];
			test = test.split("\\)")[0];
		}
		if(model.getExpressions().get(test) != null) {
			InstanceInfo obj = (InstanceInfo) model.getExpressions().get(test);
			if(obj instanceof QueryVariable && !(obj.getName().equalsIgnoreCase("CalendarAgeInYearsAt") ||obj.getName().equalsIgnoreCase("CalendarAgeInMonthsAt") ||
					obj.getName().equalsIgnoreCase("CalendarAgeInDaysAt")  || obj.getName().equalsIgnoreCase("ToDate"))){
				isTable = true;
			}
		}
		 return isTable;
	}
	
	/*
	 * This method is used to create Constraint Template
	 * input are 
	 * 1. expressionName to check if its not equal to queryName
	 * 2. queryName check whether its temp table or not
	 * 3. CqlModel
	 */
	
	public static boolean isTempTable(String expressionName,String queryName, CQLModel model){
		
		boolean isTable = false;
		if(queryName == null)
			return isTable;
		
		String test = queryName;
		if(queryName.startsWith("Count(")){
			test = (queryName.split("Count\\("))[1];
			test = test.split("\\)")[0];
		}
		InstanceInfo obj = (InstanceInfo) model.getExpressions().get(test);
		if(obj != null && (obj instanceof QueryVariable) && !(expressionName.equals(queryName))
				&& !(obj.getName().equalsIgnoreCase("CalendarAgeInYearsAt") ||obj.getName().equalsIgnoreCase("CalendarAgeInMonthsAt") ||
						obj.getName().equalsIgnoreCase("CalendarAgeInDaysAt")  || obj.getName().equalsIgnoreCase("ToDate"))){
			isTable = true;
		}
		 return isTable;
	}
	/*
	 * This method is used set dependent object for queryvariable
	 * input are 
	 * 1. QueryVariable to add dependent object  
	 * 2. operandVal to add in dependentobject in queryvariable
	 */
	public static void setStatementDependentObject(QueryVariable statement ,String operandVal){
		if(!statement.getDependentObjects().contains(operandVal))
		{
			statement.getDependentObjects().add(operandVal);
		}
	}	
	/*
	 * this method is to merge different constraints to operator 'in' from binary And/Or. 
	 */
	public static void mergeConstraints(QueryVariable statement){
		CopyOnWriteArrayList<Constraint> constraintList = new CopyOnWriteArrayList<>();
		List<String> leftOperands = new ArrayList<>();// to hold all the available leftOperands of the constraints
		List<String> leftOperName = new ArrayList<>();// to hold the name of the constraint without the id after that
		int index = 0;
		Constraint constraint = new Constraint();
		Interval interval = new Interval();
		String leftOperand = "";
		String name = "";
		if((statement.getConstraintList().size()>1) ){
			constraintList = statement.getConstraintList();
			for (Constraint constra : constraintList) {
				leftOperands.add(constra.getLeftOperand());
			}
			for (int j = 0 ; j < constraintList.size() ; j++) {
				if( (Collections.frequency(leftOperands, constraintList.get(j).getLeftOperand()) > 1) 
						&& (Collections.frequency(leftOperName, constraintList.get(j).getName().split("|")[0]+"-"+constraintList.get(j).getLeftOperand()) == 0) ){
					leftOperName.add(constraintList.get(j).getName().split("|")[0]+"-"+constraintList.get(j).getLeftOperand());
					index = constraintList.indexOf(constraintList.get(j));
					Constraint consObj = constraintList.get(index);
					if(consObj.getLeftOperand().equalsIgnoreCase(constraintList.get(index+1).getLeftOperand()) 
							&& (consObj.getRightOperand()!=null) && ((index+2) < constraintList.size() )
							&& (constraintList.get(index+2).getOperator().equalsIgnoreCase("AND") || constraintList.get((index+2)).getOperator().equalsIgnoreCase("OR") )
							&& consObj.getOperator().equalsIgnoreCase("GreaterOrEqual") ){
						name = constraintList.get(index+2).getName();
						leftOperand = "In("+consObj.getLeftOperand()+", Interval("+consObj.getRightOperand()+","+constraintList.get((index+1)).getRightOperand()+"))";
						interval.setLowValue(consObj.getRightOperand());
						interval.setHighValue(constraintList.get(index+1).getRightOperand());
						if(constraintList.get((index+1)).getOperator().equalsIgnoreCase("Less")){
							interval.setLowClosed(true);
							interval.setHighClosed(false);
						} else if(constraintList.get(index+1).getOperator().equalsIgnoreCase("LessOrEqual")){
							interval.setLowClosed(true);
							interval.setHighClosed(true);
						} else if(constraintList.get(index+1).getOperator().equalsIgnoreCase("Equals")){
							statement.setEqualsOperatorClosed(1);
							interval.setLowClosed(true);
						} 
						constraintList.remove(index+2);
						constraintList.remove(index+1);
						constraintList.remove(index);
						constraint.setLeftOperand(leftOperand);
						constraint.setRightOperand(null);
						constraint.setOperator("In");
						constraint.setDependentQueryVariable(null);
						constraint.setFromClause(false);
						constraint.setResolved(false);
						constraint.setName(name);
						constraintList.add(0, constraint);
						statement.setConstraintList(constraintList);
						statement.setIntervalOperator(interval);
						break;
					}
				}
			}
		}
	}
	
	/*
	 * this method is to check whether cqlmodel has population
	 */
	
	public static boolean populationCriteriaCheck(CQLModel model) {
		boolean isGreater = false;
		if(model.getPopulationCriteria().size() > 1) {
			isGreater = true;
		} 
		return isGreater;
	}
	
	/*
	 * this method is to convert operator to its binary form
	 */
	public static String converttoBinaryOperator(String Operator)
	{
		String returnOperator = null;
		if (Operator.equals("Union") || Operator.equals("Or"))
			returnOperator =  "||";
		else if (Operator.equals("And"))
			returnOperator =  "&&";
		return returnOperator;
	}
	
	/*
	 * this method is check whether operator is binary or not
	 */
	
	public static boolean isBinaryOperator(String Operator)
	{
		boolean returnOperator = false;
		if (Operator.equals("Union") || Operator.equals("Or") || Operator.equals("And") || Operator.equals("QueryAnd"))
			returnOperator =  true;
		
		return returnOperator;
	}
	
	/*
	 * this method is used to modify episode measures before drl generation
	 */
	
	public static void modifyEpisodeMeasures(CQLModel model) {
	
		Map<String, Object> expression = model.getExpressions();
		for (Entry<String, Object> singleExpression : expression.entrySet()) {
			if(!singleExpression.getKey().equals("Initial Population")
					&&(singleExpression.getKey().equals("Numerator") || singleExpression.getKey().equals("Denominator Exceptions") || singleExpression.getKey().equals("Denominator")))
			{
				if(((QueryVariable) singleExpression.getValue()).getConstraintList()!=null && !(((QueryVariable) singleExpression.getValue()).getConstraintList().isEmpty()))
				{
					for (Constraint singleConstraint : ((QueryVariable)singleExpression.getValue()).getConstraintList()) {
						
						if(isBinaryOperator(singleConstraint.getOperator()))
						{
							if(((singleConstraint.getRightOperand()!=null && model.getExpressions().containsKey(singleConstraint.getRightOperand())))
									&& (singleConstraint.getLeftOperand()!=null && model.getExpressions().containsKey(singleConstraint.getLeftOperand())))
							{
								QueryVariable rightQueryVar = (QueryVariable) model.getExpressions().get(singleConstraint.getRightOperand());
								QueryVariable leftQueryVar = (QueryVariable) model.getExpressions().get(singleConstraint.getLeftOperand());
								setQdmFunction(model,singleConstraint,null,rightQueryVar,leftQueryVar);
							}
						}
						else if (singleConstraint.getOperator().equals("ExpressionRef"))
						{
							if(model.getExpressions().containsKey(singleConstraint.getLeftOperand()) && !(singleConstraint.getLeftOperand().equals("Initial Population")))
							{
								setQdmFunction(model,singleConstraint,((QueryVariable) singleExpression.getValue()),(QueryVariable)model.getExpressions().get(singleConstraint.getLeftOperand()));
							}
							
						}
					}
				}
			}
		}
	}
	
	/*
	 * this method is used to set qdmfunction in queryvariable
	 */
	

	public static String setQdmFunction(CQLModel model,Constraint constraint, QueryVariable parentQueryVar,QueryVariable queryVar)
	{
		String qdmFunction = null;
			if(queryVar.getValueSetDataTypeMap().size()>0 && constraint.getRightOperand()!=null)
			{
				for(Entry<String,String> singleValueSetDataType : queryVar.getValueSetDataTypeMap().entrySet())
				{
					if("ENCOUNTER_PERFORMED".equals(singleValueSetDataType.getKey()) && model.getExpressions().containsKey(constraint.getRightOperand()))
					{
						constraint.setLeftOperand(null);
						constraint.setLeftOperand(constraint.getRightOperand());
						constraint.setRightOperand(null);
						constraint.setOperator("QueryExpressionRef");
					}
					else
					{
						for (Constraint singleConstraint : queryVar.getConstraintList()) {
							if(model.getExpressions().containsKey(singleConstraint.getLeftOperand())
									&& !(model.getExpressions().containsKey(singleConstraint.getRightOperand())))
							{
								queryVar.getConstraintList().remove(singleConstraint);
								queryVar.setEncounterQueryVariable(true);
							}
						}
					}
				}
			}
			else if(queryVar.getConstraintList().size()>0)
			{
				for (Constraint singleConstraint : queryVar.getConstraintList()) {
					if(model.getExpressions().containsKey(singleConstraint.getLeftOperand()))
					{
						setQdmFunction(model,constraint,parentQueryVar,(QueryVariable)model.getExpressions().get(singleConstraint.getLeftOperand()));
					}
				}
			}
			else if (queryVar.getDependentObjects().size()>0)
			{
				for (String singleDependentObj : queryVar.getDependentObjects()) {
					
					if(model.getExpressions().containsKey(singleDependentObj))
					{
						setQdmFunction(model,constraint,parentQueryVar,(QueryVariable)model.getExpressions().get(singleDependentObj));
					}
				}
			}
		return qdmFunction;
	}
	
	/*
	 * this method is used to set qdmfunction in queryvariable with an extra check of whether quryariable contains valueset  and datatype or not
	 */
	
	public static String setQdmFunction(CQLModel model,Constraint singleConstraint,QueryVariable parentQueryVar,QueryVariable rightQueryVar,QueryVariable leftQueryVar)
	{
		String qdmFunction = null ;
		if(leftQueryVar.getValueSetDataTypeMap()!=null && !leftQueryVar.getValueSetDataTypeMap().isEmpty())
		{
			for(Entry<String,String> singleValueSetDataType : leftQueryVar.getValueSetDataTypeMap().entrySet())
			{
				if(singleValueSetDataType.getKey().equals("ENCOUNTER_PERFORMED") && rightQueryVar.getValueSetDataTypeMap().size()>0)
				{
					rightQueryVar.setEncounterQueryVariable(true);
					qdmFunction = leftQueryVar.getQdmFunctionVariable().toString();
					rightQueryVar.setQdmFunctionVariable(leftQueryVar.getQdmFunctionVariable());
					singleConstraint.setLeftOperand(null);
					singleConstraint.setLeftOperand(singleConstraint.getRightOperand());
					singleConstraint.setRightOperand(null);
					singleConstraint.setOperator("QueryExpressionRef");
				}
				else if(singleValueSetDataType.getKey().equals("ENCOUNTER_PERFORMED") && rightQueryVar.getDependentObjects().size()>0)
				{
					for(String singleDependentObj : rightQueryVar.getDependentObjects())
					{
						((QueryVariable)model.getExpressions().get(singleDependentObj)).setEncounterQueryVariable(true);
						qdmFunction = leftQueryVar.getQdmFunctionVariable().toString();
						((QueryVariable)model.getExpressions().get(singleDependentObj)).setQdmFunctionVariable(leftQueryVar.getQdmFunctionVariable());
						singleConstraint.setLeftOperand(null);
						singleConstraint.setLeftOperand(singleConstraint.getRightOperand());
						singleConstraint.setRightOperand(null);
						singleConstraint.setOperator("QueryExpressionRef");
					}
				}
			}
		}
		else if (leftQueryVar.getDependentObjects()!=null && !leftQueryVar.getDependentObjects().isEmpty())
		{
			for(String singleDependentObj : leftQueryVar.getDependentObjects())
			{
				if(model.getExpressions().containsKey(singleDependentObj))
				{
					setQdmFunction(model,singleConstraint,leftQueryVar,rightQueryVar,(QueryVariable)model.getExpressions().get(singleDependentObj));
				}
			}
		}
		return qdmFunction;
	}

	/*
	 * this method is used to set interval period and append qdmfunctions
	 */
	public static void setIntervalPeriodAndQdmFunction(QueryVariable expression,String operator)
	{
		if(expression.isIntervalLowOperation())
		{
			expression.setIntervalLowPeriodOperator(operator);
		}
		if (expression.isIntervalHighOperation())
		{
			expression.setIntervalHighPeriodOperator(operator);
		}
		if(expression.getQdmFunctionVariable().toString().isEmpty() && expression.isAddQdmFunction())
		{
			expression.getQdmFunctionVariable().append(operator.toLowerCase()+"s");
		}
		else if (expression.isAddQdmFunction() && !(expression.getQdmFunctionVariable().toString().substring(0, expression.getQdmFunctionVariable().toString().length()-1).equalsIgnoreCase(operator)))
		{
			expression.getQdmFunctionVariable().append(operator);
		}
		expression.setAddQdmFunction(true);
	}
	
	/*
	 * this method is used set parent dependent condition
	 */
	
	public static void setParentDependentCondition(Map<Integer, List<String>> childMapDetails, CQLModel model){
		for (Entry<Integer, List<String>> dependantVariables : childMapDetails.entrySet()) {
			Integer parentConditionNum = dependantVariables.getKey();
			for (String queryVariableName : dependantVariables.getValue()) {
				QueryVariable queryVariable = (QueryVariable)(model.getExpressions().get(queryVariableName));
				if(queryVariable!=null && !(queryVariable.getName().contains(RuleGroup.IPP.getValue1()) || queryVariable.getName().contains(RuleGroup.DENOM.getValue1())
						|| queryVariable.getName().contains(RuleGroup.DENOM_EXCL.getValue1()) || queryVariable.getName().contains(RuleGroup.DENOM_EXCLS.getValue1()) 
						|| queryVariable.getName().contains(RuleGroup.NUMER.getValue1()) || queryVariable.getName().contains(RuleGroup.DENOM_EXCEP.getValue1())) ) 
				queryVariable.setParentDependentConditionNumber(parentConditionNum);
			}
		}
	}
	
	
	/*
	 * this method is used to parse dependent queryvariable along with its parent queryvariable
	 */
	
	public static void parseDependentQueryVariable(QueryVariable parentStatement, CQLModel model, 
			SequenceInfo sequenceInfo, Map<String, Object> expression)
	{
		if(parentStatement.getDependentObjects()!=null && !parentStatement.getDependentObjects().isEmpty())
		{
			for (String singleDependentQueryVariable : parentStatement.getDependentObjects())
			{
				if(!model.getExpressions().containsKey(singleDependentQueryVariable))
				{
					evaluateDependentexpression(singleDependentQueryVariable,model,sequenceInfo,expression);
				}
				else 
				{
					for (String singleLeafDependentQueryVariable : ((QueryVariable)model.getExpressions().get(singleDependentQueryVariable)).getDependentObjects())
					{
						if(!model.getExpressions().containsKey(singleLeafDependentQueryVariable) ||
						(((QueryVariable)model.getExpressions().get(singleLeafDependentQueryVariable)).getConditionNumber() == 0)){
							
							evaluateDependentexpression(singleLeafDependentQueryVariable,model,sequenceInfo,expression);
						}
					}
				}
			}
		}
	}
	/*
	 * this method is used to parse dependent queryvariable along with its parent queryvariable
	 */
	
	public static Expression getExpressionDetails(Map<String, Expression> expressionMap, String dependentQueryVariable)
	{
		Expression statementValue = null;
		for (Entry<String, Expression> def : expressionMap.entrySet()) {
			if(def.getKey().equals(dependentQueryVariable))
				statementValue =  def.getValue();
		}
		return statementValue;
	}
	
	/*
	 * this method is used to evaluate dependent expression
	 */
	
	public static void evaluateDependentexpression(String singleLeafDependentQueryVariable, CQLModel model, SequenceInfo sequenceInfo, Map<String, Object> expression)
	{

		Expression statementValue = null;
		QueryVariable statement = new QueryVariable();
		statement.setName(singleLeafDependentQueryVariable);
		statement.setMiddleqdmFunctionVariable("");
		statement.setQdmFunctionVariable(new StringBuilder(""));
		statement.setCreateQueryVariable(true);
		statement.setAddQdmFunction(true);
		statementValue = getExpressionDetails(model.getStatements(),singleLeafDependentQueryVariable);
		if(statementValue!=null)
		{
			ExpressionEvaluators.expressionEvaluator(statementValue, statement, model);
			sequenceInfo.getCheckMap().put(singleLeafDependentQueryVariable, true);
			expression.put(singleLeafDependentQueryVariable, statement);
			model.setExpressions(expression);
		}
	
	}
	
	/*
	 * this method is used to check whether queryvariable is further dependent on any other queryvariable
	 */
	
	public static void isfurtherDepedent(CQLModel model)
	{
		for (Map.Entry<String, Object> singleQueryVar : model.getExpressions().entrySet()) 
		{
			QueryVariable singleQueryVariable = (QueryVariable)singleQueryVar.getValue();
			int conditionNumber = singleQueryVariable.getConditionNumber();
			List<String> dependentObjects = singleQueryVariable.getDependentObjects();
			for (Map.Entry<String, Object> singleExpression : model.getExpressions().entrySet()) 
			{
				List<Integer> childConditionNumber =  ((QueryVariable)singleExpression.getValue()).getChildConditionNumber();
				if(!childConditionNumber.isEmpty())
				{
					for (Integer singleChildNumber : childConditionNumber) 
					{
						if(singleChildNumber == conditionNumber)
						{
							singleQueryVariable.setFurtherDepedent(true);
							break;
						}
				    }
				}
			}
			if(!dependentObjects.isEmpty())
			{
				for (String singleDependentObject : dependentObjects) 
				{
					if(model.getExpressions().containsKey(singleDependentObject) && singleQueryVariable.isFurtherDepedent())
					{
						((QueryVariable)model.getExpressions().get(singleDependentObject)).setFurtherDepedent(singleQueryVariable.isFurtherDepedent());
					}
				}
			}
		}
	}
	
	/*
	 * this method is used to set ParentDependentConditionNumber
	 */
	
	public static void setParentDependentConditionNumber(QueryVariable expression,int parentConditionNumber)
	{
		if(!(expression.getParentDependentConditionNumber()>0))
		{
			expression.setParentDependentConditionNumber(parentConditionNumber);
		}
	}
}
