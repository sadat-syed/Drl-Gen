//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.11 at 05:13:32 PM IST 
//


package com.ct.cql.elm.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The Case operator allows for multiple conditional expressions to be chained together in a single expression, rather than having to nest multiple If operators. In addition, the comparand operand provides a variant on the case that allows a single value to be compared in each conditional.
 * 			
 * If a comparand is not provided, the type of each when element of the caseItems within the Case is expected to be boolean. If a comparand is provided, the type of each when element of the caseItems within the Case is expected to be of the same type as the comparand. An else element must always be provided.
 * 
 * The static type of the then argument within the first caseItem determines the type of the result, and the then argument of each subsequent caseItem and the else argument must be of that same type.
 * 
 * <p>Java class for Case complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Case">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}Expression">
 *       &lt;sequence>
 *         &lt;element name="comparand" type="{urn:hl7-org:elm:r1}Expression" minOccurs="0"/>
 *         &lt;element name="caseItem" type="{urn:hl7-org:elm:r1}CaseItem" maxOccurs="unbounded"/>
 *         &lt;element name="else" type="{urn:hl7-org:elm:r1}Expression"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Case", propOrder = {
    "comparand",
    "caseItem",
    "_else"
})
public class Case
    extends Expression
{

    protected Expression comparand;
    @XmlElement(required = true)
    protected List<CaseItem> caseItem;
    @XmlElement(name = "else", required = true)
    protected Expression _else;

    /**
     * Gets the value of the comparand property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getComparand() {
        return comparand;
    }

    /**
     * Sets the value of the comparand property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setComparand(Expression value) {
        this.comparand = value;
    }

    /**
     * Gets the value of the caseItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the caseItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCaseItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CaseItem }
     * 
     * 
     */
    public List<CaseItem> getCaseItem() {
        if (caseItem == null) {
            caseItem = new ArrayList<CaseItem>();
        }
        return this.caseItem;
    }

    /**
     * Gets the value of the else property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getElse() {
        return _else;
    }

    /**
     * Sets the value of the else property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setElse(Expression value) {
        this._else = value;
    }

}
