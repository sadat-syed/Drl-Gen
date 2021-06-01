//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.11 at 05:13:32 PM IST 
//


package com.ct.cql.elm.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ErrorSeverity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ErrorSeverity">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="info"/>
 *     &lt;enumeration value="warning"/>
 *     &lt;enumeration value="error"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ErrorSeverity", namespace = "urn:hl7-org:cql-annotations:r1")
@XmlEnum
public enum ErrorSeverity {


    /**
     * The message is informational and does not impact the translator output.
     * 
     */
    @XmlEnumValue("info")
    INFO("info"),

    /**
     * The message is a warning that some unexpected behavior may occur, but is not critical enough to prevent translation.
     * 
     */
    @XmlEnumValue("warning")
    WARNING("warning"),

    /**
     * The message is an error that is preventing the translation from completing successfully.
     * 
     */
    @XmlEnumValue("error")
    ERROR("error");
    private final String value;

    ErrorSeverity(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ErrorSeverity fromValue(String v) {
        for (ErrorSeverity c: ErrorSeverity.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
