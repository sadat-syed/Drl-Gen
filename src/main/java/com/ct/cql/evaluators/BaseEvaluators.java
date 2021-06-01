package com.ct.cql.evaluators;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ct.cql.elm.model.Add;
import com.ct.cql.elm.model.After;
import com.ct.cql.elm.model.AliasRef;
import com.ct.cql.elm.model.As;
import com.ct.cql.elm.model.Before;
import com.ct.cql.elm.model.BinaryExpression;
import com.ct.cql.elm.model.Collapse;
import com.ct.cql.elm.model.Count;
import com.ct.cql.elm.model.DateFrom;
import com.ct.cql.elm.model.DurationBetween;
import com.ct.cql.elm.model.End;
import com.ct.cql.elm.model.Expression;
import com.ct.cql.elm.model.ExpressionRef;
import com.ct.cql.elm.model.FunctionRef;
import com.ct.cql.elm.model.In;
import com.ct.cql.elm.model.InValueSet;
import com.ct.cql.elm.model.IncludedIn;
import com.ct.cql.elm.model.Interval;
import com.ct.cql.elm.model.IsNull;
import com.ct.cql.elm.model.Literal;
import com.ct.cql.elm.model.Not;
import com.ct.cql.elm.model.OperandRef;
import com.ct.cql.elm.model.Overlaps;
import com.ct.cql.elm.model.OverlapsAfter;
import com.ct.cql.elm.model.OverlapsBefore;
import com.ct.cql.elm.model.ParameterRef;
import com.ct.cql.elm.model.Property;
import com.ct.cql.elm.model.Quantity;
import com.ct.cql.elm.model.Retrieve;
import com.ct.cql.elm.model.Start;
import com.ct.cql.elm.model.Subtract;
import com.ct.cql.elm.model.Sum;
import com.ct.cql.entity.CQLModel;
import com.ct.cql.entity.Constants;
import com.ct.cql.entity.Constraint;
import com.ct.cql.entity.QueryVariable;
import com.ct.cql.parser.CQLHelper;

public class BaseEvaluators {

	/*
	 * this method is used to evaluate base condition whether its unary or binary and parse accordingly
	 */
	public static String evaluateBaseCondition(Expression exp ,QueryVariable statement, CQLModel model) {
		String operand= null;
		
		if(exp instanceof Retrieve){
			System.out.println("inside Retrieve");
			operand = Operators.evaluateRetrieveOperator((Retrieve)exp, null, statement, null,model);
		}
		else if(Constants.UNARY.contains(exp.getClass().getSimpleName()))
		{
			operand = evaluateUnaryBaseCondition(exp ,statement, model);
		}
		else if (Constants.BINARY.contains(exp.getClass().getSimpleName()))
		{
			operand = evaluateBinaryBaseCondition(exp ,statement, model);
		}
		return operand;
	}
	
	/*
	 * this method is used to evaluate base unary condition 
	 */
	
