
package com.ct.cql.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ct.cql.elm.model.Interval;


public class QueryVariable extends InstanceInfo implements Comparator<QueryVariable>{

	private List<String> valueSetList = new ArrayList<>();
	
	private List<String> dataType;

	@JsonIgnore
	private List<QueryVariable> queryVariable; 
	
	private CopyOnWriteArrayList<Constraint> constraintList;
	
	private String dataSet;
	
	private int conditionNumber;
	
	private String operator;

	private Interval intervalOperator;
	
	private String qdmPeriodVariable = "";

	private StringBuilder qdmFunctionVariable;

	private String middleqdmFunctionVariable;

	private boolean createQueryVariable;
	
	private boolean addQdmFunction;

	private String arithmeticOperand;
	
	private String arithmeticLeftOperand;
	
	private String arithmeticRightOperand;
	
	private boolean isLastOperator;
	
	private List<String> codeSetList = new ArrayList<>();
	
	private boolean binaryOperator;
	
	private int parentConditionNumber;
	
	private List<Integer> childConditionNumber = new ArrayList<>();

	private int parentDependentConditionNumber;
	
	private boolean expressionResolved;
	
	private List<Integer> occuranceQueryVariable = new ArrayList<Integer>();
	
	private int equalsOperatorClosed;
	
	private int occuranceQueryVariableCounter = 0;
	
	private Map<String, String> valueSetDataTypeMap = new LinkedHashMap<>();
	
	private boolean encounterQueryVariable;
	
	private int whereConditionEvaluated = 0;

	private boolean intervalLowOperation = false;

	private boolean intervalHighOperation = false;
	
	private String intervalLowPeriodOperator;
	
	private String intervalHighPeriodOperator;
	
	private boolean createAndOperator;
	
	private boolean withExpressionRef;

	private boolean whereExpression;
	
	private QueryVariable childQueryVariable;
	
	private boolean isFirstOperator;
	
	private boolean furtherDepedent = false;
	
	private boolean parentBinary = false;
	
	public boolean isParentBinary() {
		return parentBinary;
	}

	public void setParentBinary(boolean parentBinary) {
		this.parentBinary = parentBinary;
	}

	public boolean isFurtherDepedent() {
		return furtherDepedent;
	}

	public void setFurtherDepedent(boolean furtherDepedent) {
		this.furtherDepedent = furtherDepedent;
	}

	public boolean isFirstOperator() {
		return isFirstOperator;
	}

	public void setFirstOperator(boolean isFirstOperator) {
		this.isFirstOperator = isFirstOperator;
	}

	public QueryVariable getChildQueryVariable() {
		return childQueryVariable;
	}

	public void setChildQueryVariable(QueryVariable childQueryVariable) {
		this.childQueryVariable = childQueryVariable;
	}

	public boolean isWhereExpression() {
		return whereExpression;
	}

	public void setWhereExpression(boolean whereExpression) {
		this.whereExpression = whereExpression;
	}

	public boolean isWithExpressionRef() {
		return withExpressionRef;
	}

	public void setWithExpressionRef(boolean withExpressionRef) {
		this.withExpressionRef = withExpressionRef;
	}

	public boolean isCreateAndOperator() {
		return createAndOperator;
	}

	public void setCreateAndOperator(boolean createAndOperator) {
		this.createAndOperator = createAndOperator;
	}

	public boolean isIntervalLowOperation() {
		return intervalLowOperation;
	}

	public void setIntervalLowOperation(boolean intervalLowOperation) {
		this.intervalLowOperation = intervalLowOperation;
	}

	public boolean isIntervalHighOperation() {
		return intervalHighOperation;
	}

	public void setIntervalHighOperation(boolean intervalHighOperation) {
		this.intervalHighOperation = intervalHighOperation;
	}

	public String getIntervalLowPeriodOperator() {
		return intervalLowPeriodOperator;
	}

	public void setIntervalLowPeriodOperator(String intervalLowPeriodOperator) {
		this.intervalLowPeriodOperator = intervalLowPeriodOperator;
	}

