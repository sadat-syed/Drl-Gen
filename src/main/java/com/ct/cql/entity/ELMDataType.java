package com.ct.cql.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

@SuppressWarnings({"javadoc", "nls"})
public enum ELMDataType {
	PATIENT_CARE_EXPERIENCE("Patient Care Experience"),
	PROVIDER_CARE_EXPERIENCE("Provider Care Experience"),
	CARE_GOAL("Care Goal"),
	//COMMUNICATION_FROM_PATIENT_TO_PROVIDER("Communication: From Patient to Provider"),
	//ADDED FOR CMS135V7
	COMMUNICATION_FROM_PATIENT_TO_PROVIDER("PositiveCommunicationFromPatientToProvider"),
	COMMUNICATION_FROM_PROVIDER_TO_PATIENT("Communication: From Provider to Patient"),
	//CMS50v7- COMMUNICATION_FROM_PROVIDER_TO_PROVIDER("Communication: From Provider to Provider"),
	COMMUNICATION_FROM_PROVIDER_TO_PROVIDER("PositiveCommunicationFromProviderToProvider"),
	DIAGNOSIS_ACTIVE("Diagnosis, Active"),
	DIAGNOSIS_FAMILY_HISTORY("Diagnosis, Family History"),
	DIAGNOSIS_INACTIVE("Diagnosis, Inactive"),
	DIAGNOSIS_RESOLVED("Diagnosis, Resolved"),
	DEVICE_ADVERSE_EVENT("Device, Adverse Event"),
	DEVICE_ALLERGY("Device, Allergy"),
	//DEVICE_APPLIED("Device, Applied"),
	//CHANGED FOR CMS145V7
	DEVICE_APPLIED("PositiveDeviceApplied"),
	DEVICE_INTOLERANCE("Device, Intolerance"),
	//DEVICE_ORDER("Device, Order"),
	//Added for CMS165v8
	DEVICE_ORDER("PositiveDeviceOrder"),
	DEVICE_RECOMMENDED("Device, Recommended"),
	DIAGNOSTIC_STUDY_ADVERSE_EVENT("Diagnostic Study, Adverse Event"),
	DIAGNOSTIC_STUDY_INTOLERANCE("Diagnostic Study, Intolerance"),
	//DIAGNOSTIC_STUDY_ORDER("Diagnostic Study, Order"),
	DIAGNOSTIC_STUDY_ORDER("PositiveDiagnosticStudyOrder"),
	DIAGNOSTIC_STUDY_PERFORMED("PositiveDiagnosticStudyPerformed"),
	DIAGNOSTIC_STUDY_RECOMMENDED("Diagnostic Study, Recommended"),
	DIAGNOSTIC_STUDY_RESULT("Diagnostic Study, Result"),
	ENCOUNTER_ACTIVE("Encounter, Active"),
	ENCOUNTER_ORDER("Encounter, Order"),
	ENCOUNTER_PERFORMED("PositiveEncounterPerformed"),
	ENCOUNTER_RECOMMENDED("Encounter, Recommended"),
	FUNCTIONAL_STATUS_ORDER("Functional Status, Order"),
	FUNCTIONAL_STATUS_PERFORMED("Functional Status, Performed"),
	FUNCTIONAL_STATUS_RECOMMENDED("Functional Status, Recommended"),
	FUNCTIONAL_STATUS_RESULT("Functional Status, Result"),
	PATIENT_CHARACTERISTIC("PatientCharacteristic"),
	//PATIENT_CHARACTERISTIC_BIRTH_DATE("Patient Characteristic Birth Date"),
	PATIENT_CHARACTERISTIC_BIRTH_DATE("PatientCharacteristicBirthdate"),
	//Changed for CMS161v7
	PATIENT_CHARACTERISTIC_EXPIRED("PatientCharacteristicExpired"),
	//PATIENT_CHARACTERISTIC_EXPIRED("Patient Characteristic Expired"),
	PATIENT_CHARACTERISTIC_CLINICAL_TRIAL_PARTICIPANT("Patient Characteristic Clinical Trial Participant"),
	PATIENT_CHARACTERISTIC_PAYER("PatientCharacteristicPayer"),
	PATIENT_CHARACTERISTIC_SEX("PatientCharacteristicSex"),	
	PATIENT_CHARACTERISTIC_ETHNICITY("PatientCharacteristicEthnicity"),
	PATIENT_CHARACTERISTIC_RACE("PatientCharacteristicRace"),
	PROVIDER_CHARACTERISTIC("Provider Characteristic"),
	INTERVENTION_ADVERSE_EVENT("Intervention, Adverse Event"),
	INTERVENTION_INTOLERANCE("Intervention, Intolerance"),
	INTERVENTION_ORDER("PositiveInterventionOrder"),
	INTERVENTION_PERFORMED("PositiveInterventionPerformed"),
	INTERVENTION_RECOMMENDED("Intervention, Recommended"),
	INTERVENTION_RESULT("Intervention, Result"),
	LABORATORY_TEST_ADVERSE_EVENT("Laboratory Test, Adverse Event"),
	LABORATORY_TEST_INTOLERANCE("Laboratory Test, Intolerance"),
	//LABORATORY_TEST_ORDER("Laboratory Test, Order"),
	LABORATORY_TEST_ORDER("PositiveLaboratoryTestOrder"),
	//LABORATORY_TEST_PERFORMED("Laboratory Test, Performed"),
	//CHANGED FOR CMS145V7
	LABORATORY_TEST_PERFORMED("PositiveLaboratoryTestPerformed"),
	LABORATORY_TEST_RECOMMENDED("Laboratory Test, Recommended"),
	LABORATORY_TEST_RESULT("Laboratory Test, Result"),
	//MEDICATION_ACTIVE("Medication, Active"),
	MEDICATION_ACTIVE("MedicationActive"),
	//MEDICATION_DISCHARGE("Medication, Discharge"),
	MEDICATION_DISCHARGE("PositiveMedicationDischarge"),
	MEDICATION_DISCHARGE_NEGATED("NegativeMedicationDischarge"),
	MEDICATION_ADMINISTERED("Medication, Administered"),
	//MEDICATION_ADVERSE_EFFECTS("Medication, Adverse Effects"),
	//changed for CMD347v2
	MEDICATION_ADVERSE_EFFECTS("AdverseEvent"),
	MEDICATION_ALLERGY("Medication, Allergy"),
	//MEDICATION_DISPENSED("Medication, Dispensed"),
	//Changed for CMS136v8
	MEDICATION_DISPENSED("PositiveMedicationDispensed"),
	MEDICATION_INTOLERANCE("Medication, Intolerance"),
	//MEDICATION_ORDER("Medication, Order"),
	//CHANGED FOR CMS145V7
	MEDICATION_ORDER("PositiveMedicationOrder"),
	PHYSICAL_EXAM_FINDING("Physical Exam, Finding"),
	PHYSICAL_EXAM_ORDER("Physical Exam, Order"),
	//PHYSICAL_EXAM_PERFORMED("Physical Exam, Performed"),
	PHYSICAL_EXAM_PERFORMED("PositivePhysicalExamPerformed"),
	PHYSICAL_EXAM_RECOMMENDED("Physical Exam, Recommended"),
	PROCEDURE_ADVERSE_EVENT("Procedure, Adverse Event"),
	ADVERSE_EVENT("AdverseEvent"),
	PROCEDURE_INTOLERANCE("Procedure, Intolerance"),
	PROCEDURE_ORDER("Procedure, Order"),
	PROCEDURE_PERFORMED("PositiveProcedurePerformed"),
	PROCEDURE_RECOMMENDED("Procedure, Recommended"),
	PROCEDURE_RESULT("Procedure, Result"),
	RISK_CATEGORY_ASSESSMENT("Risk Category Assessment"),
	SUBSTANCE_ADMINISTERED("Substance, Administered"),
	SUBSTANCE_ADVERSE_EVENT("Substance, Adverse Event"),
	SUBSTANCE_ALLERGY("Substance, Allergy"),
	SUBSTANCE_INTOLERANCE("Substance, Intolerance"),
	SUBSTANCE_ORDER("Substance, Order"),
	SUBSTANCE_RECOMMENDED("Substance, Recommended"),
	SYMPTOM_ACTIVE("Symptom, Active"),
	SYMPTOM_ASSESSED("Symptom, Assessed"),
	SYMPTOM_INACTIVE("Symptom, Inactive"),
	SYMPTOM_RESOLVED("Symptom, Resolved"),
	SYSTEM_CHARACTERISTIC("System Characteristic"),
	TRANSFER_FROM("Transfer From"),
	TRANSFER_TO("Transfer To"),
	