	public static String evaluateUnaryBaseCondition(Expression exp ,QueryVariable statement, CQLModel model) {
		
		String operand = null;
		if(exp instanceof FunctionRef)
		{
			operand = ((FunctionRef) exp).getName();
		}
		else if (exp instanceof ParameterRef)
		{
			operand = ((ParameterRef) exp).getName();
		}
		else if (exp instanceof OperandRef)
		{
			operand = ((OperandRef) exp).getName();
		}
		else if (exp instanceof AliasRef)
		{
			operand = ((AliasRef) exp).getName();
		}
		else if(exp instanceof ExpressionRef){
			operand = ((ExpressionRef) exp).getName();
			if(model.getExpressions().containsKey(operand) && ((QueryVariable)model.getExpressions().get(operand)).getConditionNumber() == 0)
			{
				CQLHelper.evaluateDependentexpression(operand, model, model.getSequenceInfo(), model.getExpressions());
			}
			if(statement.isWhereExpression() && model.getExpressions().containsKey(operand))
			{
				statement.setChildQueryVariable((QueryVariable)model.getExpressions().get(operand));
			}
			CQLHelper.getOrCreateQueryVariable(statement,operand, model);
		}
		else if(exp instanceof Literal){
			operand = ((Literal) exp).getValue();
		}
		else if(exp instanceof End){
			operand = ExpressionEvaluators.expressionEvaluator(((End) exp).getOperand(),statement,model);
			CQLHelper.setIntervalPeriodAndQdmFunction(statement,"End");
			statement.setMiddleqdmFunctionVariable("OrConcurrentWith");
		}
		else if(exp instanceof Start){
			operand = ExpressionEvaluators.expressionEvaluator(((Start) exp).getOperand(),statement,model);
			CQLHelper.setIntervalPeriodAndQdmFunction(statement,"Start");
			statement.setMiddleqdmFunctionVariable("OrConcurrentWith");
		}
		else if(exp instanceof Quantity){
			operand = evaluateQuantity(exp,statement);
		}
		else if(exp instanceof As){
			Expression expList = ((As) exp).getOperand();
			statement.setCreateQueryVariable(true);
			operand =ExpressionEvaluators.expressionEvaluator(expList, statement, model);
		}
		else if (exp instanceof Not)
		{
			Expression not = ((Not) exp).getOperand();
			statement.setMiddleqdmFunctionVariable("Not");
			operand = ExpressionEvaluators.expressionEvaluator(not, statement, model);
			
		}
		else if (exp instanceof IsNull)
		{
			String isNullOperator = null;
			Expression IsNull = ((IsNull) exp).getOperand();
			String leftOperand = ExpressionEvaluators.expressionEvaluator(IsNull,statement,model);
			Constraint constraint = CQLHelper.getConstraintTemplate(statement);
			if(statement.getMiddleqdmFunctionVariable()!=null)
			{
				isNullOperator = "Is"+statement.getMiddleqdmFunctionVariable()+"Null";
			}
			else 
			{
				isNullOperator = "IsNull";
			}
			constraint.setOperator(isNullOperator);
			constraint.setLeftOperand(leftOperand);
			statement.getConstraintList().add(constraint);
			operand = constraint.getName();
			statement.setMiddleqdmFunctionVariable(null);
		}
		else if (exp instanceof Sum)
		{
			Expression Sum = ((Sum) exp).getSource();
			operand = ExpressionEvaluators.expressionEvaluator(Sum, statement, model);
		}
		else if (exp instanceof Collapse)
		{
			Expression Collapse = ((Collapse) exp).getOperand();
			System.out.println("Collapse : "+Collapse);
			operand = ExpressionEvaluators.expressionEvaluator(Collapse, statement, model);
		}
		else if (exp instanceof DateFrom)
		{
			System.out.println("inside datefrom");
			Expression dateFrom = ((DateFrom) exp).getOperand();
			operand = ExpressionEvaluators.expressionEvaluator(dateFrom, statement, model);
			System.out.println("operand : "+operand);
		}
		return operand;
	}
	
	/*
	 * this method is used to evaluate base binary condition 
	 */
	
