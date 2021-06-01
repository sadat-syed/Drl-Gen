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
import javax.xml.bind.annotation.XmlType;


/**
 * TupleTypeSpecifier defines the possible elements of a tuple.
 * 
 * <p>Java class for TupleTypeSpecifier complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TupleTypeSpecifier">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}TypeSpecifier">
 *       &lt;sequence>
 *         &lt;element name="element" type="{urn:hl7-org:elm:r1}TupleElementDefinition" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TupleTypeSpecifier", propOrder = {
    "element"
})
public class TupleTypeSpecifier
    extends TypeSpecifier
{

    protected List<TupleElementDefinition> element;

    /**
     * Gets the value of the element property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the element property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TupleElementDefinition }
     * 
     * 
     */
    public List<TupleElementDefinition> getElement() {
        if (element == null) {
            element = new ArrayList<TupleElementDefinition>();
        }
        return this.element;
    }

}
