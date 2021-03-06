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
 * The Is operator allows the type of a result to be tested. The language must support the ability to test against any type. If the run-time type of the argument is of the type being tested, the result of the operator is true; otherwise, the result is false.
 * 
 * <p>Java class for Is complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Is">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}UnaryExpression">
 *       &lt;sequence>
 *         &lt;element name="isTypeSpecifier" type="{urn:hl7-org:elm:r1}TypeSpecifier" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="isType" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Is", propOrder = {
    "isTypeSpecifier"
})
public class Is
    extends UnaryExpression
{

    protected TypeSpecifier isTypeSpecifier;
    @XmlAttribute(name = "isType")
    protected QName isType;

    /**
     * Gets the value of the isTypeSpecifier property.
     * 
     * @return
     *     possible object is
     *     {@link TypeSpecifier }
     *     
     */
    public TypeSpecifier getIsTypeSpecifier() {
        return isTypeSpecifier;
    }

    /**
     * Sets the value of the isTypeSpecifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeSpecifier }
     *     
     */
    public void setIsTypeSpecifier(TypeSpecifier value) {
        this.isTypeSpecifier = value;
    }

    /**
     * Gets the value of the isType property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getIsType() {
        return isType;
    }

    /**
     * Sets the value of the isType property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setIsType(QName value) {
        this.isType = value;
    }

}