	public static String evaluateBinaryBaseCondition(Expression exp ,QueryVariable statement, CQLModel model) {
		
		String operand = null;
		String leftOperand = null;
		String rightOperand = null;
		Constraint constraint = null;
		 if(exp instanceof Subtract){
			Expression left = ((Subtract) exp).getOperand().get(0);
			Expression right = ((Subtract) exp).getOperand().get(1);
			statement.setAddQdmFunction(false);
			leftOperand = ExpressionEvaluators.expressionEvaluator(left,statement, model);
			rightOperand = ExpressionEvaluators.expressionEvaluator(right,statement, model);
			statement.getQdmFunctionVariable().append("Before");
			statement.setMiddleqdmFunctionVariable("OrConcurrentWith");
			statement.setArithmeticOperand("Subtract");
			statement.setArithmeticLeftOperand(leftOperand);
			statement.setArithmeticRightOperand(rightOperand);
			operand = leftOperand +"-"+rightOperand;
		}
		else if(exp instanceof Add){
			Expression left = ((Add) exp).getOperand().get(0);
			Expression right = ((Add) exp).getOperand().get(1);
//			statement.setAddQdmFunction(false);
			statement.getQdmFunctionVariable().append("After");
			leftOperand = ExpressionEvaluators.expressionEvaluator(left,statement, model);
			rightOperand = ExpressionEvaluators.expressionEvaluator(right,statement, model);
			statement.setArithmeticOperand("Add");
			statement.setMiddleqdmFunctionVariable("OrConcurrentWith");
			statement.setArithmeticLeftOperand(leftOperand);
			statement.setArithmeticRightOperand(rightOperand);
			operand = leftOperand +"+"+rightOperand;
		}
		else if(exp instanceof Interval){
			Interval interval = new Interval();
			Expression left = ((Interval) exp).getLow();
			Expression right = ((Interval) exp).getHigh();
			statement.setIntervalLowOperation(true);
			leftOperand = ExpressionEvaluators.expressionEvaluator(left,statement, model);
			/*if(statement.getMiddleqdmFunctionVariable()!=null && !statement.getMiddleqdmFunctionVariable().isEmpty())
			{
				statement.getQdmFunctionVariable().append(statement.getMiddleqdmFunctionVariable());
			}*/
			statement.setIntervalHighOperation(true);
			rightOperand = ExpressionEvaluators.expressionEvaluator(right,statement, model);
			operand = "Interval("+leftOperand +", "+rightOperand +")";
			interval.setLowValue(leftOperand);
			interval.setLowClosed(((Interval) exp).isLowClosed());
			interval.setHighValue(rightOperand);
			interval.setHighClosed(((Interval) exp).isHighClosed());
			statement.setIntervalOperator(interval);
		}
		else if(exp instanceof Count){
			Expression expression  = ((Count) exp).getSource();
			leftOperand = ExpressionEvaluators.expressionEvaluator(expression, statement, model);
			operand = "Count("+leftOperand + ")";
		}
		else if(exp instanceof InValueSet){
			constraint = CQLHelper.getConstraintTemplate(statement, "InValueSet");
			String valueSetName =(((InValueSet) exp).getValueset().getName());
			leftOperand = model.getValueSets().get(valueSetName);
			constraint.setLeftOperand(leftOperand);
			if(statement.getConstraintList().isEmpty()){
				statement.setConstraintList(new CopyOnWriteArrayList<Constraint>());
				statement.getConstraintList().add(constraint);
				if(model.getExpressions().containsKey((statement.getDependentObjects().isEmpty())?"":statement.getDependentObjects().get(0)))
				{
					model.getExpressions().remove(statement.getDependentObjects().get(0));
				}
				model.getExpressions().remove(statement.getName());
				statement.setName(statement.getName()+"_"+valueSetName);
				model.getExpressions().put(statement.getName(), statement);
				for(Entry<String, Object> singlequeryVariable:model.getExpressions().entrySet())
				{
					if(singlequeryVariable.getKey().contains(valueSetName))
					{
						if((!((QueryVariable)singlequeryVariable.getValue()).getDependentObjects().isEmpty())
								&& (((QueryVariable)singlequeryVariable.getValue()).getDependentObjects()).contains(statement.getName()))
						{
							((QueryVariable)singlequeryVariable.getValue()).getDependentObjects().add(statement.getName());
						}
					}
				}
			}
			else
			{
				List<Constraint> constraintList = statement.getConstraintList();
				if(statement.getDependentObjects().size()>0)
				{
					for(String singleQueryVar :  (statement.getDependentObjects()))
					{
						if(model.getExpressions().containsKey(singleQueryVar))
						{
							QueryVariable dependentQueryVar = (QueryVariable) model.getExpressions().get(singleQueryVar);
							dependentQueryVar.getConstraintList().add(constraint);
							model.getExpressions().remove(dependentQueryVar.getName());
							statement.getDependentObjects().remove(dependentQueryVar.getName());
							for (Constraint singleConstraint : constraintList)
							{
								if(singleConstraint.getLeftOperand().equalsIgnoreCase(dependentQueryVar.getName()))
								{
									singleConstraint.setLeftOperand(dependentQueryVar.getName()+"_"+valueSetName);
								}
								else if (singleConstraint.getRightOperand().equalsIgnoreCase(dependentQueryVar.getName()))
								{
									singleConstraint.setRightOperand(dependentQueryVar.getName()+"_"+valueSetName);
								}
							}
								dependentQueryVar.setName(dependentQueryVar.getName()+"_"+valueSetName);
								model.getExpressions().put(dependentQueryVar.getName(), dependentQueryVar);
								statement.getDependentObjects().add(dependentQueryVar.getName());
						}
						else
						{
							statement.getConstraintList().add(constraint);
						}
					}
				}
				else
				{
					statement.getConstraintList().add(constraint);
				}
					model.getExpressions().remove(statement.getName());
					statement.setName(statement.getName()+"_"+valueSetName);
					model.getExpressions().put(statement.getName(), statement);
			}
			operand = constraint.getName();
		}
		
		else if(exp instanceof IncludedIn || exp instanceof Overlaps || exp instanceof OverlapsAfter || exp instanceof OverlapsBefore){
			String operator = exp.getClass().getSimpleName();
			operator = operator.substring(0, 1).toLowerCase() + operator.substring(1);
			constraint = CQLHelper.getConstraintTemplate(statement, operator);
			if(operator.startsWith("overlaps"))
			{
				if(statement.getQdmFunctionVariable()!=null && !statement.getQdmFunctionVariable().toString().isEmpty())
				{
					statement.getQdmFunctionVariable().append(","+operator);
				}
				else
				{
					statement.getQdmFunctionVariable().append(operator);
				}
			}
			else
			{
				statement.getQdmFunctionVariable().append("during");
			}
			List<Expression> expList =((BinaryExpression) exp).getOperand();
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			leftOperand = ExpressionEvaluators.expressionEvaluator(left,statement,model);
			rightOperand = ExpressionEvaluators.expressionEvaluator(right,statement,model);
			if (leftOperand == null)
			{
				leftOperand = evaluateTemporalFunction(left,statement);
			}
			else if (rightOperand == null)
			{
				rightOperand = evaluateTemporalFunction(right,statement);
			}
			if(null != leftOperand && null != rightOperand){
				if(rightOperand.contains("MP.")){
				 if(exp instanceof IncludedIn)
					 operator=operator +"MP";
				 
				 operator = operator +"("+leftOperand +")";
				}
				else
				{
					constraint.setLeftOperand(leftOperand);
					constraint.setRightOperand(rightOperand);
				}
			}
			if(null == statement.getConstraintList()){
				statement.setConstraintList(new CopyOnWriteArrayList<Constraint>());
			}
			statement.getConstraintList().add(constraint);
			operand = constraint.getName();
		}
		else if(exp instanceof Before){
			constraint = CQLHelper.getConstraintTemplate(statement);
			List<Expression> expList =((Before) exp).getOperand();
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			constraint.setOperator(left.getClass().getSimpleName().toLowerCase()+"sBefore"+right.getClass().getSimpleName());
			leftOperand = ExpressionEvaluators.expressionEvaluator(left,statement, model);
			statement.getQdmFunctionVariable().append("Before");
			rightOperand = ExpressionEvaluators.expressionEvaluator(right,statement, model);
			if(null != leftOperand && null != rightOperand){
				constraint.setLeftOperand(left.getClass().getSimpleName().toLowerCase()+"sBefore"+right.getClass().getSimpleName()+"("+leftOperand+", "+rightOperand +")");
			}
			if(null == statement.getConstraintList()){
				statement.setConstraintList(new CopyOnWriteArrayList<Constraint>());
			}
			statement.getConstraintList().add(constraint);
			operand = constraint.getName();
		}
		else if(exp instanceof After){
			constraint = CQLHelper.getConstraintTemplate(statement);
			List<Expression> expList =((After) exp).getOperand();
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			leftOperand = ExpressionEvaluators.expressionEvaluator(left,statement, model);
			if(left.getClass().getSimpleName().equals(Property.class.getSimpleName()) && leftOperand.contains("authorDatetime"))
			{
				constraint.setOperator("startsAfter"+right.getClass().getSimpleName());
			}
			else
			{
				constraint.setOperator(left.getClass().getSimpleName().toLowerCase()+"sAfter"+right.getClass().getSimpleName());
			}
				
			statement.getQdmFunctionVariable().append("After");
			rightOperand = ExpressionEvaluators.expressionEvaluator(right,statement, model);
			if(null != leftOperand && null != rightOperand){
				constraint.setLeftOperand(leftOperand);
				constraint.setRightOperand(rightOperand);
			}
			if(null == statement.getConstraintList()){
				statement.setConstraintList(new CopyOnWriteArrayList<Constraint>());
			}
			statement.getConstraintList().add(constraint);
			operand = constraint.getName();
		}
		
		else if(exp instanceof In){
			constraint = CQLHelper.getConstraintTemplate(statement);
			constraint.setOperator("In");
			List<Expression> expList =((In) exp).getOperand();
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			leftOperand = ExpressionEvaluators.expressionEvaluator(left,statement, model);
			if(leftOperand == null)
			{
				leftOperand = evaluateTemporalFunction(left,statement);
			}
			if(((In) exp).getPrecision()!=null)
			{
				statement.setMiddleqdmFunctionVariable(evaluateQdmFunction(((In) exp).getPrecision().toString()));
			}
			if(statement.getQdmFunctionVariable().toString().isEmpty())
			{
				statement.getQdmFunctionVariable().append("during");
			}
			rightOperand = ExpressionEvaluators.expressionEvaluator(right,statement, model);
			if(rightOperand == null)
			{
				rightOperand = evaluateTemporalFunction(right,statement);
			}
			if(null == statement.getConstraintList()){
				statement.setConstraintList(new CopyOnWriteArrayList<Constraint>());
			}
			if(null != leftOperand && null != rightOperand){
				constraint.setLeftOperand(leftOperand);
				constraint.setRightOperand(rightOperand);
				statement.getConstraintList().add(constraint);
			}
			operand = constraint.getName();
		}
		else if (exp instanceof DurationBetween)
		{
			constraint = CQLHelper.getConstraintTemplate(statement, "DurationBetween");
			List <Expression> expList =((DurationBetween) exp).getOperand();
			Expression left = expList.get(0);
			Expression right = expList.get(1);
			leftOperand = ExpressionEvaluators.expressionEvaluator(left,statement, model);
			if(leftOperand == null)
			{
				leftOperand = evaluateTemporalFunction(left,statement);
			}
			rightOperand = ExpressionEvaluators.expressionEvaluator(right,statement, model);
			if(rightOperand == null)
			{
				rightOperand = evaluateTemporalFunction(right,statement);
			}
			if(null != leftOperand && null != rightOperand){
				constraint.setLeftOperand(leftOperand);
				constraint.setRightOperand(rightOperand);
				statement.getConstraintList().add(constraint);
			}
			operand = constraint.getName();
		}
		
		return operand;
	}
	
