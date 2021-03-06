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
 * The retrieve expression defines clinical data that will be used by the artifact. This expression allows clinically relevant filtering criteria to be provided in a well-defined and computable way. This operation defines the integration boundary for artifacts. The result of a retrieve is defined to return the same data for subsequent invocations within the same evaluation request. This means in particular that patient data updates made during the evaluation request are not visible to the artifact. In effect, the patient data is a snapshot of the data as of the start of the evaluation. This ensures strict deterministic and functional behavior of the artifact, and allows the implementation engine freedom to cache intermediate results in order to improve performance.
 * 
 * <p>Java class for Retrieve complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Retrieve">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}Expression">
 *       &lt;sequence>
 *         &lt;element name="codes" type="{urn:hl7-org:elm:r1}Expression" minOccurs="0"/>
 *         &lt;element name="dateRange" type="{urn:hl7-org:elm:r1}Expression" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dataType" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="templateId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="idProperty" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codeProperty" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dateProperty" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dateLowProperty" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dateHighProperty" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="scope" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Retrieve", propOrder = {
    "codes",
    "dateRange"
})
public class Retrieve
    extends Expression
{

    protected Expression codes;
    protected Expression dateRange;
    @XmlAttribute(name = "dataType", required = true)
    protected QName dataType;
    @XmlAttribute(name = "templateId")
    protected String templateId;
    @XmlAttribute(name = "idProperty")
    protected String idProperty;
    @XmlAttribute(name = "codeProperty")
    protected String codeProperty;
    @XmlAttribute(name = "dateProperty")
    protected String dateProperty;
    @XmlAttribute(name = "dateLowProperty")
    protected String dateLowProperty;
    @XmlAttribute(name = "dateHighProperty")
    protected String dateHighProperty;
    @XmlAttribute(name = "scope")
    protected String scope;

    /**
     * Gets the value of the codes property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getCodes() {
        return codes;
    }

    /**
     * Sets the value of the codes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setCodes(Expression value) {
        this.codes = value;
    }

    /**
     * Gets the value of the dateRange property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getDateRange() {
        return dateRange;
    }

    /**
     * Sets the value of the dateRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setDateRange(Expression value) {
        this.dateRange = value;
    }

    /**
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setDataType(QName value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the templateId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * Sets the value of the templateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemplateId(String value) {
        this.templateId = value;
    }

    /**
     * Gets the value of the idProperty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdProperty() {
        return idProperty;
    }

    /**
     * Sets the value of the idProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdProperty(String value) {
        this.idProperty = value;
    }

    /**
     * Gets the value of the codeProperty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeProperty() {
        return codeProperty;
    }

    /**
     * Sets the value of the codeProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeProperty(String value) {
        this.codeProperty = value;
    }

    /**
     * Gets the value of the dateProperty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateProperty() {
        return dateProperty;
    }

    /**
     * Sets the value of the dateProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateProperty(String value) {
        this.dateProperty = value;
    }

    /**
     * Gets the value of the dateLowProperty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateLowProperty() {
        return dateLowProperty;
    }

    /**
     * Sets the value of the dateLowProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateLowProperty(String value) {
        this.dateLowProperty = value;
    }

    /**
     * Gets the value of the dateHighProperty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateHighProperty() {
        return dateHighProperty;
    }

    /**
     * Sets the value of the dateHighProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateHighProperty(String value) {
        this.dateHighProperty = value;
    }

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScope(String value) {
        this.scope = value;
    }

}
