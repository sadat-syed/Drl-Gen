//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.11 at 05:13:32 PM IST 
//


package com.ct.cql.elm.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * The Now operator returns the date and time of the start timestamp associated with the evaluation request. Now is defined in this way for two reasons:
 *  1) The operation will always return the same value within any given evaluation, ensuring that the result of an expression containing Now will always return the same result.
 * 
 *  2) The operation will return the timestamp associated with the evaluation request, allowing the evaluation to be performed with the same timezone information as the data delivered with the evaluation request.
 * 
 * <p>Java class for Now complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Now">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}Expression">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Now")
public class Now
    extends Expression
{


}
