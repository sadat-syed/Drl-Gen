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
 * The ToInteger operator converts the value of its argument to an Integer value. The operator accepts strings using the following format:
 * 
 * (+|-)?#0
 * 
 * Meaning an optional polarity indicator, followed by any number of digits (including none), followed by at least one digit.
 * 
 * Note that the integer value returned by this operator must be a valid value in the range representable for Integer values in CQL.
 * 
 * If the input string is not formatted correctly, or cannot be interpreted as a valid Integer value, a run-time error is thrown.
 * 
 * If the argument is null, the result is null.
 * 
 * <p>Java class for ToInteger complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ToInteger">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}UnaryExpression">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ToInteger")
public class ToInteger
    extends UnaryExpression
{


}