	/*
	 * this method is used to evaluate Temporal Function
	 */
	
	public static String evaluateTemporalFunction(Expression exp,QueryVariable statement){
	    String operand = null;
		if(exp instanceof Property){
			operand =  ((Property)exp).getPath() ;
			operand = operand.replace("relevantPeriod", "period");
			if(((Property)exp).getScope()!= null)
			{
				operand = ((Property)exp).getScope()+"."+operand;//set scope.path
			}
			else if (((Property)exp).getSource()!= null)
			{
				operand = ((Property)exp).getSource()+"."+operand;
			}
			if(((Property) exp).getPath()!=null && ((Property) exp).getPath().equalsIgnoreCase("authorDatetime"))
			{
				statement.getQdmFunctionVariable().append("starts");
			}
			else if(((Property) exp).getPath()!=null && ((Property) exp).getPath().equalsIgnoreCase("expiredDatetime"))
			{
				statement.getQdmFunctionVariable().append("ends");
			}
		}
		else if (exp instanceof ParameterRef){
			operand = ((ParameterRef) exp).getName();
			operand = operand.replace("Measurement Period", "$audit.measurePeriod");
		}
		return operand;
	}

	/*
	 * this method is used to evaluate Quantity in operator
	 */
	
	public static String evaluateQuantity(Expression exp, QueryVariable queryVariable){
	    String quanity = null;
		String unit = ((Quantity) exp).getUnit();
		BigDecimal value = ((Quantity) exp).getValue();
		if(null != unit && null != value)
			queryVariable.setQdmPeriodVariable(unit.toUpperCase());
		quanity = value.toString();
		return quanity;
	}

	/*
	 * this method is used to evaluate QdmFunction
	 */
	public static String evaluateQdmFunction(String precision)
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
}
