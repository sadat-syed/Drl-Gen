package com.ct.hqmf.drl.gen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class ConditionDrlGenerator {
	static String measure = null;
	static List<String> ipp = null;
	static List<String> denominator = null;
	static List<String> denomExclusion = null;
	static List<String> numerator = null;
	static List<String> numeratorExcusions = null;
	static List<String> denomException = null;

	public static void main(String[] args) {
		try{
			File folder = new File(DroolConstants.eSpecFolder);
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				if (file.isFile()) {
					generate(file.getName());
				}
			}
			System.out.println("Done.");
		}catch(Exception e){
			System.out.println("Error reading eSpec files from " + DroolConstants.eSpecFolder +" folder.");
		}
	}

	private static void generate(String fileName) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String measureSplit[] = getMeasure(fileName);
			measure = measureSplit[0] + "v" + measureSplit[1];
			measure = measure.substring(0, measure.length() - 4);
			generateConditionDrl();
			map.put("measure", measure);
			map.put("measureId", measureSplit[0]);
			map.put("measureVersion", measureSplit[1].charAt(0));
			map.put("ipp", ipp);
			map.put("denominator", denominator);
			map.put("denomExclusion", denomExclusion);
			map.put("numerator", numerator);
			map.put("numeratorExcusions", numeratorExcusions);
			map.put("denomException", denomException);
			StringWriter out = new StringWriter();
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_24);
			configuration.setDirectoryForTemplateLoading(new File("templates"));
			configuration.setDefaultEncoding("UTF-8");
			configuration
					.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			configuration.setLogTemplateExceptions(false);
			Template template = configuration.getTemplate("conditionDrl.ftl");
			template.process(map, out);
			writeToFile(measureSplit, out);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Method to write conditions to file
	 * @param measureSplit
	 * @param out
	 */
	private static void writeToFile(String measureSplit[], StringWriter out){
		File file = null;
		BufferedWriter output = null;
		try{
			file = new File(DroolConstants.outputFolder +"//" +measureSplit[0]
					+ "v" + measureSplit[1].charAt(0) + "_Condition.drl");
			if (!file.exists()) {
				file.createNewFile();
			}
			output = new BufferedWriter(new FileWriter(file));
			output.write(out.getBuffer().toString());
			output.close();
			out.close();
			System.out.println("Condition Drl generated for " + measureSplit[0]
					+ "v" + measureSplit[1]);
		}catch(Exception exception){
			System.out.println("Exception in writing conditions to file: "+ file.getName());
		}
	}

	private static void generateConditionDrl() {
		ipp = santizeConditions(getConditions(DroolConstants.xPathIPP, measure));
		denominator = santizeConditions(getConditions(
				DroolConstants.xPathDenominator, measure));
		denomExclusion = santizeConditions(getConditions(
				DroolConstants.xPathDenominatorExclusions, measure));
		numerator = santizeConditions(getConditions(
				DroolConstants.xPathNumerartor, measure));
		numeratorExcusions = santizeConditions(getConditions(
				DroolConstants.xPathNumeratorExclusions, measure));
		denomException = santizeConditions(getConditions(
				DroolConstants.xPathDenominatorExceptions, measure));
	}

	/**
	 * Method to fetch group wise conditions from eSpec xml using xPath
	 * @param xPath
	 * @param measure
	 * @return
	 */
	private static String getConditions(String xPath, String measure) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String conditions = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("2018-eSpecs/" + measure + ".xml");
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(xPath);
			conditions = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conditions;
	}
	
	/**
	 * Method to remove :OR and :AND from list of rule statements
	 * @param conditions
	 * @return
	 */
	private static List<String> santizeConditions(String conditions) {
		List<String> cleanConditions = new ArrayList<String>();
		String split[] = null;
		split = conditions.trim().split("\\n");
		for (String condition : split) {
			if (!condition.trim().equals(DroolConstants.AND)
					&& !condition.trim().equals(DroolConstants.OR)) {
				cleanConditions.add(condition);
			}
		}
		return cleanConditions;
	}

	/**
	 * Method to split measure into measureName + measureVersion
	 * @param CQMMeasure
	 * @return
	 */
	private static String[] getMeasure(String CQMMeasure) {
		return CQMMeasure.split("v");
	}

}
