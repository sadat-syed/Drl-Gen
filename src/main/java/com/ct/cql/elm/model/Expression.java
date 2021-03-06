//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.11 at 05:13:32 PM IST 
//


package com.ct.cql.elm.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.ct.cql.elm.model.AggregateExpression;
import com.ct.cql.elm.model.AliasRef;


/**
 * The Expression type defines the abstract base type for all expressions used in the ELM expression language.
 * 
 * <p>Java class for Expression complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Expression">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:elm:r1}Element">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Expression")
@XmlSeeAlso({
    Query.class,
    Repeat.class,
    CodeRef.class,
    PositionOf.class,
    Code.class,
    ValueSetRef.class,
    QueryLetRef.class,
    Split.class,
    ConceptRef.class,
    Instance.class,
    AliasRef.class,
    ParameterRef.class,
    ForEach.class,
    Last.class,
    Literal.class,
    List.class,
    First.class,
    IdentifierRef.class,
    Descendents.class,
    Now.class,
    Combine.class,
    Time.class,
    Children.class,
    Filter.class,
    Retrieve.class,
    CodeSystemRef.class,
    DateTime.class,
    Substring.class,
    IndexOf.class,
    MaxValue.class,
    LastPositionOf.class,
    If.class,
    Current.class,
    Interval.class,
    Case.class,
    Property.class,
    Tuple.class,
    Null.class,
    OperandRef.class,
    TernaryExpression.class,
    MinValue.class,
    Sort.class,
    Concept.class,
    NaryExpression.class,
    InValueSet.class,
    Slice.class,
    InCodeSystem.class,
    Message.class,
    ExpressionRef.class,
    Round.class,
    TimeOfDay.class,
    AggregateExpression.class,
    Quantity.class,
    Today.class,
    BinaryExpression.class,
    UnaryExpression.class
})
public abstract class Expression
    extends Element
{


}
