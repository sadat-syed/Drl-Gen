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
 * The As operator allows the result of an expression to be cast as a given target type. This allows expressions to be written that are statically typed against the expected run-time type of the argument. If the argument is not of the specified type, and the strict attribute is false (the default), the result is null. If the argument is not of the specified type and the strict attribute is true, an exception is thrown.
 * 
 * <p>Java class for As complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="As">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}UnaryExpression">
 *       &lt;sequence>
 *         &lt;element name="asTypeSpecifier" type="{urn:hl7-org:elm:r1}TypeSpecifier" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="asType" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="strict" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "As", propOrder = {
    "asTypeSpecifier"
})
public class As
    extends UnaryExpression
{

    protected TypeSpecifier asTypeSpecifier;
    @XmlAttribute(name = "asType")
    protected QName asType;
    @XmlAttribute(name = "strict")
    protected Boolean strict;

    /**
     * Gets the value of the asTypeSpecifier property.
     * 
     * @return
     *     possible object is
     *     {@link TypeSpecifier }
     *     
     */
    public TypeSpecifier getAsTypeSpecifier() {
        return asTypeSpecifier;
    }

    /**
     * Sets the value of the asTypeSpecifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeSpecifier }
     *     
     */
    public void setAsTypeSpecifier(TypeSpecifier value) {
        this.asTypeSpecifier = value;
    }

    /**
     * Gets the value of the asType property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getAsType() {
        return asType;
    }

    /**
     * Sets the value of the asType property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setAsType(QName value) {
        this.asType = value;
    }

    /**
     * Gets the value of the strict property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isStrict() {
        if (strict == null) {
            return false;
        } else {
            return strict;
        }
    }

    /**
     * Sets the value of the strict property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStrict(Boolean value) {
        this.strict = value;
    }

}