	public String getIntervalHighPeriodOperator() {
		return intervalHighPeriodOperator;
	}

	public void setIntervalHighPeriodOperator(String intervalHighPeriodOperator) {
		this.intervalHighPeriodOperator = intervalHighPeriodOperator;
	}
	
	public int getWhereConditionEvaluated() {
		return whereConditionEvaluated;
	}

	public void setWhereConditionEvaluated(int whereConditionEvaluated) {
		this.whereConditionEvaluated = whereConditionEvaluated;
	}

	public boolean isEncounterQueryVariable() {
		return encounterQueryVariable;
	}

	public void setEncounterQueryVariable(boolean encounterQueryVariable) {
		this.encounterQueryVariable = encounterQueryVariable;
	}

	public Map<String, String> getValueSetDataTypeMap() {
		return valueSetDataTypeMap;
	}

	public void setValueSetDataTypeMap(Map<String, String> valueSetDataTypeMap) {
		this.valueSetDataTypeMap = valueSetDataTypeMap;
	}

	public int getOccuranceQueryVariableCounter() {
		return occuranceQueryVariableCounter;
	}

	public void setOccuranceQueryVariableCounter(int occuranceQueryVariableCounter) {
		this.occuranceQueryVariableCounter = occuranceQueryVariableCounter;
	}

	public List<Integer> getOccuranceQueryVariable() {
		return occuranceQueryVariable;
	}

	public void setOccuranceQueryVariable(List<Integer> occuranceQueryVariable) {
		this.occuranceQueryVariable = occuranceQueryVariable;
	}

	public int getEqualsOperatorClosed() {
		return equalsOperatorClosed;
	}

	public void setEqualsOperatorClosed(int equalsOperatorClosed) {
		this.equalsOperatorClosed = equalsOperatorClosed;
	}

	public boolean isExpressionResolved() {
		return expressionResolved;
	}

	public void setExpressionResolved(boolean expressionResolved) {
		this.expressionResolved = expressionResolved;
	}

	public int getParentDependentConditionNumber() {
		return parentDependentConditionNumber;
	}

	public void setParentDependentConditionNumber(int parentDependentConditionNumber) {
		this.parentDependentConditionNumber = parentDependentConditionNumber;
	}

	public List<Integer> getChildConditionNumber() {
		return childConditionNumber;
	}

	public void setChildConditionNumber(List<Integer> childConditionNumber) {
		this.childConditionNumber = childConditionNumber;
	}

	public int getParentConditionNumber() {
		return parentConditionNumber;
	}

	public void setParentConditionNumber(int parentConditionNumber) {
		this.parentConditionNumber = parentConditionNumber;
	}

	public boolean isBinaryOperator() {
		return binaryOperator;
	}

	public void setBinaryOperator(boolean binaryOperator) {
		this.binaryOperator = binaryOperator;
	}

	public List<String> getCodeSetList() {
		return codeSetList;
	}

	public void setCodeSetList(List<String> codeSetList) {
		this.codeSetList = codeSetList;
	}

	public boolean isLastOperator() {
		return isLastOperator;
	}

	public void setLastOperator(boolean isLastOperator) {
		this.isLastOperator = isLastOperator;
	}

	public String getArithmeticOperand() {
		return arithmeticOperand;
	}

	public void setArithmeticOperand(String arithmeticOperand) {
		this.arithmeticOperand = arithmeticOperand;
	}
	
	public String getArithmeticLeftOperand() {
		return arithmeticLeftOperand;
	}

	public void setArithmeticLeftOperand(String arithmeticLeftOperand) {
		this.arithmeticLeftOperand = arithmeticLeftOperand;
	}

	public String getArithmeticRightOperand() {
		return arithmeticRightOperand;
	}

	public void setArithmeticRightOperand(String arithmeticRightOperand) {
		this.arithmeticRightOperand = arithmeticRightOperand;
	}
	
	public boolean isCreateQueryVariable() {
		return createQueryVariable;
	}

	public void setCreateQueryVariable(boolean createQueryVariable) {
		this.createQueryVariable = createQueryVariable;
	}

	public boolean isAddQdmFunction() {
		return addQdmFunction;
	}