	//negated datatypes follow, these are not part of qdm spec, we 
	//created for ease of identifying negating elements during rule processing
	PATIENT_CARE_EXPERIENCE_NEGATED("Patient Care Experience not done"),
	PROVIDER_CARE_EXPERIENCE_NEGATED("Provider Care Experience not done"),
	CARE_GOAL_NEGATED("Care Goal not done"),
	COMMUNICATION_FROM_PATIENT_TO_PROVIDER_NEGATED("Communication: From Patient to Provider not done"),
	COMMUNICATION_FROM_PROVIDER_TO_PATIENT_NEGATED("Communication: From Provider to Patient not done"),
	//COMMUNICATION_FROM_PROVIDER_TO_PROVIDER_NEGATED("Communication: From Provider to Provider not done"
	COMMUNICATION_FROM_PROVIDER_TO_PROVIDER_NEGATED("NegativeCommunicationFromProviderToProvider"),
	DIAGNOSIS_ACTIVE_NEGATED("Diagnosis, Active not done"),
	DIAGNOSIS_FAMILY_HISTORY_NEGATED("Diagnosis, Family History not done"),
	DIAGNOSIS_INACTIVE_NEGATED("Diagnosis, Inactive not done"),
	DIAGNOSIS_RESOLVED_NEGATED("Diagnosis, Resolved not done"),
	DEVICE_ADVERSE_EVENT_NEGATED("Device, Adverse Event not done"),
	DEVICE_ALLERGY_NEGATED("Device, Allergy not done"),
	DEVICE_APPLIED_NEGATED("Device, Applied not done"),
	DEVICE_INTOLERANCE_NEGATED("Device, Intolerance not done"),
	DEVICE_ORDER_NEGATED("Device, Order not done"),
	DEVICE_RECOMMENDED_NEGATED("Device, Recommended not done"),
	DIAGNOSTIC_STUDY_ADVERSE_EVENT_NEGATED("Diagnostic Study, Adverse Event not done"),
	DIAGNOSTIC_STUDY_INTOLERANCE_NEGATED("Diagnostic Study, Intolerance not done"),
	//CMS22v7- DIAGNOSTIC_STUDY_ORDER_NEGATED("Diagnostic Study, Order not done"),
	DIAGNOSTIC_STUDY_ORDER_NEGATED("NegativeDiagnosticStudyOrder"),
	DIAGNOSTIC_STUDY_PERFORMED_NEGATED("Diagnostic Study, Performed not done"),
	DIAGNOSTIC_STUDY_RECOMMENDED_NEGATED("Diagnostic Study, Recommended not done"),
	DIAGNOSTIC_STUDY_RESULT_NEGATED("Diagnostic Study, Result not done"),
	ENCOUNTER_ACTIVE_NEGATED("Encounter, Active not done"),
	ENCOUNTER_ORDER_NEGATED("Encounter, Order not done"),
	ENCOUNTER_PERFORMED_NEGATED("Encounter, Performed not done"),
	ENCOUNTER_RECOMMENDED_NEGATED("Encounter, Recommended not done"),
	FUNCTIONAL_STATUS_ORDER_NEGATED("Functional Status, Order not done"),
	FUNCTIONAL_STATUS_PERFORMED_NEGATED("Functional Status, Performed not done"),
	FUNCTIONAL_STATUS_RECOMMENDED_NEGATED("Functional Status, Recommended not done"),
	FUNCTIONAL_STATUS_RESULT_NEGATED("Functional Status, Result not done"),
	PROVIDER_CHARACTERISTIC_NEGATED("Provider Characteristic not done"),
	INTERVENTION_ADVERSE_EVENT_NEGATED("Intervention, Adverse Event not done"),
	INTERVENTION_INTOLERANCE_NEGATED("Intervention, Intolerance not done"),
	//CMS22v7-INTERVENTION_ORDER_NEGATED("Intervention, Order not done"),
	INTERVENTION_ORDER_NEGATED("NegativeInterventionOrder"),
	//CMS149v7
	//INTERVENTION_PERFORMED_NEGATED("Intervention, Performed not done"),
	INTERVENTION_PERFORMED_NEGATED("NegativeInterventionPerformed"),
	INTERVENTION_RECOMMENDED_NEGATED("Intervention, Recommended not done"),
	INTERVENTION_RESULT_NEGATED("Intervention, Result not done"),
	LABORATORY_TEST_ADVERSE_EVENT_NEGATED("Laboratory Test, Adverse Event not done"),
	LABORATORY_TEST_INTOLERANCE_NEGATED("Laboratory Test, Intolerance not done"),
	//LABORATORY_TEST_ORDER_NEGATED("Laboratory Test, Order not done"),
	LABORATORY_TEST_ORDER_NEGATED("NegativeLaboratoryTestOrder"),
	LABORATORY_TEST_PERFORMED_NEGATED("Laboratory Test, Performed not done"),
	LABORATORY_TEST_RECOMMENDED_NEGATED("Laboratory Test, Recommended not done"),
	LABORATORY_TEST_RESULT_NEGATED("Laboratory Test, Result not done"),
	MEDICATION_ACTIVE_NEGATED("Medication, Active not done"),
	MEDICATION_ADMINISTERED_NEGATED("Medication, Administered not done"),
	MEDICATION_ADVERSE_EFFECTS_NEGATED("Medication, Adverse Effects not done"),
	MEDICATION_ALLERGY_NEGATED("Medication, Allergy not done"),
	MEDICATION_DISPENSED_NEGATED("Medication, Dispensed not done"),
	MEDICATION_INTOLERANCE_NEGATED("Medication, Intolerance not done"),
	//MEDICATION_ORDER_NEGATED("Medication, Order not done"),
	//CHANGED FOR CMS145V7
	MEDICATION_ORDER_NEGATED("NegativeMedicationOrder"),
	PHYSICAL_EXAM_FINDING_NEGATED("Physical Exam, Finding not done"),
	PHYSICAL_EXAM_ORDER_NEGATED("Physical Exam, Order not done"),
	//CMS22v7- PHYSICAL_EXAM_PERFORMED_NEGATED("Physical Exam, Performed not done"),
	PHYSICAL_EXAM_PERFORMED_NEGATED("NegativePhysicalExamPerformed"),
	PHYSICAL_EXAM_RECOMMENDED_NEGATED("Physical Exam, Recommended not done"),
	PROCEDURE_ADVERSE_EVENT_NEGATED("Procedure, Adverse Event not done"),
	PROCEDURE_INTOLERANCE_NEGATED("Procedure, Intolerance not done"),
	PROCEDURE_ORDER_NEGATED("Procedure, Order not done"),
	PROCEDURE_PERFORMED_NEGATED("NegativeProcedurePerformed"),
	//PROCEDURE_PERFORMED_NEGATED("Procedure, Performed not done"),
	PROCEDURE_RECOMMENDED_NEGATED("Procedure, Recommended not done"),
	PROCEDURE_RESULT_NEGATED("Procedure, Result not done"),
	RISK_CATEGORY_ASSESSMENT_NEGATED("Risk Category Assessment not done"),
	SUBSTANCE_ADMINISTERED_NEGATED("Substance, Administered not done"),
	SUBSTANCE_ADVERSE_EVENT_NEGATED("Substance, Adverse Event not done"),
	SUBSTANCE_ALLERGY_NEGATED("Substance, Allergy not done"),
	SUBSTANCE_INTOLERANCE_NEGATED("Substance, Intolerance not done"),
	SUBSTANCE_ORDER_NEGATED("Substance, Order not done"),
	SUBSTANCE_RECOMMENDED_NEGATED("Substance, Recommended not done"),
	SYMPTOM_ACTIVE_NEGATED("Symptom, Active not done"),
	SYMPTOM_ASSESSED_NEGATED("Symptom, Assessed not done"),
	SYMPTOM_INACTIVE_NEGATED("Symptom, Inactive not done"),
	SYMPTOM_RESOLVED_NEGATED("Symptom, Resolved not done"),
	SYSTEM_CHARACTERISTIC_NEGATED("System Characteristic not done"),
	TRANSFER_FROM_NEGATED("Transfer From not done"),
	TRANSFER_TO_NEGATED("Transfer To not done"),
	
