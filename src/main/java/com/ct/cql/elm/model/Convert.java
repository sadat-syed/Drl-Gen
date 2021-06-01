//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.11 at 05:13:32 PM IST 
//


package com.ct.cql.elm.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * The Convert operator converts a value to a specific type. The result of the operator is the value of the argument converted to the target type, if possible. Note that use of this operator may result in a run-time exception being thrown if there is no valid conversion from the actual value to the target type.
 * 
 * This operator supports conversion between String and each of Boolean, Integer, Decimal, Quantity, DateTime, and Time, as well as conversion from Integer to Decimal and from Code to Concept.
 * 
 * Conversion between String and DateTime/Time is performed using the ISO-8601 standard format: YYYY-MM-DDThh:mm:ss(+|-)hh:mm.
 * 
 * <p>Java class for Convert complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Convert">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}UnaryExpression">
 *       &lt;sequence>
 *         &lt;element name="toTypeSpecifier" type="{urn:hl7-org:elm:r1}TypeSpecifier" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="toType" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Convert", propOrder = {
    "toTypeSpecifier"
})
public class Convert
    extends UnaryExpression
{

    protected TypeSpecifier toTypeSpecifier;
    @XmlAttribute(name = "toType")
    protected QName toType;

    /**
     * Gets the value of the toTypeSpecifier property.
     * 
     * @return
     *     possible object is
     *     {@link TypeSpecifier }
     *     
     */
    public TypeSpecifier getToTypeSpecifier() {
        return toTypeSpecifier;
    }

    /**
     * Sets the value of the toTypeSpecifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeSpecifier }
     *     
     */
    public void setToTypeSpecifier(TypeSpecifier value) {
        this.toTypeSpecifier = value;
    }

    /**
     * Gets the value of the toType property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getToType() {
        return toType;
    }

    /**
     * Sets the value of the toType property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setToType(QName value) {
        this.toType = value;
    }

}