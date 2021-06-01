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
 * The Slice operator returns a portion of the elements in a list, beginning at the start index and ending just before the ending index.
 * 			
 * If the source list is null, the result is null.
 * 
 * If the startIndex is null, the slice begins at the first element of the list.
 * 
 * If the endIndex is null, the slice continues to the last element of the list.
 * 
 * If the startIndex or endIndex is less than 0, or if the endIndex is less than the startIndex, the result is an empty list.
 * 
 * <p>Java class for Slice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Slice">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}Expression">
 *       &lt;sequence>
 *         &lt;element name="source" type="{urn:hl7-org:elm:r1}Expression"/>
 *         &lt;element name="startIndex" type="{urn:hl7-org:elm:r1}Expression"/>
 *         &lt;element name="endIndex" type="{urn:hl7-org:elm:r1}Expression"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Slice", propOrder = {
    "source",
    "startIndex",
    "endIndex"
})
public class Slice
    extends Expression
{

    @XmlElement(required = true)
    protected Expression source;
    @XmlElement(required = true)
    protected Expression startIndex;
    @XmlElement(required = true)
    protected Expression endIndex;

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setSource(Expression value) {
        this.source = value;
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
     * Gets the value of the endIndex property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getEndIndex() {
        return endIndex;
    }

    /**
     * Sets the value of the endIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setEndIndex(Expression value) {
        this.endIndex = value;
    }

}
