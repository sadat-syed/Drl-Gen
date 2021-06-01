/*
 * @Copyright("2018 General Electric Company")
 *
 * All Rights Reserved.
 * No portions of this source code or the resulting compiled program may be used without
 * express written consent and licensing by GE Healthcare 
 */

package com.ct.hqmf.drl.gen;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ct.hqmf.drl.model.Criteria;
import com.ct.hqmf.drl.model.Measure;
import com.ct.hqmf.drl.model.Precondition;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author 212473687
 *
 */
public class MeasureGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException, TemplateException {
		String contents = new String(Files.readAllBytes(Paths.get("2018/CMS347v1.xml.json")));
		contents = contents.replaceAll("\\?", "_");
		ObjectMapper mapper = new ObjectMapper();
		Measure measure = mapper.readValue(contents, Measure.class);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS,false);
		//System.out.println(measure);

		Map<String, Criteria> dataCriteriaMap = measure.getPopulation_criteria();
		
		List<Precondition> listOfPreConditionsIPP = new LinkedList<Precondition>();
		listOfPreConditionsIPP = dataCriteriaMap.get("IPP").getPreconditions();
		
		List<Precondition> listOfPreConditionsDenom = new LinkedList<Precondition>();
		listOfPreConditionsDenom = dataCriteriaMap.get("DENOM").getPreconditions();
		
		List<Precondition> listOfPreConditionsNumer = new LinkedList<Precondition>();
		listOfPreConditionsNumer = dataCriteriaMap.get("NUMER").getPreconditions();
	
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<String> popConjunctions = new LinkedList<String>();
		popConjunctions.add(listOfPreConditionsIPP.get(0).getConjunction_code());
		popConjunctions.add(listOfPreConditionsNumer.get(0).getConjunction_code());
		
		map.put("measure", measure);
		map.put("ippPC",  listOfPreConditionsIPP.get(0).getPreconditions());
		map.put("numerPC",  listOfPreConditionsNumer.get(0).getPreconditions());
		map.put("popConjunctions", popConjunctions);
		if(null == listOfPreConditionsDenom){
			map.put("denomPC", "denomPC");
		}
		
		StringWriter out = new StringWriter();
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_24);
        cfg.setDirectoryForTemplateLoading(new File("templates"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        Template temp = cfg.getTemplate( "measure_multiPop.ftl" );
        temp.process( map, out );
	    System.out.println( out.getBuffer().toString() );
	}

}
