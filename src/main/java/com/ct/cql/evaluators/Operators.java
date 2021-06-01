package com.ct.cql.evaluators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections4.CollectionUtils;

import com.ct.cql.elm.model.AliasedQuerySource;
import com.ct.cql.elm.model.CodeRef;
import com.ct.cql.elm.model.Expression;
import com.ct.cql.elm.model.ExpressionRef;
import com.ct.cql.elm.model.LetClause;
import com.ct.cql.elm.model.Query;
import com.ct.cql.elm.model.RelationshipClause;
import com.ct.cql.elm.model.Retrieve;
import com.ct.cql.elm.model.ReturnClause;
import com.ct.cql.elm.model.SortClause;
import com.ct.cql.elm.model.ToList;
import com.ct.cql.elm.model.ValueSetRef;
import com.ct.cql.entity.CQLModel;
import com.ct.cql.entity.Constants;
import com.ct.cql.entity.Constraint;
import com.ct.cql.entity.QueryVariable;
import com.ct.cql.parser.CQLHelper;

public class Operators {

	/*
	 * This method is used to evaluate all the binary operators in recession style 
	 * input are 1.expression list containing left and right operands
	 * 2. Query variable on which operation is getting performed
	 * 3. Binary operator name
	 * 4. CqlModel
	 */
	public static String evaluateBinaryOperator(List <Expression>expList, QueryVariable statement,String operatorName, CQLModel model){
		Constraint constraint = null;
		List<String> codeList = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(expList)){
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			constraint =CQLHelper.getConstraintTemplate(statement,operatorName);
			//left 
			String leftOperand = ExpressionEvaluators.expressionEvaluator(left, statement, model);

			if(operatorName.equalsIgnoreCase("SameOrBefore"))
			{
				statement.getQdmFunctionVariable().append("BeforeOrConcurrentWith");
			}
			else if (operatorName.equalsIgnoreCase("SameOrAfter"))
			{
				statement.getQdmFunctionVariable().append("AfterOrConcurrentWith");
				statement.getOccuranceQueryVariableCounter();
				constraint.setOccuranceQueryVariableCounter(statement.getOccuranceQueryVariableCounter());
				statement.setOccuranceQueryVariableCounter((statement.getOccuranceQueryVariableCounter())+1);
			}
			else if (operatorName.equalsIgnoreCase("SameAs"))
			{
				statement.getQdmFunctionVariable().append("OnConcurrentDay");
			}

			String rightOperand = ExpressionEvaluators.expressionEvaluator(right,statement,model);
			if(leftOperand != null && leftOperand.startsWith("Count(")){
				String tempLeftOperand = (leftOperand.split("Count\\("))[1];
				tempLeftOperand = tempLeftOperand.split("\\)")[0];
				constraint.setFromClause(true);
				constraint.setLeftOperand(tempLeftOperand);
				constraint.setRightOperand(leftOperand+","+rightOperand);
				CQLHelper.setStatementDependentObject(statement ,tempLeftOperand);
				statement.getConstraintList().add(constraint);
			}
			
			else if(leftOperand!=null && rightOperand!=null && !(statement.getName().equals(leftOperand) || statement.getName().equals(rightOperand)))
			{
				if(CQLHelper.isTempTable(statement.getName(),leftOperand, model)){
					constraint.setFromClause(true);
					CQLHelper.setStatementDependentObject(statement ,leftOperand);
				}
				if(model.getCodeSets().get(leftOperand)!=null)
				{
					codeList.add(model.getCodeSets().get(leftOperand));
					constraint.setCodeSetList(codeList);
				}
				if( model.getCodeSets().get(rightOperand)!=null)
				{
					codeList.add(model.getCodeSets().get(rightOperand));
					constraint.setCodeSetList(codeList);
				}
				constraint.setLeftOperand(leftOperand);
				constraint.setRightOperand(rightOperand);
				
				if(CQLHelper.isTempTable(statement.getName(),rightOperand, model)){
					constraint.setFromClause(true);
					CQLHelper.setStatementDependentObject(statement ,rightOperand);
				}
				statement.getConstraintList().add(constraint);
			}
		}
		return constraint.getName();
	}
	
	/*
	 * This method is used to evaluate Exists operator
	 * input are 
	 * 1. exist expression to evaluate
	 * 2. Queryvariable on which operation is getting performed
	 * 3. CqlModel
	 */
	public static String evaluateExists(Expression expression, QueryVariable statement,CQLModel model){
		Constraint constraint = null;
		if(statement.getMiddleqdmFunctionVariable()!=null && !statement.getMiddleqdmFunctionVariable().isEmpty())
		{
			constraint =  CQLHelper.getConstraintTemplate(statement, statement.getMiddleqdmFunctionVariable()+"Exists");
		}
		else
		{
			constraint =  CQLHelper.getConstraintTemplate(statement, "Exists");
		}
		
		if(null != expression){
			//left 
			String operand = ExpressionEvaluators.expressionEvaluator(expression, statement, model);
			if(!(statement.getName().equals(operand)))// && !(expression.getClass() == Query.class))
			{
					constraint.setLeftOperand(operand);
					
					if(CQLHelper.isTempTable(statement.getName(),operand, model)){
						constraint.setFromClause(true);
						CQLHelper.setStatementDependentObject(statement,operand);
					}
					statement.getConstraintList().add(constraint);
			}
		}
		return constraint.getName();
	}
	
	/*
	 * This method is used to evaluate Retrieve operator
	 * input are 
	 * 1. Retrieve expression to evaluate
	 * 2. Queryvariable on which operation is getting performed
	 * 3. CqlModel
	 */
	
	public static String evaluateRetrieveOperator(Retrieve expression,QueryVariable parentStatement,
			QueryVariable statement, String withAlias,CQLModel model) {
		String queryVariable = "";
		HashMap<String, String> valueSetDataTypeMap;
		QueryVariable queryVar;
		if(null != expression){
			Expression code = expression.getCodes();
			if(code instanceof ValueSetRef){
				String name = (((ValueSetRef) code).getName());
				if((withAlias!=null && !withAlias.isEmpty()) && (parentStatement!=null))
				{
					 queryVar = CQLHelper.getOrCreateQueryVariable(statement,withAlias, model);
				}
				else
				{
					 queryVar = CQLHelper.getOrCreateQueryVariable(statement,name, model);
				}
				if(statement.isCreateQueryVariable() &&!(queryVar.getName().equals(name))){
					valueSetDataTypeMap = (HashMap<String, String>) queryVar.getValueSetDataTypeMap();
					valueSetDataTypeMap.put(CQLHelper.getDataType(expression.getDataType().getLocalPart()), "V-"+model.getValueSets().get(name));
					queryVar.setValueSetDataTypeMap(valueSetDataTypeMap);
					CQLHelper.setStatementDependentObject(statement,queryVar.getName());
					 if(statement.getParentConditionNumber()!=0)
						{
						  queryVar.getChildConditionNumber().add(statement.getParentConditionNumber());
						}
					model.getExpressions().put(queryVar.getName(), queryVar);
					queryVariable = queryVar.getName();
				} 
				else {
						valueSetDataTypeMap = (HashMap<String, String>) statement.getValueSetDataTypeMap();
						if(valueSetDataTypeMap.get(CQLHelper.getDataType(expression.getDataType().getLocalPart())) != null)
						{
							StringBuffer keyValue = new StringBuffer(valueSetDataTypeMap.get(CQLHelper.getDataType(expression.getDataType().getLocalPart())));
							keyValue.append(",V-"+model.getValueSets().get(name));
							valueSetDataTypeMap.put(CQLHelper.getDataType(expression.getDataType().getLocalPart()), keyValue.toString());
						}
						else
						{
							valueSetDataTypeMap.put(CQLHelper.getDataType(expression.getDataType().getLocalPart()), "V-"+model.getValueSets().get(name));
						}
						
						statement.setValueSetDataTypeMap(valueSetDataTypeMap);
						queryVariable = statement.getName();
				}
			}
			else if (code instanceof ToList)
			{
				Expression operand = ((ToList) code).getOperand();
				if(operand instanceof CodeRef)
				{
					String name = ((CodeRef) operand).getName();
					if((withAlias!=null && !withAlias.isEmpty()) && (parentStatement!=null))
					{
						 queryVar = CQLHelper.getOrCreateQueryVariable(statement,withAlias, model);
					}
					else
					{
						 queryVar = CQLHelper.getOrCreateQueryVariable(statement,name, model);
					}
					if(statement.isCreateQueryVariable() && !(queryVar.getName().equals(name))){

						valueSetDataTypeMap = (HashMap<String, String>) queryVar.getValueSetDataTypeMap();
						valueSetDataTypeMap.put(CQLHelper.getDataType(expression.getDataType().getLocalPart()), "C-"+model.getValueSets().get(name));
						queryVar.setValueSetDataTypeMap(valueSetDataTypeMap);
						CQLHelper.setStatementDependentObject(statement,queryVar.getName());
						 if(statement.getParentConditionNumber()!=0)
							{
							  queryVar.getChildConditionNumber().add(statement.getParentConditionNumber());
							}
						model.getExpressions().put(queryVar.getName(), queryVar);
						queryVariable = queryVar.getName();
					
					}
					else
					{
						valueSetDataTypeMap = (HashMap<String, String>) statement.getValueSetDataTypeMap();
						if(valueSetDataTypeMap.get(CQLHelper.getDataType(expression.getDataType().getLocalPart())) != null)
						{
							StringBuffer keyValue = new StringBuffer(valueSetDataTypeMap.get(CQLHelper.getDataType(expression.getDataType().getLocalPart())));
							keyValue.append(",C-"+model.getCodeSets().get(name));
							valueSetDataTypeMap.put(CQLHelper.getDataType(expression.getDataType().getLocalPart()), keyValue.toString());
						}
						else
						{
							valueSetDataTypeMap.put(CQLHelper.getDataType(expression.getDataType().getLocalPart()), "C-"+model.getCodeSets().get(name));
						}
						statement.setValueSetDataTypeMap(valueSetDataTypeMap);
						queryVariable = name;
					}
				}
			}
			else if(withAlias!=null && !withAlias.isEmpty())
			{
				statement.setQdmPeriodVariable(withAlias);
				queryVariable = withAlias;
			}
			else
			{
				valueSetDataTypeMap = (HashMap<String, String>) statement.getValueSetDataTypeMap();
				valueSetDataTypeMap.put(CQLHelper.getDataType(expression.getDataType().getLocalPart()), null);
			}
		}
		return queryVariable;
	}
	
	/*
	 * This method is used to evaluate Query  operator
	 * input are 
	 * 1. Query expression to evaluate
	 * 2. Queryvariable on which operation is getting performed
	 * 3. CqlModel
	 */
	
	public static String evaluateQueryOperator(Query expression, QueryVariable statement,CQLModel model){
		String queryVarName = null;
		QueryVariable queryVar = null;
		String whereExpressionRef = null;
		int sourceCounter = 0;
		SortClause sortExp = expression.getSort();
		List<LetClause> letExp = expression.getLet();
		Expression whereExp = expression.getWhere();
		int initialCounter = expression.getSource().size();
		ReturnClause returnexp = expression.getReturn();
		List<RelationshipClause> withExp = expression.getRelationship();
		for(AliasedQuerySource sourceObj :expression.getSource())
		{
			String withQueryName = null;
			Expression exp = sourceObj.getExpression();
			if(null != exp)
			{
				if(sortExp!=null)
				{
					if(exp.getClass() == Retrieve.class) {
						statement.setCreateQueryVariable(false);
					}
					else if (exp.getClass() == ExpressionRef.class && model.getExpressions().containsKey(((ExpressionRef)exp).getName()))
					{
						Constraint constraint = CQLHelper.getConstraintTemplate(statement, "QueryExpressionRef");
						constraint.setLeftOperand(((ExpressionRef)exp).getName());
						statement.getConstraintList().add(constraint);
					}
					ExpressionEvaluators.expressionEvaluator(exp,  statement, model);
					if(whereExp!=null && statement.getWhereConditionEvaluated() == 0 && exp.getClass() !=Query.class)
					{
						statement.setWhereExpression(true);
						ExpressionEvaluators.expressionEvaluator(whereExp, statement, model);
						//statement.setWhereConditionEvaluated(1);
					}
					if(withExp!=null && !withExp.isEmpty())
					{
						 evaluateWithOperator(withExp,null,statement,model);
					}
					queryVarName = statement.getName();
				}
				else
				{
					  if(exp.getClass() == ExpressionRef.class) 
					  {
						 if(model.getExpressions().containsKey(sourceObj.getAlias()))
						 {
							 if(model.isEpisodeMeasure())
							 {
								  statement.getDependentObjects().add(sourceObj.getAlias());
								  Constraint constraint =  CQLHelper.getConstraintTemplate(statement, "QueryExpressionRef");
								  constraint.setLeftOperand(sourceObj.getAlias());
								  statement.getConstraintList().add(constraint);
								  if(whereExp!=null && statement.getWhereConditionEvaluated() == 0)
									{
									  	statement.setWhereExpression(true);
										ExpressionEvaluators.expressionEvaluator(whereExp, statement, model);
										statement.setWhereConditionEvaluated(1);
									}
							 }
							 else{
							 if(((QueryVariable)model.getExpressions().get(sourceObj.getAlias())).getValueSetDataTypeMap()!=null && 
									 !((QueryVariable)model.getExpressions().get(sourceObj.getAlias())).getValueSetDataTypeMap().isEmpty())
							 {
								 for (Entry<String, String> entry : (((QueryVariable)model.getExpressions().get(sourceObj.getAlias())).getValueSetDataTypeMap()).entrySet()) {
									    String dataType = entry.getKey();
									    String valueSetList = entry.getValue();
									    statement.getValueSetDataTypeMap().put(dataType,valueSetList);
									}
							 }
							 statement.getDependentObjects().add(sourceObj.getAlias());
							 statement.setCreateQueryVariable(false);
								if(whereExp!=null && statement.getWhereConditionEvaluated() == 0)
								{
									statement.setWhereExpression(true);
									ExpressionEvaluators.expressionEvaluator(whereExp, statement, model);
									statement.setWhereConditionEvaluated(1);
								}
								if(withExp!=null && !withExp.isEmpty())
								{
									 evaluateWithOperator(withExp,null,statement,model);
								}
								queryVarName = statement.getName();
							 }
						 }						 
						  else if(model.getExpressions().containsKey(((ExpressionRef)exp).getName()) && !(initialCounter>1))
							 {
							  Constraint constraint = new Constraint();
							  if(withExp == null && whereExp == null)
							  {
								  withQueryName = ExpressionEvaluators.expressionEvaluator(exp,  statement, model);
								  statement.getDependentObjects().add(withQueryName);
								  constraint = CQLHelper.getConstraintTemplate(statement, "QueryExpressionRef");
								  constraint.setLeftOperand(withQueryName);
								  statement.getConstraintList().add(constraint);
							  	statement.setParentConditionNumber(((QueryVariable) model.getExpressions().get(((ExpressionRef)exp).getName())).getConditionNumber());
								
							  }
							  if(withExp!=null && !withExp.isEmpty())
								{
									withQueryName = evaluateWithOperator(withExp,statement,statement,model);
									if(statement.isCreateAndOperator() && !statement.isWithExpressionRef())
									{
										constraint = CQLHelper.getConstraintTemplate(statement, "And");
										constraint.setLeftOperand(((ExpressionRef)exp).getName());
										constraint.setRightOperand(withQueryName);
										statement.getConstraintList().add(constraint);
									}
									else
									{
										 constraint = CQLHelper.getConstraintTemplate(statement, "QueryExpressionRef");
										 constraint.setLeftOperand(((ExpressionRef)exp).getName());
										 statement.getConstraintList().add(constraint);
									}
								}
								if(whereExp!=null && statement.getWhereConditionEvaluated() == 0)
								{
									statement.setWhereExpression(true);
									whereExpressionRef = ExpressionEvaluators.expressionEvaluator(whereExp, statement, model);
									if(whereExp.getClass() == ExpressionRef.class)
									{
										constraint = CQLHelper.getConstraintTemplate(statement, "And");
										constraint.setLeftOperand(((ExpressionRef)exp).getName());
										constraint.setRightOperand(whereExpressionRef);
										statement.getConstraintList().add(constraint);
									}
									else
									{
										constraint = CQLHelper.getConstraintTemplate(statement, "QueryExpressionRef");
										constraint.setLeftOperand(((ExpressionRef)exp).getName());
										statement.getConstraintList().add(constraint);
										statement.getDependentObjects().add(((ExpressionRef)exp).getName());
									}
									statement.setWhereConditionEvaluated(1);
								}
								if(returnexp!=null && withQueryName == null)
								{
									withQueryName = ExpressionEvaluators.expressionEvaluator(returnexp.getExpression(), statement, model);
								}
								
								queryVarName = withQueryName;
							 }
						  else if (model.getExpressions().containsKey(((ExpressionRef)exp).getName()) && (initialCounter>1))
						  {
							  	setChildQueryVariable(exp,statement,sourceObj.getAlias(),model);
								if(sourceCounter == 0 && whereExp!=null && statement.getWhereConditionEvaluated() == 0)
								{
									ExpressionEvaluators.expressionEvaluator(whereExp, statement, model);
									statement.setWhereConditionEvaluated(1);
								}
								if(withExp!=null && !withExp.isEmpty())
								{
									withQueryName = evaluateWithOperator(withExp,statement,queryVar,model);
								}
						  }
						}
					  else
					  {
						  if(returnexp!=null)
						  {
							  ExpressionEvaluators.expressionEvaluator(exp, statement, model);
							  ExpressionEvaluators.expressionEvaluator(returnexp.getExpression(), statement, model);
							  if(whereExp!=null && statement.getWhereConditionEvaluated() == 0)
							  {
								  statement.setWhereExpression(true);
								  ExpressionEvaluators.expressionEvaluator(whereExp, statement, model);
								  statement.setWhereConditionEvaluated(1);
							  }
							  
							  if(withExp!=null && !withExp.isEmpty())
								{
									withQueryName = evaluateWithOperator(withExp,statement,statement,model);
								}
						  }
						  else
						  {
							  if(letExp!=null && !letExp.isEmpty())
							  {
								  queryVar = CQLHelper.getOrCreateQueryVariable(statement,statement.getName()+"_"+(sourceObj.getAlias()), model);
							  }
							  else
							  {
								  if(expression.getSource().size() >= 1 && !(exp.getClass() == Retrieve.class)) {
									  //to check if the expression has a source and its not using the alias with key from other files ex. matglobal
									  queryVar = CQLHelper.getOrCreateQueryVariable(statement, statement.getName()+Constants.ALIAS, model);
								  } 
								  else
								  {
									  queryVar = CQLHelper.getOrCreateQueryVariable(statement, sourceObj.getAlias(), model);
								  }
							  }
							  if(!queryVar.isExpressionResolved())
							  {
								  if(statement.getParentConditionNumber()!=0)
									{
									  queryVar.getChildConditionNumber().add(statement.getParentConditionNumber());
									}
									 if(exp.getClass() == Retrieve.class) {
										queryVar.setCreateQueryVariable(false);
									 	ExpressionEvaluators.expressionEvaluator(exp, queryVar, model);
										
										if(withExp!=null && !withExp.isEmpty())
										{
											queryVar.setCreateQueryVariable(true);
											withQueryName = evaluateWithOperator(withExp,statement,queryVar,model);
											if(!queryVar.isWithExpressionRef())
											{
												Constraint constraint = CQLHelper.getConstraintTemplate(statement, "And");
												constraint.setLeftOperand(queryVar.getName());
												constraint.setRightOperand(withQueryName);
												statement.getConstraintList().add(constraint);
											}
										}
										if(whereExp!=null && queryVar.getWhereConditionEvaluated() == 0)
										{
									 		statement.setWhereExpression(true);
									 		queryVar.setWhereExpression(true);
											ExpressionEvaluators.expressionEvaluator(whereExp, queryVar, model);
											queryVar.setWhereConditionEvaluated(1);
										}
										statement.getQueryVariable().add(queryVar);
										CQLHelper.setStatementDependentObject(statement,queryVar.getName());
									 }
									 else
									 {
										 if(withExp!=null && !withExp.isEmpty())
											{
												withQueryName = evaluateWithOperator(withExp,statement,queryVar,model);
											}
										 	ExpressionEvaluators.expressionEvaluator(exp, queryVar, model);
										 	if(whereExp!=null && queryVar.getWhereConditionEvaluated() == 0)
											{
										 		queryVar.setWhereExpression(true);
										 		ExpressionEvaluators.expressionEvaluator(whereExp, queryVar, model);
										 		queryVar.setWhereConditionEvaluated(1);
											}
											statement.getQueryVariable().add(queryVar);
											CQLHelper.setStatementDependentObject(statement,queryVar.getName());
											queryVar.setOperator(CQLHelper.converttoBinaryOperator(exp.getClass().getSimpleName()));
									 }
									if(withQueryName !=null && !withQueryName.isEmpty() && !withQueryName.contains("|"))
										queryVarName = withQueryName;
										else
										{
											queryVarName = queryVar.getName();
										}
										queryVar.setExpressionResolved(true);
							  }
							  else
							  {
								  withQueryName = ExpressionEvaluators.expressionEvaluator(exp,  statement, model);
								  if(whereExp!=null && statement.getWhereConditionEvaluated() == 0)
								  {
									  statement.setWhereExpression(true);
									  ExpressionEvaluators.expressionEvaluator(whereExp, statement, model);
									  statement.setWhereConditionEvaluated(1);
								  }

									if(withExp!=null && !withExp.isEmpty())
									{
										withQueryName = evaluateWithOperator(withExp,statement,statement,model);
									}
									queryVarName = withQueryName;
									statement.setExpressionResolved(true);
							  }
						  }
					  }
				}
			}
			sourceCounter++;
		}
		if(letExp!=null && !letExp.isEmpty())
		{
			evaluateLetOperator(letExp,whereExp,statement,model);
		}
		return queryVarName;
	}
	
	/*
	 * This method is used to evaluate Let operator
	 * input are 
	 * 1. Let expression list to evaluate
	 * 2. Queryvariable on which operation is getting performed
	 * 3. CqlModel
	 */
	
	public static void evaluateLetOperator(List<LetClause> letExp,Expression whereExp, QueryVariable statement,CQLModel model){
		QueryVariable queryVar = null;
		String queryVariableName = null;
		for(LetClause letClause: letExp)
		{
			Expression exp = letClause.getExpression();
			queryVar = CQLHelper.getOrCreateQueryVariable(statement,statement.getName()+"_"+letClause.getIdentifier(), model);
			queryVariableName = ExpressionEvaluators.expressionEvaluator(exp, queryVar, model);
			if(statement.getParentConditionNumber()!=0)
			{
			  queryVar.getChildConditionNumber().add(statement.getParentConditionNumber());
			}
			if(whereExp!=null && queryVar.getWhereConditionEvaluated() == 0)
			{
				queryVar.setWhereExpression(true);
				ExpressionEvaluators.expressionEvaluator(whereExp, queryVar, model);
				queryVar.setWhereConditionEvaluated(1);
			}
			if(statement.getDependentObjects()!=null && !statement.getDependentObjects().isEmpty())
			{
				Constraint constraint = CQLHelper.getConstraintTemplate(statement, "And");
				constraint.setLeftOperand(statement.getDependentObjects().get(0));
				statement.getDependentObjects().add(queryVariableName);
				constraint.setRightOperand(queryVariableName);
				statement.getConstraintList().add(constraint);
			}
		}
	}
	
	/*
	 * This method is used to evaluate With operator that comes with Query Operator
	 * input are 
	 * 1. relationship expression list to evaluate
	 * 2. Parent Queryvariable on which operation is getting performed
	 * 3. Child Queryvariable on which operation is getting performed
	 * 4. CqlModel
	 */
	public static String evaluateWithOperator(List<RelationshipClause> relationshipExp, QueryVariable  parentStatement,
			QueryVariable statement,CQLModel model){
		String queryVariableName = null ;
		
	if(relationshipExp.size()>1)
	{
		for(RelationshipClause relationshipClause : relationshipExp)
		{
			Expression exp = relationshipClause.getExpression();
			Expression suchThatExp = relationshipClause.getSuchThat();
			if (exp.getClass() == ExpressionRef.class && model.getExpressions().containsKey(((ExpressionRef)exp).getName()))
			{
				if(((QueryVariable)model.getExpressions().get(((ExpressionRef)exp).getName())).getConditionNumber() == 0)
				{
					CQLHelper.evaluateDependentexpression(((ExpressionRef)exp).getName(), model, model.getSequenceInfo(), model.getExpressions());
				}
				statement.getChildConditionNumber().add(((QueryVariable) model.getExpressions().get(((ExpressionRef)exp).getName())).getConditionNumber());
				queryVariableName = ExpressionEvaluators.expressionEvaluator(suchThatExp, statement, model);
				statement.setWithExpressionRef(true);
				Constraint constraint = CQLHelper.getConstraintTemplate(statement, "childExpressionRef");
				constraint.setLeftOperand(((ExpressionRef)exp).getName());
				statement.getConstraintList().add(constraint);
			}
			if(exp.getClass() == Retrieve.class) {
				statement.setCreateAndOperator(false);
				queryVariableName = evaluateRetrieveOperator((Retrieve)exp, parentStatement,statement, relationshipClause.getAlias(),model);
				evaluateRetrieveSuchThatOperator(suchThatExp,statement,model);
			}
		}
	}
	else
	{
		for(RelationshipClause relationshipClause : relationshipExp)
		{
			Expression exp = relationshipClause.getExpression();
			Expression suchThatExp = relationshipClause.getSuchThat();
			if(exp!=null)
			{
				if(statement.getChildConditionNumber().isEmpty())
				{
					if(exp.getClass() == Retrieve.class) {
						statement.setCreateAndOperator(false);
						queryVariableName = evaluateRetrieveOperator((Retrieve)exp, parentStatement,statement, relationshipClause.getAlias(),model);
						evaluateRetrieveSuchThatOperator(suchThatExp,statement,model);
					}
					
					else if (exp.getClass() == ExpressionRef.class && model.getExpressions().containsKey(((ExpressionRef)exp).getName()))
					{
						if(model.getExpressions().containsKey(((ExpressionRef)exp).getName()) && ((QueryVariable)model.getExpressions().get(((ExpressionRef)exp).getName())).getConditionNumber() == 0)
						{
							CQLHelper.evaluateDependentexpression(((ExpressionRef)exp).getName(), model, model.getSequenceInfo(), model.getExpressions());
						}
						statement.getChildConditionNumber().add(((QueryVariable) model.getExpressions().get(((ExpressionRef)exp).getName())).getConditionNumber());
						queryVariableName = ExpressionEvaluators.expressionEvaluator(suchThatExp, statement, model);
						statement.setWithExpressionRef(true);
						Constraint constraint = CQLHelper.getConstraintTemplate(statement, "childExpressionRef");
						constraint.setLeftOperand(((ExpressionRef)exp).getName());
						statement.getConstraintList().add(constraint);
						CQLHelper.setParentDependentConditionNumber(((QueryVariable) model.getExpressions().get(((ExpressionRef)exp).getName())),statement.getConditionNumber());
					}
					else
					{
						queryVariableName = ExpressionEvaluators.expressionEvaluator(exp, statement, model);
						ExpressionEvaluators.expressionEvaluator(suchThatExp, statement, model);
					}
				}
				else
				{
					parentStatement.setDependentObjects((CopyOnWriteArrayList<String>) statement.getDependentObjects());
					for(Entry<String, String> singleDataTypeValueSet: statement.getValueSetDataTypeMap().entrySet())
					{
						parentStatement.getValueSetDataTypeMap().put(singleDataTypeValueSet.getKey(), singleDataTypeValueSet.getValue());
					}
					parentStatement.getConstraintList().addAll((List<Constraint>) statement.getConstraintList());
					parentStatement.setQdmFunctionVariable(statement.getQdmFunctionVariable());
					if (exp.getClass() == ExpressionRef.class && model.getExpressions().containsKey(((ExpressionRef)exp).getName())){
						parentStatement.getChildConditionNumber().add(((QueryVariable) model.getExpressions().get(((ExpressionRef)exp).getName())).getConditionNumber());
					}
					ExpressionEvaluators.expressionEvaluator(suchThatExp, parentStatement, model);
					queryVariableName = parentStatement.getName();
				}
			}
		}
	}
		return queryVariableName;
	}
	
	/*
	 * This method is used to evaluate suchthat operator within Retrieve operator 
	 * input are 
	 * 1. suchThat expression list to evaluate
	 * 2. Queryvariable on which operation is getting performed
	 * 3. CqlModel
	 */
	
	public static void evaluateRetrieveSuchThatOperator(Expression suchThatExp, QueryVariable statement,CQLModel model){
				ExpressionEvaluators.expressionEvaluator(suchThatExp, statement, model);
		}
	
	
	/*
	 * This method is used to set child queryvariable that are being referred by queryvariable
	 * input are 
	 * 1. suchThat expression list to evaluate
	 * 2. Queryvariable on which operation is getting performed
	 * 3. SourceAlias to set in right operand
	 * 4. CqlModel
	 */
	
	public static void setChildQueryVariable(Expression exp,QueryVariable expression,String SourceAlias,CQLModel model)
	{
		if(model.getExpressions().containsKey(((ExpressionRef)exp).getName()))
		{
			Constraint constraint = CQLHelper.getConstraintTemplate(expression, "childExpressionRef");
			if(!expression.getOccuranceQueryVariable().contains(((QueryVariable)model.getExpressions().get(((ExpressionRef)exp).getName())).getConditionNumber()))
			{
				expression.getOccuranceQueryVariable().add(((QueryVariable)model.getExpressions().get(((ExpressionRef)exp).getName())).getConditionNumber());
				constraint.setOccuranceQueryVariable(((QueryVariable)model.getExpressions().get(((ExpressionRef)exp).getName())).getConditionNumber());
			}
			
			constraint.setLeftOperand(((ExpressionRef)exp).getName());
			constraint.setRightOperand(SourceAlias);
			expression.getConstraintList().add(constraint);
		}
		else
		{
			ExpressionEvaluators.expressionEvaluator(exp, expression, model);
		}
	}
}

