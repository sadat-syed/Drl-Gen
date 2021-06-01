package com.ct.cql.entity;

import java.util.List;

import com.ct.cql.elm.model.Interval;

public class Constraint extends InstanceInfo{

	private String leftOperand;

	private String rightOperand;
	
	private String operator;
	
	private List<String> dependentQueryVariable;
	
	private boolean isFromClause;

	private List<String> codeSetList;
	
	private String dataType;
	
	private int occuranceQueryVariable ;
	
	private int occuranceQueryVariableCounter;
	
	private String arithmeticOperand;
	
	private String arithmeticLeftOperand;
	
	private String arithmeticRightOperand;
	
	private Interval intervalOperator;
	
	private boolean operandExpressionRef = false;
	
	public boolean isOperandExpressionRef() {
		return operandExpressionRef;
	}

	public void setOperandExpressionRef(boolean operandExpressionRef) {
		this.operandExpressionRef = operandExpressionRef;
	}
	
	public Interval getIntervalOperator() {
		return intervalOperator;
	}

	public void setIntervalOperator(Interval intervalOperator) {
		this.intervalOperator = intervalOperator;
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

	public int getOccuranceQueryVariableCounter() {
		return occuranceQueryVariableCounter;
	}

	public void setOccuranceQueryVariableCounter(int occuranceQueryVariableCounter) {
		this.occuranceQueryVariableCounter = occuranceQueryVariableCounter;
	}
	
	public int getOccuranceQueryVariable() {
		return occuranceQueryVariable;
	}

	public void setOccuranceQueryVariable(int occuranceQueryVariable) {
		this.occuranceQueryVariable = occuranceQueryVariable;
	}
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public List<String> getCodeSetList() {
		return codeSetList;
	}

	public void setCodeSetList(List<String> codeSetList) {
		this.codeSetList = codeSetList;
	}

	/**
	 * @return the leftOperand
	 */
	public String getLeftOperand() {
		return leftOperand;
	}

	public List<String> getDependentQueryVariable() {
		return dependentQueryVariable;
	}

	public void setDependentQueryVariable(List<String> dependentQueryVariable) {
		this.dependentQueryVariable = dependentQueryVariable;
	}

	/**
	 * @param leftOperand the leftOperand to set
	 */
	public void setLeftOperand(String leftOperand) {
		this.leftOperand = leftOperand;
	}

	/**
	 * @return the rightOperand
	 */
	public String getRightOperand() {
		return rightOperand;
	}

	/**
	 * @param rightOperand the rightOperand to set
	 */
	public void setRightOperand(String rightOperand) {
		this.rightOperand = rightOperand;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	public boolean isFromClause() {
		return isFromClause;
	}

	public void setFromClause(boolean isFromClause) {
		this.isFromClause = isFromClause;
	}

	@Override
	public String toString() {
		return "Constraint [leftOperand=" + leftOperand + ", rightOperand="
				+ rightOperand + ", operator=" + operator + ", codeSetList="+codeSetList + ", dataType = "+dataType+
				", occuranceQueryVariableCounter = "+occuranceQueryVariableCounter+
				", arithmeticLeftOperand = "+arithmeticLeftOperand+
				", arithmeticRightOperand = "+arithmeticRightOperand
				+ ", dependentQueryVariable=" + dependentQueryVariable+", occuranceQueryVariable = "+occuranceQueryVariable
				+ ", isFromClause=" + isFromClause + ", isResolved()="
				+ isResolved() + ", getName()=" + getName()+"]";
	}

	


}
