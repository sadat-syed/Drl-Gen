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
 * The OperandDef type defines an operand to a function that can be referenced by name anywhere within the body of a function definition.
 * 
 * <p>Java class for OperandDef complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OperandDef">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}Element">
 *       &lt;sequence>
 *         &lt;element name="operandTypeSpecifier" type="{urn:hl7-org:elm:r1}TypeSpecifier" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="operandType" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperandDef", propOrder = {
    "operandTypeSpecifier"
})
public class OperandDef
    extends Expression
	
{

    protected TypeSpecifier operandTypeSpecifier;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "operandType")
    protected QName operandType;

    /**
     * Gets the value of the operandTypeSpecifier property.
     * 
     * @return
     *     possible object is
     *     {@link TypeSpecifier }
     *     
     */
    public TypeSpecifier getOperandTypeSpecifier() {
        return operandTypeSpecifier;
    }

    /**
     * Sets the value of the operandTypeSpecifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeSpecifier }
     *     
     */
    public void setOperandTypeSpecifier(TypeSpecifier value) {
        this.operandTypeSpecifier = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the operandType property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getOperandType() {
        return operandType;
    }

    /**
     * Sets the value of the operandType property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setOperandType(QName value) {
        this.operandType = value;
    }

}
