package com.ct.cql.parser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ct.cql.entity.DrlFileModel;

public class SourceParser {

	/*
	 * parse HQMFFileName/measureFile.Xml i.e. CMS123v7.xml & get,set /*
	 * cqlXml/elmXml file i.e.DiabetesFootExam-7.5.000.xml
	 */
	public static void parseXml(DrlFileModel drlFileModel) {
		String cqlXmlFileName = null;
		List<String> populationCriteriaList = new ArrayList<>();
		int stratificationCriteria = 0;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new FileInputStream(drlFileModel.getMeasureXmlFile()));
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			String expression;
			String stratification;
			Node node;
			NodeList nodePopCriteria;
			NodeList nodeStratCriteria;

			/*
			 * .1st element's value in refrence tag ../<translation><refrence
			 * value="123"></refrence><refrence value=""></refrence>
			 * </translation>
			 */
			expression = "/QualityMeasureDocument/relatedDocument/expressionDocument/text/translation/reference/@value";
			node = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);
			cqlXmlFileName = node.getTextContent();

			/*
			 * .2 getPopulateCriteriaList,get all"./id" elmnts dat hv
			 * "extension" attr
			 */
			expression = "/QualityMeasureDocument/component/populationCriteriaSection/id[@extension]";
			nodePopCriteria = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
			for (int i = 0; i < nodePopCriteria.getLength(); i++) {
				populationCriteriaList.add(nodePopCriteria.item(i).getAttributes().getNamedItem("extension").getNodeValue());
			}
			
			stratification = "/QualityMeasureDocument/component/populationCriteriaSection/component/stratifierCriteria/id[@extension]";
			nodeStratCriteria = (NodeList) xpath.evaluate(stratification, doc, XPathConstants.NODESET);
			if(nodeStratCriteria != null && nodeStratCriteria.item(0) != null) {
				stratificationCriteria = nodeStratCriteria.item(0).getAttributes().getLength();
				drlFileModel.setStratCriteria(stratificationCriteria);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("drlFileModel.getMeasureDirectoryPath() : "+drlFileModel.getMeasureDirectoryPath());
		System.out.println("cqlXmlFileName : "+cqlXmlFileName.substring(cqlXmlFileName.lastIndexOf("/")+1));
		if(cqlXmlFileName.contains("/"))
		{
			cqlXmlFileName = cqlXmlFileName.substring(cqlXmlFileName.lastIndexOf("/")+1);
		}
		drlFileModel.setCqlXmlFilePath(drlFileModel.getMeasureDirectoryPath() + File.separatorChar + cqlXmlFileName);
		drlFileModel.setPopulationCriteriaList(populationCriteriaList);
	}
}
