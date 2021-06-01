package com.ct.cql.sqlgen;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.commons.collections4.CollectionUtils;

import com.ct.cql.entity.Constraint;
import com.ct.cql.entity.ELMDataType;
import com.ct.cql.entity.FunctionsEnum;
import com.ct.cql.entity.InstanceInfo;
import com.ct.cql.entity.QueryVariable;

public class SqlGeneratorUtility {
/*	
	
	public static void  queryFactory(QueryVariable query, Map<String,Object> tocMap, Map<String,String> queryMap, String dataset){
			
			String queryname = query.getName();
			InstanceInfo obj = (InstanceInfo) tocMap.get(queryname);
			// check is query is already resolved no action required	
			   if(obj.isResolved()){
				   return;
			   }else{
				   
				   
				   setDataSet(query, tocMap, dataset);
				   
				   // if its dependent queries are present first resolve those one by one
				   if(CollectionUtils.isNotEmpty(query.getQueryVariable())){
					   
					   for(QueryVariable dependentQuery : query.getQueryVariable()){
						   
						   QueryVariable subquery = (QueryVariable) tocMap.get(dependentQuery.getName());
						   String subqueryDataSet = subquery.getDataSet();
						   if(subqueryDataSet == null)
							   subqueryDataSet = dataset;
						   queryFactory(dependentQuery,tocMap, queryMap, subqueryDataSet);
					   }
					   
				   }
				  
				   baseQueryVariableGenerator(query,tocMap, queryMap); 
			    }
	   
	}
	
	
	public static void  baseQueryGenerator(QueryVariable query, Map<String,Object> tocMap, Map<String,String> queryMap){
		
		if(query instanceof Statement){
			baseStatementGenerator((Statement) query, tocMap, queryMap);
			
		}
		if(query instanceof QueryVariable){
			baseQueryVariableGenerator((QueryVariable) query, tocMap, queryMap);
		}
		
		System.out.println("resolved the query for - "+ query.getName());
		query.setResolved(true);
		tocMap.put(query.getName(), query);
		
	}
	
	
	public static void  baseStatementGenerator(Statement query, Map<String,Object> tocMap, Map<String,String> queryMap){
		
		String sqlFromClause = "";
		PriorityQueue<String> sqlWhereClause = new PriorityQueue<String>() ;
		String finalQuery = "";
		
		if(CollectionUtils.isNotEmpty(query.getConstraintList())){
			
			for(Constraint constraint : query.getConstraintList()){
				sqlFromClause = evaluateOperatorsToQuery(query, constraint, sqlFromClause, sqlWhereClause,tocMap,queryMap);
				}
			
		}
		
		if(CollectionUtils.isEmpty(sqlWhereClause)){
			if(sqlFromClause.isEmpty() && query.getQueryVariable().size() == 1){
				finalQuery = "Select patientId, qdmElementId from "+ query.getQueryVariable().get(0).getName();
			}
			
		}else{
			
			if(sqlFromClause.isEmpty()){
				sqlFromClause =  "Select patientId, qdmElementId from QdmElements";			
			}
			
			String finalWhere =" where " + sqlWhereClause.remove();
			finalQuery = sqlFromClause + finalWhere;
		}
		
	//	System.out.println(finalQuery);
		queryMap.put(query.getName(), finalQuery);
		
	}
	
	public static void  baseQueryVariableGenerator(QueryVariable query, Map<String,Object> tocMap,  Map<String,String> queryMap){
		

		
		
		String sqlFromClause = "";
		String finalWhere = "";
		String finalQuery = "";
		
		String innerJoin = "";
		if(query.getDataSet() != null && !query.getDataSet().equals("QdmElements"))
			innerJoin = " QdmElements AS QD Inner Join "+ query.getDataSet() +" ON  QD.patientId = "+query.getDataSet()+".patientId";
		else
			innerJoin = " QdmElements AS QD";
		
		PriorityQueue<String> sqlWhereClause = new PriorityQueue<String>() ;
		
		if(CollectionUtils.isNotEmpty(query.getConstraintList())){
			for(Constraint constraint : query.getConstraintList()){
				sqlFromClause = evaluateOperatorsToQuery(query, constraint, sqlFromClause, sqlWhereClause,tocMap,queryMap);
			}
		}
		// if query does not have any where clause and sqlFromclause is empty with only one depended query
		if(CollectionUtils.isEmpty(sqlWhereClause)){
			if(sqlFromClause.isEmpty() && CollectionUtils.isNotEmpty(query.getQueryVariable()) && query.getQueryVariable().size() == 1){
				sqlFromClause = "Select patientId, qdmElementId  from "+ query.getQueryVariable().get(0).getName();
			}
			
		}
			
		if(sqlFromClause.isEmpty()){
				sqlFromClause =  "Select QD.patientId, QD.qdmElementId from "+innerJoin;			
		}
		
		
		
		if(query.getDataType() != null){
			if(CollectionUtils.isNotEmpty(sqlWhereClause)){
				String where1 = sqlWhereClause.remove();
				sqlWhereClause.add(where1 + " and "+ "dataType = '"+query.getDataType()+"'"); 	
			}else{
				sqlWhereClause.add("dataType = '"+query.getDataType()+"'");
			}
			
			if(query.getDataType().equals(ELMDataType.ENCOUNTER_PERFORMED.name())){
				String where1 = sqlWhereClause.remove();
				sqlWhereClause.add(where1 + " and "+ "array_contains(sourceList, $providerId)");
			}
		
		}
		
			
		if(query.getValueSetList() != null){
			if(CollectionUtils.isNotEmpty(sqlWhereClause)){
				String where1 = sqlWhereClause.remove();
				sqlWhereClause.add(where1 + " and "+ "array_contains(VALUE_SET_OID ,'"+query.getValueSetList()+"')"); 	
			}else{
				sqlWhereClause.add("dataType = '"+query.getDataType()+"'");
			}
		
		}
		
		if(CollectionUtils.isNotEmpty(sqlWhereClause)){
			finalWhere = " where "+ sqlWhereClause.remove();
		}
		 
		finalQuery = sqlFromClause + finalWhere;
	//	System.out.println(finalQuery);
		queryMap.put(query.getName(), finalQuery);
		
	//	System.out.println("resolved the query for - "+ query.getName());
		query.setResolved(true);
		tocMap.put(query.getName(), query);
		
	}
	
	
	public static String  unionQueryGenerator(String leftOperand, String rightOperand, String operator, String sqlquery, Map<String,Object> tocMap, Map<String,String> queryMap){
	
		
		
		boolean isleftTempTable = istempTable(leftOperand, tocMap);
		boolean isRightTempTable = istempTable(rightOperand, tocMap);
		// this is first time select query is formed
		if(sqlquery.isEmpty()){
			
			if(isleftTempTable && isRightTempTable){
				sqlquery = "((Select patientId, qdmElementId from "+ leftOperand + ")"+ " "+operator+" "+"(Select patientId, qdmElementId from "+ rightOperand + "))";
			}
			else if(isleftTempTable){
				sqlquery = "Select patientId, qdmElementId from "+ leftOperand;
			}
			else if(isRightTempTable)
				sqlquery = "Select patientId, qdmElementId from "+ rightOperand;
		}
		// generation of select query is already done
		else{
			
			if(isleftTempTable  && !isRightTempTable)
				sqlquery=  "((Select patientId, qdmElementId from "+ leftOperand + ")"+ " "+operator+" " + sqlquery + ")";
			if(!isleftTempTable && isRightTempTable)
				sqlquery=  "("+sqlquery+ " "+operator+" " +"(Select patientId, qdmElementId from "+ rightOperand + "))";
			
		}
		
			return sqlquery;
	}
	
//handle 	
public static String  andQueryGenerator(String leftOperand, String rightOperand,List<Constraint> constrains ,String sqlquery, Map<String,Object> tocMap){
	
		
		
		boolean isleftTempTable = istempTable(leftOperand, tocMap);
		boolean isRightTempTable = istempTable(rightOperand, tocMap);
		// this is first time select query is formed
		if(sqlquery.isEmpty()){
			// this means both left and right are resolved and dataset was imposed on right table
			if(isleftTempTable && isRightTempTable){
				sqlquery = "Select patientId, qdmElementId from "+ rightOperand;;
			}
			else if(isleftTempTable){
				List<String> dependentrightChilds = getRightQuery(leftOperand, constrains , tocMap);
				if(CollectionUtils.isNotEmpty(dependentrightChilds)){
					for(String child : dependentrightChilds){
						sqlquery = "Select patientId, qdmElementId from "+ child+ " Union";
					}
					int index = sqlquery.lastIndexOf(" Union");
					sqlquery = sqlquery.substring(0, index);
					
				}
			}
			else if(isRightTempTable){
				
				//todo create getLeftQuery method to replicate the above code
			}
				
		}
		// this case is assumed to be never occuring
		else{
			
			if(isleftTempTable  && !isRightTempTable)
				sqlquery=  "((Select patientId, qdmElementId from "+ leftOperand + ")"+ " "+operator+" " + sqlquery + ")";
			if(!isleftTempTable && isRightTempTable)
				sqlquery=  "("+sqlquery+ " "+operator+" " +"(Select patientId, qdmElementId from "+ rightOperand + "))";
			
		
	//	System.out.println("code is required for else");	
		}
		
			return sqlquery;
	}
	
	
	
	public static boolean istempTable(String queryName, Map<String,Object> tocMap){
		
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
	
	
	public static String evaluateOperatorsToQuery(QueryVariable query,Constraint constrain, String sqlFromClause, PriorityQueue<String> sqlWhereClause, Map<String,Object> tocMap, Map<String,String> queryMap){

		
		String leftOperand = constrain.getLeftOperand();
		String rightOperand = constrain.getRightOperand();
		String operator = constrain.getOperator();
		
		
		//resolve left and right operand incase it is Temp Query
		if(leftOperand != null && istempTable(leftOperand, tocMap) ){
			QueryVariable left = (QueryVariable) tocMap.get(leftOperand);
			if(left != null && !left.isResolved()){
				
				if(istempTable(left.getDataSet(), tocMap)){
					
					QueryVariable depedentQuery = (QueryVariable) tocMap.get(left.getDataSet());
					queryFactory(depedentQuery, tocMap, queryMap, depedentQuery.getDataSet());
					
				}
				
				queryFactory(left, tocMap, queryMap, left.getDataSet());
				
			}
			
		}
		
		if(rightOperand != null  && istempTable(rightOperand, tocMap)){
			QueryVariable right = (QueryVariable) tocMap.get(rightOperand);
			if(right != null && !right.isResolved()){
				
				if(istempTable(right.getDataSet(), tocMap)){
					
					QueryVariable depedentQuery = (QueryVariable) tocMap.get(right.getDataSet());
					queryFactory(depedentQuery, tocMap, queryMap, depedentQuery.getDataSet());
					
				}
				
				queryFactory(right, tocMap, queryMap, right.getDataSet());
				
			}
		}
		
		if(operator.equalsIgnoreCase("GreaterOrEqual")){
			sqlFromClause = getFromClauseTemplate(sqlFromClause, query, tocMap);
			if(!leftOperand.equals(FunctionsEnum.CALCULATE_AGE_AT.getValue()))
				sqlFromClause = "Select patientId, qdmElementId from "+ sqlFromClause + " AS " + query.getName();
			else
				query.setDataType("PATIENT_CHARACTERISTIC_BIRTH_DATE");
			sqlWhereClause.add(leftOperand + " >= " + rightOperand);
		}
		else if(operator.equalsIgnoreCase("Less")){
			sqlFromClause = getFromClauseTemplate(sqlFromClause, query, tocMap);
			if(!leftOperand.equals(FunctionsEnum.CALCULATE_AGE_AT.getValue()))
				sqlFromClause = "Select patientId, qdmElementId from "+ sqlFromClause + " AS " + query.getName();
			else
				query.setDataType("PATIENT_CHARACTERISTIC_BIRTH_DATE");
			sqlWhereClause.add(leftOperand + " < " + rightOperand);
		}
		else if(operator.equalsIgnoreCase("Equal")){
			sqlFromClause = getFromClauseTemplate(sqlFromClause, query, tocMap);
			if(!leftOperand.equals(FunctionsEnum.CALCULATE_AGE_AT.getValue()))
				sqlFromClause = "Select patientId, qdmElementId from "+ sqlFromClause + " AS " + query.getName();
			
			sqlWhereClause.add(leftOperand + " = " + rightOperand);
		}
		else if(operator.equalsIgnoreCase("IncludedIn")){
			sqlFromClause = getFromClauseTemplate(sqlFromClause, query, tocMap);
			if(!leftOperand.equals(FunctionsEnum.CALCULATE_AGE_AT.getValue())){
				if(!sqlFromClause.contains("AS "+ query.getName())){
					sqlFromClause = "Select QD.patientId, QD.qdmElementId from QdmElements AS QD Inner Join "+ sqlFromClause + " AS " + query.getName()
							+" ON QD.patientId = "+query.getName()+".patientId and QD.qdmElementId = "+query.getName()+".qdmElementId";
				}
				
			}
				
			
			sqlWhereClause.add(leftOperand);
		}
		else if(operator.equalsIgnoreCase("Before")){
			sqlFromClause = getFromClauseTemplate(sqlFromClause, query, tocMap);
			if(!leftOperand.equals(FunctionsEnum.CALCULATE_AGE_AT.getValue()))
				sqlFromClause = "Select patientId, qdmElementId from "+ sqlFromClause + " AS " + query.getName();
			
			
			sqlWhereClause.add(leftOperand);
		}
		else if(operator.equalsIgnoreCase("Union")){
			if(constrain.isFromClause())
			 sqlFromClause = unionQueryGenerator(leftOperand, rightOperand, "UNION", sqlFromClause, tocMap, queryMap);
		}
		else if(operator.equalsIgnoreCase("And")){
			if(constrain.isFromClause()){
				//sqlFromClause = unionQueryGenerator(leftOperand, rightOperand, "INTERSECTION", sqlFromClause, tocMap, queryMap);
				sqlFromClause = andQueryGenerator(leftOperand, rightOperand, query.getConstraintList(),sqlFromClause,tocMap);
			
			}else{
				// we removed exist code hence discrepency is resolved by checking if 2 where clause are present
				if(sqlWhereClause.size() == 2){
					String where1 = sqlWhereClause.remove();
					String where2 = sqlWhereClause.remove();
					sqlWhereClause.add(where1 + " and "+ where2); 
				}
			}
	   }
		else if(operator.equalsIgnoreCase("expressionRef")){
			sqlFromClause =  "Select patientId, qdmElementId from "+leftOperand;
		}
		// removed exists for female in demographics
		else if(operator.equalsIgnoreCase("Exists")){
		   
		   if(istempTable(leftOperand,tocMap)){
			  sqlWhereClause.add("Exists(Select patientId, qdmElementId from "+ leftOperand +")");
		   }else{
			   if(CollectionUtils.isNotEmpty(sqlWhereClause)){
				   String where1 = sqlWhereClause.remove();
				   sqlFromClause = sqlFromClause + where1;
			   }
			   sqlWhereClause.add("Exists("+ sqlFromClause +")");  
		   }
		   
	   }
		return sqlFromClause;
	 
	}
	
	
	public static String getFromClauseTemplate(String sqlFromClause, QueryVariable query, Map<String,Object> tocMap){
		
		String finalFromCaluse = null;
		
		// if sqlfrom clause is already formed return
		if(sqlFromClause != null && !sqlFromClause.isEmpty())
			finalFromCaluse =  sqlFromClause;
		else{
			
			String innerJoin = "";
			if(query.getDataSet() != null && !query.getDataSet().equals("QdmElements"))
				innerJoin = " QdmElements AS QD Inner Join "+ query.getDataSet() +" ON  QD.patientId = "+query.getDataSet()+".patientId";
			else if(CollectionUtils.isNotEmpty(query.getQueryVariable()) && query.getQueryVariable().size() == 1) {
				QueryVariable obj = query.getQueryVariable().get(0);
				innerJoin = " QdmElements AS QD Inner Join "+ obj.getName() +" ON  QD.patientId = "+obj.getName()+".patientId";
					
			}
			else
				innerJoin = " QdmElements AS QD";
			
			finalFromCaluse = "Select QD.patientId, QD.qdmElementId from "+ innerJoin;
			
		}
		
		return finalFromCaluse;
	}
	
	
	
	public static void setDataSet(QueryVariable query, Map<String,Object> tocMap, String dataSet){
		 
		List<String> dependentQueryList = query.getDependentObjects();
		System.out.println("dependentQueryList : "+dependentQueryList.toString());
		List<Constraint> constraints = query.getConstraintList();
		
		if(CollectionUtils.isNotEmpty(dependentQueryList)){
			
			List<String> linkedDependentQuery = new ArrayList<String>(new LinkedHashSet<String>(dependentQueryList));
				
				  for(String dependentquery : linkedDependentQuery){  
					  
					  //set dataset of parent
					  QueryVariable variable = (QueryVariable)tocMap.get(dependentquery);
					  System.out.println("variable in  setDataSet : "+variable);
					  if(variable!=null){
						  if(variable.getDataSet() == null){
						  variable.setDataSet(dataSet);
						  tocMap.put(variable.getName(), variable);
						  }
					  } 
					  //set dataset of its child
					  List<String> rightQuerylist = getRightQuery(dependentquery,constraints, tocMap);
					  if(CollectionUtils.isNotEmpty(rightQuerylist)){
						  
						  for(String rightChild : rightQuerylist){
							  QueryVariable rightVariable = (QueryVariable)tocMap.get(rightChild);
							  rightVariable.setDataSet(variable.getName());
							  tocMap.put(rightVariable.getName(), rightVariable);
						  }
					  }
					  
				  }  	
		}			
		
	}
	
	public static List<String> getRightQuery(String parentQuery, List<Constraint> constraints, Map<String,Object> tocMap){
		
		List<String> rightChilds = new ArrayList<String>();
				
		if(CollectionUtils.isNotEmpty(constraints)){
			
			for(Constraint constrain : constraints){
				
				if(constrain.getLeftOperand() != null && constrain.getLeftOperand().equals(parentQuery) && constrain.getOperator().equals("And")){
					String child  = constrain.getRightOperand();
					if(istempTable(child, tocMap)){
						rightChilds.add(child);	
					}
					else if(child != null && child.contains("|")){
						
						for(Constraint con : constraints){
							
							if(con.getName().equals(child)){
								
								if(istempTable(con.getLeftOperand(), tocMap)){
									
									rightChilds.add(con.getLeftOperand()) ;
								}
								if(istempTable(con.getRightOperand(), tocMap)){
									
									rightChilds.add(con.getRightOperand()) ;
								}
								
							}
						
						}
					}
				
				}
			}
		}
		
		return rightChilds;
	}
	

*/}