	//QDM V4 DataTypes
	FAMILY_HISTORY("Family History"),
    //IMMUNIZATION_ADMINISTERED("Immunization, Administered"),
	IMMUNIZATION_ADMINISTERED("PositiveImmunizationAdministered"),
    IMMUNIZATION_ALLERGY("Immunization, Allergy"),
    IMMUNIZATION_INTOLERENCE("Immunization, Intolerance"),
    IMMUNIZATION_ORDER("Immunization, Order"),
    DIAGNOSIS("Diagnosis"),
	IMMUNIZATION("Immunization"),
	
	//QDM V4 DataTypes Negated
	FAMILY_HISTORY_NEGATED("Family History not done"),
    //IMMUNIZATION_ADMINISTERED_NEGATED("Immunization, Administered not done"),
	//Changes for CMS147v8
    IMMUNIZATION_ADMINISTERED_NEGATED("NegativeImmunizationAdministered"),
    IMMUNIZATION_ALLERGY_NEGATED("Immunization, Allergy not done"),
    IMMUNIZATION_INTOLERENCE_NEGATED("Immunization, Intolerance not done"),
    IMMUNIZATION_ORDER_NEGATED("Immunization, Order not done"),
    DIAGNOSIS_NEGATED("Diagnosis not done"),
	IMMUNIZATION_NEGATED("Immunization not done"),
	
