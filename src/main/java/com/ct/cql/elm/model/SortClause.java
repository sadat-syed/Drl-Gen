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
 * The SortClause element defines the sort order for the query.
 * 
 * <p>Java class for SortClause complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SortClause">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}Element">
 *       &lt;sequence>
 *         &lt;element name="by" type="{urn:hl7-org:elm:r1}SortByItem" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SortClause", propOrder = {
    "by"
})
public class SortClause
    extends Element
{

    @XmlElement(required = true)
    protected List<SortByItem> by;

    /**
     * Gets the value of the by property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the by property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SortByItem }
     * 
     * 
     */
    public List<SortByItem> getBy() {
        if (by == null) {
            by = new ArrayList<SortByItem>();
        }
        return this.by;
    }

}
