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
 * The Substring operator returns the string within stringToSub, starting at the 0-based index startIndex, and consisting of length characters.
 * 			
 * If length is ommitted, the substring returned starts at startIndex and continues to the end of stringToSub.
 * 
 * If stringToSub or startIndex is null, or startIndex is out of range, the result is null.
 * 
 * <p>Java class for Substring complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Substring">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}Expression">
 *       &lt;sequence>
 *         &lt;element name="stringToSub" type="{urn:hl7-org:elm:r1}Expression"/>
 *         &lt;element name="startIndex" type="{urn:hl7-org:elm:r1}Expression"/>
 *         &lt;element name="length" type="{urn:hl7-org:elm:r1}Expression" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Substring", propOrder = {
    "stringToSub",
    "startIndex",
    "length"
})
public class Substring
    extends Expression
{

    @XmlElement(required = true)
    protected Expression stringToSub;
    @XmlElement(required = true)
    protected Expression startIndex;
    protected Expression length;

    /**
     * Gets the value of the stringToSub property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getStringToSub() {
        return stringToSub;
    }

    /**
     * Sets the value of the stringToSub property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setStringToSub(Expression value) {
        this.stringToSub = value;
    }

    /**
     * Gets the value of the startIndex property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getStartIndex() {
        return startIndex;
    }

    /**
     * Sets the value of the startIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setStartIndex(Expression value) {
        this.startIndex = value;
    }

    /**
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setLength(Expression value) {
        this.length = value;
    }

}
