//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.11 at 05:13:32 PM IST 
//


package com.ct.cql.elm.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The PositionOf operator returns the 0-based index of the beginning given pattern in the given string.
 * 			
 * If the pattern is not found, the result is -1.
 * 
 * If either argument is null, the result is null.
 * 
 * <p>Java class for PositionOf complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PositionOf">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}Expression">
 *       &lt;sequence>
 *         &lt;element name="pattern" type="{urn:hl7-org:elm:r1}Expression"/>
 *         &lt;element name="string" type="{urn:hl7-org:elm:r1}Expression"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PositionOf", propOrder = {
    "pattern",
    "string"
})
public class PositionOf
    extends Expression
{

    @XmlElement(required = true)
    protected Expression pattern;
    @XmlElement(required = true)
    protected Expression string;

    /**
     * Gets the value of the pattern property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getPattern() {
        return pattern;
    }

    /**
     * Sets the value of the pattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setPattern(Expression value) {
        this.pattern = value;
    }

    /**
     * Gets the value of the string property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getString() {
        return string;
    }

    /**
     * Sets the value of the string property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setString(Expression value) {
        this.string = value;
    }

}
