package com.ct.cql.drlgen;
/*package com.ct.cql.drlgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.ct.cql.entity.Constraint;
import com.ct.cql.entity.InstanceInfo;
import com.ct.cql.entity.QueryVariable;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class DrlGeneratorUtility {
	
	static public Map<String, List<String>> conditionDrlMap = new HashMap<String, List<String>>();
	
	//public static int conditionNumber = 0;
	
	public static void createDroolsForGroup(QueryVariable query, Map<String,Object> tocMap, String dataset, String populationCriteria){
		
		 List<String> orderList = new ArrayList<String>();
		 droolsFactory(query, tocMap, orderList, dataset);
		 try {
			droolsGenerator(orderList, tocMap, populationCriteria);
			createConditionDrlMap(populationCriteria, orderList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void  droolsFactory(QueryVariable query, Map<String,Object> tocMap, List<String> orderList, String dataset){
			
			String queryname = query.getName();
			InstanceInfo obj = (InstanceInfo) tocMap.get(queryname);
			// check is query is already resolved no action required	
			   if(obj.isResolved()){
				   return;
			   }else{
				   
				   //set the required dependent conditions
				   setDataSet(query, tocMap, dataset);
				   // if its dependent queries are present first resolve those one by one
				    if(CollectionUtils.isNotEmpty(query.getQueryVariable())){
					   
					   for(QueryVariable dependentQuery : query.getQueryVariable()){
						   if(!dependentQuery.getDependentObjects().contains(queryname)){
							   QueryVariable subquery = (QueryVariable) tocMap.get(dependentQuery.getName());
							   if(subquery!=null)
							   {
								   String subqueryDataSet = subquery.getDataSet();
								   if(subqueryDataSet == null)
									   subqueryDataSet = dataset;
								   droolsFactory(dependentQuery,tocMap, orderList, subqueryDataSet);
							   }
						   }
						   else  {
							   query.setResolved(true);
						   }
					   }
				   }
				  
				   baseQueryVariableGenerator(query,tocMap, orderList); 
			    }
	}
	
	public static void  baseQueryVariableGenerator(QueryVariable query,  Map<String,Object> tocMap, List<String> orderList){
		
		if(CollectionUtils.isNotEmpty(query.getConstraintList())){
			for(Constraint constraint : query.getConstraintList()){
				evaluateOperatorsToQuery(query, constraint, tocMap, orderList);
			}
		}
	
		// add the query in orderList and mark the query as resolved
		//query.setConditionNumber(conditionNumber++);
		orderList.add(query.getName());
		query.setResolved(true);
		tocMap.put(query.getName(), query);
		
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
	
	public static void evaluateOperatorsToQuery(QueryVariable query,Constraint constrain, Map<String,Object> tocMap, List<String> orderList){

		
		String leftOperand = constrain.getLeftOperand();
		String rightOperand = constrain.getRightOperand();
		
		//resolve left and right operand incase it is Temp Query
		if(leftOperand != null && istempTable(leftOperand, tocMap) ){
			QueryVariable left = (QueryVariable) tocMap.get(leftOperand);
			if(left != null && !left.isResolved()){
				
				if(istempTable(left.getDataSet(), tocMap)){
					
					QueryVariable depedentQuery = (QueryVariable) tocMap.get(left.getDataSet());
					droolsFactory(depedentQuery, tocMap, orderList, depedentQuery.getDataSet());
					
				}
				droolsFactory(left, tocMap, orderList, left.getDataSet());
			}
			
		}
		
		if(rightOperand != null  && istempTable(rightOperand, tocMap)){
			QueryVariable right = (QueryVariable) tocMap.get(rightOperand);
			if(right != null && !right.isResolved()){
				
				if(istempTable(right.getDataSet(), tocMap)){
					
					QueryVariable depedentQuery = (QueryVariable) tocMap.get(right.getDataSet());
					droolsFactory(depedentQuery, tocMap, orderList, depedentQuery.getDataSet());
					
				}
				
				droolsFactory(right, tocMap, orderList, right.getDataSet());
				
			}
		}
		
	 
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
		List<Constraint> constraints = query.getConstraintList();
		if(CollectionUtils.isNotEmpty(dependentQueryList)){
			List<String> linkedDependentQuery = new ArrayList<String>(new LinkedHashSet<String>(dependentQueryList));
				  for(String dependentquery : linkedDependentQuery){  
						//set dataset of parent
					  QueryVariable variable = (QueryVariable)tocMap.get(dependentquery);
					 if(variable!=null) {
						  if(variable.getDataSet() == null){
						  variable.setDataSet(dataSet);
						 }
					 }
					  //set dataset of its child
					  List<String> rightQuerylist = getRightQuery(dependentquery,constraints, tocMap);
					  if(CollectionUtils.isNotEmpty(rightQuerylist)){
						  
						  for(String rightChild : rightQuerylist){
							  QueryVariable rightVariable = (QueryVariable)tocMap.get(rightChild);
							  rightVariable.setDataSet(variable.getName());
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
	
	
	public static void droolsGenerator(List<String> orderList, Map<String,Object> tocMap, String populationCriteria) {
		
		Map<String, Object> map = new HashMap<String, Object>();
        BufferedWriter bw = null;
		FileWriter fw = null;

		map.put("orderList", orderList);
		map.put("tocMap", tocMap);
		map.put("populationCriteria", populationCriteria);
		map.put("class", QueryVariable.class);
		StringWriter out = new StringWriter();
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_24);
        try {
			cfg.setDirectoryForTemplateLoading(new File("templates"));
			  cfg.setDefaultEncoding("UTF-8");
		        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		        cfg.setLogTemplateExceptions(false);
		        Template temp = cfg.getTemplate( "drlMeasure.ftl");
		        temp.process( map, out );
		        final String FILENAME = "src//main//resources//drlOutput//CMS125//CMS125v7.drl";
				File file = new File(FILENAME);
				
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
		        
				fw = new FileWriter(file.getAbsoluteFile(), true);
				bw = new BufferedWriter(fw);
				bw.write(out.getBuffer().toString());
				System.out.println(out.getBuffer().toString());
				if((!(out.getBuffer().toString().contains("\\|"))))
				{
					if(out.getBuffer().toString().contains("QdmElement("))
					{
						{
							bw.write(out.getBuffer().toString());
							System.out.println(out.getBuffer().toString());
						}
					}
				}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
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
	
	public static void ruleBaseGenerator(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("class", QueryVariable.class);
		StringWriter out = new StringWriter();
		 BufferedWriter bw = null;
		 FileWriter fw = null;
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_24);
        try {
			cfg.setDirectoryForTemplateLoading(new File("templates"));
			 cfg.setDefaultEncoding("UTF-8");
		        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		        cfg.setLogTemplateExceptions(false);
		        Template temp = cfg.getTemplate( "BaseMeasure.ftl" );
		        temp.process( map, out );
		        
		        //final String FILENAME = "D:\\sourcecode\\CQR1SR1-20140304\\utilities\\drl.gen\\src\\main\\resources\\drlOutput\\CMS125\\CMS125.drl";
		        final String FILENAME = "src/main/resources/drlOutput/CMS125/CMS125.drl";
				File file = new File(FILENAME);
				
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
		        
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				String data = out.getBuffer().toString();
				bw.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
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


	public static void createConditionDrlMap( String populationCriteria, List<String> orderList) throws IOException, TemplateException{
	conditionDrlMap.put(populationCriteria, orderList);
}


	public static void conditionDrlGenerator( Map<String, Object> tocMap) throws IOException, TemplateException{
	
	
	
	Map<String, Object> map = new HashMap<String, Object>();
    map.put("condtionDrlMap", conditionDrlMap);
	map.put("tocMap", tocMap);
	
	//System.out.println("conditionDrlMap : "+conditionDrlMap);
	List<String> ipp = conditionDrlMap.get("Initial Patient Population");
	List<String> denominator = conditionDrlMap.get("Denominator");
	List<String> denominatorExclusions = conditionDrlMap.get("Denominator Exclusions");
	map.put("measure", "CMS125v7");
	map.put("measureId", "CMS125");
	map.put("measureVersion", "7");
	map.put("ipp", ipp);
	map.put("denominator", denominator);
	map.put("denominatorExclusions", denominatorExclusions);
	StringWriter out = new StringWriter();
	Configuration cfg = new Configuration(Configuration.VERSION_2_3_24);
    cfg.setDirectoryForTemplateLoading(new File("templates"));
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setLogTemplateExceptions(false);
    Template temp = cfg.getTemplate( "conditionDrlCql.ftl" );
    temp.process(map, out);
    System.out.println("final ourput : "+out.getBuffer().toString());
}
	
}


*/