	//QDM v4.3 DataTypes
	//ASSESSMENT_PERFORMED("Assessment, Performed"),	
	ASSESSMENT_PERFORMED("PositiveAssessmentPerformed"),
	
	// QDM v4.3 DataTypes Negated
	ASSESSMENT_PERFORMED_NEGATED("NegativeAssessmentPerformed"),
	
	//NOTE: non-standard QDM datatypes for PQRS follow 
	IND_CHARACTERISTIC_ADV_DIRECTIVE("Individual Characteristic, Advance Directive"),
	// Added for CMS22 & CMS69 ALM 1551 and ALM 1559, CQR Custom category	
	ATTRIBUTE_REASON("Attribute, Reason"),
	
	//Added for CMS145v7
	ALLERGY_INTOLERANCE("AllergyIntolerance"),
	
	//Added for CMS161v7
	Patient("Patient"),
	
	//Added for CMS165v8
	SYMPTOM("Symptom"),

	COMMUNICATION_PERFORMED("PositiveCommunicationPerformed");
	
	//map of all String values (lower-cased) for reverse lookup
	private static Map<String, ELMDataType> map = new HashMap<String, ELMDataType>();
	static {
		for (ELMDataType dataType : ELMDataType.values()) {
			map.put(dataType.getValue().toLowerCase(), dataType);
		}
	}
	
	private String value;
	
	private ELMDataType(String value) {
		this.value = value.toLowerCase();
	}

	/**
	 * Get string value for enum.
	 * 
	 * @return String value
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * Get negated datatype for passed datatype, null if not valid
	 * @param dataType dataType to convert
	 * @return negated qdm datatype, exception if not valid
	 */
	public static ELMDataType getNegatedDataType(ELMDataType dataType) {
		String value = dataType.getValue();
		if (value.endsWith(" not done")) {
			return dataType; //it's already a negated datatype
		}
		
		return fromString(dataType.getValue() + " not done");
	}
	
	/**
	 * Get datatype by string value
	 * @param value
	 * @return qdm datatype, exception if not found
	 */
	@JsonCreator
	public static ELMDataType fromString(String value) {
		ELMDataType result = map.get(value.toLowerCase());
		if (result == null) {
			throw new IllegalArgumentException("bad ELMDataType: " + value);
		}
		
		return result;
	}
}
