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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The First operator returns the first element in a list. If the order by attribute is specified, the list is sorted by that ordering prior to returning the first element.
 * 			
 * If the argument is null, the result is null.
 * 
 * <p>Java class for First complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="First">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}Expression">
 *       &lt;sequence>
 *         &lt;element name="source" type="{urn:hl7-org:elm:r1}Expression"/>
 *       &lt;/sequence>
 *       &lt;attribute name="orderBy" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "First", propOrder = {
    "source"
})
public class First
    extends Expression
{

    @XmlElement(required = true)
    protected Expression source;
    @XmlAttribute(name = "orderBy")
    protected String orderBy;

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
     * Gets the value of the orderBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Sets the value of the orderBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderBy(String value) {
        this.orderBy = value;
    }

}
