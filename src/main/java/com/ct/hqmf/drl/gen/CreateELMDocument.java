package com.ct.hqmf.drl.gen;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.ct.cql.elm.model.Expression;
import com.ct.cql.elm.model.Library;
import com.ct.cql.elm.model.ObjectFactory;
import com.ct.cql.elm.model.Retrieve;
import com.ct.cql.elm.model.UnaryExpression;

public class CreateELMDocument {

	
	  public static void main(String[] args) throws JAXBException {
		    //1. We need to create JAXContext instance
		    JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);

		    //2. Use JAXBContext instance to create the Unmarshaller.
		    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		    //3. Use the Unmarshaller to unmarshal the XML document to get an instance of JAXBElement.
		    //JAXBElement<Library> unmarshalledObject = (JAXBElement<Library>)unmarshaller.unmarshal(new File("D://GE/Dhananjay Utility/BCS125-0.0.005.xml"));
		    JAXBElement<Library> unmarshalledObject = (JAXBElement<Library>)unmarshaller.unmarshal(new File("src/main/resources/BCS125-0.0.005.xml"));
		    
		    //4. Get the instance of the required JAXB Root Class from the JAXBElement.
		    Library libObj = unmarshalledObject.getValue();
		   

		    //Obtaining all the required data from the JAXB Root class instance.
		    UnaryExpression obj = (UnaryExpression) libObj.getStatements().getDef().get(0).getExpression();
		    Retrieve ret = (Retrieve) obj.getOperand();
		    System.out.println("object is library: "+ret.getDataType().getLocalPart());
		  
		    
	  }	  
	  
	  
	  
	  public static Library parseelm(File file) {

		  Library libObj = null;
				  
		   try{
			  
			 //1. We need to create JAXContext instance
			    JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);

			 //2. Use JAXBContext instance to create the Unmarshaller.
			    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			 //3. Use the Unmarshaller to unmarshal the XML document to get an instance of JAXBElement.
			    JAXBElement<Library> unmarshalledObject = (JAXBElement<Library>)unmarshaller.unmarshal(file);

			 //4. Get the instance of the required JAXB Root Class from the JAXBElement.
			     libObj = unmarshalledObject.getValue();
			   
		   }catch(JAXBException e){
			  System.out.println("Unable to parse elm document : "+ e.getMessage());
			   
		   }
		    return libObj;
	  }
	
}
