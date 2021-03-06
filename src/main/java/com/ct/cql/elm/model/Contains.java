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
 * The Contains operator returns true if the first operand contains the second.
 * 			
 * There are two overloads of this operator:
 * 	List, T : The type of T must be the same as the element type of the list.
 * 	Interval, T : The type of T must be the same as the point type of the interval.
 * 	
 * For the List, T overload, this operator returns true if the given element is in the list, using equivalence semantics. If the list-valued argument is null, it should be treated as the empty list.
 * 
 * For the Interval, T overload, this operator returns true if the given point is greater than or equal to the starting point of the interval, and less than or equal to the ending point of the interval. For open interval boundaries, exclusive comparison operators are used. For closed interval boundaries, if the interval boundary is null, the result of the boundary comparison is considered true. If either argument is null, the result is null. If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the specified precision.
 * 
 * <p>Java class for Contains complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Contains">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}BinaryExpression">
 *       &lt;attribute name="precision" type="{urn:hl7-org:elm:r1}DateTimePrecision" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Contains")
public class Contains
    extends BinaryExpression
{

    @XmlAttribute(name = "precision")
    protected DateTimePrecision precision;

    /**
     * Gets the value of the precision property.
     * 
     * @return
     *     possible object is
     *     {@link DateTimePrecision }
     *     
     */
    public DateTimePrecision getPrecision() {
        return precision;
    }

    /**
     * Sets the value of the precision property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTimePrecision }
     *     
     */
    public void setPrecision(DateTimePrecision value) {
        this.precision = value;
    }

}