	public void setAddQdmFunction(boolean addQdmFunction) {
		this.addQdmFunction = addQdmFunction;
	}
	public String getMiddleqdmFunctionVariable() {
		return middleqdmFunctionVariable;
	}

	public void setMiddleqdmFunctionVariable(String middleqdmFunctionVariable) {
		this.middleqdmFunctionVariable = middleqdmFunctionVariable;
	}

	public StringBuilder getQdmFunctionVariable() {
		return qdmFunctionVariable;
	}

	public void setQdmFunctionVariable(StringBuilder qdmFunctionVariable) {
		this.qdmFunctionVariable = qdmFunctionVariable;
	}

	public String getQdmPeriodVariable() {
		return qdmPeriodVariable;
	}

	public void setQdmPeriodVariable(String qdmPeriodVariable) {
		this.qdmPeriodVariable = qdmPeriodVariable;
	}

	public Interval getIntervalOperator() {
		return intervalOperator;
	}

	public void setIntervalOperator(Interval intervalOperator) {
		this.intervalOperator = intervalOperator;
	}

	public int getConditionNumber() {
		return conditionNumber;
	}

	public void setConditionNumber(int conditionNumber) {
		this.conditionNumber = conditionNumber;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public QueryVariable(){
		super();
		this.constraintList = new CopyOnWriteArrayList<Constraint>();
		this.queryVariable = new ArrayList<QueryVariable>();
	}
	
	public List<QueryVariable> getQueryVariable() {
		return queryVariable;
	}

	public void setQueryVariable(List<QueryVariable> queryVariable) {
		this.queryVariable = queryVariable;
	}

	public CopyOnWriteArrayList<Constraint> getConstraintList() {
		return constraintList;
	}

	public void setConstraintList(CopyOnWriteArrayList<Constraint> constraintList) {
		this.constraintList = constraintList;
	}

	/**
	 * @return the dataType
	 */
	public List<String> getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(List<String> dataType) {
		this.dataType = dataType;
	}

	public List<String> getValueSetList() {
		return valueSetList;
	}

	public void setValueSetList(List<String> valueSetList) {
		this.valueSetList = valueSetList;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "QueryVariable [getName()=" + getName() 
				+ ", conditionNumber()=" + getConditionNumber()
				+ ", getDependentObjects()=" + getDependentObjects()
				+ ", isResolved()=" + isResolved() 
				+ ", dataType="+ dataType + ", constraintList=" + constraintList
				+",childQueryVariable="+childQueryVariable+", occuranceQueryVariable = "+occuranceQueryVariable
				+",occuranceQueryVariableCounter="+occuranceQueryVariableCounter
				+",qdmPeriodVariable="+qdmPeriodVariable
				+",qdmFunctionVariable="+qdmFunctionVariable
				+",arithmeticOperand = "+arithmeticOperand
				+",arithmeticLeftOperand="+arithmeticLeftOperand
				+",arithmeticRightOperand="+arithmeticRightOperand
				+",isfurtherDepedent="+furtherDepedent
				+",parentConditionNumber = "+parentConditionNumber
				+",childConditionNumber = "+childConditionNumber
				+",parentDependentConditionNumber = "+parentDependentConditionNumber
				+", equalsOperatorClosed = "+equalsOperatorClosed
				+", intervalOperator = "+intervalOperator
				+", valueSetDataTypeMap = "+valueSetDataTypeMap
				+", intervalLowOperation = "+intervalLowOperation
				+", intervalHighOperation = "+intervalHighOperation
				+", intervalLowPeriodOperator = "+intervalLowPeriodOperator
				+", intervalHighPeriodOperator = "+intervalHighPeriodOperator
				+", encounterQueryVariable = "+encounterQueryVariable
				+", isLastOperator = "+isLastOperator
				+ ", dataSet=" + dataSet + "]";
	}

@Override
	public int compare(QueryVariable arg0, QueryVariable arg1) {
		if(arg0.getConditionNumber() > arg1.getConditionNumber()){
            return 1;
        } else {
            return -1;
        }
	}
}
