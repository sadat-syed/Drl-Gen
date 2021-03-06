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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.ct.cql.elm.model.Add;
import com.ct.cql.elm.model.After;
import com.ct.cql.elm.model.And;


/**
 * The BinaryExpression type defines the abstract base type for expressions that take two arguments.
 * 
 * <p>Java class for BinaryExpression complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BinaryExpression">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}Expression">
 *       &lt;sequence>
 *         &lt;element name="operand" type="{urn:hl7-org:elm:r1}Expression" maxOccurs="2" minOccurs="2"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BinaryExpression", propOrder = {
    "operand"
})
@XmlSeeAlso({
    Or.class,
    ProperIn.class,
    SameOrAfter.class,
    NotEqual.class,
    ProperIncludes.class,
    OverlapsBefore.class,
    Contains.class,
    Subtract.class,
    Xor.class,
    EndsWith.class,
    LessOrEqual.class,
    Overlaps.class,
    Starts.class,
    Matches.class,
    Union.class,
    SameAs.class,
    After.class,
    Power.class,
    DurationBetween.class,
    Add.class,
    Meets.class,
    Divide.class,
    Implies.class,
    Multiply.class,
    ProperContains.class,
    CalculateAgeAt.class,
    StartsWith.class,
    ProperIncludedIn.class,
    SameOrBefore.class,
    MeetsAfter.class,
    Equal.class,
    DifferenceBetween.class,
    MeetsBefore.class,
    Log.class,
    Modulo.class,
    Ends.class,
    OverlapsAfter.class,
    In.class,
    GreaterOrEqual.class,
    Intersect.class,
    Less.class,
    And.class,
    Equivalent.class,
    TruncatedDivide.class,
    Except.class,
    Before.class,
    IncludedIn.class,
    Includes.class,
    Indexer.class,
    Times.class,
    Greater.class
})
public class BinaryExpression
    extends Expression
{

    @XmlElement(required = true)
    protected List<Expression> operand;

    /**
     * Gets the value of the operand property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operand property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperand().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Expression }
     * 
     * 
     */
    public List<Expression> getOperand() {
        if (operand == null) {
            operand = new ArrayList<Expression>();
        }
        return this.operand;
    }

}
