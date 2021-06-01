package com.ct.cql.evaluators;

import java.util.List;

import com.ct.cql.elm.model.BinaryExpression;
import com.ct.cql.elm.model.CodeRef;
import com.ct.cql.elm.model.Exists;
import com.ct.cql.elm.model.Expression;
import com.ct.cql.elm.model.First;
import com.ct.cql.elm.model.Last;
import com.ct.cql.elm.model.Property;
import com.ct.cql.elm.model.Query;
import com.ct.cql.elm.model.QueryLetRef;
import com.ct.cql.elm.model.SingletonFrom;
import com.ct.cql.elm.model.UnaryExpression;
import com.ct.cql.entity.CQLModel;
import com.ct.cql.entity.QueryVariable;

public class ExpressionEvaluators {
 
	/*
	 * This method is used to evaluate all the binary operators in recession style 
	 * input are 1.expression to evaluate whether its unary or binary or base condition or others 
	 * 2. Query variable on which operation is getting performed
	 * 3. CqlModel
	 */
	
	public static String expressionEvaluator(Expression exp, QueryVariable statement,CQLModel model){
		boolean isBinary = false;
		boolean isUnary = false;
		String queryVariable = null;
		int conditionNumber ;
		
		if(statement.getConditionNumber() == 0)
		{
			statement.setConditionNumber(model.getConditionNumber());
			conditionNumber = model.getConditionNumber();
			model.setConditionNumber(++conditionNumber);
		}
		
		queryVariable = BaseEvaluators.evaluateBaseCondition(exp,statement, model);
		if(null == queryVariable){
			isBinary = exp instanceof BinaryExpression;//
			isUnary = UnaryExpression.class.isInstance(exp);//anotherWay
			/*Type casting Acc to identified tYPE*/
			if(isBinary){
				List <Expression> expList =((BinaryExpression) exp).getOperand();
				String operatorName = exp.getClass().getSimpleName();
				queryVariable = Operators.evaluateBinaryOperator(expList,statement,operatorName, model);
			} else if(isUnary){
				 Expression expression =((UnaryExpression) exp).getOperand();
				 if(exp instanceof Exists){
					queryVariable = Operators.evaluateExists(expression,statement, model);
					
				}else if (exp instanceof SingletonFrom){
					queryVariable = expressionEvaluator(expression,statement, model);
				}
				
			}else if(exp instanceof Query){
		    	 	queryVariable = Operators.evaluateQueryOperator((Query)exp,statement, model);
			}
			else if (exp instanceof Property)
			{
				Expression source =((Property) exp).getSource();
				String path =((Property) exp).getPath();
				String scope =((Property) exp).getScope();
				if(path!=null && path.equalsIgnoreCase("authorDatetime"))
				{					
					if(statement.getQdmFunctionVariable().toString().isEmpty())
					{
						statement.getQdmFunctionVariable().append("starts");
					}
					else
					{
						statement.getQdmFunctionVariable().append("Start");
					}
					
				}
				else if (path!=null && path.equalsIgnoreCase("expiredDatetime"))
				{					
					if(statement.getQdmFunctionVariable().toString().isEmpty())
					{
						statement.getQdmFunctionVariable().append("ends");
					}
					else
					{
						statement.getQdmFunctionVariable().append("End");
					}
					
				}
				if(path!=null && source!=null)
				{
					queryVariable = expressionEvaluator(source,statement, model);
					queryVariable = queryVariable+"."+path;
				}
				else if (path!=null && scope!=null)
				{
						queryVariable = scope+"."+path;
				}
				else
				{
					queryVariable = scope;
				}
			}
			else if (exp instanceof Last){
				statement.setLastOperator(true);
				queryVariable = expressionEvaluator(((Last) exp).getSource(),statement,model);
			}
			else if (exp instanceof QueryLetRef)
			{
				queryVariable = ((QueryLetRef) exp).getName();
			}
			else if(exp instanceof CodeRef)
			{
				String name = ((CodeRef) exp).getName();
				queryVariable = name;
			}
			else if (exp instanceof First){
				statement.setFirstOperator(true);
				queryVariable = expressionEvaluator(((First) exp).getSource(),statement,model);
			}
		}
		return queryVariable;
	}

}

