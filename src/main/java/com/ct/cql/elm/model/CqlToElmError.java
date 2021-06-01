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


/**
 * Represents CQL to ELM conversion errors
 * 
 * <p>Java class for CqlToElmError complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CqlToElmError">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:cql-annotations:r1}Locator">
 *       &lt;attribute name="message" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="errorType" use="required" type="{urn:hl7-org:cql-annotations:r1}ErrorType" />
 *       &lt;attribute name="errorSeverity" type="{urn:hl7-org:cql-annotations:r1}ErrorSeverity" />
 *       &lt;attribute name="targetIncludeLibraryId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="targetIncludeLibraryVersionId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CqlToElmError", namespace = "urn:hl7-org:cql-annotations:r1")
public class CqlToElmError
    extends Locator
{

    @XmlAttribute(name = "message", required = true)
    protected String message;
    @XmlAttribute(name = "errorType", required = true)
    protected ErrorType errorType;
    @XmlAttribute(name = "errorSeverity")
    protected ErrorSeverity errorSeverity;
    @XmlAttribute(name = "targetIncludeLibraryId")
    protected String targetIncludeLibraryId;
    @XmlAttribute(name = "targetIncludeLibraryVersionId")
    protected String targetIncludeLibraryVersionId;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the errorType property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorType }
     *     
     */
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * Sets the value of the errorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorType }
     *     
     */
    public void setErrorType(ErrorType value) {
        this.errorType = value;
    }

    /**
     * Gets the value of the errorSeverity property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorSeverity }
     *     
     */
    public ErrorSeverity getErrorSeverity() {
        return errorSeverity;
    }

    /**
     * Sets the value of the errorSeverity property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorSeverity }
     *     
     */
    public void setErrorSeverity(ErrorSeverity value) {
        this.errorSeverity = value;
    }

    /**
     * Gets the value of the targetIncludeLibraryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetIncludeLibraryId() {
        return targetIncludeLibraryId;
    }

    /**
     * Sets the value of the targetIncludeLibraryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetIncludeLibraryId(String value) {
        this.targetIncludeLibraryId = value;
    }

    /**
     * Gets the value of the targetIncludeLibraryVersionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetIncludeLibraryVersionId() {
        return targetIncludeLibraryVersionId;
    }

    /**
     * Sets the value of the targetIncludeLibraryVersionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetIncludeLibraryVersionId(String value) {
        this.targetIncludeLibraryVersionId = value;
    }

}
