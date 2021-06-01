package com.ct.cql.drlgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.ct.cql.entity.CQLModel;
import com.ct.cql.entity.Constants;
import com.ct.cql.entity.Constraint;
import com.ct.cql.entity.DrlFileModel;
import com.ct.cql.entity.InstanceInfo;
import com.ct.cql.entity.PopulationCriteriaName;
import com.ct.cql.entity.QueryVariable;
import com.ct.cql.entity.RuleGroup;
import com.ct.cql.parser.CQLHelper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class DrlGenerator {

	public static Map<String, List<QueryVariable>> conditionDrlMap = new HashMap<>();
	private DrlFileModel drlFileModel;
	

	/* set class level DRL File object for reuse * */
	public void init(DrlFileModel droolFileModel) {
		drlFileModel = droolFileModel;
	}

	/*
	 * this method is used to resolve all the queryvariable in cqlmodel
	 */
	public static void droolsFactory(QueryVariable query, CQLModel model, List<QueryVariable> orderList, String dataset) {
		try{
			String queryname = query.getName();
			InstanceInfo obj = (InstanceInfo) model.getExpressions().get(queryname);
			// check is query is already resolved no action required
			if (obj.isResolved()) {
				return;
			} else {
				// set the required dependent conditions
				setDataSet(query, model, dataset);
				// if its dependent queries are present first resolve those one by one
				if (CollectionUtils.isNotEmpty(query.getQueryVariable())) {

					for (QueryVariable dependentQuery : query.getQueryVariable()) {
						if (!dependentQuery.getDependentObjects().contains(queryname)) {
							QueryVariable subquery = (QueryVariable) model.getExpressions().get(dependentQuery.getName());
							if (subquery != null) {
								String subqueryDataSet = subquery.getDataSet();
								if (subqueryDataSet == null)
									subqueryDataSet = dataset;
								droolsFactory(dependentQuery, model, orderList, subqueryDataSet);
							}
						} else {
							query.setResolved(true);
						}
					}
				}

				baseQueryVariableGenerator(query, model, orderList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * this method is used to pass the resolved queryvariabble to ftl file to generate base drl file
	 */
	public void ruleBaseGenerator(CQLModel model, String populationCriteriaName) {
		Map<String, Object> map = new HashMap<>();
		map.put("class", QueryVariable.class);
		map.put("measureName", drlFileModel.getMeasureName());
		map.put("measureId", drlFileModel.getMeasureId());
		map.put("measureVersion", drlFileModel.getMeasureVersion());
		map.put("tocMap", model.getExpressions());
		
		if(model.getPopulationCriteria() != null && model.getPopulationCriteria().size() > 1) {
			map.put("populationCriteriaName", PopulationCriteriaName.fromInt(populationCriteriaName));
		}
		
		StringWriter out = new StringWriter();
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_24);
		try {
			cfg.setDirectoryForTemplateLoading(new File("templates"));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			cfg.setLogTemplateExceptions(false);
			Template temp;
			//used for episode
			  if (model.isEpisodeMeasure()) {
				temp = cfg.getTemplate("episodeBaseMeasure.ftl");
			} else
				temp = cfg.getTemplate("BaseMeasure.ftl");
			temp.process(map, out);
			//System.out.println("Base :" + out.getBuffer().toString());
			model.getDrlOutput().append(out.getBuffer().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * this method is used to pass the resolved queryvariabble to ftl file to generate drl file
	 */
	
	public void droolsGenerator(List<QueryVariable> orderList, CQLModel model, String populationCriteria,String populationCriteriaName) {
		Map<String, Object> map = new HashMap<>();
		map.put("orderList", orderList);
		map.put("tocMap", model.getExpressions());
		map.put("populationCriteria", populationCriteria);
		map.put("class", QueryVariable.class);
		StringWriter out = new StringWriter();
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_24);
		try {
			cfg.setDirectoryForTemplateLoading(new File("templates"));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			cfg.setLogTemplateExceptions(false);

			if(model.getPopulationCriteria() != null && model.getPopulationCriteria().size() > 1) {
				map.put("populationCriteriaName", PopulationCriteriaName.fromInt(populationCriteriaName));
			}
			
			Template temp;
			if (model.isEpisodeMeasure()){
				temp = cfg.getTemplate("episodeMeasure.ftl");
			}else
				temp = cfg.getTemplate("drlMeasure.ftl");
			temp.process(map, out);
			model.getDrlOutput().append(out.getBuffer().toString());
			System.out.println(out.getBuffer().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * this method is used to pass the resolved queryvariabble to ftl file to generate condition drl file
	 */
	
	public void conditionDrlGenerator(CQLModel model) throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<>();
		map.put("condtionDrlMap", conditionDrlMap);
		map.put("expressions", model.getExpressions());
		
		for(RuleGroup rule : RuleGroup.values()) {
			if(rule.getValue1().equals(RuleGroup.DENOM_EXCL.getValue1()) && conditionDrlMap.get(rule.getValue1()) != null) {
				/**
				 * Added this condition to handle Denominator Exclusion without 's' for CMS160 (Multi IPP & Strat)
				 */
				map.put(rule.getValue1().replace(" ","").concat("s"), conditionDrlMap.get(rule.getValue1()));
			} 
			else {
				map.put(rule.getValue1().replace(" ",""), conditionDrlMap.get(rule.getValue1()));
			}
		}
		
		map.put("measure", drlFileModel.getMeasureName());
		map.put("measureId", drlFileModel.getMeasureId());
		map.put("measureVersion", drlFileModel.getMeasureVersion());
		
		StringWriter out = new StringWriter();
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_24);
		cfg.setDirectoryForTemplateLoading(new File("templates"));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		Template temp = cfg.getTemplate("conditionDrlCql.ftl");
		temp.process(map, out);
		model.setConditionDrlOutput(out.getBuffer().toString());
	}
	
	/*
	 * this method is used to pass the resolved queryvariabble to ftl file to generate condition drl group
	 */
	public void createDroolsForGroup(String rule, CQLModel model, String populationCriteria, String populationCriteriaName) {
		List<QueryVariable> orderList = new ArrayList<>();
		try {
			QueryVariable query = (QueryVariable) model.getExpressions().get(rule);
			if(query != null) {
				droolsFactory(query, model, orderList, Constants.CONSTANTS_QDM_ELEMENTS);
				Collections.sort(orderList, new QueryVariable());
				droolsGenerator(orderList, model, populationCriteria, populationCriteriaName);
				conditionDrlMap.put(populationCriteria, orderList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * this method is used to set dataset for each queryvariable
	 */
	
	public static void setDataSet(QueryVariable query, CQLModel model, String dataSet) {
		List<String> dependentQueryList = query.getDependentObjects();
		List<Constraint> constraints = query.getConstraintList();
		if (CollectionUtils.isNotEmpty(dependentQueryList)) {
			List<String> linkedDependentQuery = new ArrayList<>(new LinkedHashSet<String>(dependentQueryList));
			for (String dependentquery : linkedDependentQuery) {
				// set dataset of parent
				QueryVariable variable = (QueryVariable) model.getExpressions().get(dependentquery);
				if (variable != null) {
					if (variable.getDataSet() == null) {
						variable.setDataSet(dataSet);
					}
				}
				// set dataset of its child
				List<String> rightQuerylist = getRightQuery(dependentquery, constraints, model);
				if (CollectionUtils.isNotEmpty(rightQuerylist)) {
					for (String rightChild : rightQuerylist) {
						QueryVariable rightVariable = (QueryVariable) model.getExpressions().get(rightChild);
						rightVariable.setDataSet(variable.getName());
					}
				}

			}
		}
	}

	/*
	 * this method is used to get right query of queryvariable
	 */
	
	public static List<String> getRightQuery(String parentQuery, List<Constraint> constraints, CQLModel model) {
		List<String> rightChilds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(constraints)) {
			for (Constraint constrain : constraints) {
				if (constrain.getLeftOperand() != null && constrain.getLeftOperand().equals(parentQuery) && constrain.getOperator().equals("And")) {
					String child = constrain.getRightOperand();
					if (CQLHelper.isTempTable(child, model)) {
						rightChilds.add(child);
					} else if (child != null && child.contains("|")) {
						for (Constraint con : constraints) {
							if (con.getName().equals(child)) {
								if (CQLHelper.isTempTable(con.getLeftOperand(), model)) {
									rightChilds.add(con.getLeftOperand());
								}
								if (CQLHelper.isTempTable(con.getRightOperand(), model)) {
									rightChilds.add(con.getRightOperand());
								}
							}
						}
					}
				}
			}
		}
		return rightChilds;
	}

	/*
	 * this method is used to evaluate each constraint in queryvariable
	 */
	
	public static void baseQueryVariableGenerator(QueryVariable query, CQLModel model, List<QueryVariable> orderList) {
		if (CollectionUtils.isNotEmpty(query.getConstraintList())) {
			for (Constraint constraint : query.getConstraintList()) {
				evaluateOperatorsToQuery(constraint, model, orderList);
			}
		}

		orderList.add(query);
		query.setResolved(true);
		model.getExpressions().put(query.getName(), query);
	}

	public static boolean contains(String test) {

		for (RuleGroup c : RuleGroup.values()) {
			if (c.name().equals(test)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * this method is used to evaluate right and left operands in queryvariable
	 */
	public static void evaluateOperatorsToQuery(Constraint constrain, CQLModel model, List<QueryVariable> orderList) {
		String leftOperand = constrain.getLeftOperand();
		String rightOperand = constrain.getRightOperand();

		// resolve left and right operand incase it is Temp Query
		if (leftOperand != null && CQLHelper.isTempTable(leftOperand, model)) {
			QueryVariable left = (QueryVariable) model.getExpressions().get(leftOperand);
			if (left != null && !left.isResolved()) {
				if (CQLHelper.isTempTable(left.getDataSet(), model)) {
					QueryVariable depedentQuery = (QueryVariable) model.getExpressions().get(left.getDataSet());
					droolsFactory(depedentQuery, model, orderList, depedentQuery.getDataSet());
				}
				droolsFactory(left, model, orderList, left.getDataSet());
			}
		}

		if (rightOperand != null && CQLHelper.isTempTable(rightOperand, model)) {
			QueryVariable right = (QueryVariable) model.getExpressions().get(rightOperand);
			if (right != null && !right.isResolved()) {
				if (CQLHelper.isTempTable(right.getDataSet(), model)) {
					QueryVariable depedentQuery = (QueryVariable) model.getExpressions().get(right.getDataSet());
					droolsFactory(depedentQuery, model, orderList, depedentQuery.getDataSet());
				}
				droolsFactory(right, model, orderList, right.getDataSet());
			}
		}
	}

	/* Writing drl file after coplete data generation. */
	public void generateDrlFile(String fileCriteria, CQLModel model) {
		
		String drlFilePath = drlFileModel.getDrlDirectoryPath().concat(String.valueOf(File.separatorChar)).concat(drlFileModel.getMeasureName());
		String conditionDrlFilePath = drlFileModel.getDrlDirectoryPath().concat(String.valueOf(File.separatorChar)).concat(drlFileModel.getMeasureName());
		
		if(model.getPopulationCriteria().size() > 1) {
			drlFilePath = drlFilePath.concat(fileCriteria).concat(Constants.DRL_EXTENSION);
			conditionDrlFilePath = conditionDrlFilePath.concat(fileCriteria).concat(Constants.CONDITION_DRL_EXTENSION);
			
			writeFile(drlFilePath, model.getDrlOutput().toString());
			writeFile(conditionDrlFilePath, model.getConditionDrlOutput());
		} else {
			drlFilePath = drlFilePath.concat(Constants.DRL_EXTENSION) ;
			conditionDrlFilePath = conditionDrlFilePath.concat(Constants.CONDITION_DRL_EXTENSION) ;
			
			writeFile(drlFilePath, model.getDrlOutput().toString());
			writeFile(conditionDrlFilePath, model.getConditionDrlOutput());
		}
	}
	
	/* writeFile - use to create new file */
	public static void writeFile(String filePath, String data) {
		File file = new File(filePath);
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.append(data);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				file = null;
				if (bw != null) {
					bw.close();
					bw = null;
				}
				if (fw != null) {
					fw.close();
					fw = null;
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			}
		}
	}
	public static String readFile(String fileName)
	{
		String data = "";
		try {
			data = new String (Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error While reading file");
			e.printStackTrace();
		}
		return data;
	}
}
